package com.blonder.inmobiliaria.ui.inquilino;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blonder.inmobiliaria.databinding.FragmentInquilinoBinding;

public class InquilinoFragment extends Fragment {

    private InquilinoViewModel mViewModel;
    private FragmentInquilinoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InquilinoViewModel.class);
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);

        mViewModel.getInquilinoMutableLiveData().observe(getViewLifecycleOwner(), inquilino -> {
            if (inquilino != null) {
                binding.etNombre.setText(inquilino.getNombre());
                binding.etApellido.setText(inquilino.getApellido());
                binding.etDni.setText(String.valueOf(inquilino.getDni()));
                binding.etTelefono.setText(inquilino.getTelefono());
                binding.etEmail.setText(inquilino.getEmail());
            }
        });

        Bundle bundle = getArguments();
        mViewModel.cargarInquilino(bundle);
        return binding.getRoot();
    }

}