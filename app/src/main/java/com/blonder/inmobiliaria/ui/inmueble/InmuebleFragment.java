package com.blonder.inmobiliaria.ui.inmueble;

import static android.view.View.*;

import static com.blonder.inmobiliaria.request.ApiClient.BASE_URL;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class InmuebleFragment extends Fragment {

    private InmuebleViewModel vm;
    private FragmentInmuebleBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InmuebleViewModel.class);
        //Observer que carga el inmueble en la vista
        vm.getInmueble().observe(getViewLifecycleOwner(), inmueble -> {
            Glide.with(Objects.requireNonNull(getContext())).load(BASE_URL + inmueble.getImagen()).placeholder(null).error(R.drawable.inmueble_default).into(b.ivImagenInmueble);

            b.etDireccion.setText(inmueble.getDireccion());
            b.etUso.setText(inmueble.getUso());
            b.etTipo.setText(inmueble.getTipo());
            b.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
            b.etSuperficie.setText(String.valueOf(inmueble.getSuperficie()));
            b.etLatitud.setText(String.valueOf(inmueble.getLatitud()));
            b.etLongitud.setText(String.valueOf(inmueble.getLongitud()));
            b.etPrecio.setText(String.valueOf(inmueble.getValor()));
            b.cbDisponible.setChecked(inmueble.isDisponible());
            b.cbContratoVigente.setChecked(inmueble.isTieneContratoVigente());
        });
        /// Listener del Boton Crear
        b.btnCrearInmueble.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Lógica para crear inmueble", Toast.LENGTH_SHORT).show();
        });
        /// Listener del Boton Actualizar
        b.btnEditarInmueble.setOnClickListener(v -> {
            setEnabledSupreme(true);
            setVisibleSupreme(2);
        });
        /// Listener del Boton Guardar
        b.btnGuardarInmueble.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Lógica para guardar cambios", Toast.LENGTH_SHORT).show();
        });

        Bundle bundle = getArguments();
        //Modo Crear
        if (bundle == null || !bundle.containsKey("inmueble")) {
            setEnabledSupreme(true);
            b.etDireccion.requestFocus();
            setVisibleSupreme(3);
            return b.getRoot();
        }
        //Modo Editar
        Inmueble inmueble = bundle.getSerializable("inmueble", Inmueble.class);
        if (inmueble == null) {
            Toast.makeText(getContext(), "Error al cargar el inmueble", Toast.LENGTH_SHORT).show();
            return b.getRoot();
        }
        vm.cargarInmueble(inmueble);
        setEnabledSupreme(false);
        setVisibleSupreme(1);
        return b.getRoot();
    }


    //Funcion auxiliar para habilitar o deshabilitar los campos de texto
    private void setEnabledSupreme(boolean estado) {
        b.etDireccion.setEnabled(estado);
        b.etUso.setEnabled(estado);
        b.etTipo.setEnabled(estado);
        b.etAmbientes.setEnabled(estado);
        b.etSuperficie.setEnabled(estado);
        b.etLatitud.setEnabled(estado);
        b.etLongitud.setEnabled(estado);
        b.etPrecio.setEnabled(estado);
        b.cbDisponible.setEnabled(estado);
        b.cbContratoVigente.setEnabled(estado);//falta ver como se comporta el back del contrato
    }

    //Funcion auxiliar para cambiar la visibilidad de los botones
    private void setVisibleSupreme(int eleccion) {
        b.btnEditarInmueble.setVisibility(eleccion == 1 ? VISIBLE : GONE);
        b.btnGuardarInmueble.setVisibility(eleccion == 2 ? VISIBLE : GONE);
        b.btnCrearInmueble.setVisibility(eleccion == 3 ? VISIBLE : GONE);
    }
}
