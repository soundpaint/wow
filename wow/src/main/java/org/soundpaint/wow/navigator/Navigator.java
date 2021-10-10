package org.soundpaint.wow.navigator;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.soundpaint.wow.I18N;
import org.soundpaint.wow.auth.SecurityFeatures;

/*
 * Navigator.java --
 * implements data structures that represent the navigation tree
 */
public class Navigator extends AbstractNavigator{

  private static Navigator defaultNavigator;

  public static void initClass(final URI webRoot) {
    if (defaultNavigator != null) {
      throw new RuntimeException("Navigator class already initialized");
    }
    defaultNavigator = new Navigator(webRoot);
  }

  public static Navigator getDefault() {
    if (defaultNavigator == null) {
      throw new RuntimeException("Navigator class not yet initialized");
    }
    return defaultNavigator;
  }

  private final ArrayList<Entry> subentries;
  private final HashMap<EntryIdent, Entry> entriesById;

  // TODO: Per-user navigator that considers the user's
  // specific access rights.  Requires User as argument
  // to the constructor.
  private Navigator(final URI webRoot) {
    super(webRoot);
    subentries = new ArrayList<Entry>();
    entriesById = new HashMap<EntryIdent, Entry>();
    Entry entry;
    entry = new Entry() {
      {
        // rank=10
        id = new EntryIdent("public");
        subentries = new ArrayList<Entry>();
        entriesById = new HashMap<EntryIdent, Entry>();
        setPagePrivilege(SecurityFeatures.PagePrivilege.Page_public);
        setURIFromString("public");
      }
      public String getLocalizedLabel(final I18N i18n) {
        // TODO: return i18n.get_label_Page_public();
        return "Public";
      }
    };
    subentries.add(entry);
    entriesById.put(entry.id, entry);
    entry = new Entry() {
      {
        // rank=50
        id = new EntryIdent("admin");
        subentries = new ArrayList<Entry>();
        entriesById = new HashMap<EntryIdent, Entry>();
        setPagePrivilege(SecurityFeatures.PagePrivilege.Page_admin);
        Entry entry;
        entry = new Entry() {
          {
            // rank=10
            id = new EntryIdent("Services");
            subentries = new ArrayList<Entry>();
            entriesById = new HashMap<EntryIdent, Entry>();
            setPagePrivilege(SecurityFeatures.PagePrivilege.Page_admin_Services);
            setURIFromString("admin/Services");
          }
          public String getLocalizedLabel(final I18N i18n) {
            // TODO: return i18n.get_label_Page_admin_Services();
            return "Admin Services";
          }
        };
        subentries.add(entry);
        entriesById.put(entry.id, entry);
        setURIFromString("admin");
      }
      public String getLocalizedLabel(final I18N i18n) {
        // TODO: return i18n.get_label_Page_admin();
        return "Admin";
      }
    };
    subentries.add(entry);
    entriesById.put(entry.id, entry);
  }

  public Entry lookupEntry(final EntryIdent id) {
    return entriesById.get(id);
  }

  public Entry getEntry(final int index) {
    return subentries.get(index);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
