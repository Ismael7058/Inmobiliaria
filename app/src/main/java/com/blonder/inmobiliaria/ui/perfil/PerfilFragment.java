package com.blonder.inmobiliaria.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blonder.inmobiliaria.Models.Propietario;
import com.blonder.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        viewModel.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                binding.email.setText(propietario.getEmail());
                binding.email.setEnabled(false);
                binding.nombre.setText(propietario.getNombre());
                binding.nombre.setEnabled(false);
                binding.apellido.setText(propietario.getApellido());
                binding.apellido.setEnabled(false);
                binding.dni.setText(String.valueOf(propietario.getDni()));
                binding.dni.setEnabled(false);
                binding.telefono.setText(propietario.getTelefono());
                binding.telefono.setEnabled(false);
                binding.btnActualizar.setVisibility(View.VISIBLE);
                binding.btnGuardar.setVisibility(View.INVISIBLE);
            }
        });
        binding.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.email.setEnabled(true);
                binding.nombre.setEnabled(true);
                binding.apellido.setEnabled(true);
                binding.dni.setEnabled(true);
                binding.telefono.setEnabled(true);
                binding.btnActualizar.setVisibility(View.INVISIBLE);
                binding.btnGuardar.setVisibility(View.VISIBLE);
            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.editarPropietario(
                        binding.nombre.getText().toString(),
                        binding.apellido.getText().toString(),
                        binding.dni.getText().toString(),
                        binding.telefono.getText().toString(),
                        binding.email.getText().toString()
                );
            }
        });
        viewModel.cargarPropietario();
    }
}