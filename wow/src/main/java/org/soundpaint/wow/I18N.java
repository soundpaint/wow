package org.soundpaint.wow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.soundpaint.wow.i18n.ArgIdent;
import org.soundpaint.wow.i18n.I18NUtils;
import org.soundpaint.wow.i18n.Language;
import org.soundpaint.wow.utils.Ident;
import org.soundpaint.wow.utils.ParseException;

public class I18N
{
  private static final Logger logger = LogManager.getLogger(I18N.class);
  private static final String lineSeparator = System.lineSeparator();

  private Locale locale;
  private I18NUtils i18nUtils;
  private Language languagePreferenceOrder[];

  private static final int LANGUAGES_COUNT = Language.values().length;

  public static I18N create()
  {
    return create((Language[]) null);
  }

  public static I18N create(final Language... languages)
  {
    I18N i18n = new I18N();
    i18n.init(new I18NUtils(i18n), languages);
    return i18n;
  }

  protected I18N() {
  }

  protected void init(final I18NUtils i18nUtils)
  {
    init(i18nUtils, (Language[]) null);
  }

  protected void init(final I18NUtils i18nUtils, final Language... languages)
  {
    this.i18nUtils = i18nUtils;
    if (languages != null) {
      setLanguagePreferenceOrder(languages);
    } else {
      final int languagesCount = getLanguagesCount();
      languagePreferenceOrder = new Language[languagesCount];
      for (int i = 0; i < languagesCount; i++) {
        languagePreferenceOrder[i] = Language.fromOrdinal(i);
      }
    }
    setLocale(new Locale(languagePreferenceOrder[0].getKey()));
  }

  public int getLanguagesCount()
  {
    return LANGUAGES_COUNT;
  }

  public void setLanguagePreferenceOrder(final Language... languages)
  {
    final int languagesCount = getLanguagesCount();
    if (languages.length != languagesCount)
      throw new IllegalArgumentException("languages size mismatch: "
          + languages.length);
    for (final Language language : Language.values()) {
      boolean found = false;
      for (int i = 0; i < languagesCount; i++) {
        if (language.equals(languages[i])) {
          found = true;
          break;
        }
      }
      if (!found) { 
        throw new IllegalArgumentException(
          "language not in priority list: " + language);
      }
    }
    System.arraycopy(languagePreferenceOrder, 0,
                     this.languagePreferenceOrder, 0, languagesCount);
  }

  public void setPreferredLanguage(final Language language)
  {
    int index = -1;
    for (int i = 0; i < getLanguagesCount(); i++) {
      if (language.equals(languagePreferenceOrder[i])) {
        index = i;
        break;
      }
    }
    if (index < 0) {
      throw new IllegalArgumentException("language not in priority list: " +
                                         language);
    }
    for (int i = index; i > 0; i--) {
      languagePreferenceOrder[i] = languagePreferenceOrder[i - 1];
    }
    languagePreferenceOrder[0] = language;
  }

  public void setLocale(final Locale locale)
  {
    if (locale == null)
      throw new NullPointerException("locale");
    this.locale = locale;
    i18nUtils.i18nChangedLocale();
  }

  public Locale getLocale()
  {
    return locale;
  }

  public I18NUtils getUtils()
  {
    return i18nUtils;
  }

  protected String instantiateTemplate(final Template template)
  {
    final int argumentsCount = template.getArgumentsCount();
    if (argumentsCount != 0) {
      throw new InternalError("arguments count mismatch: " +
                              argumentsCount + " (expected: 0)");
    }
    for (short preferIndex = 0; preferIndex < getLanguagesCount(); preferIndex++) {
      final Language language = languagePreferenceOrder[preferIndex];
      final int languageIndex = language.ordinal();
      if (template.isDefinedForLanguage(languageIndex)) {
        return template.instantiate(languageIndex);
      }
    }
    return null;
  }

