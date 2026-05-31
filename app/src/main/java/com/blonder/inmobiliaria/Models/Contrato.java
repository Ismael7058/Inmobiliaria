package com.blonder.inmobiliaria.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Contrato
{
    private int idContrato;
    private Date fechaInicio;
    private Date fechaFinalizacion;
    private double montoAlquiler;
    private boolean estado;
    private int idInquilino;
    private int idInmueble;

    private Inquilino inquilino;
    private Inmueble inmueble;

    public Contrato() {
    }

    public Contrato(int idContrato, Date fechaInicio, Date fechaFinalizacion, double monto, boolean estado, int idInquilino, int idInmueble, Inquilino inquilino, Inmueble inmueble) {
        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.montoAlquiler = monto;
        this.estado = estado;
        this.idInquilino = idInquilino;
        this.idInmueble = idInmueble;
        this.inquilino = inquilino;
        this.inmueble = inmueble;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public Date getFechaInicio() {

        return fechaInicio;
    }
    public String getFechaInicioFormateada() {
        if (this.fechaInicio == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return simpleDateFormat.format(this.fechaInicio);
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }
    public String getFechaFinalizacionFormateada() {
        if (this.fechaFinalizacion == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return simpleDateFormat.format(this.fechaFinalizacion);
    }
    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public double getMontoAlquiler() {
        return montoAlquiler;
    }

    public void setMontoAlquiler(double monto) {
        this.montoAlquiler = monto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    @Override
    public String toString() {
        return "Contrato{" +
                "idContrato=" + idContrato +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinalizacion=" + fechaFinalizacion +
                ", monto=" + montoAlquiler +
                ", estado=" + estado +
                ", idInquilino=" + idInquilino +
                ", idInmueble=" + idInmueble +
                ", inquilino=" + inquilino +
                ", inmueble=" + inmueble +
                '}';
    }
}
