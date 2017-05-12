package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Usuario;

public class UsuariosHelper {

    private SQLiteDatabase database;

    public  UsuariosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarUsuario(String Codigo, String nombre, String Usuario,
                                   String Contrasenia, String Tipo, String Ruta,
                                      String Canal,String TasaCambio) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Nombre, nombre);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Usuario, Usuario);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Contrasenia, Contrasenia);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Tipo, Tipo);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Ruta, Ruta);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Canal, Canal);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_TasaCambio, TasaCambio);
         database.insert(variables_publicas.TABLE_USUARIOS, null, contentValues);
    }
    public Usuario BuscarUsuarios(String Usuario,String Contrasenia) {
        Usuario usuario=null;
        String selectQuery="SELECT * FROM " + variables_publicas.TABLE_USUARIOS
                + " WHERE UPPER("+variables_publicas.USUARIOS_COLUMN_Usuario +") = UPPER('"+Usuario+"') AND "+ variables_publicas.USUARIOS_COLUMN_Contrasenia+" = '"+Contrasenia+"'";
        Cursor c= database.rawQuery(selectQuery , null);
        if (c.moveToFirst()) {
            do {
                usuario = (new Usuario(c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Codigo)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Nombre)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Usuario)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Contrasenia)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Ruta)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_Canal)),
                        c.getString(c.getColumnIndex(variables_publicas.USUARIOS_COLUMN_TasaCambio))

                ));
            } while (c.moveToNext());
        }
        c.close();
        return usuario;
    }
    public Cursor BuscarUsuariosCount() {
         return database.rawQuery("select COUNT(*) from " + variables_publicas.TABLE_USUARIOS + "", null);
    }
    public  void EliminaUsuarios() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_USUARIOS+";");
        Log.d("Usuario_elimina", "Datos eliminados");
    }


    }