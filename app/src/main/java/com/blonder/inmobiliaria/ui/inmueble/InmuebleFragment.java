package com.blonder.inmobiliaria.ui.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

public class InmuebleFragment extends Fragment {
    private FragmentInmuebleBinding binding;
    private InmuebleViewModel viewModel;

    public static InmuebleFragment newInstance() {
        return new InmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InmuebleViewModel.class);
        
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.listItem.setLayoutManager(gridLayoutManager);


        viewModel.getListaDisponibles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean valor) {
                viewModel.cargarLista(valor);
            }
        });

        viewModel.getListaInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getLayoutInflater());
                binding.listItem.setAdapter(adapter);
            }
        });

        binding.btnTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnDisponibles.setEnabled(true);
                binding.btnTodos.setEnabled(false);
                viewModel.setListaDisponibles(true);
            }
        });

        binding.btnDisponibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnDisponibles.setEnabled(false);
                binding.btnTodos.setEnabled(true);
                viewModel.setListaDisponibles(false);
            }
        });

        viewModel.setListaDisponibles(true);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.recargarLista();
    }
}