  protected String instantiateTemplate(final Template template,
                                       final String... args)
  {
    final int argumentsCount = template.getArgumentsCount();
    if (argumentsCount != args.length) {
      throw new InternalError("arguments count mismatch: " + argumentsCount +
                              " (expected: " + args.length + ")"); }
    for (short preferIndex = 0; preferIndex < getLanguagesCount(); preferIndex++) {
      final Language language = languagePreferenceOrder[preferIndex];
      final int languageIndex = language.ordinal();
      if (template.isDefinedForLanguage(languageIndex)) {
        final String templateInstance =
            template.instantiate(languageIndex, args);
        return templateInstance;
      }
    }
    return null;
  }

  public static class Argument
  {
    private Ident name;
    private String description;

    private Argument() {}

    public Argument(final String name, final String description)
    {
      this();
      if (name == null) { throw new NullPointerException("name"); }
      if (description == null) { throw new NullPointerException("description"); }
      this.name = ArgIdent.parse(name);
      this.description = description;
    }

    public Ident getName()
    {
      return name;
    }

    public String getDescription()
    {
      return description;
    }
  }

  protected static class Template
  {
    public static interface Segment
    {
      public String instantiate(final String[] args);
      public String toSource();
    }

    private static final Segment[] EMPTY_SEGMENTS_ARRAY = {};

    public static class TextSegment implements Segment
    {
      private String text;

      public static final TextSegment EMPTY_TEXT_SEGMENT = new TextSegment("");

      private TextSegment() {}

      public TextSegment(final String text)
      {
        this();
        if (text == null) { throw new NullPointerException("text"); }
        this.text = text;
      }

      public String instantiate(final String[] args)
      {
        return text;
      }

      public String toSource()
      {
        return quoteSegmentSource(text);
      }
    }

    public static class ArgumentSegment implements Segment
    {
      private int argIndex;

      private ArgumentSegment() {}

      public ArgumentSegment(final int argIndex)
      {
        this();
        this.argIndex = argIndex;
      }

      public int getArgumentIndex()
      {
        return argIndex;
      }

      public String instantiate(final String args[])
      {
        return args[argIndex];
      }

      public String toSource()
      {
        return "{" + argIndex + "}";
      }
    }

    private static String quoteSegmentSource(final String source)
    {
      return source.replace("\\", "\\\\").replace("{", "\\{");
    }

    private String key;
    private String description;
    private String[] source;
    private int languagesCount;
    private int argumentsCount;
    private String[] argumentNames;
    private String[] argumentDescriptions;
    private Segment[][] segments;

    private Template() {}

    public Template(final String key,
                    final String description,
                    final int languagesCount)
    {
      this();
      this.key = key;
      this.description = description;
      this.languagesCount = languagesCount;
      argumentsCount = 0;
      argumentNames = null;
      argumentDescriptions = null;
      source = new String[languagesCount];
      segments = new Segment[languagesCount][];
    }

    public Template(final String key,
                    final String description,
                    final int languagesCount,
                    final Argument... arguments)
    {
      this();
      this.key = key;
      this.description = description;
      this.languagesCount = languagesCount;
      if (arguments == null) { throw new NullPointerException("arguments"); }
      argumentsCount = arguments.length;
      if (argumentsCount == 0) {
        throw new IllegalArgumentException("empty arguments array");
      }
      argumentNames = new String[argumentsCount];
      argumentDescriptions = new String[argumentsCount];
      for (int argIndex = 0; argIndex < argumentsCount; argIndex++) {
        argumentNames[argIndex] = arguments[argIndex].getName().toString();
        argumentDescriptions[argIndex] = arguments[argIndex].getDescription();
      }
      source = new String[languagesCount];
      segments = new Segment[languagesCount][];
    }

    public String getSource(final int languageIndex)
    {
      return source[languageIndex];
    }

    public String getKey()
    {
      return key;
    }

    public String getDescription()
    {
      return description;
    }

    public int getLanguagesCount()
    {
      return languagesCount;
    }

    public int getArgumentsCount()
    {
      return argumentsCount;
    }

    public String getArgumentName(final int argIndex)
    {
      if (argIndex < 0) {
        throw new IllegalArgumentException("argIndex < 0: " + argIndex);
      }
      if (argIndex >= argumentsCount) {
        throw new IllegalArgumentException("argIndex >= " +
                                           argumentsCount + "): " + argIndex);
      }
      return argumentNames[argIndex];
    }

