package com.banquillo.core;

import com.banquillo.core.excepciones.DorsalRepetidoException;
import com.banquillo.core.excepciones.EntrenamientoDuplicadoException;
import com.banquillo.core.excepciones.JugadorNoEncontradoException;
import com.banquillo.core.modelo.Categoria;
import com.banquillo.core.modelo.Entrenador;
import com.banquillo.core.modelo.Entrenamiento;
import com.banquillo.core.modelo.Equipo;
import com.banquillo.core.modelo.Jugador;
import com.banquillo.core.modelo.Notificable;
import com.banquillo.core.modelo.Posicion;
import com.banquillo.core.persistencia.RepositorioJson;
import com.banquillo.core.servicio.RotacionJusta;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static final Scanner teclado = new Scanner(System.in);
    private static final RepositorioJson repositorio = new RepositorioJson(Path.of("datos", "banquillo.json"));
    private static Equipo equipo;

    public static void main(String[] args) {
        cargarDatos();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerNumero("Elige una opción: ");
            switch (opcion) {
                case 1 -> anadirJugador();
                case 2 -> listarPlantilla();
                case 3 -> borrarJugador();
                case 4 -> crearEntrenamiento();
                case 5 -> listarEntrenamientos();
                case 6 -> pasarLista();
                case 7 -> verAsistencia();
                case 8 -> verFicha();
                case 9 -> registrarEntrenador();
                case 10 -> avisarATodos();
                case 11 -> verEstadisticas();
                case 12 -> guardarDatos();
                case 13 -> prepararPartido();
                case 0 -> {
                    guardarDatos();
                    System.out.println("¡Hasta el próximo entrenamiento!");
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void cargarDatos() {
        try {
            equipo = repositorio.cargar().orElse(new Equipo("Alevín A", Categoria.ALEVIN));
            System.out.println("Datos cargados: " + equipo.getPlantilla().size() + " jugadores y "
                    + equipo.getEntrenamientos().size() + " entrenamientos.");
        } catch (IOException | RuntimeException e) {
            System.out.println("No se pudieron cargar los datos (" + e.getMessage() + "). Se empieza con un equipo vacío.");
            equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        }
    }

    private static void guardarDatos() {
        try {
            repositorio.guardar(equipo);
            System.out.println("Datos guardados.");
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== BANQUILLO · " + equipo.getNombre() + " (" + equipo.getCategoria().getNombreVisible() + ") ===");
        System.out.println("1. Añadir jugador");
        System.out.println("2. Ver plantilla");
        System.out.println("3. Borrar jugador");
        System.out.println("4. Crear entrenamiento");
        System.out.println("5. Ver entrenamientos");
        System.out.println("6. Pasar lista");
        System.out.println("7. Ver asistencia");
        System.out.println("8. Ficha de jugador");
        System.out.println("9. Registrar entrenador");
        System.out.println("10. Avisar a todos");
        System.out.println("11. Estadísticas");
        System.out.println("12. Guardar ahora");
        System.out.println("13. Preparar partido");
        System.out.println("0. Salir");
    }

    private static int leerNumero(String mensaje) {
        System.out.print(mensaje);
        while (!teclado.hasNextInt()) {
            teclado.next();
            System.out.print("Eso no es un número. Prueba otra vez: ");
        }
        int numero = teclado.nextInt();
        teclado.nextLine();
        return numero;
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private static Posicion leerPosicion() {
        while (true) {
            String texto = leerTexto("Posición (PORTERO, DEFENSA, CENTROCAMPISTA, DELANTERO): ").toUpperCase();
            try {
                return Posicion.valueOf(texto);
            } catch (IllegalArgumentException e) {
                System.out.println("Posición no válida.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Usa el formato AAAA-MM-DD, por ejemplo 2026-11-03.");
            }
        }
    }

    private static void anadirJugador() {
        String nombre = leerTexto("Nombre: ");
        int dorsal = leerNumero("Dorsal (1-99): ");
        Posicion posicion = leerPosicion();
        String contacto = leerTexto("Contacto de la familia (email inventado, Enter para dejarlo vacío): ");
        try {
            Jugador jugador = new Jugador(nombre, dorsal, posicion, contacto);
            equipo.anadirJugador(jugador);
            System.out.println("Añadido: " + jugador);
        } catch (DorsalRepetidoException | IllegalArgumentException e) {
            System.out.println("No se pudo añadir: " + e.getMessage());
        }
    }

    private static void listarPlantilla() {
        if (equipo.getPlantilla().isEmpty()) {
            System.out.println("La plantilla está vacía.");
            return;
        }
        for (Jugador jugador : equipo.getPlantilla()) {
            System.out.println(jugador);
        }
    }

    private static void borrarJugador() {
        if (equipo.getPlantilla().isEmpty()) {
            System.out.println("La plantilla está vacía.");
            return;
        }
        listarPlantilla();
        int dorsal = leerNumero("Dorsal del jugador a borrar: ");
        try {
            equipo.borrarJugador(dorsal);
            System.out.println("Jugador con el dorsal " + dorsal + " borrado.");
        } catch (JugadorNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void crearEntrenamiento() {
        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
        String objetivo = leerTexto("Objetivo: ");
        try {
            Entrenamiento entrenamiento = new Entrenamiento(fecha, objetivo);
            equipo.anadirEntrenamiento(entrenamiento);
            System.out.println("Entrenamiento creado: " + entrenamiento);
        } catch (EntrenamientoDuplicadoException | IllegalArgumentException e) {
            System.out.println("No se pudo crear: " + e.getMessage());
        }
    }

    private static void listarEntrenamientos() {
        if (equipo.getEntrenamientos().isEmpty()) {
            System.out.println("No hay entrenamientos.");
            return;
        }
        for (Entrenamiento entrenamiento : equipo.getEntrenamientos()) {
            System.out.println(entrenamiento);
        }
    }

    private static void pasarLista() {
        if (equipo.getPlantilla().isEmpty()) {
            System.out.println("La plantilla está vacía.");
            return;
        }
        LocalDate fecha = leerFecha("Fecha del entrenamiento (AAAA-MM-DD): ");
        Entrenamiento entrenamiento = equipo.buscarEntrenamiento(fecha);
        if (entrenamiento == null) {
            System.out.println("No hay ningún entrenamiento el " + fecha + ". Créalo primero con la opción 4.");
            return;
        }
        for (Jugador jugador : equipo.getPlantilla()) {
            String respuesta = leerTexto("¿Ha venido " + jugador.getNombre() + " (" + jugador.getDorsal() + ")? (s/n): ");
            if (respuesta.equalsIgnoreCase("s")) {
                entrenamiento.marcarAsistencia(jugador);
            }
        }
        System.out.println("Lista guardada: " + entrenamiento);
    }

    private static void verAsistencia() {
        if (equipo.getPlantilla().isEmpty()) {
            System.out.println("La plantilla está vacía.");
            return;
        }
        for (Jugador jugador : equipo.jugadoresPorAsistencia()) {
            System.out.println(jugador.getDorsal() + " · " + jugador.getNombre() + ": "
                    + String.format("%.1f %%", equipo.porcentajeAsistencia(jugador)));
        }
    }

    private static void verFicha() {
        int dorsal = leerNumero("Dorsal: ");
        Jugador jugador = equipo.buscarPorDorsal(dorsal);
        if (jugador == null) {
            System.out.println("No hay ningún jugador con el dorsal " + dorsal + ".");
            return;
        }
        System.out.println(jugador);
        System.out.println("Asistencia: " + String.format("%.1f %%", equipo.porcentajeAsistencia(jugador))
                + " (" + equipo.entrenamientosAsistidos(jugador) + " de "
                + equipo.getEntrenamientos().size() + " entrenamientos)");
    }

    private static void registrarEntrenador() {
        String nombre = leerTexto("Nombre del entrenador: ");
        String licencia = leerTexto("Licencia (por ejemplo, Nivel 1): ");
        String contacto = leerTexto("Contacto (email inventado, Enter para dejarlo vacío): ");
        try {
            Entrenador entrenador = new Entrenador(nombre, licencia, contacto);
            equipo.setEntrenador(entrenador);
            System.out.println("Registrado: " + entrenador);
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private static void avisarATodos() {
        if (equipo.destinatariosAvisos().isEmpty()) {
            System.out.println("No hay nadie a quien avisar.");
            return;
        }
        String mensaje = leerTexto("Mensaje: ");
        for (Notificable destinatario : equipo.destinatariosAvisos()) {
            if (destinatario.getContacto() != null) {
                System.out.println("Aviso a " + destinatario.getContacto() + ": " + mensaje);
            } else {
                System.out.println("(" + destinatario.getNombre() + " no tiene contacto)");
            }
        }
    }

    private static void verEstadisticas() {
        System.out.println("Equipo: " + equipo.getNombre() + " (" + equipo.getCategoria().getNombreVisible() + ")");
        System.out.println("Jugadores: " + equipo.getPlantilla().size()
                + " · Entrenamientos: " + equipo.getEntrenamientos().size());
        System.out.println("Jugadores por posición: " + equipo.jugadoresPorPosicion());
        System.out.println("Asistencia media: " + String.format("%.1f %%", equipo.asistenciaMedia()));
        List<String> nombres = equipo.jugadoresConAsistenciaMenorQue(50).stream()
                .map(Jugador::getNombre)
                .toList();
        System.out.println("Con menos del 50 %: " + (nombres.isEmpty() ? "nadie" : String.join(", ", nombres)));
    }

    private static void prepararPartido() {
        if (equipo.getPlantilla().isEmpty()) {
            System.out.println("La plantilla está vacía.");
            return;
        }
        listarPlantilla();
        String texto = leerTexto("Dorsales convocados (separados por comas): ");
        List<Jugador> convocados = new ArrayList<>();
        try {
            for (String trozo : texto.split(",")) {
                int dorsal = Integer.parseInt(trozo.trim());
                Jugador jugador = equipo.buscarPorDorsal(dorsal);
                if (jugador == null) {
                    System.out.println("No hay ningún jugador con el dorsal " + dorsal + ".");
                    return;
                }
                if (!convocados.contains(jugador)) {
                    convocados.add(jugador);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Escribe solo números separados por comas, por ejemplo: 1,3,7");
            return;
        }
        int duracion = leerNumero("Duración del partido (min): ");
        int enCampo = leerNumero("Jugadores en el campo: ");
        int bloque = leerNumero("Minutos por bloque: ");
        try {
            Map<Jugador, Integer> minutos = new RotacionJusta().repartirMinutos(convocados, duracion, enCampo, bloque);
            System.out.println();
            for (Map.Entry<Jugador, Integer> entrada : minutos.entrySet()) {
                Jugador jugador = entrada.getKey();
                System.out.println(jugador.getDorsal() + " · " + jugador.getNombre() + ": " + entrada.getValue() + " min");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo preparar: " + e.getMessage());
        }
    }
}
