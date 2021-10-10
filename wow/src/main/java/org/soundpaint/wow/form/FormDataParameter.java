package org.soundpaint.wow.form;

public class FormDataParameter
{
  private String name;
  private AbstractFormDataValue value;

  private FormDataParameter() {}

  public FormDataParameter(final String name, final AbstractFormDataValue value)
  {
    this();
    this.name = name;
    this.value = value;
  }

  public String getName()
  {
    return name;
  }

  public AbstractFormDataValue getValue()
  {
    return value;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
