package com.safi_d.sistemas.safdiscomert.Entidades;

public class DiasCierre {
    String DiaInicio;
    String DiaFin;
    String HoraInicio;
    String HoraFin;
    String FechaInicio;
    String FechaFin;

    public DiasCierre(String diainicio,String diafin,String horainicio,String horafin,String fechainicio,String fechafin) {
        this.DiaInicio = diainicio;
        this.DiaFin = diafin;
        this.HoraInicio =horainicio;
        this.HoraFin = horafin;
        this.FechaInicio =fechainicio;
        this.FechaFin = fechafin;
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

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.FechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.FechaFin = fechaFin;
    }
}
