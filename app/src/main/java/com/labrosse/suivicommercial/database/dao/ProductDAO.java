package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.MProduct;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ahmedhammami on 31/12/2016.
 */

public class ProductDAO extends BasicDAO{
    /**
     * Singleton
     */
    private static ProductDAO sInstance;

    public static synchronized ProductDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ProductDAO(context);
        }
        return sInstance;
    }

    public ProductDAO(Context context) {
        super();
    }

    public long addProduct(MProduct product){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_ID, product.getC_bpsub_product_id());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_C_BPSUB_ID, product.getC_bpsub_id());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_M_PRODUCT_ID, product.getM_product_id());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTNAME, product.getProductname());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTVALUE, product.getProductvalue());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_QTY_MIN, product.getQty_min());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_SYNCHRONISED, getSynchroniseDate(Calendar.getInstance().getTime()) );

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPSUB_PRODUCT, null, cv);
    }

    public int updateProduct(MProduct product){

        ContentValues cv = new ContentValues();

        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_C_BPSUB_ID, product.getC_bpsub_id());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_M_PRODUCT_ID, product.getM_product_id());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTNAME, product.getProductname());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTVALUE, product.getProductvalue());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_QTY_MIN, product.getQty_min());
        cv.put(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_SYNCHRONISED, getSynchroniseDate(Calendar.getInstance().getTime()) );

        String selection =  EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getC_bpsub_product_id())};

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_BPSUB_PRODUCT, cv, selection, selectionArgs);
    }

    public ArrayList<MProduct> getProductsByFiliale(long c_bpsub_id){

        String selection = EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_C_BPSUB_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(c_bpsub_id) };

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPSUB_PRODUCT, null, selection, selectionArgs, null,
                        null, null);
        cursor.moveToFirst();

        ArrayList<MProduct> mProducts = new ArrayList<>();
        MProduct product;

        while (cursor.moveToNext()) {
            product = getProductFromCursor(cursor);
            mProducts.add(product);
        }

        return mProducts;
    }

    private MProduct getProductFromCursor(Cursor cursor) {
        MProduct product = new MProduct();

        product.setC_bpsub_product_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_ID)));
        product.setC_bpsub_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_C_BPSUB_ID)));
        product.setM_product_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_M_PRODUCT_ID)));
        product.setProductname(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTNAME)));
        product.setProductvalue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_PRODUCTVALUE)));
        product.setQty_min(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_QTY_MIN)));
        product.setSynchronisedate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_PRODUCT_TABLE.KEY_SYNCHRONISED)));

        return product;
    }


}