    public String getArgumentDescription(final int argIndex)
    {
      if (argIndex < 0) {
        throw new IllegalArgumentException("argIndex < 0: " + argIndex);
      }
      if (argIndex >= argumentsCount) {
        throw new IllegalArgumentException("argIndex >= " +
                                           argumentsCount + "): " + argIndex);
      }
      return argumentDescriptions[argIndex];
    }

    public boolean isDefinedForLanguage(final int languageIndex)
    {
      return segments[languageIndex] != null;
    }

    private void setSegments(final int languageIndex, final Segment... segments)
    {
      if (segments == null) { throw new NullPointerException("segments"); }
      if (segments.length == 0) {
        throw new IllegalArgumentException("empty list of segments");
      }
      for (final Segment segment : segments) {
        if (segment instanceof ArgumentSegment) {
          final ArgumentSegment argumentSegment = (ArgumentSegment) segment;
          final int argIndex = argumentSegment.getArgumentIndex();
          if (argIndex >= argumentsCount) { throw new IllegalArgumentException(
              "invalid argument index: " + argIndex + "; have only "
                  + argumentsCount + " arguments"); }
        }
      }
      this.segments[languageIndex] = segments;
    }

    private static boolean isIdentChar(final char ch)
    {
      return Character.isJavaIdentifierPart(ch);
    }

    private static class I18NParseException extends ParseException
    {
      /**
       * Default serial version unique identifier.
       */
      private static final long serialVersionUID = 1L;

      private static String createFullMessage(final String key,
                                              final String value,
                                              final String languageId,
                                              final String shortMessage)
      {
        return "i18n property '" + key + "': value '" + value
            + "' for language '" + languageId + "': "
            + shortMessage;
      }

      public I18NParseException(final String key,
                                final String value,
                                final String languageId,
                                final String shortMessage)
      {
        super(createFullMessage(key, value, languageId, shortMessage));
      }

      public I18NParseException(final String key,
                                final String value,
                                final String languageId,
                                final String shortMessage,
                                final Exception source)
      {
        super(createFullMessage(key, value, languageId, shortMessage), source);
      }
    }

    private void config(final Language lang,
                        final String source)
    {
      config(lang.ordinal(), lang.getKey(), source);
    }

    private void config(final int languageIndex,
                        final String languageId,
                        final String source)
    {
      final List<Segment> segments = new ArrayList<Segment>();
      final StringBuffer token = new StringBuffer();
      TokenType tokenType = TokenType.NONE;
      for (int charPos = 0; charPos < source.length(); charPos++) {
        final char ch = source.charAt(charPos);
        switch (tokenType) {
        case NONE:
          if (ch == '{') {
            tokenType = TokenType.ARGUMENT;
          } else if (ch == '\\') {
            tokenType = TokenType.ESCAPED_TEXT;
          } else {
            token.append(ch);
            tokenType = TokenType.TEXT;
          }
          break;
        case ESCAPED_TEXT:
          // next char is whatever literal
          token.append(ch);
          tokenType = TokenType.TEXT;
          break;
        case TEXT:
          if (ch == '\\') {
            tokenType = TokenType.ESCAPED_TEXT;
          } else if (ch == '{') {
            segments.add(new TextSegment(token.toString()));
            token.setLength(0);
            tokenType = TokenType.ARGUMENT;
          } else {
            token.append(ch);
          }
          break;
        case ARGUMENT:
          if (ch == '}') {
            int argIndex;
            for (argIndex = 0; argIndex < argumentsCount; argIndex++) {
              if (token.toString().equals(argumentNames[argIndex]))
                break;
            }
            if (argIndex == argumentsCount) {
              throw new I18NParseException(key, source, languageId,
                                           "unknown argument '" + token + "'");
            }
            segments.add(new ArgumentSegment(argIndex));
            token.setLength(0);
            tokenType = TokenType.NONE;
          } else if (isIdentChar(ch)) {
            token.append(ch);
          } else {
            throw new I18NParseException(key, source, languageId,
              "argument contains an invalid character: " + ch);
          }
          break;
        default:
          throw new I18NParseException(key, source, languageId,
            "illegal parse state: unexpected token type: " + tokenType);
        }
      }
      switch (tokenType) {
      case NONE:
        // templateSource stops immediately behind argument => nothing to do
        break;
      case TEXT:
        // add pending text as last chunk
        segments.add(new TextSegment(token.toString()));
        break;
      case ARGUMENT:
        throw new I18NParseException(key, source, languageId,
          "unterminated i18n argument");
      case ESCAPED_TEXT:
        throw new I18NParseException(key, source, languageId,
          "unterminated escaped text");
      default:
        throw new I18NParseException(key, source, languageId,
          "illegal parse state: unexpected token type: " + tokenType);
      }
      if (segments.size() == 0) {
        // ensure there is at least one segment
        segments.add(TextSegment.EMPTY_TEXT_SEGMENT);
      }
      setSegments(languageIndex, segments.toArray(EMPTY_SEGMENTS_ARRAY));
      this.source[languageIndex] = source;
    }

