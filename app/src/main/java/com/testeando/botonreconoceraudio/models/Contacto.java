package com.testeando.botonreconoceraudio.models;

public class Contacto {
    private int idContacto;
    private String nombreContacto;
    private String macContacto;

    public Contacto(int idContacto, String nombreContacto, String macContacto) {
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.macContacto = macContacto;
    }

    public int getIdContacto() {
        return idContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public String getMacContacto() {
        return macContacto;
    }
}
