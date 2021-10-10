package org.soundpaint.wow;

import java.util.Date;

/**
 * Global configuration variables that are not part of the
 * core Web system infrastructure itself, but are specific for
 * the web application (application domain specific
 * configuration).  That is, all global configuration
 * variables defined in this class are referenced only from
 * (user-defined) templates or template stubs.
 * 
 * TODO: This file should be either automatically created
 * from an XML file, or we should make use of Java annotations
 * for a more stringent format.
 */
public class AppConfig
{
  private static final AppConfig defaultInstance = new AppConfig();

  private static final UserProperty[] userProps = new UserProperty[]
  {
    new UserBooleanProperty(
      "boolean.donationReceipt.isTaxFavoredInstitutionWithDeductibleDues",
      true,
      "True, if the institution is tax favored with deductible dues."
    ),
    new UserStringProperty(
      "label.donationReceipt.assessmentPeriod",
      "2009 bis 2011",
      "The tax assessment period to be printed on donation receipts.",
      (short)255
    ),
    new UserStringProperty(
      "label.donationReceipt.favoredPurposes",
      "Förderung von Kunst und Kultur",
      "A (possible multiple line) text that lists the favored purposes (\"begünstigte Zwecke\") according to EStG.",
      (short)255
    ),
    new UserStringProperty(
      "uri.defaultStartPage",
      "public/Preview",
      "Default URI of page that follows after user login.",
      (short)255
    ),
    new UserStringProperty(
      "filePath.archiveRoot",
      "/home/reuter/work/website/poly-galerie.org/jsp/archive",
      "File system root path of the document archive.",
      (short)255
    ),
    new UserStringProperty(
      "label.donationReceipt.institutionAddress",
      "Viktoriastr. 9, 76133 Karlsruhe",
      "The name of the institution to be printed on donation receipts.",
      (short)255
    ),
    new UserDateProperty(
      "date.donationReceipt.dateOfNoticeOfExemption",
      new Date(1352934000000L) /* 2012-11-15 */,
      "The date of the notice of exemption for this institution.  Format: yyyy-MM-dd (as defined\n    in the java.text.SimpleDateFormat class)."
    ),
    new UserDateProperty(
      "date.systemStart",
      new Date(1199142000000L) /* 2008-01-01 */,
      "The date of the first day for which database values can be considered\n    valid for display over a time axis; database values before that day may reflect\n    summarized values that have been abstracted from individual time of occurrence and\n    therefore may display wrong if event time is significant.\n    Format: yyyy-MM-dd (as defined in the java.text.SimpleDateFormat class).\n    "
    ),
    new UserBooleanProperty(
      "boolean.donationReceipt.isWaivingOfReimbursementOfExpenses",
      false,
      "True, if the donation is waiving of reimbursement of expenses."
    ),
    new UserDateProperty(
      "claim.startDate",
      new Date(1199142000000L) /* 2008-01-01 */,
      "The date of the first day for which claims for member\n    fees are automatically generated.  Format: yyyy-MM-dd (as defined\n    in the java.text.SimpleDateFormat class)."
    ),
    new UserBooleanProperty(
      "boolean.donationReceipt.isPreliminaryNoticeOfExemption",
      false,
      "True, if the notice of exemption is preliminary."
    ),
    new UserStringProperty(
      "label.donationReceipt.taxNumber",
      "35022/71714",
      "The tax number of this institution.",
      (short)255
    ),
    new UserStringProperty(
      "label.donationReceipt.taxOffice",
      "Karlsruhe",
      "The name (typically the location) of the tax office having jurisdiction for this institution.",
      (short)255
    ),
    new UserDateProperty(
      "date.donationReceipt.preliminaryExemptionStartDate",
      new Date(1325372400000L) /* 2012-01-01 */,
      "Start date of exemption.  Format: yyyy-MM-dd (as defined\n    in the java.text.SimpleDateFormat class)."
    ),
    new UserStringProperty(
      "i18n.currencySymbol",
      "€",
      "Symbol of the currency that is used for all monetary\n    data and calculations.",
      (short)10
    ),
    new UserStringProperty(
      "label.donationReceipt.institutionName",
      "Produzentengalerie e.V.",
      "The name of the institution to be printed on donation receipts.",
      (short)255
    ),
    new UserStringProperty(
      "filePath.tmpDir",
      "/tmp",
      "File system path of the directory for temporary files.",
      (short)255
    ),
    new UserStringProperty(
      "label.enterpriseName",
      "Poly Produzentengalerie e.V.",
      "Untranslated name of the enterprise.  Appears on\n    legally required web pages (such as the site's imprint page), and\n    on official reports and certificates, where translation into other\n    languages is not appropriate for legal reasons.",
      (short)120
    ),
    new UserStringProperty(
      "label.treasurerName",
      "Jürgen Reuter",
      "Treasurer's name.  Appears on treasurer's reports and\n    contribution certificates.",
      (short)80
    ),
    new UserStringProperty(
      "label.enterpriseStreetAndCity",
      "Viktoriastr. 9, 76133 Karlsruhe",
      "Untranslated address (street and city) of the\n    enterprise.  Appears on legally required web pages (such as the\n    site's imprint page), and on official reports and certificates,\n    where translation into other languages is not appropriate for\n    legal reasons.",
      (short)80
    )
  };

