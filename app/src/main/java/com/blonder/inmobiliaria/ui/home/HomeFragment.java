package com.blonder.inmobiliaria.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.SupportMapFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mViewModel.getMapaActual().observe(this, new Observer<HomeViewModel.MapaActual>() {
            @Override
            public void onChanged(HomeViewModel.MapaActual mapaActual) {
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);
                if(mapFragment != null){
                    mapFragment.getMapAsync(mapaActual);
                }
            }
        });
        mViewModel.cargarMapa();
        return fragmentHomeBinding.getRoot();
    }
}