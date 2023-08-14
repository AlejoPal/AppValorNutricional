package com.aproyectousco.valornutricionalalimentoscolombianos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TablaAdapter extends RecyclerView.Adapter<TablaAdapter.TablaViewHolder> {

    private List<TablaItem> tablaItemList;

    public TablaAdapter(List<TablaItem> tablaItemList) {
        this.tablaItemList = tablaItemList;
    }

    @NonNull
    @Override
    public TablaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabla_alimentos, parent, false);
        return new TablaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TablaViewHolder holder, int position) {
        TablaItem item = tablaItemList.get(position);

        // Asignar los valores de TablaItem a las vistas en la fila del RecyclerView
        holder.txtAlimentos.setText(item.getComidas());
        holder.txtcarbohidratos.setText(item.getCarbohidratos());
        holder.txtcolesterol.setText(item.getColesterol());
        holder.txtenergia.setText(item.getEnergia());
        holder.txtgsaturadas.setText(item.getGsat());
        holder.txtlipidos.setText(item.getLipidos());
        holder.txtproteinas.setText(item.getProteinas());
        holder.txtsodio.setText(item.getSodio());
    }

    @Override
    public int getItemCount() {
        return tablaItemList.size();
    }

    static class TablaViewHolder extends RecyclerView.ViewHolder {
        TextView txtAlimentos, txtcarbohidratos, txtcolesterol, txtenergia, txtgsaturadas, txtlipidos, txtproteinas, txtsodio;

        public TablaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlimentos = itemView.findViewById(R.id.txtAlimentos);
            txtcarbohidratos = itemView.findViewById(R.id.txtcarbohidratos);
            txtcolesterol = itemView.findViewById(R.id.txtcolesterol);
            txtenergia = itemView.findViewById(R.id.txtenergia);
            txtgsaturadas = itemView.findViewById(R.id.txtgsaturadas);
            txtlipidos = itemView.findViewById(R.id.txtlipidos);
            txtproteinas = itemView.findViewById(R.id.txtproteinas);
            txtsodio = itemView.findViewById(R.id.txtsodio);
        }
    }
}
