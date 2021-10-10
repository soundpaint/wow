package org.soundpaint.wow.auth;

import java.net.URI;
import java.util.HashMap;

import org.soundpaint.wow.db.types.DBGGUID;
import org.soundpaint.wow.utils.Ident;
import org.soundpaint.wow.utils.SimpleIdent;

/*
 * Role.java --
 * implements the generic role model
 * Automatically generated on Thu Aug 13 03:29:38 CEST 2015.
 * Created by org.poly.security.CustomerSecurityFeatures.
 *
 * (C) 2013 by Jürgen Reuter, 76185 Karlsruhe, Germany
 */
public class Role extends AbstractRole {

  // privileges

  private int privilege_Page_ = 0;

  protected boolean havePrivilege_Page_() {
    return
      privilege_Page_ > 0;
  }

  private int privilege_Page_public = 0;

  protected boolean havePrivilege_Page_public() {
    return
      privilege_Page_public > 0;
  }

  private int privilege_Page_public_Home = 0;

  protected boolean havePrivilege_Page_public_Home() {
    return
      privilege_Page_public_Home > 0;
  }

  private int privilege_Page_public_News = 0;

  protected boolean havePrivilege_Page_public_News() {
    return
      privilege_Page_public_News > 0;
  }

  private int privilege_Page_public_Preview = 0;

  protected boolean havePrivilege_Page_public_Preview() {
    return
      privilege_Page_public_Preview > 0;
  }

  private int privilege_Page_public_Archive = 0;

  protected boolean havePrivilege_Page_public_Archive() {
    return
      privilege_Page_public_Archive > 0;
  }

  private int privilege_Page_public_About = 0;

  protected boolean havePrivilege_Page_public_About() {
    return
      privilege_Page_public_About > 0;
  }

  private int privilege_Page_public_PlantLayout = 0;

  protected boolean havePrivilege_Page_public_PlantLayout() {
    return
      privilege_Page_public_PlantLayout > 0;
  }

  private int privilege_Page_public_Articles = 0;

  protected boolean havePrivilege_Page_public_Articles() {
    return
      privilege_Page_public_Articles > 0;
  }

  private int privilege_Page_public_Events = 0;

  protected boolean havePrivilege_Page_public_Events() {
    return
      privilege_Page_public_Events > 0;
  }

  private int privilege_Page_public_ContactList = 0;

  protected boolean havePrivilege_Page_public_ContactList() {
    return
      privilege_Page_public_ContactList > 0;
  }

  private int privilege_Page_public_Links = 0;

  protected boolean havePrivilege_Page_public_Links() {
    return
      privilege_Page_public_Links > 0;
  }

  private int privilege_Page_public_Contact = 0;

  protected boolean havePrivilege_Page_public_Contact() {
    return
      privilege_Page_public_Contact > 0;
  }

  private int privilege_Page_members = 0;

  protected boolean havePrivilege_Page_members() {
    return
      privilege_Page_members > 0;
  }

  private int privilege_Page_members_Members = 0;

  protected boolean havePrivilege_Page_members_Members() {
    return
      privilege_Page_members_Members > 0;
  }

  private int privilege_Page_members_Members_ProfileView = 0;

  protected boolean havePrivilege_Page_members_Members_ProfileView() {
    return
      privilege_Page_members_Members_ProfileView > 0;
  }

  private int privilege_Page_members_Members_MemberStatusHistory = 0;

  protected boolean havePrivilege_Page_members_Members_MemberStatusHistory() {
    return
      privilege_Page_members_Members_MemberStatusHistory > 0;
  }

  private int privilege_Page_members_Members_AccountStatement = 0;

  protected boolean havePrivilege_Page_members_Members_AccountStatement() {
    return
      privilege_Page_members_Members_AccountStatement > 0;
  }

  private int privilege_Page_members_Events = 0;

  protected boolean havePrivilege_Page_members_Events() {
    return
      privilege_Page_members_Events > 0;
  }

  private int privilege_Page_members_Events_EventView = 0;

  protected boolean havePrivilege_Page_members_Events_EventView() {
    return
      privilege_Page_members_Events_EventView > 0;
  }

  private int privilege_Page_members_Events_EventRegistrations = 0;

  protected boolean havePrivilege_Page_members_Events_EventRegistrations() {
    return
      privilege_Page_members_Events_EventRegistrations > 0;
  }

