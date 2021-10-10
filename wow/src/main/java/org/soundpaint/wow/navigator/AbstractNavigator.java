package org.soundpaint.wow.navigator;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.auth.Privilege;
import org.soundpaint.wow.auth.Role;
import org.soundpaint.wow.auth.SecurityFeatures;
import org.soundpaint.wow.auth.SecurityFeatures.PagePrivilege;

/**
 * Implements the abstract super class for the automatically generated Navigator
 * class that holds data that represents the navigation structure of the site.
 */
public abstract class AbstractNavigator
{
  private Map<Privilege, Entry> privilege2entry =
    new HashMap<Privilege, Entry>();
  private final URI webRoot;

  public abstract class Entry
  {
    protected EntryIdent id;
    protected List<Entry> subentries;
    protected HashMap<EntryIdent, Entry> entriesById;
    private PagePrivilege pagePrivilege;
    private Entry defaultChild;
    private URI uri;

    protected void setPagePrivilege(final PagePrivilege pagePrivilege)
    {
      PagePrivilege oldPagePrivilege = this.pagePrivilege;
      if (privilege2entry.containsKey(oldPagePrivilege)) {
        final Entry oldEntry = privilege2entry.get(oldPagePrivilege);
        if (oldEntry != this) { throw new RuntimeException(
            "navigator-entry mapping corrupted: current page privilege mapped to wrong entry"); }
        privilege2entry.remove(this.pagePrivilege);
      }
      if (privilege2entry.containsKey(pagePrivilege)) { throw new RuntimeException(
          "navigator-entry mapping corrupted: page privilege already mapped to different entry"); }
      this.pagePrivilege = pagePrivilege;
      privilege2entry.put(pagePrivilege, this);
    }

    protected void setDefaultChild(final Entry defaultChild)
    {
      this.defaultChild = defaultChild;
    }

    public Entry getDefaultChild()
    {
      return defaultChild;
    }

    protected void setURIFromString(final String uriStr)
    {
      uri = webRoot.resolve(uriStr);
    }

    public URI getURI()
    {
      return uri;
    }

    public EntryIdent getId()
    {
      return id;
    }

    protected Entry()
    {
      subentries = new ArrayList<Entry>();
    }

    public Entry getChild(final int index)
    {
      return subentries.get(index);
    }

    public int size()
    {
      return subentries.size();
    }

    public abstract String getLocalizedLabel(final I18N i18n);

    public String toString()
    {
      final StringBuffer s = new StringBuffer();
      for (final Entry subentry : subentries) {
        if (s.length() > 0) {
          s.append(", ");
        }
        s.append(subentry);
      }
      final String defaultChildIdStr =
          (defaultChild != null) ? String.valueOf(defaultChild.id) : "";
      return "Entry[id=" + id + ", pagePrivilege=" + pagePrivilege
          + ", defaultChild=" + defaultChildIdStr + ", URI=" + uri
          + ", subentries=" + s + "]";
    }
  }

  protected AbstractNavigator(final URI webRoot)
  {
    if (webRoot == null) {
      throw new NullPointerException("webRoot");
    }
    this.webRoot = webRoot;
  }

  private Entry getEntryByPrivilege(final PagePrivilege pagePrivilege)
  {
    return privilege2entry.get(pagePrivilege);
  }

  public List<Entry> getPrivilegedNavigationEntries(final Role role)
  {
    final List<Entry> privilegedNavigationEntries = new ArrayList<Entry>();
    for (final PagePrivilege pagePrivilege :
         SecurityFeatures.PagePrivilege.values()) {
      if (pagePrivilege.isGrantedForRole(role)) {
        final Entry entry = getEntryByPrivilege(pagePrivilege);
        if (entry == null) {
          if (!pagePrivilege.equals(SecurityFeatures.PagePrivilege.Page_public)) {
            // DEBUG START
            final StringBuffer debugInfo = new StringBuffer();
            for (final Privilege privilege : privilege2entry.keySet()) {
              if (debugInfo.length() > 0) {
                debugInfo.append(", ");
              }
              debugInfo.append("(" + privilege + "->" + privilege2entry.get(privilege)
                  + ")");
            }
            // DEBUG END
            throw new NullPointerException(
                "failed finding entry for privilege " + pagePrivilege
                    + ", privilege2entry=[" + debugInfo + "]");
          } else {
            // The root page is not represented by an entry in the navigator =>
            // skip this entry.
          }
        } else {
          privilegedNavigationEntries.add(entry);
        }
      }
    }
    return privilegedNavigationEntries;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
