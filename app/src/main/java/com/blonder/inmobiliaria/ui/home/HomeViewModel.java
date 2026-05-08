package com.blonder.inmobiliaria.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeViewModel extends AndroidViewModel {
    public class MapaActual implements OnMapReadyCallback{
        LatLng ubicacionDeInmobiliaria = new LatLng(-33.280576,-66.332482);
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(ubicacionDeInmobiliaria).title("Nosotros"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDeInmobiliaria,15.0f));

        }
    }
    private MutableLiveData<MapaActual> mapaActualMutableLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<MapaActual> getMapaActual(){
        return mapaActualMutableLiveData;
    }
    public void cargarMapa(){
        MapaActual mapaActual = new MapaActual();
        mapaActualMutableLiveData.setValue(mapaActual);
    }
}