  private int privilege_Page_members_MembershipCertificate = 0;

  protected boolean havePrivilege_Page_members_MembershipCertificate() {
    return
      privilege_Page_members_MembershipCertificate > 0;
  }

  private int privilege_Page_members_Protocols = 0;

  protected boolean havePrivilege_Page_members_Protocols() {
    return
      privilege_Page_members_Protocols > 0;
  }

  private int privilege_Page_finances = 0;

  protected boolean havePrivilege_Page_finances() {
    return
      privilege_Page_finances > 0;
  }

  private int privilege_Page_finances_TreasurersReport = 0;

  protected boolean havePrivilege_Page_finances_TreasurersReport() {
    return
      privilege_Page_finances_TreasurersReport > 0;
  }

  private int privilege_Page_finances_GlobalAccountStatement = 0;

  protected boolean havePrivilege_Page_finances_GlobalAccountStatement() {
    return
      privilege_Page_finances_GlobalAccountStatement > 0;
  }

  private int privilege_Page_finances_ContributionCertificate = 0;

  protected boolean havePrivilege_Page_finances_ContributionCertificate() {
    return
      privilege_Page_finances_ContributionCertificate > 0;
  }

  private int privilege_Page_finances_OpenClaims = 0;

  protected boolean havePrivilege_Page_finances_OpenClaims() {
    return
      privilege_Page_finances_OpenClaims > 0;
  }

  private int privilege_Page_finances_DonationReceipt = 0;

  protected boolean havePrivilege_Page_finances_DonationReceipt() {
    return
      privilege_Page_finances_DonationReceipt > 0;
  }

  private int privilege_Page_files = 0;

  protected boolean havePrivilege_Page_files() {
    return
      privilege_Page_files > 0;
  }

  private int privilege_Page_files_DocumentList = 0;

  protected boolean havePrivilege_Page_files_DocumentList() {
    return
      privilege_Page_files_DocumentList > 0;
  }

  private int privilege_Page_admin = 0;

  protected boolean havePrivilege_Page_admin() {
    return
      privilege_Page_admin > 0;
  }

  private int privilege_Page_admin_Services = 0;

  protected boolean havePrivilege_Page_admin_Services() {
    return
      privilege_Page_admin_Services > 0;
  }

  private int privilege_Page_admin_GlobalConfig = 0;

  protected boolean havePrivilege_Page_admin_GlobalConfig() {
    return
      privilege_Page_admin_GlobalConfig > 0;
  }

  private int privilege_Page_admin_EnvironmentInfo = 0;

  protected boolean havePrivilege_Page_admin_EnvironmentInfo() {
    return
      privilege_Page_admin_EnvironmentInfo > 0;
  }

  private int privilege_Page_admin_SysInfo = 0;

  protected boolean havePrivilege_Page_admin_SysInfo() {
    return
      privilege_Page_admin_SysInfo > 0;
  }

  private int privilege_Page_admin_I18NConfig = 0;

  protected boolean havePrivilege_Page_admin_I18NConfig() {
    return
      privilege_Page_admin_I18NConfig > 0;
  }

  private int privilege_Page_admin_UsersView = 0;

  protected boolean havePrivilege_Page_admin_UsersView() {
    return
      privilege_Page_admin_UsersView > 0;
  }

  private int privilege_Page_dbTables = 0;

  protected boolean havePrivilege_Page_dbTables() {
    return
      privilege_Page_dbTables > 0;
  }

  private int privilege_Page_dbTables_Address = 0;

  protected boolean havePrivilege_Page_dbTables_Address() {
    return
      privilege_Page_dbTables_Address > 0;
  }

  private int privilege_Page_dbTables_PrivilegeSet = 0;

  protected boolean havePrivilege_Page_dbTables_PrivilegeSet() {
    return
      privilege_Page_dbTables_PrivilegeSet > 0;
  }

  private int privilege_Page_dbTables_Navigator = 0;

  protected boolean havePrivilege_Page_dbTables_Navigator() {
    return
      privilege_Page_dbTables_Navigator > 0;
  }

  private int privilege_Page_dbTables_Role = 0;

  protected boolean havePrivilege_Page_dbTables_Role() {
    return
      privilege_Page_dbTables_Role > 0;
  }

  private int privilege_Page_dbTables_Transfer = 0;

