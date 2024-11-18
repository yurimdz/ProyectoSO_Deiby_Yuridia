package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.List;

class EstructuraTablaProcesos extends AbstractTableModel {

    private final List<ProcesoSistema> datos;

    private final String[] columnas = { "ID", "Estatus", "T. de llegada", "Prioridad Inicial", "Prioridad Actual",
            "Tiempo de procesador requerido", "Tiempo de procesador restante", "Memoria Requerida",
            "Ubicación en memoria", "Impresoras Solicitadas", "Impresoras asignadas", "Escáneres Solicitados",
            "Escáneres asignados", "Módems solicitados", "Módems asignados", "CDs solicitados", "CDs asignados",
            "Cámaras solicitadas", "Cámaras asignadas", "Parlantes solicitados", "Parlantes asignados" };

    public EstructuraTablaProcesos(List<ProcesoSistema> datos) { 
        this.datos = datos;
    }

    @Override
    public int getRowCount() {
        return datos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProcesoSistema dato = datos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return dato.getIdProceso();
            case 1:
                return dato.getEstatus();
            case 2:
                return dato.getTiempoLlegada();
            case 3:
                return dato.getPrioridadInicial();
            case 4:
                return dato.getPrioridadActual();
            case 5:
                return dato.getTiempoProcesadorRequerido();
            case 6:
                return dato.getTiempoProcesadorRestante();
            case 7:
                return dato.getMemoriaRequerida();
            case 8:
                return dato.getUbicacionMemoria();
            case 9:
                return dato.getImpresorasSolicitadas();
            case 10:
                return dato.getImpresorasAsignadas();
            case 11:
                return dato.getEscaneresSolicitados();
            case 12:
                return dato.getEscaneresAsignados();
            case 13:
                return dato.getModemsSolicitados();
            case 14:
                return dato.getModemsAsignados();
            case 15:
                return dato.getCdsSolicitados();
            case 16:
                return dato.getCdsAsignados();
            case 17:
                return dato.getCamarasSolicitadas();
            case 18:
                return dato.getCamarasAsignadas();
            case 19:
                return dato.getParlantesSolicitados();
            case 20:
                return dato.getParlantesAsignados();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}