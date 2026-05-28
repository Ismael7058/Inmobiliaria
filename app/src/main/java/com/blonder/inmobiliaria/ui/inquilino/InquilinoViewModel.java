package com.blonder.inmobiliaria.ui.inquilino;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blonder.inmobiliaria.Models.Inquilino;

public class InquilinoViewModel extends AndroidViewModel {
    private MutableLiveData<Inquilino> inquilinoMutableLiveData;

    public InquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getInquilinoMutableLiveData() {
        if (inquilinoMutableLiveData == null) {
            inquilinoMutableLiveData = new MutableLiveData<>();
        }
        return inquilinoMutableLiveData;
    }

    public void cargarInquilino(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("inquilino")) {
            Toast.makeText(getApplication(), "No hay bundle", Toast.LENGTH_SHORT).show();
            return;
        }
        Inquilino inquilino = bundle.getSerializable("inquilino", Inquilino.class);
        if (inquilino == null) {
            Toast.makeText(getApplication(), "Fallo al cargar inquilino", Toast.LENGTH_SHORT).show();
            return;
        }
        inquilinoMutableLiveData.setValue(inquilino);
    }
}