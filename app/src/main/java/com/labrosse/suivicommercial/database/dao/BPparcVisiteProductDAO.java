package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.BPparcVisite;
import com.labrosse.suivicommercial.model.database.BPparcVisiteProduct;
import com.labrosse.suivicommercial.model.database.ExtendedBPparcVisiteProduct;

import java.util.ArrayList;
import java.util.Calendar;

import static com.labrosse.suivicommercial.database.DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD;

public class BPparcVisiteProductDAO extends BasicDAO {
    /**
     * Singleton
     */

    private static BPparcVisiteProductDAO sInstance;

    public static synchronized BPparcVisiteProductDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BPparcVisiteProductDAO(context);
        }
        return sInstance;
    }

    public BPparcVisiteProductDAO(Context context) {
        super();
    }

    /**
     * Add
     */

    public long addBPparcVisiteProduct(ExtendedBPparcVisiteProduct product, BPparcVisite c_bpparc_visit) {
        ContentValues cv = new ContentValues();
        cv.putNull(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_ID);
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_VALUE, String.valueOf(Calendar.getInstance().getTimeInMillis()));
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_C_BPPARC_VISIT_ID, c_bpparc_visit.getC_bpparc_visit_id());
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_VISIT_VALUE, c_bpparc_visit.getValue());
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_M_PRODUCT_ID , product.getM_product_id());
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_QTY, product.getQty());
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_SYNCHRONISED, "N");

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD, null, cv);
    }

    /**
     * Update
     */

    public int updateProduct(ExtendedBPparcVisiteProduct product){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_QTY, product.getQty());

        String selection = EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_ID + " = ? AND " + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_C_BPPARC_VISIT_ID + " =  ?";

        String[] selectionArgs = { String.valueOf(product.getC_bpparc_visit_prod()), String.valueOf(product.getC_bpparc_visit()) };

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD, cv, selection, selectionArgs);
    }

    public boolean setSynchronised(int c_bpparc_visit, int c_bpparc_visit_prod) {
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_SYNCHRONISED, EntryKey.KEY_IS_SYNCHRONISED);

        String selection = EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_C_BPPARC_VISIT_ID + " = ? AND "
                            + EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_ID + " =  ?";

        String[] selectionArgs = { String.valueOf(c_bpparc_visit), String.valueOf(c_bpparc_visit_prod) };

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD, cv, selection, selectionArgs) > 0;
    }

    /**
     * Select
     */

    public BPparcVisiteProduct getBPparcVisiteProduct(long id){

        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = getReadableDataBase().query(TABLE_C_BPPARC_VISIT_PROD, null,
                        selection, selectionArgs, null, null, null);

        if(cursor != null && cursor.getCount() > 0){
            BPparcVisiteProduct bPparcVisiteProduct = getBPparcVisiteProductFromCursor(cursor);
            return bPparcVisiteProduct;
        }

        return null;
    }

    public ArrayList<ExtendedBPparcVisiteProduct> getProductsForVisit(long c_bpsub_id, long visit_id,  boolean isNewVisite){

        ArrayList<ExtendedBPparcVisiteProduct> pparcVisiteProducts = null;

        StringBuilder builder;

        // New visite
        if(isNewVisite ==  true){
            builder = new StringBuilder();
            builder.append("SELECT -1 as C_BPPARC_VISIT_PROD_ID, ")
                    .append(" -1 as C_BPPARC_VISIT_ID, ")
                    .append(" product.M_PRODUCT_ID, ")
                    .append(" product.PRODUCTNAME, " )
                    .append(" product.PRODUCTVALUE, " )
                    .append(" 0 as QTY FROM C_BPSUB_PRODUCT  product ")
                    .append(" WHERE product.C_BPSUB_ID = ? ORDER BY product.PRODUCTVALUE ASC");
        }

        // Update
        else{
            builder = new StringBuilder();
            builder.append("SELECT visit_prod.C_BPPARC_VISIT_PROD_ID, ")
                    .append(" visit_prod.C_BPPARC_VISIT_ID, ")
                    .append(" product.M_PRODUCT_ID, ")
                    .append(" product.PRODUCTNAME, " )
                    .append(" product.PRODUCTVALUE, " )
                    .append(" visit_prod.QTY FROM C_BPPARC_VISIT_PROD  visit_prod ")
                    .append(" INNER JOIN C_BPSUB_PRODUCT  product  ")
                    .append(" ON product.M_PRODUCT_ID = visit_prod.M_PRODUCT_ID ")
                    .append(" WHERE product.C_BPSUB_ID = ? AND visit_prod.C_BPPARC_VISIT_ID = ? ORDER BY product.PRODUCTVALUE ASC" );
        }

        //Log.e("getProductsForVisit" , " isNewVisite " +  isNewVisite + " ID " + id + " \n" + builder.toString() );

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(String.valueOf(c_bpsub_id));

        if(visit_id != -1 ){
            arrayList.add(String.valueOf(visit_id));
        }

        // selectionArgs
        String[] selectionArgs =  new String[arrayList.size()];
        selectionArgs = arrayList.toArray(selectionArgs);

        // Execute query
        Cursor cursor = getReadableDataBase().rawQuery(builder.toString(), selectionArgs);

        // Get results
        if(cursor != null && cursor.getCount() > 0){
            pparcVisiteProducts = new ArrayList<>();

            while (cursor.moveToNext()){
                ExtendedBPparcVisiteProduct bPparcVisiteProduct = getExtendedBPparcVisiteProductFromCursor(cursor);
                pparcVisiteProducts.add(bPparcVisiteProduct);
            }

        }

        return pparcVisiteProducts;
    }

    public ArrayList<BPparcVisiteProduct> getProductsToSychronise() {

        String selection =  EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED + " = ?";
        String[] selectionArgs = {EntryKey.KEY_IS_NOT_SYNCHRONISED};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD, null,
                        selection, selectionArgs,
                        null, null, null);

        ArrayList<BPparcVisiteProduct> pparcVisiteProducts = new ArrayList<>();
        // Get results
        if(cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){
                BPparcVisiteProduct bPparcVisiteProduct = getBPparcVisiteProductFromCursor(cursor);
                pparcVisiteProducts.add(bPparcVisiteProduct);
            }
        }

        return pparcVisiteProducts;

    }

    /**
     * Common
     */

    private BPparcVisiteProduct getBPparcVisiteProductFromCursor(Cursor cursor) {
        BPparcVisiteProduct bPparcVisiteProduct = new BPparcVisiteProduct();

        bPparcVisiteProduct.setC_bpparc_visit_prod(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_ID)));
        bPparcVisiteProduct.setValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_VALUE)));
        bPparcVisiteProduct.setC_bpparc_visit(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_C_BPPARC_VISIT_ID)));
        bPparcVisiteProduct.setVisitValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_VISIT_VALUE)));
        bPparcVisiteProduct.setM_product_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_M_PRODUCT_ID)));
        bPparcVisiteProduct.setQty(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_QTY)));
        bPparcVisiteProduct.setSynchronised(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_SYNCHRONISED)));

        return bPparcVisiteProduct;
    }

    private ExtendedBPparcVisiteProduct getExtendedBPparcVisiteProductFromCursor(Cursor cursor) {
        ExtendedBPparcVisiteProduct bPparcVisiteProduct = new ExtendedBPparcVisiteProduct();

        bPparcVisiteProduct.setC_bpparc_visit_prod(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_ID)));
        bPparcVisiteProduct.setC_bpparc_visit(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_C_BPPARC_VISIT_ID)));
        bPparcVisiteProduct.setM_product_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_M_PRODUCT_ID)));
        bPparcVisiteProduct.setProduct_name(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTNAME)));
        bPparcVisiteProduct.setProduct_value(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTVALUE)));
        bPparcVisiteProduct.setQty(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_QTY)));

        return bPparcVisiteProduct;
    }

    public boolean setNonSynchronised(){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_PROD_TABLE.KEY_SYNCHRONISED, "N");
        int count = getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD, cv, null, null);
        return count > 0;
    }
}
