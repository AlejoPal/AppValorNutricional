package com.aproyectousco.valornutricionalalimentoscolombianos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Agregar el OnClickListener al botón de eliminación
        holder.btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Eliminar el elemento de la lista y notificar cambios
                tablaItemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tablaItemList.size());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRootReference = database.getReference();

                // Aquí debes implementar la lógica para eliminar el elemento de la base de datos (Firebase)
                DatabaseReference databaseRef = mRootReference.child("Usuario").child(item.getCorreo()).child(item.getFecha()).child(item.getTiempo()).child("Alimentos").child(item.getComidas());
                databaseRef.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tablaItemList.size();
    }

    static class TablaViewHolder extends RecyclerView.ViewHolder {
        TextView txtAlimentos, txtcarbohidratos, txtcolesterol, txtenergia, txtgsaturadas, txtlipidos, txtproteinas, txtsodio;
        ImageView btnImagen;

        public TablaViewHolder(@NonNull View itemView) {
            super(itemView);
            btnImagen = itemView.findViewById(R.id.btnEliminar);
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