  protected boolean havePrivilege_Page_dbTables_Transfer() {
    return
      privilege_Page_dbTables_Transfer > 0;
  }

  private int privilege_Page_dbTables_Claim = 0;

  protected boolean havePrivilege_Page_dbTables_Claim() {
    return
      privilege_Page_dbTables_Claim > 0;
  }

  private int privilege_Page_dbTables_Event = 0;

  protected boolean havePrivilege_Page_dbTables_Event() {
    return
      privilege_Page_dbTables_Event > 0;
  }

  private int privilege_Page_dbTables_Registration = 0;

  protected boolean havePrivilege_Page_dbTables_Registration() {
    return
      privilege_Page_dbTables_Registration > 0;
  }

  private int privilege_Page_dbTables_User = 0;

  protected boolean havePrivilege_Page_dbTables_User() {
    return
      privilege_Page_dbTables_User > 0;
  }

  private int privilege_Page_dbTables_Config = 0;

  protected boolean havePrivilege_Page_dbTables_Config() {
    return
      privilege_Page_dbTables_Config > 0;
  }

  private int privilege_Page_dbTables_SystemEnumerationTypes = 0;

  protected boolean havePrivilege_Page_dbTables_SystemEnumerationTypes() {
    return
      privilege_Page_dbTables_SystemEnumerationTypes > 0;
  }

  private int privilege_Page_dbTables_Appointment = 0;

  protected boolean havePrivilege_Page_dbTables_Appointment() {
    return
      privilege_Page_dbTables_Appointment > 0;
  }

  private int privilege_Page_dbTables_UserStatusChange = 0;

  protected boolean havePrivilege_Page_dbTables_UserStatusChange() {
    return
      privilege_Page_dbTables_UserStatusChange > 0;
  }

  private int privilege_Page_dbTables_SystemEnumerationTypesValues = 0;

  protected boolean havePrivilege_Page_dbTables_SystemEnumerationTypesValues() {
    return
      privilege_Page_dbTables_SystemEnumerationTypesValues > 0;
  }

  private int privilege_CRUD_createEvent = 0;

  protected boolean havePrivilege_CRUD_createEvent() {
    return
      privilege_CRUD_createEvent > 0;
  }

  private int privilege_CRUD_createMember = 0;

  protected boolean havePrivilege_CRUD_createMember() {
    return
      privilege_CRUD_createMember > 0;
  }

  private int privilege_CRUD_deleteEvent = 0;

  protected boolean havePrivilege_CRUD_deleteEvent() {
    return
      privilege_CRUD_deleteEvent > 0;
  }

  private int privilege_CRUD_deleteMember = 0;

  protected boolean havePrivilege_CRUD_deleteMember() {
    return
      privilege_CRUD_deleteMember > 0;
  }

  private int privilege_CRUD_readEvent = 0;

  protected boolean havePrivilege_CRUD_readEvent() {
    return
      privilege_CRUD_readEvent > 0;
  }

  private int privilege_CRUD_readMember = 0;

  protected boolean havePrivilege_CRUD_readMember() {
    return
      privilege_CRUD_readMember > 0;
  }

  private int privilege_CRUD_readTransfer = 0;

  protected boolean havePrivilege_CRUD_readTransfer() {
    return
      privilege_CRUD_readTransfer > 0;
  }

  private int privilege_CRUD_updateEvent = 0;

  protected boolean havePrivilege_CRUD_updateEvent() {
    return
      privilege_CRUD_updateEvent > 0;
  }

  private int privilege_CRUD_updateMember = 0;

  protected boolean havePrivilege_CRUD_updateMember() {
    return
      privilege_CRUD_updateMember > 0;
  }

  // privilege sets

  public static enum PrivilegeSet implements AbstractRole.AbstractPrivilegeSet {

