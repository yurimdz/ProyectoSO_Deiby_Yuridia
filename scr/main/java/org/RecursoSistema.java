package org.example;

public class RecursoSistema {

    int id;
    String nombre;
    boolean Ocupado;
    int ubicacion;

    public RecursoSistema(int id, String nombre, boolean Ocupado, int ubicacion) {

        this.id = id;
        this.nombre = nombre;
        this.Ocupado = Ocupado;
        this.ubicacion = ubicacion;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isOcupado() {
        return Ocupado;
    }

    public void setOcupado(boolean Ocupado) {
        this.Ocupado = Ocupado;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }
}