package com.blonder.inmobiliaria.ui.inmueble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blonder.inmobiliaria.Models.Inmueble;

public class InmuebleViewModel extends AndroidViewModel {
private MutableLiveData<Inmueble> mInmueble = new MutableLiveData<>();
    public InmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }

    public void cargarInmueble(Inmueble inmueble) {
        mInmueble.setValue(inmueble);
    }

}