    private static enum TokenType
    {
      NONE, TEXT, ESCAPED_TEXT, ARGUMENT
    }

    public void unsetTextSegments(final int languageIndex)
    {
      segments[languageIndex] = null;
    }

    public String instantiate(final int languageIndex) {
      if (argumentsCount > 0) { throw new IllegalArgumentException(
          "invalid number of arguments in template instantiation (found: 0; expected: "
              + argumentsCount + ")"); }
      return segments[languageIndex][0].instantiate(null);
    }

    public String instantiate(final int languageIndex, final String[] args)
    {
      if (args.length != argumentsCount) {
        throw new IllegalArgumentException(
          "invalid number of arguments in template instantiation (found: "
              + args.length + "; expected: " + argumentsCount + ")");
      }
      final StringBuffer value = new StringBuffer();
      final Segment[] localeSegments = segments[languageIndex];
      final int localeSegmentsLength = localeSegments.length;
      for (int i = 0; i < localeSegmentsLength; i++) {
        value.append(segments[languageIndex][i].instantiate(args));
      }
      return value.toString();
    }
  }

  protected static final List<Template> templates = new ArrayList<Template>();

  public static int getTemplatesCount()
  {
    return templates.size();
  }

  public static String getTemplateKey(final int keyIndex)
  {
    return templates.get(keyIndex).getKey();
  }

  public static String getTemplateDescription(final int keyIndex)
  {
    return templates.get(keyIndex).getDescription();
  }

  public static String getTemplateSource(final int keyIndex,
                                         final int languageIndex)
  {
    return templates.get(keyIndex).getSource(languageIndex);
  }

  public static void setTemplateSource(final int keyIndex,
                                       final int languageIndex,
                                       final String languageId,
                                       final String source)
  {
    templates.get(keyIndex).config(languageIndex, languageId, source);
  }

  public static int getArgumentsCount(final int keyIndex)
  {
    return templates.get(keyIndex).getArgumentsCount();
  }

  public static String getArgumentName(final int keyIndex,
                                       final int argIndex)
  {
    return templates.get(keyIndex).getArgumentName(argIndex);
  }

  public static String getArgumentDescription(final int keyIndex,
                                              final int argIndex)
  {
    return templates.get(keyIndex).getArgumentDescription(argIndex);
  }

  private static final Template template_booleanValueFalse =
    new Template("booleanValueFalse", "", LANGUAGES_COUNT);

  public String get_booleanValueFalse()
  {
    return instantiateTemplate(template_booleanValueFalse);
  }

  private static final Template template_booleanValueNo =
    new Template("booleanValueNo", "", LANGUAGES_COUNT);

  public String get_booleanValueNo()
  {
    return instantiateTemplate(template_booleanValueNo);
  }

  private static final Template template_booleanValueTrue =
    new Template("booleanValueTrue", "", LANGUAGES_COUNT);

  public String get_booleanValueTrue()
  {
    return instantiateTemplate(template_booleanValueTrue);
  }

  private static final Template template_booleanValueYes =
    new Template("booleanValueYes", "", LANGUAGES_COUNT);

