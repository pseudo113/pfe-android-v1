package com.labrosse.suivicommercial.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.labrosse.suivicommercial.MyApplication;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "cupboardTest.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * ====== Table names
     */

    // AD_USER
    public static final String TABLE_AD_USER                = "AD_USER";

    // C_CITY
    public static final String TABLE_C_CITY                 = "C_CITY";

    // C_BPSUB
    public static final String TABLE_C_BPSUB                = "C_BPSUB";

    // C_BPSUB_PRODUCT
    public static final String TABLE_C_BPSUB_PRODUCT        = "C_BPSUB_PRODUCT";

    // C_BPPARCOURS
    public static final String TABLE_C_BPPARCOURS           = "C_BPPARCOURS";

    // C_BPPARC_SUB
    public static final String TABLE_C_BPPARC_SUB                 = "C_BPPARC_SUB";

    // C_BPPARC_VISIT
    public static final String TABLE_C_BPPARC_VISIT         = "C_BPPARC_VISIT";

    // C_BPPARC_VISIT_PROD
    public static final String TABLE_C_BPPARC_VISIT_PROD    = "C_BPPARC_VISIT_PROD";

    // C_BPPARC_VISIT_PIC
    public static final String TABLE_C_BPPARC_VISIT_PIC     = "C_BPPARC_VISIT_PIC";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(MyApplication.getContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // ADUser
        createUserTable(sqLiteDatabase);

        // City
        createCityTable(sqLiteDatabase);

        // Product
        createProductTable(sqLiteDatabase);

        // Filiale
        createFilialeTable(sqLiteDatabase);

        // Parcours
        createParcoursTable(sqLiteDatabase);

        // Theoric Visit
        createTheoricVisitTable(sqLiteDatabase);

        // Real Visit
        createRealVisitTable(sqLiteDatabase);

        // Visit Product
        createVisitProductTable(sqLiteDatabase);

        // Visit Image
        createVisitImageTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     *
     * ===== Table creation
     *
     */

    private void createUserTable(SQLiteDatabase db){
        String sql = "CREATE TABLE AD_USER( "
                       + EntryKey.AD_USER_TABLE.KEY_ID + " INTEGER PRIMARY KEY , "
                       + EntryKey.AD_USER_TABLE.KEY_NAME + " TEXT , "
                       + EntryKey.AD_USER_TABLE.KEY_EMAIL + " TEXT , "
                       + EntryKey.AD_USER_TABLE.KEY_PASSWORD + " TEXT , "
                       + EntryKey.AD_USER_TABLE.KEY_PHONE + " TEXT , "
                       + EntryKey.AD_USER_TABLE.KEY_GPS + " TEXT , "
                       + EntryKey.AD_USER_TABLE.KEY_ACTIVE + " INTEGER , "
                       + EntryKey.AD_USER_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);
    }

    private void createCityTable(SQLiteDatabase db){
        String sql = "CREATE TABLE C_CITY("
                + EntryKey.C_CITY_TABLE.KEY_ID + " INTEGER PRIMARY KEY, "
                + EntryKey.C_CITY_TABLE.KEY_NAME + " TEXT  , "
                + EntryKey.C_CITY_TABLE.KEY_SYNCHRONISED + " TEXT ) ";

        db.execSQL(sql);
    }

    private void createFilialeTable(SQLiteDatabase db){
        String sql = "CREATE TABLE  C_BPSUB("
                + EntryKey.C_BPSUB_TABLE.KEY_ID + " INTEGER PRIMARY KEY, "
                + EntryKey.C_BPSUB_TABLE.KEY_C_BPARTNER_ID + " INTEGER , "
                + EntryKey.C_BPSUB_TABLE.KEY_VALUE + " TEXT , "
                + EntryKey.C_BPSUB_TABLE.KEY_NAME + " TEXT , "
                + EntryKey.C_BPSUB_TABLE.KEY_C_CITY_ID + " INTEGER , "
                + EntryKey.C_BPSUB_TABLE.KEY_ADRESSE + " TEXT , "
                + EntryKey.C_BPSUB_TABLE.KEY_GPS + " TEXT , "
                + EntryKey.C_BPSUB_TABLE.KEY_SYNCHRONISEDATE + " TEXT )";

        db.execSQL(sql);
    }

    private void createProductTable(SQLiteDatabase db){
        String sql = "CREATE TABLE  C_BPSUB_PRODUCT("
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_ID + " INTEGER PRIMARY KEY, "
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_C_BPSUB_ID + " INTEGER , "
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_M_PRODUCT_ID + " INTEGER , "
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTNAME + " TEXT , "
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTVALUE + " TEXT , "
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_QTY_MIN + " INTEGER , "
                    + EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);
    }

    private void createParcoursTable(SQLiteDatabase db){
        String sql = "CREATE TABLE  C_BPPARCOURS("
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_VALUE + " TEXT , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " INTEGER , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_STARTDATE + " DATE , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_ENDDATE  + " DATE , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING + " TEXT , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_STAT + " TEXT , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTREAL + " TEXT , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTTHE + " TEXT , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEPROPOSED + " INTEGER , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEDO + " INTEGER , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_KMREALDO + " INTEGER , "
                    + EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);
    }

    private void createTheoricVisitTable(SQLiteDatabase db){
        String sql = "CREATE TABLE C_BPPARC_SUB("
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPPARCOURS_ID + " INTEGER , "
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_BPPARCOURS_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPSUB_ID + " INTEGER , "
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_SEQ + " INTEGER , "
                + EntryKey.C_BPPARC_SUB_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);
    }

    private void createRealVisitTable(SQLiteDatabase db){
        String sql = "CREATE TABLE C_BPPARC_VISIT("
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_BPPARCOURS_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPSUB_ID + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_SEQ + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_STARTDATE + " DATE , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_ENDDATE + " DATE , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_GPS + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);
    }

    private void createVisitProductTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE C_BPPARC_VISIT_PROD("
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_C_BPPARC_VISIT_ID + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_VISIT_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_M_PRODUCT_ID + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_QTY + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);
    }

    private void createVisitImageTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE  C_BPPARC_VISIT_PIC("
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID + " INTEGER , "
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_VISIT_VALUE + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PIC + " BLOB , "
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICNAME + " TEXT , "
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICDATE + " DATE , "
                + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_SYNCHRONISED + " TEXT )";

        db.execSQL(sql);

    }


}
