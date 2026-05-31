package com.blonder.inmobiliaria.ui.home;

import static android.content.Context.SENSOR_SERVICE;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Application;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class HomeViewModel extends AndroidViewModel implements SensorEventListener {
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
        sensorManager = (SensorManager) getApplication().getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        SHAKE_THRESHOLD = 12.0f;
    }
    public LiveData<MapaActual> getMapaActual(){
        return mapaActualMutableLiveData;
    }
    public void cargarMapa(){
        MapaActual mapaActual = new MapaActual();
        mapaActualMutableLiveData.setValue(mapaActual);
    }


    private MutableLiveData<Intent> intentMutable = new MutableLiveData<>();
    private SensorManager sensorManager;
    private Sensor sensor;
    private float SHAKE_THRESHOLD; // Sensibilidad
    private long lastUpdate = 0;
    public LiveData<Intent> getAbrirTelefono(){
        return intentMutable;
    }
    public void limpiarIntent() {
        intentMutable.setValue(null);
    }

    public void comenzarDeteccion() {
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void detenerDeteccion() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculamos la aceleracion total restando la gravedad
            double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            if (acceleration > SHAKE_THRESHOLD) {
                long curTime = System.currentTimeMillis();
                // Evitar multiples disparos seguidos
                if ((curTime - lastUpdate) > 1000) {
                    lastUpdate = curTime;
                    dispararLlamada();
                }
            }
        }
    }

    private void dispararLlamada() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:2664553747"));
        intentMutable.setValue(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}