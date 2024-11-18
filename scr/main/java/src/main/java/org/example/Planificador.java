package org.example;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Queue;
import java.util.function.Consumer;

public class Planificador {

    ArrayList<ProcesoSistema> procesosTabla = new ArrayList<>();
    LinkedList<ProcesoSistema> procesos = new LinkedList<>();
    LinkedList<ProcesoSistema> procesosTiempoReal = new LinkedList<>();
    LinkedList<ProcesoSistema> procesosUsuario = new LinkedList<>();

    EstructuraTablaProcesos estructuraTablaProcesos = new EstructuraTablaProcesos(procesosTabla);

    Queue<ProcesoSistema> prioridad0 = new LinkedList<>();
    Queue<ProcesoSistema> prioridad1 = new LinkedList<>();
    Queue<ProcesoSistema> prioridad2 = new LinkedList<>();
    Queue<ProcesoSistema> prioridad3 = new LinkedList<>();
    GUI gui;
    ArrayList<RecursoSistema> recursosSistema;

    int memoria = 2048;

    public Planificador() throws InterruptedException {
        
        leerArchivo();

        gui = new GUI("SSOP - Simulador de Planificador", procesosTabla);
        inicializarRecursos();

        while (!procesosUsuario.isEmpty() || !procesos.isEmpty() ||
                !prioridad0.isEmpty() || !prioridad1.isEmpty() ||
                !prioridad2.isEmpty() || !prioridad3.isEmpty()) {

            if (!prioridad0.isEmpty()) {
                ejecutarProceso(prioridad0.poll());
            } else if (!procesos.isEmpty()) {
                ProcesoSistema proceso = procesos.poll();
                if (Asignar(proceso)) {
                    ejecutar();
                }
            } else {
                ejecutar();
            }
        }
    }

    private void inicializarRecursos() {
        recursosSistema = new ArrayList<>();
        recursosSistema.add(new RecursoSistema(1, "Impresora", false, 0));
        recursosSistema.add(new RecursoSistema(2, "Impresora", false, 1));
        recursosSistema.add(new RecursoSistema(3, "Escaner", false, 2));
        recursosSistema.add(new RecursoSistema(4, "CD", false, 3));
        recursosSistema.add(new RecursoSistema(5, "CD", false, 4));
        recursosSistema.add(new RecursoSistema(6, "Modem", false, 5));
        recursosSistema.add(new RecursoSistema(7, "Camara", false, 6));
        recursosSistema.add(new RecursoSistema(8, "Parlantes", false, 7));
    }

    public boolean Asignar(ProcesoSistema proceso) {

        if (proceso.getPrioridadInicial() == 0) {
            proceso.setEstatus("Listo");
            proceso.setUbicacionMemoria("1 - 2");
            prioridad0.offer(proceso);
            return true;
        } else {
            return asignarUsuario(proceso);
        }
    }

    private boolean asignarUsuario(ProcesoSistema proceso) {

        boolean bloquesContinuos = false;
        int necesarios = 1;
        Color color = colorAleatorio();
    
        while (true) {
            if (proceso.getMemoriaRequerida() <= 32 * necesarios) {
                if (memoria - 32 * necesarios >= 0 && verificarRecursos(proceso)) {
                    for (int i = 2; i < gui.bloquesMemoria.length; i++) {
                        if (!gui.bloquesMemoria[i].isOcupado()) {
                            bloquesContinuos = verificarContinuos(i, necesarios, proceso);
                            if (bloquesContinuos)
                                break;
                        }
                    }
                    for (int i : proceso.getUbicaciones()) {
                        gui.bloquesMemoria[i].getLabelBloque().setBackground(color);
                        gui.bloquesMemoria[i].getLabelBloque().setText(String.valueOf(proceso.getIdProceso()));
                        gui.bloquesMemoria[i].setOcupado(true);
                    }
                    if (!proceso.getUbicaciones().isEmpty()) {
                        asignarRecursos(proceso);
                        memoria -= 32 * necesarios;
                        proceso.setEstatus("Listo");
                        if (proceso.getPrioridadInicial() == 1)
                            prioridad1.offer(proceso);
                        if (proceso.getPrioridadInicial() == 2)
                            prioridad2.offer(proceso);
                        if (proceso.getPrioridadInicial() == 3)
                            prioridad3.offer(proceso);
                        return true;
                    } else {
                        proceso.setEstatus("Bloqueado");
                        procesosUsuario.offer(proceso);
                        return false;
                    }
                } else {
                    proceso.setEstatus("Bloqueado");
                    procesosUsuario.offer(proceso);
                    return false;
                }
            } else {
                necesarios++;
            }
        }
    }
    

