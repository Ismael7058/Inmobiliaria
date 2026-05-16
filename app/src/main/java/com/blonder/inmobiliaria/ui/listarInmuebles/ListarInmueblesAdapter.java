package com.blonder.inmobiliaria.ui.listarInmuebles;

import static com.blonder.inmobiliaria.request.ApiClient.BASE_URL;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.ItemInmubleBinding;
import com.bumptech.glide.Glide;

import java.util.List;

public class ListarInmueblesAdapter extends RecyclerView.Adapter<ListarInmueblesAdapter.ViewHolerInmueble> {
    private List<Inmueble> inmuebleList;
    private LayoutInflater inflater;

    public ListarInmueblesAdapter(List<Inmueble> inmuebleList, LayoutInflater inflater) {
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
        Glide.with(inflater.getContext())
                        .load(BASE_URL + inmueble.getImagen())
                        .placeholder(null)
                        .error(R.drawable.inmueble_default)
                        .into(holder.ivInmueble);

        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Accion para rediregir a detalle", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return inmuebleList.size();
    }


    public class ViewHolerInmueble extends RecyclerView.ViewHolder {
        private ItemInmubleBinding binding;
        ImageView ivInmueble;
        TextView direccion;
        ConstraintLayout contenedor;
        public ViewHolerInmueble(@NonNull View itemView) {
            super(itemView);
            binding = ItemInmubleBinding.bind(itemView);
            ivInmueble = binding.ivInmueble;
            direccion = binding.direccion;
            contenedor = binding.contenedor;
        }



    }
}
