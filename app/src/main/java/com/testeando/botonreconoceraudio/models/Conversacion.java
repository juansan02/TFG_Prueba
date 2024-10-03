package com.testeando.botonreconoceraudio.models;

public class Conversacion {
    private int idConversacion;
    private int idUsuario;
    private int idContacto;
    private String nombreUsuario;
    private String nombreContacto;
    private String fechaInicio;
    private String fechaFin;
    private int duracion;


    public Conversacion(int idConversacion, String fechaInicio, int duracion) {
        this.idConversacion = idConversacion;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
    }

    public Conversacion(int idConversacion, int idUsuario, int idContacto, String nombreUsuario, String nombreContacto,
                        String fechaInicio, String fechaFin, int duracion) {
        this.idConversacion = idConversacion;
        this.idUsuario = idUsuario;
        this.idContacto = idContacto;
        this.nombreUsuario = nombreUsuario;
        this.nombreContacto = nombreContacto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracion = duracion;
    }

    public int getId() {
        return idConversacion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setId(int id) {
        this.idConversacion = id;
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
