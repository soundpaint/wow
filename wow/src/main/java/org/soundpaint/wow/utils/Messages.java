package org.soundpaint.wow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Messages {

  public static enum Severity {
    INFO, WARNING, ERROR;

    private static final int length = Severity.values().length;

    public static int size() {
      return length;
    }
  };

  private final List<HashMap<Object, List<String>>> messages;
  private Severity maxSeverity;

  public Messages() {
    messages = new ArrayList<HashMap<Object, List<String>>>(Severity.size());
    for (int index = 0; index < Severity.size(); index++) {
      messages.add(new HashMap<Object, List<String>>());
    }
    maxSeverity = null;
  }

  public void add(Severity severity, Object key, String message) {
    int index = severity.ordinal();
    HashMap<Object, List<String>> severityMessages = messages.get(index);
    List<String> messageList;
    if (!severityMessages.containsKey(key)) {
      messageList = new ArrayList<String>();
      severityMessages.put(key, messageList);
    } else {
      messageList = severityMessages.get(key);
    }
    messageList.add(message);
    if ((maxSeverity == null) || (severity.ordinal() > maxSeverity.ordinal()))
      maxSeverity = severity;
  }

  public Severity getMaxSeverity() {
    return maxSeverity;
  }

  public boolean have(Severity severity, Object key) {
    int index = severity.ordinal();
    return messages.get(index).containsKey(key);
  }

  public List<String> get(Severity severity, Object key) {
    int index = severity.ordinal();
    // TODO: Maybe return a copy to avoid the caller manipulating the
    // original List.
    return messages.get(index).get(key);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
