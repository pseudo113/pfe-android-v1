package com.labrosse.suivicommercial.database;


public interface EntryKey {
    // constant keys
    String KEY_IS_SYNCHRONISED = "Y";
    String KEY_IS_NOT_SYNCHRONISED = "N";


    // AD_USER
    interface AD_USER_TABLE {
        // TABLE AD_USER
        String KEY_ID           = "AD_USER_ID";
        String KEY_NAME         = "NAME";
        String KEY_EMAIL        = "EMAIL";
        String KEY_PASSWORD     = "PASSWORD";
        String KEY_PHONE        = "PHONE";
        String KEY_GPS          = "GPS";
        String KEY_ACTIVE       = "AD_USER_ACTIVE";
        String KEY_SYNCHRONISED = "SYNCHRONISED";

        // constant keys
        int KEY_AD_USER_IS_ACTIVE = 1;
        int KEY_AD_USER_IS_INACTIVE = 0;
    }

    // TABLE ZONE
    interface C_CITY_TABLE {
        String KEY_ID = "C_CITY_ID";
        String KEY_NAME = "NAME";
        String KEY_SYNCHRONISED = "SYNCHRONISED";
    }

    // TABLE FILIALE
    interface C_BPSUB_TABLE{
        String KEY_ID               = "C_BPSUB_ID";
        String KEY_C_BPARTNER_ID    = "C_BPARTNER_ID";
        String KEY_VALUE            = "VALUE";
        String KEY_NAME             = "NAME";
        String KEY_C_CITY_ID        = "C_CITY_ID";
        String KEY_ADRESSE          = "ADRESSE";
        String KEY_GPS              = "GPS";
        String KEY_SYNCHRONISEDATE  = "SYNCHRONISEDATE";
    }

    // C_BPSUB_PRODUCT
    interface C_BPSUB_PRODUCT_TABLE{
        String KEY_ID               = "C_BPSUB_PRODUCT_ID";
        String KEY_C_BPSUB_ID       = "C_BPSUB_ID";
        String KEY_M_PRODUCT_ID     = "M_PRODUCT_ID";
        String KEY_PRODUCTNAME      = "PRODUCTNAME";
        String KEY_PRODUCTVALUE     = "PRODUCTVALUE";
        String KEY_QTY_MIN          = "QTY_MIN";
        String KEY_SYNCHRONISED     = "SYNCHRONISED";
    }

    // C_BPPARCOURS
    interface C_BPPARCOURS_TABLE{
        String KEY_ID              = "C_BPPARCOURS_ID";
        String KEY_AD_USER_ID      = "AD_USER_ID";
        String KEY_STARTDATE       = "STARTDATE";
        String KEY_ENDDATE         = "ENDDATE";
        String KEY_PROCESSING      = "PROCESSING";
        String KEY_STAT            = "STAT";
        String KEY_GPSSTARTREAL    = "GPSSTARTREAL";
        String KEY_GPSSTARTTHE     = "GPSSTARTTHE";
        String KEY_KMTHEPROPOSED   = "KMTHEPROPOSED";
        String KEY_KMTHEDO         = "KMTHEDO";
        String KEY_KMREALDO        = "KMREALDO";
        String KEY_SYNCHRONISED    = "SYNCHRONISED";
        String KEY_VALUE           = "VALUE";

        // constant keys
        String KEY_IS_STARTED       = "D";
        String KEY_IS_STOPED        = "A";

        String KEY_IS_KEY_PROCESSING  = "Y";
        String KEY_IS_KEY_NOT_PROCESSING  = "N";

    }

    // TABLE BPPARC_SUB
    interface C_BPPARC_SUB_TABLE{
        String KEY_ID                  = "C_BPPARC_SUB_ID";
        String KEY_VALUE               = "VALUE";
        String KEY_C_BPPARCOURS_ID     = "C_BPPARCOURS_ID";
        String KEY_BPPARCOURS_VALUE    = "BPPARCOURS_VALUE";
        String KEY_C_BPSUB_ID          = "C_BPSUB_ID";
        String KEY_SEQ                 = "SEQ";
        String KEY_SYNCHRONISED        = "SYNCHRONISED";
    }

    // TABLE C_BPPARC_VISIT
    interface C_BPPARC_VISIT_TABLE{

        String KEY_ID                = "C_BPPARC_VISIT_ID";
        String KEY_VALUE             = "VALUE";
        String KEY_C_BPPARCOURS_ID   = "C_BPPARCOURS_ID";
        String KEY_BPPARCOURS_VALUE  = "BPPARCOURS_VALUE";
        String KEY_C_BPSUB_ID        = "C_BPSUB_ID";
        String KEY_SEQ               = "SEQ";
        String KEY_STARTDATE         = "STARTDATE";
        String KEY_ENDDATE           = "ENDDATE";
        String KEY_PROCESSING        = "PROCESSING";
        String KEY_GPS               = "GPS";
        String KEY_SYNCHRONISED      = "SYNCHRONISED";

        String KEY_IS_KEY_PROCESSING  = "Y";
        String KEY_IS_KEY_NOT_PROCESSING  = "N";
    }

    // TABLE VISITE_IMAGE
    interface C_BPPARC_VISIT_PIC_TABLE{
        String KEY_ID                       = "C_BPPARC_VISIT_PIC_ID";
        String KEY_C_BPPARC_VISIT_ID        = "C_BPPARC_VISIT_ID";
        String KEY_VISIT_VALUE              = "VISIT_VALUE";
        String KEY_PIC                      = "PIC";
        String KEY_PICNAME                  = "PICNAME";
        String KEY_PICDATE                  = "PICDATE";
        String KEY_SYNCHRONISED             = "SYNCHRONISED";
    }

    // TABLE C_BPPARC_VISIT_PROD
    interface C_BPPARC_VISIT_PROD_TABLE{
        String KEY_ID                   = "C_BPPARC_VISIT_PROD_ID";
        String KEY_VALUE                = "VALUE";
        String KEY_C_BPPARC_VISIT_ID    = "C_BPPARC_VISIT_ID";
        String KEY_VISIT_VALUE          = "VISIT_VALUE";
        String KEY_M_PRODUCT_ID         = "M_PRODUCT_ID";
        String KEY_QTY                  = "QTY";
        String KEY_SYNCHRONISED         = "SYNCHRONISED";
    }

}
