package com.aproyectousco.valornutricionalalimentoscolombianos;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        final boolean[] isButtonEnabled = {true};

        // Asignar los valores de TablaItem a las vistas en la fila del RecyclerView
        holder.txtAlimentos.setText(item.getComidas());
        holder.txtcarbohidratos.setText(item.getCarbohidratos());
        holder.txtcolesterol.setText(item.getColesterol());
        holder.txtenergia.setText(item.getEnergia());
        holder.txtgsaturadas.setText(item.getGsat());
        holder.txtlipidos.setText(item.getLipidos());
        holder.txtproteinas.setText(item.getProteinas());
        holder.txtsodio.setText(item.getSodio());


        if (position == 0) {
            holder.btnImagen.setVisibility(View.GONE); // Ocultar el botón para el primer elemento
        } else {
            holder.btnImagen.setVisibility(View.VISIBLE); // Mostrar el botón para los otros elementos
        }

        // Agregar el OnClickListener al botón de eliminación

        holder.btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (isButtonEnabled[0]) {
                    isButtonEnabled[0] = false;

                    // Deshabilita el botón durante 1 segundo
                    holder.btnImagen.setEnabled(false);

                    // Crea un temporizador para habilitar el botón después de 1 segundo
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.btnImagen.setEnabled(true);
                            isButtonEnabled[0] = true;
                        }
                    }, 2000); // 1000 milisegundos = 1 segundo



                    int currentPosition = holder.getAdapterPosition(); // Obtén la posición actual del elemento
                    if (currentPosition != RecyclerView.NO_POSITION) { // Verifica si la posición es válida
                        TablaItem itemToRemove = tablaItemList.get(currentPosition);

                        // Resto del código para eliminar el elemento y actualizar los valores
                        final double[] totalCarbohidratos = {0};
                        final double[] totalColesterol = {0};
                        final double[] totalEnergia = {0};
                        final double[] totalGsat = {0};
                        final double[] totalLipidos = {0};
                        final double[] totalProteinas = {0};
                        final double[] totalSodio = {0};

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRootReference = database.getReference();

                        // Obtén el elemento a eliminar


                        // Obtén la referencia al nodo "Alimentos" en la base de datos
                        DatabaseReference alimentosRef = mRootReference.child("Usuario")
                                .child(itemToRemove.getCorreo())
                                .child(itemToRemove.getFecha())
                                .child(itemToRemove.getTiempo())
                                .child("Alimentos");


                        tablaItemList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        // Aquí debes implementar la lógica para eliminar el elemento de la base de datos (Firebase)
                        DatabaseReference databaseRef = mRootReference.child("Usuario").child(item.getCorreo()).child(item.getFecha()).child(item.getTiempo()).child("Alimentos").child(item.getComidas());
                        databaseRef.removeValue();

                        // Agrega un listener para obtener los valores de cada alimento y sumarlos
                        alimentosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                for (DataSnapshot alimentoSnapshot : dataSnapshot.getChildren()) {
                                    // Obtén los valores de cada alimento y suma a los totales
                                    double carbohidratos = alimentoSnapshot.child("Carbohidratos").getValue(Double.class);
                                    double colesterol = alimentoSnapshot.child("Colesterol").getValue(Double.class);
                                    double energia = alimentoSnapshot.child("Energia").getValue(Double.class);
                                    double gsat = alimentoSnapshot.child("Gsat").getValue(Double.class);
                                    double lipidos = alimentoSnapshot.child("Lipidos").getValue(Double.class);
                                    double proteinas = alimentoSnapshot.child("Proteina").getValue(Double.class);
                                    double sodio = alimentoSnapshot.child("Sodio").getValue(Double.class);

                                    totalCarbohidratos[0] += carbohidratos;
                                    totalColesterol[0] += colesterol;
                                    totalEnergia[0] += energia;
                                    totalGsat[0] += gsat;
                                    totalLipidos[0] += lipidos;
                                    totalProteinas[0] += proteinas;
                                    totalSodio[0] += sodio;
                                }

                                // Actualiza los valores en el nodo "Resumen" en Firebase
                                DatabaseReference resumenRef = mRootReference.child("Usuario")
                                        .child(itemToRemove.getCorreo())
                                        .child(itemToRemove.getFecha())
                                        .child(itemToRemove.getTiempo());


                                resumenRef.child("Carbohidratos").setValue(totalCarbohidratos[0]);
                                resumenRef.child("Colesterol").setValue(totalColesterol[0]);
                                resumenRef.child("Energia").setValue(totalEnergia[0]);
                                resumenRef.child("Gsat").setValue(totalGsat[0]);
                                resumenRef.child("Lipidos").setValue(totalLipidos[0]);
                                resumenRef.child("Proteina").setValue(totalProteinas[0]);
                                resumenRef.child("Sodio").setValue(totalSodio[0]);

                                // Actualizar los valores mostrados en el primer elemento del RecyclerView
                                TablaItem firstItem = tablaItemList.get(0);
                                firstItem.setCarbohidratos(String.format("%.2f", totalCarbohidratos[0]));
                                firstItem.setColesterol(String.format("%.2f", totalColesterol[0]));
                                firstItem.setEnergia(String.format("%.2f", totalEnergia[0]));
                                firstItem.setGsat(String.format("%.2f", totalGsat[0]));
                                firstItem.setLipidos(String.format("%.2f", totalLipidos[0]));
                                firstItem.setProteinas(String.format("%.2f", totalProteinas[0]));
                                firstItem.setSodio(String.format("%.2f", totalSodio[0]));

                                // Notificar que el primer elemento ha cambiado para que se actualice en el RecyclerView
                                notifyItemChanged(0);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejo de errores en caso de fallo en la lectura de la base de datos
                            }
                        });


                    }

                }
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
