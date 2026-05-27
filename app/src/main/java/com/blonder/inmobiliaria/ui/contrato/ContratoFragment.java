package com.blonder.inmobiliaria.ui.contrato;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blonder.inmobiliaria.Models.Contrato;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentContratoBinding;

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
                binding.etFechaInicio.setText(contrato.getFechaInicio().toString());
                binding.etFechaFinalizacion.setText(contrato.getFechaFinalizacion().toString());
                binding.etMontoAlquiler.setText(String.valueOf(contrato.getMontoAlquiler()));
            }
        });
        mViewModel.cargarContrato(bundle);
        return  binding.getRoot();
    }

}