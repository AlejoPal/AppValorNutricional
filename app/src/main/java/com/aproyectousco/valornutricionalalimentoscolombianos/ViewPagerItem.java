package com.aproyectousco.valornutricionalalimentoscolombianos;

import java.util.List;

public class ViewPagerItem {

    List<TablaItem> tablaItems;
    String Alimento;

    public void setTablaItems(List<TablaItem> tablaItems) {
        this.tablaItems = tablaItems;
    }

    public String getAlimento() {
        return Alimento;
    }

    public void setAlimento(String alimento) {
        Alimento = alimento;
    }

    public ViewPagerItem(List<TablaItem> tablaItems, String Alimento) {
        this.tablaItems = tablaItems;
        this.Alimento = Alimento;

    }

    public List<TablaItem> getTablaItems() {
        return tablaItems;
    }
}
