package com.blonder.inmobiliaria.ui.perfilPassword;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blonder.inmobiliaria.databinding.FragmentPerfilPasswordBinding;

public class perfilPasswordFragment extends Fragment {

    private PerfilPasswordViewModel mViewModel;
    private FragmentPerfilPasswordBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilPasswordBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(PerfilPasswordViewModel.class);
        EditText anteriorPassword = binding.etAnteriorPassword;
        EditText nuevaPassword = binding.etNuevaPassword;

        binding.bActualizarPassword.setOnClickListener(v -> mViewModel.cambiarContra(anteriorPassword.getText().toString(), nuevaPassword.getText().toString()));

        mViewModel.getOk().observe(getViewLifecycleOwner(), ok -> {
            if (ok) {
                anteriorPassword.setText("");
                nuevaPassword.setText("");
                Navigation.findNavController(requireView()).popBackStack();
                return;
            }
            anteriorPassword.requestFocus();
        });
        return binding.getRoot();
    }


}
