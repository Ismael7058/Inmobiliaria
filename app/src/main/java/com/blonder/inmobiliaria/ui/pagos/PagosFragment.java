package com.blonder.inmobiliaria.ui.pagos;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blonder.inmobiliaria.Models.Pago;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentPagosBinding;

import java.util.List;

public class PagosFragment extends Fragment {

    private PagosViewModel viewModel;
    private FragmentPagosBinding binding;

    public static PagosFragment newInstance() {
        return new PagosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding  = FragmentPagosBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        Bundle bundle = getArguments();
        viewModel.cargarLista(bundle);

        viewModel.getListaPago().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                PagosAdapter adapter = new PagosAdapter(pagos, getLayoutInflater());
                binding.listItem.setAdapter(adapter);
                binding.sinPagos.setVisibility(View.GONE);
                binding.listItem.setVisibility(View.VISIBLE);

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}