  public String get_booleanValueYes()
  {
    return instantiateTemplate(template_booleanValueYes);
  }

  private static final Template template_currencyFormat =
    new Template("currencyFormat", "", LANGUAGES_COUNT);

  public String get_currencyFormat()
  {
    return instantiateTemplate(template_currencyFormat);
  }

  private static final Template template_dateFormat_dateRange =
    new Template("dateFormat.dateRange", "", LANGUAGES_COUNT, new Argument[] {
      new Argument("start", "range start date"),
      new Argument("stop", "range stop date"),
    });

  public String get_dateFormat_dateRange(final String argument_start,
                                         final String argument_stop)
  { 
    return
      instantiateTemplate(template_dateFormat_dateRange, argument_start, argument_stop);
  }

  private static final Template template_dateFormat_fullDate =
    new Template("dateFormat.fullDate", "", LANGUAGES_COUNT);

  public String get_dateFormat_fullDate()
  {
    return instantiateTemplate(template_dateFormat_fullDate);
  }

  private static final Template template_dateFormat_fullDateTime =
    new Template("dateFormat.fullDateTime", "", LANGUAGES_COUNT);

  public String get_dateFormat_fullDateTime()
  {
    return instantiateTemplate(template_dateFormat_fullDateTime);
  }

  private static final Template template_dateFormat_monthAndDay =
    new Template("dateFormat.monthAndDay", "", LANGUAGES_COUNT);

  public String get_dateFormat_monthAndDay()
  {
    return instantiateTemplate(template_dateFormat_monthAndDay);
  }

  private static final Template template_dateFormat_yearAndMonth =
    new Template("dateFormat.yearAndMonth", "", LANGUAGES_COUNT);

  public String get_dateFormat_yearAndMonth()
  {
    return instantiateTemplate(template_dateFormat_yearAndMonth);
  }

  private static final Template template_dateFormat_yearAndNumericMonth =
    new Template("dateFormat.yearAndNumericMonth", "", LANGUAGES_COUNT);

  public String get_dateFormat_yearAndNumericMonth()
  {
    return instantiateTemplate(template_dateFormat_yearAndNumericMonth);
  }

  private static final Template template_message_error_invalidURL =
    new Template("message.error.invalidURL", "", LANGUAGES_COUNT,
      new Argument[] {
        new Argument("url", "URL"),
      });

  public String get_message_error_invalidURL(final String argument_url)
  {
    return
      instantiateTemplate(template_message_error_invalidURL, argument_url);
  }

  private static final Template template_title_logoff =
    new Template("title.logoff", "", LANGUAGES_COUNT,
      new Argument[] {
        new Argument("name", "user name"),
      });

  public String get_title_logoff(final String argument_name)
  {
    return
      instantiateTemplate(template_title_logoff, argument_name);
  }
  
  private static void init()
  {
    template_dateFormat_monthAndDay.config(Language.DE, "dd. MMMMM");
    template_dateFormat_monthAndDay.config(Language.EN, "MMMMM dd");
    templates.add(template_dateFormat_monthAndDay);
    template_dateFormat_yearAndMonth.config(Language.DE, "MMMMM yyyy");
    template_dateFormat_yearAndMonth.config(Language.EN, "yyyy MMMMM");
    templates.add(template_dateFormat_yearAndMonth);
    template_dateFormat_yearAndNumericMonth.config(Language.DE, "MM/yyyy");
    template_dateFormat_yearAndNumericMonth.config(Language.EN, "yyyy/MM");
    templates.add(template_dateFormat_yearAndNumericMonth);
    template_dateFormat_fullDate.config(Language.DE, "dd.MM.yyyy");
    template_dateFormat_fullDate.config(Language.EN, "yyyy-MM-dd");
    templates.add(template_dateFormat_fullDate);
    template_dateFormat_fullDateTime.config(Language.DE, "dd.MM.yyyy, HH:mm:ss");
    template_dateFormat_fullDateTime.config(Language.EN, "yyyy-MM-dd HH:mm:ss");
    templates.add(template_dateFormat_fullDateTime);
 }
  
  static { init(); }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
