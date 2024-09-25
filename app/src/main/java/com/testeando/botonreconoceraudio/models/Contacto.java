package com.testeando.botonreconoceraudio.models;

public class Contacto {
    private int idContacto;
    private String nombreContacto;
    private String nombreDispositivo;
    private String macDispositivo;

    public Contacto(int idContacto, String nombreContacto, String nombreDispositivo, String macDispositivo) {
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.nombreDispositivo = nombreDispositivo;
        this.macDispositivo = macDispositivo;
    }

    public int getIdContacto() {
        return idContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public String getMacDispositivo() {
        return macDispositivo;
    }
}
