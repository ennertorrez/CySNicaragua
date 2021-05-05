package com.safi_d.sistemas.safdiscomert.Entidades;

public class DiasCierre {
    String DiaInicio;
    String DiaFin;
    String HoraInicio;
    String HoraFin;

    public DiasCierre(String diainicio,String diafin,String horainicio,String horafin) {
        this.DiaInicio = diainicio;
        this.DiaFin = diafin;
        this.HoraInicio =horainicio;
        this.HoraFin = horafin;
    }

    public DiasCierre() {

    }

    public String getDiaInicio() {
        return DiaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        this.DiaInicio = diaInicio;
    }

    public String getDiaFin() {
        return DiaFin;
    }

    public void setDiaFin(String diaFin) {
        this.DiaFin = diaFin;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.HoraInicio = horaInicio;
    }

    public String getHoraFin() {
        return HoraFin;
    }

    public void setHoraFin(String horaFin) {
        this.HoraFin = horaFin;
    }

}
