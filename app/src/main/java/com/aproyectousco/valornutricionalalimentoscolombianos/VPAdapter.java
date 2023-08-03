package com.aproyectousco.valornutricionalalimentoscolombianos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {
    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        holder.txtiempo.setText(viewPagerItem.comidas);
        holder.txCarbohidratos.setText(viewPagerItem.carbohidratos);
        holder.txColesterol.setText(viewPagerItem.colesterol);
        holder.txEnergia.setText(viewPagerItem.energia);
        holder.txSaturadas.setText(viewPagerItem.gsat);
        holder.txLipidos.setText(viewPagerItem.lipidos);
        holder.txProteinas.setText(viewPagerItem.proteinas);
        holder.txSodio.setText(viewPagerItem.sodio);


    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtiempo, txCarbohidratos, txColesterol, txEnergia, txSaturadas, txLipidos, txProteinas, txSodio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtiempo = itemView.findViewById(R.id.tiempo);
            txCarbohidratos = itemView.findViewById(R.id.txtcarbohidratos);
            txColesterol = itemView.findViewById(R.id.txtcolesterol);
            txEnergia = itemView.findViewById(R.id.txtenergia);
            txSaturadas = itemView.findViewById(R.id.txtgsaturadas);
            txLipidos = itemView.findViewById(R.id.txtlipidos);
            txProteinas = itemView.findViewById(R.id.txtproteinas);
            txSodio = itemView.findViewById(R.id.txtsodio);

        }
    }

}
