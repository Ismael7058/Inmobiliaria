package com.blonder.inmobiliaria.ui.pagos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blonder.inmobiliaria.Models.Pago;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.ItemPagoBinding;

import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolerPagos>{

    private List<Pago> pagoList;
    private LayoutInflater inflater;
    public PagosAdapter(List<Pago> pagoList, LayoutInflater inflater) {
        this.pagoList = pagoList;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public ViewHolerPagos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_pago, parent, false);
        return new ViewHolerPagos(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolerPagos holder, int position) {
        Pago pago = pagoList.get(position);
        holder.idPago.setText("Cod: ".concat(String.valueOf(pago.getIdPago())));
        holder.detalle.setText("Detalle: ".concat(pago.getDetalle()));
        holder.monto.setText("$".concat(String.valueOf(pago.getMonto())));
        holder.fecha.setText("Fecha: ".concat(pago.getFechaPago()));

        if (pago.isEstado()){
            holder.estado.setText("Pagado");
        }else{
            holder.estado.setText("Cancelado");
            holder.estado.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.colorDanger));
        }

    }

    @Override
    public int getItemCount() {
        return pagoList.size();
    }


    public class ViewHolerPagos extends RecyclerView.ViewHolder {
        private ItemPagoBinding binding;
        TextView idPago, estado, monto, detalle, fecha;
        ConstraintLayout contenedor;
        public ViewHolerPagos(@NonNull View itemView) {
            super(itemView);
            binding = ItemPagoBinding.bind(itemView);
            idPago = binding.idPago;
            estado = binding.estado;
            monto = binding.monto;
            detalle = binding.detalle;
            fecha = binding.fecha;
            contenedor = binding.contenedor;
        }
    }

}