    public boolean verificarContinuos(int inicio, int cantidad, ProcesoSistema proceso) {
        ArrayList<Integer> ubicaciones = new ArrayList<>();
        for (int i = inicio; i < inicio + cantidad && i < 32; i++) {
            if (!gui.bloquesMemoria[i].isOcupado()) {
                ubicaciones.add(i);
            }
        }
        if (ubicaciones.size() == cantidad) {
            String ubis = " ";
            for (int ubicacion : ubicaciones) {
                ubis += ubicacion + " ";
                proceso.ubicaciones.add(ubicacion);
            }
            proceso.setUbicacionMemoria(ubis);
            return true;
        } else {
            return false;
        }
    }

    public boolean verificarRecursos(ProcesoSistema actual) {
        int impresorasDisponibles = contarRecursosDisponibles("Impresora");
        int cdsDisponibles = contarRecursosDisponibles("CD");
        int modemsDisponibles = contarRecursosDisponibles("Modem");
        int escanersDisponibles = contarRecursosDisponibles("Escaner");
        int camarasDisponibles = contarRecursosDisponibles("Camara");
        int parlantesDisponibles = contarRecursosDisponibles("Parlantes");

        return actual.getImpresorasSolicitadas() <= impresorasDisponibles &&
                actual.getCdsSolicitados() <= cdsDisponibles &&
                actual.getModemsSolicitados() <= modemsDisponibles &&
                actual.getEscaneresSolicitados() <= escanersDisponibles &&
                actual.getCamarasSolicitadas() <= camarasDisponibles &&
                actual.getParlantesSolicitados() <= parlantesDisponibles;
    }

    private int contarRecursosDisponibles(String tipo) {
        return (int) recursosSistema.stream()
                .filter(recurso -> recurso.getNombre().equals(tipo) && !recurso.isOcupado())
                .count();
    }

    public void asignarRecursos(ProcesoSistema proceso) {
        asignarRecursosTipo(proceso, "Impresora", proceso.getImpresorasSolicitadas(), proceso::setImpresorasAsignadas);
        asignarRecursosTipo(proceso, "Escaner", proceso.getEscaneresSolicitados(), proceso::setEscaneresAsignados);
        asignarRecursosTipo(proceso, "CD", proceso.getCdsSolicitados(), proceso::setCdsAsignados);
        asignarRecursosTipo(proceso, "Modem", proceso.getModemsSolicitados(), proceso::setModemsAsignados);
        asignarRecursosTipo(proceso, "Camara", proceso.getCamarasSolicitadas(), proceso::setCamarasAsignadas);
        asignarRecursosTipo(proceso, "Parlantes", proceso.getParlantesSolicitados(), proceso::setParlantesAsignados);
    }

    private void asignarRecursosTipo(ProcesoSistema proceso, String tipo, int cantidadSolicitada,
            Consumer<Integer> asignadosSetter) {
        int asignados = 0;
        while (asignados != cantidadSolicitada) {
            for (RecursoSistema recurso : recursosSistema) {
                if (recurso.getNombre().equals(tipo) && !recurso.isOcupado()) {
                    asignados++;
                    proceso.recursosAsignados.add(recurso);
                    asignadosSetter.accept(asignados);
                    recurso.setOcupado(true);
                    break;
                }
            }
        }
    }

