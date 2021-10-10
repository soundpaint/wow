package org.soundpaint.wow.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soundpaint.wow.utils.ParseException;

public class Credential
{
  private static final Logger logger = LogManager.getLogger(Credential.class);
  private static final String UTF8_ENCODING = "UTF-8";
  private static final MessageDigest md5Digest;

  private final HashMethod hashMethod;
  private final String saltValue;
  private byte[] binaryPasswordHash;

  static
  {
    try {
      md5Digest = MessageDigest.getInstance("MD5");
    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException("failed initializing class " +
                                 Credential.class + ": " + e);
    }
  }

  public static enum HashMethod
  {
    PLAIN("plain",
          passwordToken -> {
            try {
              return passwordToken.getBytes(UTF8_ENCODING);
            } catch (final UnsupportedEncodingException e) {
              // this should never occur
              throw new RuntimeException("unexpected exception: " + e);
            }
          },
          binarySaltedPassword -> {
            logger.warn("using plain password hash method");
            return binarySaltedPassword;
          }),
    MD5("md5",
        passwordToken -> { return parseHexBytes(passwordToken); },
        binarySaltedPassword -> {
          logger.info("using MD5 password hash method");
          return md5Digest.digest(binarySaltedPassword);
        });

    private final String id;
    private final Function<String, byte[]> convertToBytesFunction;
    private final Function<byte[], byte[]> hashFunction;

    private HashMethod(final String id,
                       final Function<String, byte[]> convertToBytesFunction,
                       final Function<byte[], byte[]> hashFunction)
    {
      this.id = id;
      this.convertToBytesFunction = convertToBytesFunction;
      this.hashFunction = hashFunction;
    }

    public static HashMethod fromId(final String id)
    {
      for (final HashMethod hashMethod : values()) {
        if (hashMethod.id.equals(id)) return hashMethod;
      }
      throw new ParseException("invalid password hash method: " + id);
    }

    public String toSource()
    {
      return id;
    }
    
    public byte[] convertToBytes(final String passwordToken)
    {
      return convertToBytesFunction.apply(passwordToken);
    }

    public byte[] createHash(final byte[] binarySaltedPassword)
    {
      return hashFunction.apply(binarySaltedPassword);
    }
  }

  /**
   * 
   * @param hashMethod
   *          The hashing method, e.g. MD5.
   * @param passwordToken
   *          A string that represents the password hash. In the case of MD5,
   *          the token holds the hexadecimal representation of the binary hash.
   *          In the case of plain text passwords, the token holds the plain
   *          text password.
   * @param saltValue
   *          An additional string for salted hashing.
   */
  public Credential(final HashMethod hashMethod,
                    final String passwordToken,
                    final String saltValue)
  {
    if (hashMethod == null)
      throw new NullPointerException("hashMethod");
    if (passwordToken == null)
      throw new NullPointerException("passwordToken");
    if (saltValue == null)
      throw new NullPointerException("saltValue");
    this.hashMethod = hashMethod;
    this.saltValue = saltValue;
    binaryPasswordHash = hashMethod.convertToBytes(passwordToken);
  }

  public Credential(final byte binaryPasswordHash[], final String saltValue)
  {
    this(HashMethod.MD5, binaryPasswordHash, saltValue);
  }

  public Credential(final HashMethod hashMethod,
                    final byte binaryPasswordHash[],
                    final String saltValue)
  {
    if (hashMethod == null)
      throw new NullPointerException("hashMethod");
    if (binaryPasswordHash == null)
      throw new NullPointerException("binaryPasswordHash");
    if (saltValue == null)
      throw new NullPointerException("saltValue");
    this.hashMethod = hashMethod;
    this.saltValue = saltValue;
    this.binaryPasswordHash = binaryPasswordHash;
  }

  private static int hex2dec(final char ch)
  {
    if ((ch >= '0') && (ch <= '9'))
      return (int) ch - (int) '0';
    else if ((ch >= 'A') && (ch <= 'F'))
      return (int) ch - (int) 'A' + 0xa;
    else if ((ch >= 'a') && (ch <= 'f'))
      return (int) ch - (int) 'a' + 0xa;
    else
      throw new IllegalStateException("illegal hex digit: " + ch);
  }

  private static byte[] parseHexBytes(final String hexSequence)
  {
    if ((hexSequence.length() & 0x1) != 0)
      throw new IllegalStateException("odd number of hex digits: '" +
                                      hexSequence + "'");
    final byte[] result = new byte[hexSequence.length() / 2];
    int count = 0;
    for (int i = 0; i < hexSequence.length();) {
      final int byteValue =
        (hex2dec(hexSequence.charAt(i++)) << 4) |
        (hex2dec(hexSequence.charAt(i++)));
      result[count++] = (byte) byteValue;
    }
    return result;
  }

  private String toHex(final byte[] bytes)
  {
    final StringBuffer s = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      s.append(Integer.toHexString(bytes[i] & 0xff));
    }
    return s.toString();
  }

  private static byte[] createBinaryPasswordHash(final String password,
                                                 final String saltValue,
                                                 final HashMethod hashMethod)
  {
    try {
      if (password == null)
        throw new NullPointerException("password");
      if (saltValue == null)
        throw new NullPointerException("saltValue");
      final byte[] binarySaltedPassword =
        (password + saltValue).getBytes(UTF8_ENCODING);
      return hashMethod.createHash(binarySaltedPassword);
    } catch (final UnsupportedEncodingException e) {
      // this should never occur
      throw new RuntimeException("unexpected exception: " + e);
    }
  }

  public HashMethod getHashMethod()
  {
    return hashMethod;
  }

  public String getSalt()
  {
    return saltValue;
  }

  public boolean canAuthenticate(final String password)
  {
    final byte[] binaryPasswordHash =
      createBinaryPasswordHash(password, saltValue, hashMethod);
    if (binaryPasswordHash.length != this.binaryPasswordHash.length) {
      logger.warn("authenticate: " +
                  "password length mismatch: " + binaryPasswordHash.length +
                  " (expected: " + this.binaryPasswordHash.length + ")");
      return false;
    }
    for (int i = 0; i < binaryPasswordHash.length; i++) {
      if (binaryPasswordHash[i] != this.binaryPasswordHash[i]) {
        logger.warn("password " + password + ": " +
                    "password hash mismatch: " + toHex(binaryPasswordHash) +
                    ", expected: " + toHex(this.binaryPasswordHash));
        return false;
      }
    }
    return true;
  }

  public boolean changePassword(final String oldPlainPassword,
                                final String newPlainPassword,
                                final String newSaltValue)
  {
    if (canAuthenticate(oldPlainPassword)) {
      final String saltValue;
      if (newSaltValue != null) {
        saltValue = newSaltValue;
      } else {
        // fall-back: take old salt value
        saltValue = this.saltValue;
      }
      binaryPasswordHash =
        createBinaryPasswordHash(newPlainPassword, saltValue, hashMethod);
      return true;
    } else {
      // authentication failed
      return false;
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
