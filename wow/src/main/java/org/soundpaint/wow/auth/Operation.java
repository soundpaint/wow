package org.soundpaint.wow.auth;

import org.soundpaint.wow.utils.ParseException;

public enum Operation {
  CREATE, READ, UPDATE, DELETE;

  private Operation() {
  }

  public static Operation parse(String operation) {
    if (operation.equals("create"))
      return CREATE;
    else if (operation.equals("read"))
      return READ;
    else if (operation.equals("update"))
      return UPDATE;
    else if (operation.equals("delete"))
      return DELETE;
    else
      throw new ParseException("invalid CRUD operation: " + operation);
  }

  public String toSource() {
    switch (this) {
    case CREATE:
      return "create";
    case READ:
      return "read";
    case UPDATE:
      return "update";
    case DELETE:
      return "delete";
    default:
      throw new InternalError("unexpected case fall through");
    }
  }

  public String toJava() {
    switch (this) {
    case CREATE:
      return "CREATE";
    case READ:
      return "READ";
    case UPDATE:
      return "UPDATE";
    case DELETE:
      return "DELETE";
    default:
      throw new InternalError("unexpected case fall through");
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
