package com.blonder.inmobiliaria.ui.logout;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blonder.inmobiliaria.LoginActivity;
import com.blonder.inmobiliaria.R;
import com.blonder.inmobiliaria.request.ApiClient;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mostrarDialogoLogout();
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    private void mostrarDialogoLogout() {
        AlertDialog show = new AlertDialog.Builder(requireContext()).setTitle("Cerrar Sesión").setMessage("¿Está seguro que desea salir?").setCancelable(false).setPositiveButton("Aceptar", (dialogInterface, i) -> {
            ///Borro el token
            ApiClient.eliminarToken(requireContext());
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }).setNegativeButton("Cancelar", (dialogInterface, i) -> {
            /// Se queda en home
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_home);
        }).show();
    }

}