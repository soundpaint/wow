package org.soundpaint.wow.auth;

/*
 * SecurityFeatures.java --
 * implements developer security features
 */
public class SecurityFeatures {
  /******** PAGE PRIVILEGES ********/

  public static enum PagePrivilege implements Privilege {
    Page_public(
      new PrivilegeIdent("Page_Index"), "/Index"
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_Page_public();
      }
    },
    Page_admin(
        new PrivilegeIdent("Page_admin"), "/admin"
      ) {
        public boolean isGrantedForRole(final Role role) {
          return role.havePrivilege_Page_admin();
        }
      },
    Page_admin_Services(
      new PrivilegeIdent("Page_admin_Services"), "/admin/Services"
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_Page_admin_Services();
      }
    };

    private final PrivilegeIdent id;
    private final String navigationPath;

    /**
     * TODO: navigationPath should not be modeled as ordinary String, but as an
     * object that guarantees syntactical correctness of the path.
     */
    private PagePrivilege(final PrivilegeIdent id, final String navigationPath) {
      if (id == null)
        throw new NullPointerException("id");
      this.id = id;
      if (navigationPath == null)
        throw new NullPointerException("navigationPath");
      this.navigationPath = navigationPath;
    }

    public PrivilegeIdent getId() {
      return id;
    }

    public String getNavigationPath() {
      return navigationPath;
    }
  }

  /******** CRUD PRIVILEGES ********/

  public static enum CRUDPrivilege implements Privilege {
    CRUD_createEvent(
      new PrivilegeIdent("CRUD_createEvent"), Operation.CREATE
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_createEvent();
      }
    },
    CRUD_createMember(
      new PrivilegeIdent("CRUD_createMember"), Operation.CREATE
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_createMember();
      }
    },
    CRUD_deleteEvent(
      new PrivilegeIdent("CRUD_deleteEvent"), Operation.DELETE
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_deleteEvent();
      }
    },
    CRUD_deleteMember(
      new PrivilegeIdent("CRUD_deleteMember"), Operation.DELETE
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_deleteMember();
      }
    },
    CRUD_readEvent(
      new PrivilegeIdent("CRUD_readEvent"), Operation.READ
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_readEvent();
      }
    },
    CRUD_readMember(
      new PrivilegeIdent("CRUD_readMember"), Operation.READ
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_readMember();
      }
    },
    CRUD_readTransfer(
      new PrivilegeIdent("CRUD_readTransfer"), Operation.READ
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_readTransfer();
      }
    },
    CRUD_updateEvent(
      new PrivilegeIdent("CRUD_updateEvent"), Operation.UPDATE
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_updateEvent();
      }
    },
    CRUD_updateMember(
      new PrivilegeIdent("CRUD_updateMember"), Operation.UPDATE
    ) {
      public boolean isGrantedForRole(final Role role) {
        return role.havePrivilege_CRUD_updateMember();
      }
    };

    private final PrivilegeIdent id;
    private final Operation op;

    private CRUDPrivilege(final PrivilegeIdent id, final Operation op) {
      if (id == null) {
        throw new NullPointerException("id");
      }
      this.id = id;
      if (op == null) {
        throw new NullPointerException("op");
      }
      this.op = op;
    }

    public PrivilegeIdent getId() {
      return id;
    }

    public Operation getOperation() {
      return op;
    }
  }

  /******** CONTEXTS FOR CRUD PRIVILEGES ********/

  public static final Context memberContext = 
    new Context(new PrivilegeIdent("memberContext"), null);

}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
