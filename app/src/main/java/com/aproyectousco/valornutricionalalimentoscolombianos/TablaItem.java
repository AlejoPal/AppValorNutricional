package com.aproyectousco.valornutricionalalimentoscolombianos;

import android.view.View;

public class TablaItem {

    String comidas, carbohidratos, colesterol, energia, gsat, lipidos, proteinas, sodio;
    String fecha, correo, tiempo;

    public TablaItem(String fecha, String correo, String tiempo, String comidas, String carbohidratos, String colesterol, String energia, String gsat, String lipidos, String proteinas, String sodio) {

        this.fecha = fecha;
        this.correo = correo;
        this.tiempo = tiempo;
        this.comidas = comidas;
        this.carbohidratos = carbohidratos;
        this.colesterol = colesterol;
        this.energia = energia;
        this.gsat = gsat;
        this.lipidos = lipidos;
        this.proteinas = proteinas;
        this.sodio = sodio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getComidas() {
        return comidas;
    }

    public void setComidas(String comidas) {
        this.comidas = comidas;
    }

    public String getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(String carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public String getColesterol() {
        return colesterol;
    }

    public void setColesterol(String colesterol) {
        this.colesterol = colesterol;
    }

    public String getEnergia() {
        return energia;
    }

    public void setEnergia(String energia) {
        this.energia = energia;
    }

    public String getGsat() {
        return gsat;
    }

    public void setGsat(String gsat) {
        this.gsat = gsat;
    }

    public String getLipidos() {
        return lipidos;
    }

    public void setLipidos(String lipidos) {
        this.lipidos = lipidos;
    }

    public String getProteinas() {
        return proteinas;
    }

    public void setProteinas(String proteinas) {
        this.proteinas = proteinas;
    }

    public String getSodio() {
        return sodio;
    }

    public void setSodio(String sodio) {
        this.sodio = sodio;
    }
}
