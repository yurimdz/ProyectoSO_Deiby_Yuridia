package org.example;

import javax.swing.*;
import java.awt.*;

public class BloqueMemoria {

    int id;
    boolean Ocupado;
    JLabel labelBloque;

    public BloqueMemoria(int id, boolean enUso, JLabel labelCasilla) {
        this.id = id;
        this.Ocupado = enUso;
        this.labelBloque = labelCasilla;
    }

    public void restablecer() {

        labelBloque.setText("Disponible");
        labelBloque.setBackground(Color.LIGHT_GRAY);
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOcupado() {
        return Ocupado;
    }

    public void setOcupado(boolean enUso) {
        this.Ocupado = Ocupado;
    }

    public JLabel getLabelBloque() {
        return labelBloque;
    }

    public void setLabelBloque(JLabel labelBloque) {
        this.labelBloque = labelBloque;
    }
}