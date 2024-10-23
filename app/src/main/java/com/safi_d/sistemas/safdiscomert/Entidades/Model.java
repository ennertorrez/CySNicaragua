package com.safi_d.sistemas.safdiscomert.Entidades;

public class Model {
    private String codigo;
    private String precio;
    private String nombre;
    private String unidadcaja;
    private boolean selected;

    public Model(String codigo,String precio,String nombre,String ucaja) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.unidadcaja = ucaja;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }
    public String getCodigo() {
        return codigo;
    }
    public String getUnidadcaja() {
        return unidadcaja;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
