package com.blonder.inmobiliaria.ui.inmueble;

import static android.view.View.*;

import static com.blonder.inmobiliaria.request.ApiClient.BASE_URL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.bumptech.glide.Glide;

import java.util.Objects;


public class InmuebleFragment extends Fragment {

    private InmuebleViewModel vm;
    private FragmentInmuebleBinding b;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Intent intent;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InmuebleViewModel.class);
        abrirGaleria();

        ///  Observer que carga la imagen en la vista
        vm.getImagenUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                b.ivImagenInmueble.setImageURI(uri);
            }
        });
        /// Observer que carga el inmueble en la vista

        vm.getInmueble().observe(getViewLifecycleOwner(), inmueble -> {
            Glide.with(requireContext()).load(BASE_URL + inmueble.getImagen()).placeholder(null).error(R.drawable.inmueble_default).into(b.ivImagenInmueble);

            b.etDireccion.setText(inmueble.getDireccion());
            b.SpinnerUso.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{inmueble.getUso()}));
            b.SpinnerTipo.setAdapter(new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, new String[]{inmueble.getTipo()}));
            b.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
            b.etSuperficie.setText(String.valueOf(inmueble.getSuperficie()));
            b.etLatitud.setText(String.valueOf(inmueble.getLatitud()));
            b.etLongitud.setText(String.valueOf(inmueble.getLongitud()));
            b.etPrecio.setText(String.valueOf(inmueble.getValor()));
            b.cbDisponible.setEnabled(true);// Habilita campo disponible
            b.cbDisponible.setChecked(inmueble.isDisponible());
            b.cbContratoVigente.setChecked(inmueble.isTieneContratoVigente());
        });
        /// Listener del Boton Crear
        b.btnCrearInmueble.setOnClickListener(v -> {
            vm.registrarInmueble(
                    b.etDireccion.getText().toString(),
                    b.SpinnerUso.getSelectedItem().toString(),
                    b.SpinnerTipo.getSelectedItem().toString(),
                    b.etAmbientes.getText().toString(),
                    b.etSuperficie.getText().toString(),
                    b.etLatitud.getText().toString(),
                    b.etLongitud.getText().toString(),
                    b.etPrecio.getText().toString(),
                    b.cbDisponible.isChecked(),
                    b.cbContratoVigente.isChecked()
            );
        });
        /// Listener del Boton Actualizar
//        b.btnEditarInmueble.setOnClickListener(v -> {
//            setEnabledSupreme(true, true);
//            setVisibleSupreme(2);
//        });
        /// Listener del Boton Guardar
        b.btnGuardarInmueble.setOnClickListener(v -> {
            setEnabledSupreme(false,false);
            setVisibleSupreme(1, true);
            Toast.makeText(getContext(), "Lógica para guardar cambios", Toast.LENGTH_SHORT).show();
        });
        /// Listener del CheckBox para cambiar la disponibilidad
        b.cbDisponible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = b.cbDisponible.isChecked();
                vm.actualizarDisponible(checked);
            }
        });
        b.btnVerContrato.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("idInmueble", vm.getInmueble().getValue().getIdInmueble());
                Navigation.findNavController(view).navigate(R.id.nav_contrato, bundle);
            }
        });
        vm.getGuardadoExitoso().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Navigation.findNavController(b.getRoot()).popBackStack();
                }
            }
        });
        ///  Listener que muestra un mensaje en pantalla, pueden ser errores o confirmaciones
        vm.getMessageToView().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        Bundle bundle = getArguments();
        //Modo Crear
        if (bundle == null || !bundle.containsKey("inmueble")) {
            setEnabledSupreme(true, true);
            b.etDireccion.requestFocus();
            setVisibleSupreme(3, false);
            ///  Listener para abrir la galeria del telefono
            b.ivImagenInmueble.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityResultLauncher.launch(intent);
                }
            });
            return b.getRoot();
        }
        //Modo Editar
        Inmueble inmueble = bundle.getSerializable("inmueble", Inmueble.class);
        boolean inmueblesNoDisponibles = bundle.getBoolean("inmueblesNoDisponibles");
        Log.d("Inmueble", "" + inmueble);
        if (inmueble == null) {
            Toast.makeText(getContext(), "Error al cargar el inmueble", Toast.LENGTH_SHORT).show();
            return b.getRoot();
        }
        vm.cargarInmueble(inmueble);
        setEnabledSupreme(false,inmueblesNoDisponibles);
        setVisibleSupreme(1,inmueblesNoDisponibles);
        return b.getRoot();
    }


    //Funcion auxiliar para habilitar o deshabilitar los campos de texto
    private void setEnabledSupreme(boolean estado, boolean afectarCbDisponible) {
        b.etDireccion.setEnabled(estado);
        b.SpinnerUso.setEnabled(estado);
        b.SpinnerTipo.setEnabled(estado);
        b.etAmbientes.setEnabled(estado);
        b.etSuperficie.setEnabled(estado);
        b.etLatitud.setEnabled(estado);
        b.etLongitud.setEnabled(estado);
        b.etPrecio.setEnabled(estado);
        if( afectarCbDisponible) //Habilita campo disponible
            b.cbDisponible.setEnabled(estado);

        b.cbContratoVigente.setEnabled(estado);//falta ver como se comporta el back del contrato
    }

    //Funcion auxiliar para cambiar la visibilidad de los botones
    private void setVisibleSupreme(int eleccion, boolean verificarContrato) {
        ///  Invierto el valor para que no se muestre ya que no existe el editar inmueble
        b.btnEditarInmueble.setVisibility(eleccion == 1 ? GONE : VISIBLE);
        if(!verificarContrato)
            b.btnVerContrato.setVisibility(eleccion == 1 ? VISIBLE : GONE);
        b.btnGuardarInmueble.setVisibility(eleccion == 2 ? VISIBLE : GONE);
        b.btnCrearInmueble.setVisibility(eleccion == 3 ? VISIBLE : GONE);
    }
    ///  Funcion auxiliar para abrir la galeria del telefono y setear la imagen en el mutable
    private void abrirGaleria(){
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult o) {
                                vm.recibirFotos(o);
                            }
                        }) ;

    }
}
