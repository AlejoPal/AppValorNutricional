package com.aproyectousco.valornutricionalalimentoscolombianos;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerItem {

    String comidas, carbohidratos, colesterol, energia, gsat, lipidos, proteinas, sodio;
    List<String> alimentos;

    public ViewPagerItem(List<String> alimentos, String comidas, String carbohidratos, String colesterol, String energia, String gsat, String lipidos, String proteinas, String sodio) {
        this.alimentos = alimentos;
        this.comidas = comidas;
        this.carbohidratos = carbohidratos;
        this.colesterol = colesterol;
        this.energia = energia;
        this.gsat = gsat;
        this.lipidos = lipidos;
        this.proteinas = proteinas;
        this.sodio = sodio;
    }
}
