package com.testeando.botonreconoceraudio.models;

public class Conversacion {
    private int id;
    private String fechaInicio;
    private int duracion;

    public Conversacion(int id, String fechaInicio, int duracion) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDuracionFormatted() {
        if (duracion < 60) {
            return duracion + " segundos";
        } else {
            int minutos = duracion / 60;
            int segundos = duracion % 60;
            return minutos + (segundos > 0 ? ":" + String.format("%02d", segundos) : "") + " minutos";
        }
    }

}
