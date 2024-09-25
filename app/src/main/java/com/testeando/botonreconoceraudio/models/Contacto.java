package com.testeando.botonreconoceraudio.models;

public class Contacto {
    private int idContacto;
    private String nombreContacto;
    private String macDispositivo; // Asegúrate de que el nombre sea coherente

    public Contacto(int idContacto, String nombreContacto, String macDispositivo) {
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.macDispositivo = macDispositivo; // Cambia 'macContacto' a 'macDispositivo'
    }

    public int getIdContacto() {
        return idContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public String getMacDispositivo() { // Cambia el nombre del método si es necesario
        return macDispositivo;
    }
}