    public void liberarRecursos(ProcesoSistema proceso) {
        if (!proceso.getRecursosAsignados().isEmpty()) {
            for (RecursoSistema recurso : proceso.getRecursosAsignados()) {
                recurso.setOcupado(false);
            }
            proceso.getRecursosAsignados().clear();
        }
        if (proceso.getPrioridadActual() == 0) {
            gui.bloquesMemoria[0].restablecer();
            gui.bloquesMemoria[1].restablecer();
            gui.bloquesMemoria[0].setOcupado(false);
            gui.bloquesMemoria[1].setOcupado(false);
        }
        if (proceso.getPrioridadActual() == 1 || proceso.getPrioridadActual() == 2
                || proceso.getPrioridadActual() == 3) {
            ArrayList<Integer> sectores = new ArrayList<>(proceso.getUbicaciones());
            for (int sector : sectores) {
                gui.bloquesMemoria[sector].restablecer();
                gui.bloquesMemoria[sector].setOcupado(false);
            }
            memoria += 32 * proceso.getUbicaciones().size();
        }
    }

    private void ejecutarProceso(ProcesoSistema proceso) {
        proceso.setEstatus("Ejecucion");
        while (proceso.getTiempoProcesadorRestante() > 0) {
            System.out.println("Proceso " + proceso.getIdProceso() + " en ejecución, tiempo restante: "
                    + proceso.getTiempoProcesadorRestante());
            proceso.setTiempoProcesadorRestante(proceso.getTiempoProcesadorRestante() - 1);
            try {
                gui.actualizar();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Proceso " + proceso.getIdProceso() + " finalizado.");
        proceso.setEstatus("Finalizado");
        liberarRecursos(proceso);
    }

    public void ejecutar() {
        if (!procesosTiempoReal.isEmpty()) {
            ejecutarProceso(procesosTiempoReal.poll());
            return;
        }
        if (!prioridad1.isEmpty()) {
            ejecutarProceso(prioridad1.poll());
            return;
        }
        if (!prioridad2.isEmpty()) {
            ejecutarProceso(prioridad2.poll());
            return;
        }
        if (!prioridad3.isEmpty()) {
            ejecutarProceso(prioridad3.poll());
            return;
        }
    }

    private void leerArchivo() {

        String archivo = "procesos2.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] valores = linea.split(",");

                if (valores.length >= 8) {

                    int tiempoLlegada = Integer.parseInt(valores[0].trim());
                    int prioridad = Integer.parseInt(valores[1].trim());
                    int tiempoProcesador = Integer.parseInt(valores[2].trim());
                    int mbytes = Integer.parseInt(valores[3].trim());
                    int cantImpresora = Integer.parseInt(valores[4].trim());
                    int cantEscan = Integer.parseInt(valores[5].trim());
                    int cantCds = Integer.parseInt(valores[6].trim());
                    int cantCamaras = Integer.parseInt(valores[7].trim());
                    int cantParlantes = valores.length > 8 ? Integer.parseInt(valores[8].trim()) : 0;

                    ProcesoSistema proceso = new ProcesoSistema(tiempoLlegada, "Creado", tiempoLlegada, prioridad, prioridad,
                            tiempoProcesador, tiempoProcesador, mbytes, "Sin asignar",
                            cantImpresora, 0, cantEscan, 0, 0, 0, cantCds, 0, cantCamaras, 0, cantParlantes, 0);

                    procesos.add(proceso);
                    procesosTabla.add(proceso);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color colorAleatorio() {

        Random random = new Random();
        int maxComponent = 200;
        return new Color(maxComponent - random.nextInt(56), maxComponent - random.nextInt(24),
                maxComponent - random.nextInt(78));
    }
}