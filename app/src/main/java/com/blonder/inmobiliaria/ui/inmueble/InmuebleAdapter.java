package com.blonder.inmobiliaria.ui.inmueble;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.ItemInmubleBinding;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolerInmueble> {
    private List<Inmueble> inmuebleList;
    private LayoutInflater inflater;

    public InmuebleAdapter(List<Inmueble> inmuebleList, LayoutInflater inflater) {
        this.inmuebleList = inmuebleList;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public ViewHolerInmueble onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_inmuble, parent, false);
        return new ViewHolerInmueble(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolerInmueble holder, int position) {
        Inmueble inmueble = inmuebleList.get(position);


        holder.direccion.setText(inmueble.getDireccion());
        holder.ivInmueble.setImageResource(R.drawable.ic_launcher_background);
        holder.btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Accion para rediregir a detalle", Toast.LENGTH_LONG).show();
            }
        });

        if (inmueble.isTieneContratoVigente()){
            holder.btnContrato.setVisibility(View.VISIBLE);
            holder.btnContrato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Accion para rediregir a contrato", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            holder.btnContrato.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return inmuebleList.size();
    }


    public class ViewHolerInmueble extends RecyclerView.ViewHolder {
        private ItemInmubleBinding binding;
        ImageView ivInmueble;
        TextView direccion;
        Button btnDetalle;
        Button btnContrato;
        public ViewHolerInmueble(@NonNull View itemView) {
            super(itemView);
            binding = ItemInmubleBinding.bind(itemView);
            ivInmueble = binding.ivInmueble;
            direccion = binding.direccion;
            btnDetalle = binding.btnDetalle;
            btnContrato = binding.btnContrato;
        }



    }
}
