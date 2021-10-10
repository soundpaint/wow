package org.soundpaint.wow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jakartaee.commons.io.IOUtils;

public class MessagePart {
  private static final Logger logger = LogManager.getLogger(MessagePart.class);

  private static File storeNewMessagePart(final byte[] data,
      final String documentId, final File tempDir) throws IOException {
    logger.debug("store new document with documentId=" + documentId
        + " from binary data");
    final File tmpFile =
        File.createTempFile("message-part_" + documentId, null, tempDir);
    final FileOutputStream outputStream = new FileOutputStream(tmpFile);
    outputStream.write(data);
    outputStream.close();
    return tmpFile;
  }

  private static void storeNewMessagePart(final InputStream inputStream,
      final OutputStream outputStream) throws IOException {
    BufferedInputStream bufferedIn = null;
    BufferedOutputStream bufferedOut = null;
    try {
      bufferedIn = new BufferedInputStream(inputStream);
      bufferedOut = new BufferedOutputStream(outputStream);
      IOUtils.copy(bufferedIn, bufferedOut);
    } finally {
      if (bufferedIn != null) {
        try {
          bufferedIn.close();
        } catch (final IOException e) {
          // ignore
        }
      }
      if (bufferedOut != null) {
        try {
          bufferedOut.close();
        } catch (final IOException e) {
          // ignore
        }
      }
    }
  }

  private static File storeNewMessagePart(final InputStream inputStream,
      final String documentId) throws IOException {
    logger.debug("store new document with documentId=" + documentId
        + " from input stream");
    final File tmpFile =
        File.createTempFile("message-part_" + documentId, null);
    final FileOutputStream outputStream = new FileOutputStream(tmpFile);
    storeNewMessagePart(inputStream, outputStream);
    outputStream.close();
    return tmpFile;
  }

  public static MessagePart fromBinaryData(final byte[] data, final String id,
      final long sizeInBytes, final File tempDir) throws IOException {
    final File tempFile = storeNewMessagePart(data, id, tempDir);
    return new MessagePart(tempFile, id, sizeInBytes);
  }

  public static MessagePart fromTempFile(final File tempFile, final String id,
      final long sizeInBytes) {
    return new MessagePart(tempFile, id, sizeInBytes);
  }

  public static MessagePart fromInputStream(final InputStream in,
      final String id, final long sizeInBytes) throws IOException {
    final File tempFile = storeNewMessagePart(in, id);
    return new MessagePart(tempFile, id, sizeInBytes);
  }

  private final File contentFile;
  private final String id;
  private final long sizeInBytes;

  private MessagePart(final File contentFile, final String id,
      final long sizeInBytes) {
    this.contentFile = contentFile;
    this.id = id;
    this.sizeInBytes = sizeInBytes;
  }

  public File getContentFile() {
    return contentFile;
  }

  public String getId() {
    return id;
  }

  public long getSizeInBytes() {
    return sizeInBytes;
  }

  public void finalize() {
    if (!contentFile.delete()) {
      logger.error("failed deleting temporary file " + contentFile.getPath());
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
