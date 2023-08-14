package com.aproyectousco.valornutricionalalimentoscolombianos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {
    ArrayList<ViewPagerItem> viewPagerItemsList;

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemsList) {
        this.viewPagerItemsList = viewPagerItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPagerItem viewPagerItem = viewPagerItemsList.get(position);
        List<TablaItem> tablaItems = viewPagerItem.getTablaItems();

        TablaAdapter tablaAdapter = new TablaAdapter(tablaItems);
        holder.recyclerView.setAdapter(tablaAdapter);
        holder.txtAlimento.setText(viewPagerItem.getAlimento());
    }

    @Override
    public int getItemCount() {
        return viewPagerItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAlimento;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlimento = itemView.findViewById(R.id.tiempo); // Actualiza el ID para el nombre del alimento
            recyclerView = itemView.findViewById(R.id.recyclerView); // Asegúrate de que esto coincida con el ID en viewpager_item.xml
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            // Verifica si ya se ha agregado el decorador, para evitar duplicados
            if (recyclerView.getItemDecorationCount() == 0) {
                // Agrega el espacio decorativo entre los elementos del RecyclerView
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
            }
        }
    }
}