    Page_admin_SysInfo("Page_admin_SysInfo", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin_SysInfo();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin_SysInfo += increment;
      }
    }),

    Page_dbTables_Address("Page_dbTables_Address", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Address();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Address += increment;
      }
    }),

    Page_admin_Services("Page_admin_Services", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin_Services();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin_Services += increment;
      }
    }),

    Page_files("Page_files", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_files();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_files += increment;
      }
    }),

    Page_members_Events_EventView("Page_members_Events_EventView", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Events_EventView();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Events_EventView += increment;
      }
    }),

    Page_dbTables_Navigator("Page_dbTables_Navigator", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Navigator();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Navigator += increment;
      }
    }),

    Page_public_About("Page_public_About", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_About();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_About += increment;
      }
    }),

    Page_members_MembershipCertificate("Page_members_MembershipCertificate", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_MembershipCertificate();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_MembershipCertificate += increment;
      }
    }),

    Page_("Page_", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_ += increment;
      }
    }),

    Page_public_PlantLayout("Page_public_PlantLayout", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_PlantLayout();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_PlantLayout += increment;
      }
    }),

    viewPostalAddress("viewPostalAddress", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_CRUD_readMember();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_CRUD_readMember += increment;
      }
    }),

    Page_dbTables_Claim("Page_dbTables_Claim", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Claim();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Claim += increment;
      }
    }),

    viewEvent("viewEvent", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_CRUD_readEvent();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_CRUD_readEvent += increment;
      }
    }),

    editMember("editMember", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_CRUD_readMember() && 
          role.havePrivilege_CRUD_updateMember() && 
          role.havePrivilege_CRUD_createMember() && 
          role.havePrivilege_CRUD_deleteMember();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_CRUD_readMember += increment;
        role.privilege_CRUD_updateMember += increment;
        role.privilege_CRUD_createMember += increment;
        role.privilege_CRUD_deleteMember += increment;
      }
    }),

    viewMember("viewMember", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_CRUD_readMember();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_CRUD_readMember += increment;
      }
    }),

    Page_members_Events_EventRegistrations("Page_members_Events_EventRegistrations", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Events_EventRegistrations();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Events_EventRegistrations += increment;
      }
    }),

    Page_finances_GlobalAccountStatement("Page_finances_GlobalAccountStatement", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_finances_GlobalAccountStatement();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_finances_GlobalAccountStatement += increment;
      }
    }),

    Page_dbTables_SystemEnumerationTypesValues("Page_dbTables_SystemEnumerationTypesValues", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_SystemEnumerationTypesValues();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_SystemEnumerationTypesValues += increment;
      }
    }),

    Page_public_News("Page_public_News", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_News();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_News += increment;
      }
    }),

    Page_admin("Page_admin", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin += increment;
      }
    }),

    Page_members_Members_MemberStatusHistory("Page_members_Members_MemberStatusHistory", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Members_MemberStatusHistory();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Members_MemberStatusHistory += increment;
      }
    }),

    Page_admin_I18NConfig("Page_admin_I18NConfig", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin_I18NConfig();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin_I18NConfig += increment;
      }
    }),

    Page_dbTables_Appointment("Page_dbTables_Appointment", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Appointment();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Appointment += increment;
      }
    }),

    Page_finances_OpenClaims("Page_finances_OpenClaims", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_finances_OpenClaims();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_finances_OpenClaims += increment;
      }
    }),

    Page_public_Contact("Page_public_Contact", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Contact();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Contact += increment;
      }
    }),

    Page_dbTables_Event("Page_dbTables_Event", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Event();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Event += increment;
      }
    }),

    viewTransfer("viewTransfer", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_CRUD_readTransfer();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_CRUD_readTransfer += increment;
      }
    }),

    Page_public_Links("Page_public_Links", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Links();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Links += increment;
      }
    }),

    editEvent("editEvent", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_CRUD_createEvent() && 
          role.havePrivilege_CRUD_deleteEvent() && 
          role.havePrivilege_CRUD_updateEvent() && 
          role.havePrivilege_CRUD_readEvent();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_CRUD_createEvent += increment;
        role.privilege_CRUD_deleteEvent += increment;
        role.privilege_CRUD_updateEvent += increment;
        role.privilege_CRUD_readEvent += increment;
      }
    }),

    Page_public_Articles("Page_public_Articles", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Articles();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Articles += increment;
      }
    }),

    Page_dbTables_PrivilegeSet("Page_dbTables_PrivilegeSet", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_PrivilegeSet();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_PrivilegeSet += increment;
      }
    }),

    Page_admin_EnvironmentInfo("Page_admin_EnvironmentInfo", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin_EnvironmentInfo();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin_EnvironmentInfo += increment;
      }
    }),

    Page_public_ContactList("Page_public_ContactList", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_ContactList();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_ContactList += increment;
      }
    }),

    Page_members("Page_members", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members += increment;
      }
    }),

    Page_admin_GlobalConfig("Page_admin_GlobalConfig", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin_GlobalConfig();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin_GlobalConfig += increment;
      }
    }),

    Page_dbTables_Role("Page_dbTables_Role", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Role();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Role += increment;
      }
    }),

    Page_public_Home("Page_public_Home", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Home();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Home += increment;
      }
    }),

    Page_dbTables_UserStatusChange("Page_dbTables_UserStatusChange", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_UserStatusChange();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_UserStatusChange += increment;
      }
    }),

    Page_finances_DonationReceipt("Page_finances_DonationReceipt", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_finances_DonationReceipt();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_finances_DonationReceipt += increment;
      }
    }),

    Page_public_Preview("Page_public_Preview", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Preview();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Preview += increment;
      }
    }),

    Page_members_Members_AccountStatement("Page_members_Members_AccountStatement", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Members_AccountStatement();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Members_AccountStatement += increment;
      }
    }),

    Page_dbTables_User("Page_dbTables_User", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_User();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_User += increment;
      }
    }),

    Page_dbTables_Registration("Page_dbTables_Registration", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Registration();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Registration += increment;
      }
    }),

    Page_dbTables("Page_dbTables", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables += increment;
      }
    }),

    Page_members_Protocols("Page_members_Protocols", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Protocols();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Protocols += increment;
      }
    }),

    Page_finances_ContributionCertificate("Page_finances_ContributionCertificate", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_finances_ContributionCertificate();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_finances_ContributionCertificate += increment;
      }
    }),

    Page_admin_UsersView("Page_admin_UsersView", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_admin_UsersView();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_admin_UsersView += increment;
      }
    }),

    Page_dbTables_SystemEnumerationTypes("Page_dbTables_SystemEnumerationTypes", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_SystemEnumerationTypes();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_SystemEnumerationTypes += increment;
      }
    }),

    Page_members_Members_ProfileView("Page_members_Members_ProfileView", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Members_ProfileView();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Members_ProfileView += increment;
      }
    }),

    Page_public_Archive("Page_public_Archive", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Archive();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Archive += increment;
      }
    }),

    Page_public_Events("Page_public_Events", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public_Events();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public_Events += increment;
      }
    }),

    Page_finances("Page_finances", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_finances();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_finances += increment;
      }
    }),

    Page_dbTables_Transfer("Page_dbTables_Transfer", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Transfer();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Transfer += increment;
      }
    }),

    Page_public("Page_public", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_public();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_public += increment;
      }
    }),

    Page_dbTables_Config("Page_dbTables_Config", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_dbTables_Config();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_dbTables_Config += increment;
      }
    }),

    Page_files_DocumentList("Page_files_DocumentList", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_files_DocumentList();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_files_DocumentList += increment;
      }
    }),

    Page_members_Events("Page_members_Events", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Events();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Events += increment;
      }
    }),

    Page_finances_TreasurersReport("Page_finances_TreasurersReport", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_finances_TreasurersReport();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_finances_TreasurersReport += increment;
      }
    }),

    Page_members_Members("Page_members_Members", new PrivilegeSetImplementation() {
      public boolean isGrantedForRole(final Role role) {
        return
          role.havePrivilege_Page_members_Members();
      }

      public void updateRoleForThisPrivilege(final Role role, final int increment) {
        role.privilege_Page_members_Members += increment;
      }
    });

    private static interface PrivilegeSetImplementation {
      public boolean isGrantedForRole(final Role role);

      public void updateRoleForThisPrivilege(final Role role, final int increment);
    }

    private static HashMap<SimpleIdent, AbstractPrivilegeSet> id2privilegeSet;

    private final SimpleIdent id;
    private final PrivilegeSetImplementation privilegeSetImplementation;

    private PrivilegeSet(final String id, final PrivilegeSetImplementation privilegeSetImplementation) {
      this.id = PrivilegeSetIdent.parse(id);
      this.privilegeSetImplementation = privilegeSetImplementation;
      addPrivilegeSet(this.id, this);
    }

    public Ident getId() {
      return id;
    }

    public boolean isGrantedForRole(final AbstractRole role) {
      return privilegeSetImplementation.isGrantedForRole((Role)role);
    }

    public void updateRoleForThisPrivilege(final AbstractRole role, final int increment) {
      privilegeSetImplementation.updateRoleForThisPrivilege((Role)role, increment);
    }

    private static synchronized void addPrivilegeSet(final SimpleIdent ident, final AbstractPrivilegeSet privilegeSet) {
      if (id2privilegeSet == null) {
        id2privilegeSet = new HashMap<SimpleIdent, AbstractPrivilegeSet>();
      }
      id2privilegeSet.put(ident, privilegeSet);
    }
  }

  public Role createCloneWithGGUID(final DBGGUID gguid) {
    final Role role = new Role(id, gguid, defaultPageURI);
    for (final AbstractPrivilegeSet privilegeSet : PrivilegeSet.id2privilegeSet.values()) {
      role.addPrivilegeSet(privilegeSet);
    }
    return role;
  }

  public AbstractPrivilegeSet lookupPrivilegeSet(final PrivilegeSetIdent ident) {
    return PrivilegeSet.id2privilegeSet.get(ident);
  }

  public Role(final RoleIdent id) {
    super(id);
  }

  public Role(final RoleIdent id, final DBGGUID gguid, final URI defaultPageURI) {
    super(id, gguid, defaultPageURI);
  }

  public void clear() {
    privilegeSets.clear();
    privilege_Page_ = 0;
    privilege_Page_public = 0;
    privilege_Page_public_Home = 0;
    privilege_Page_public_News = 0;
    privilege_Page_public_Preview = 0;
    privilege_Page_public_Archive = 0;
    privilege_Page_public_About = 0;
    privilege_Page_public_PlantLayout = 0;
    privilege_Page_public_Articles = 0;
    privilege_Page_public_Events = 0;
    privilege_Page_public_ContactList = 0;
    privilege_Page_public_Links = 0;
    privilege_Page_public_Contact = 0;
    privilege_Page_members = 0;
    privilege_Page_members_Members = 0;
    privilege_Page_members_Members_ProfileView = 0;
    privilege_Page_members_Members_MemberStatusHistory = 0;
    privilege_Page_members_Members_AccountStatement = 0;
    privilege_Page_members_Events = 0;
    privilege_Page_members_Events_EventView = 0;
    privilege_Page_members_Events_EventRegistrations = 0;
    privilege_Page_members_MembershipCertificate = 0;
    privilege_Page_members_Protocols = 0;
    privilege_Page_finances = 0;
    privilege_Page_finances_TreasurersReport = 0;
    privilege_Page_finances_GlobalAccountStatement = 0;
    privilege_Page_finances_ContributionCertificate = 0;
    privilege_Page_finances_OpenClaims = 0;
    privilege_Page_finances_DonationReceipt = 0;
    privilege_Page_files = 0;
    privilege_Page_files_DocumentList = 0;
    privilege_Page_admin = 0;
    privilege_Page_admin_Services = 0;
    privilege_Page_admin_GlobalConfig = 0;
    privilege_Page_admin_EnvironmentInfo = 0;
    privilege_Page_admin_SysInfo = 0;
    privilege_Page_admin_I18NConfig = 0;
    privilege_Page_admin_UsersView = 0;
    privilege_Page_dbTables = 0;
    privilege_Page_dbTables_Address = 0;
    privilege_Page_dbTables_PrivilegeSet = 0;
    privilege_Page_dbTables_Navigator = 0;
    privilege_Page_dbTables_Role = 0;
    privilege_Page_dbTables_Transfer = 0;
    privilege_Page_dbTables_Claim = 0;
    privilege_Page_dbTables_Event = 0;
    privilege_Page_dbTables_Registration = 0;
    privilege_Page_dbTables_User = 0;
    privilege_Page_dbTables_Config = 0;
    privilege_Page_dbTables_SystemEnumerationTypes = 0;
    privilege_Page_dbTables_Appointment = 0;
    privilege_Page_dbTables_UserStatusChange = 0;
    privilege_Page_dbTables_SystemEnumerationTypesValues = 0;
    privilege_CRUD_createEvent = 0;
    privilege_CRUD_createMember = 0;
    privilege_CRUD_deleteEvent = 0;
    privilege_CRUD_deleteMember = 0;
    privilege_CRUD_readEvent = 0;
    privilege_CRUD_readMember = 0;
    privilege_CRUD_readTransfer = 0;
    privilege_CRUD_updateEvent = 0;
    privilege_CRUD_updateMember = 0;
  }

}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
