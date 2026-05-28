package com.blonder.inmobiliaria.ui.contrato;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blonder.inmobiliaria.Models.Contrato;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentContratoBinding;

import java.util.Date;

public class ContratoFragment extends Fragment {

    private ContratoViewModel mViewModel;
    private FragmentContratoBinding binding;

    public static ContratoFragment newInstance() {
        return new ContratoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratoBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ContratoViewModel.class);
        Bundle bundle = getArguments();

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getContratoMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                binding.etDireccionInmueble.setText(contrato.getInmueble().getDireccion());
                binding.etFechaInicio.setText(contrato.getFechaInicioFormateada() );
                binding.etFechaFinalizacion.setText(contrato.getFechaFinalizacionFormateada());
                binding.etMontoAlquiler.setText(String.valueOf(contrato.getMontoAlquiler()));
            }
        });

        ///  Accion para ver los pagos de un contrato
        binding.btnVerPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idContrato", mViewModel.getIdContrato());
                assert getView() != null;
                Navigation.findNavController(getView()).navigate(R.id.nav_pagos, bundle);
            }
        });
        mViewModel.cargarContrato(bundle);
        return  binding.getRoot();
    }

}