package com.safi_d.sistemas.safdiscomert.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import com.safi_d.sistemas.safdiscomert.Auxiliar.variables_publicas;
public class PromocionesHelper {
    private SQLiteDatabase database;
    public PromocionesHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarPromociones(String codpromo ,
                                   String itemv ,
                                   String cantv ,
                                   String itemb ,
                                   String cantb ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_codPromo, codpromo);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_itemV, itemv);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_cantV, cantv);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_itemB, itemb);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_cantB, cantb);

        database.insert(variables_publicas.TABLE_PROMOCIONES, null, contentValues);
    }
    public  void EliminaPromociones() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_PROMOCIONES+";");
        Log.d("Promociones_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public HashMap<String, String> BuscarPromocion(int promo){
        HashMap<String,String> promocionDetalle = new HashMap<String, String>();

        String selectQuery3 = "SELECT DISTINCT GROUP_CONCAT(pr."+ variables_publicas.PROMOCIONES_COLUMN_itemV +") AS Productos, PR."+ variables_publicas.PROMOCIONES_COLUMN_cantV +" as CantidadV " +
                ", PR."+ variables_publicas.PROMOCIONES_COLUMN_itemB +" as ItemB,PR."+ variables_publicas.PROMOCIONES_COLUMN_cantB +" as CantidadB FROM "+variables_publicas.TABLE_PROMOCIONES+" PR " +
                "WHERE PR."+variables_publicas.PROMOCIONES_COLUMN_codPromo+"= "+ promo +"";
        Cursor c = database.rawQuery(selectQuery3,null);
        if (c.moveToFirst()) {
            do {
                promocionDetalle.put("Articulos",c.getString(c.getColumnIndex("Productos")));
                promocionDetalle.put("CantidadV",c.getString(c.getColumnIndex("CantidadV")));
                promocionDetalle.put("ItemB", c.getString(c.getColumnIndex("ItemB")));
                promocionDetalle.put("CantidadB", c.getString(c.getColumnIndex("CantidadB")));

            } while (c.moveToNext());
        }
        c.close();

        return promocionDetalle;
    }

    @SuppressLint("Range")
    public int  BuscarCodPromocion(String producto) {

        String selectQuery = "SELECT pr."+ variables_publicas.PROMOCIONES_COLUMN_codPromo +" FROM "+variables_publicas.TABLE_PROMOCIONES+" PR " +
                "WHERE PR."+variables_publicas.PROMOCIONES_COLUMN_itemV+"= '"+producto+"' ORDER BY cast(PR."+variables_publicas.PROMOCIONES_COLUMN_codPromo+" as integer) DESC LIMIT 1";
        Cursor c = database.rawQuery(selectQuery,null);
        int vIdPromo=0;
        if (c.moveToFirst()) {
            do {
                vIdPromo= Integer.parseInt(c.getString(c.getColumnIndex(variables_publicas.PROMOCIONES_COLUMN_codPromo)));
            } while (c.moveToNext());
        }
        c.close();

        return vIdPromo;
    }
/*    @SuppressLint("Range")
    public int  BuscarCodPromocion2(String producto, int Cantidad) {

        String selectQuery = "SELECT pr."+ variables_publicas.PROMOCIONES_COLUMN_codPromo +" FROM "+variables_publicas.TABLE_PROMOCIONES+" PR " +
                "WHERE PR."+ variables_publicas.PROMOCIONES_COLUMN_itemV +"= '"+producto+"' AND  "+ Cantidad +">= cast(PR."+ variables_publicas.PROMOCIONES_COLUMN_cantV +" as integer)  ORDER BY cast(PR."+variables_publicas.PROMOCIONES_COLUMN_cantV+" as integer) DESC LIMIT 1";
        Cursor c = database.rawQuery(selectQuery,null);
        int vIdPromo=0;
        if (c.moveToFirst()) {
            do {
                vIdPromo= Integer.parseInt(c.getString(c.getColumnIndex(variables_publicas.PROMOCIONES_COLUMN_codPromo)));
            } while (c.moveToNext());
        }
        c.close();

        return vIdPromo;
    }*/

    @SuppressLint("Range")
    public int  BuscarCodPromocionesTipo(String ItemV, String Canal, String Fecha, String Cantidad) {
        String selectQueryCartilla = "SELECT count(*) as Cantidad FROM "+variables_publicas.TABLE_CARTILLAS_BC+" cb INNER JOIN "+variables_publicas.TABLE_DETALLE_CARTILLAS_BC+" db ON cb.codigo= db.codigo " +
                "WHERE db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV+"= '"+ItemV+"' AND DB."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo+" = '"+Canal+"' COLLATE NOCASE " +
                "AND cast(db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad+" as integer) <= cast("+Cantidad+" as integer) AND  ( DATE('"+Fecha+"') BETWEEN DATE(cb.fechaini) AND DATE(cb.fechafinal) )" +
                " AND db.activo = 'true'  ORDER BY cast(db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad+" as integer) DESC LIMIT 1";
        Cursor c = database.rawQuery(selectQueryCartilla,null);
        int vExiste=0;
        int vIdPromo=0;
        if (c.moveToFirst()) {
            do {
                vExiste = Integer.parseInt(c.getString(c.getColumnIndex("Cantidad")));
            } while (c.moveToNext());
        }
        if (vExiste==0){
            String selectQuery = "SELECT pr."+ variables_publicas.PROMOCIONES_COLUMN_codPromo +" FROM "+variables_publicas.TABLE_PROMOCIONES+" PR " +
                    "WHERE PR."+ variables_publicas.PROMOCIONES_COLUMN_itemV +"= '"+ItemV+"'  ORDER BY cast(PR."+variables_publicas.PROMOCIONES_COLUMN_codPromo+" as integer) DESC LIMIT 1";
            Cursor c2 = database.rawQuery(selectQuery,null);

            if (c2.moveToFirst()) {
                do {
                    vIdPromo= Integer.parseInt(c2.getString(c2.getColumnIndex(variables_publicas.PROMOCIONES_COLUMN_codPromo)));
                } while (c2.moveToNext());
            }
            c2.close();
        }else{
            vIdPromo=99999;
        }

        return vIdPromo;
    }

}

