package com.blonder.inmobiliaria;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.blonder.inmobiliaria.Models.Propietario;
import com.blonder.inmobiliaria.databinding.ActivityMainBinding;
import com.blonder.inmobiliaria.databinding.NavHeaderMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding b;
    private MainActivityViewModel viewModel;
    private NavHeaderMainBinding navHeaderMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        navHeaderMainBinding = NavHeaderMainBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(MainActivityViewModel.class);

        setContentView(b.getRoot());
        setSupportActionBar(b.appBarMain.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_inmueble, R.id.nav_contrato, R.id.nav_inquilino, R.id.nav_logout, R.id.nav_perfil).setOpenableLayout(b.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(b.navView, navController);
        viewModel.getPropietarioMutableLiveData().observe(this, new Observer<Propietario>() {
            @Override
            public void onChanged(@NonNull Propietario propietario) {
                View headerView = b.navView.getHeaderView(0);
                TextView name = headerView.findViewById(R.id.nameLabelHeader);
                TextView mail = headerView.findViewById(R.id.tvMailHeader);
                name.setText(propietario.getNombre().concat(", ").concat(propietario.getApellido()));
                mail.setText(propietario.getEmail());

            }
        });
        viewModel.cargarPropietario();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}