  public static AppConfig getDefault()
  {
    return defaultInstance;
  }

  public int getPropertiesCount()
  {
    return userProps.length;
  };

  public UserProperty getProperty(final int propertyIndex)
  {
    return userProps[propertyIndex];
  };

  public Boolean get_boolean_donationReceipt_isTaxFavoredInstitutionWithDeductibleDues()
  {
    return ((UserBooleanProperty)userProps[0]).getValue();
  }

  public String get_label_donationReceipt_assessmentPeriod()
  {
    return ((UserStringProperty)userProps[1]).getValue();
  }

  public String get_label_donationReceipt_favoredPurposes()
  {
    return ((UserStringProperty)userProps[2]).getValue();
  }

  public String get_uri_defaultStartPage()
  {
    return ((UserStringProperty)userProps[3]).getValue();
  }

  public String get_filePath_archiveRoot()
  {
    return ((UserStringProperty)userProps[4]).getValue();
  }

  public String get_label_donationReceipt_institutionAddress()
  {
    return ((UserStringProperty)userProps[5]).getValue();
  }

  public Date get_date_donationReceipt_dateOfNoticeOfExemption()
  {
    return ((UserDateProperty)userProps[6]).getValue();
  }

  public Date get_date_systemStart()
  {
    return ((UserDateProperty)userProps[7]).getValue();
  }

  public Boolean get_boolean_donationReceipt_isWaivingOfReimbursementOfExpenses()
  {
    return ((UserBooleanProperty)userProps[8]).getValue();
  }

  public Date get_claim_startDate()
  {
    return ((UserDateProperty)userProps[9]).getValue();
  }

  public Boolean get_boolean_donationReceipt_isPreliminaryNoticeOfExemption()
  {
    return ((UserBooleanProperty)userProps[10]).getValue();
  }

  public String get_label_donationReceipt_taxNumber()
  {
    return ((UserStringProperty)userProps[11]).getValue();
  }

  public String get_label_donationReceipt_taxOffice()
  {
    return ((UserStringProperty)userProps[12]).getValue();
  }

  public Date get_date_donationReceipt_preliminaryExemptionStartDate()
  {
    return ((UserDateProperty)userProps[13]).getValue();
  }

  public String get_i18n_currencySymbol()
  {
    return ((UserStringProperty)userProps[14]).getValue();
  }

  public String get_label_donationReceipt_institutionName()
  {
    return ((UserStringProperty)userProps[15]).getValue();
  }

  public String get_filePath_tmpDir()
  {
    return ((UserStringProperty)userProps[16]).getValue();
  }

  public String get_label_enterpriseName()
  {
    return ((UserStringProperty)userProps[17]).getValue();
  }

  public String get_label_treasurerName()
  {
    return ((UserStringProperty)userProps[18]).getValue();
  }

  public String get_label_enterpriseStreetAndCity()
  {
    return ((UserStringProperty)userProps[19]).getValue();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:java
 * End:
 */
