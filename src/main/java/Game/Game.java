/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Utilidades.Carril;
import Utilidades.Posicion;
import Utilidades.Carro;
import Utilidades.Conductor;
import Identificacion.CarroId;
import Identificacion.JuegoId;
import Identificacion.JugadorId;
import Identificacion.Nombre;
import Persistencia.Persistencia;
import Valores.Pista;
import Valores.Podio;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author haroldalre <haroldalre@gmail.com>
 */
public class Game {

    private Nombre Nombre;

    public Game() {
    }

    protected Map<JugadorId, Jugador> players = new HashMap<>();
    protected Pista track;
    protected Boolean playing;
    protected Podio podium = new Podio();
    protected ArrayList<Pista> tracks = new ArrayList<>();
    protected ArrayList<Carro> carsPlaying = new ArrayList<>();
    protected ArrayList<Carril> linesPlaying = new ArrayList<>();
    protected ArrayList<GanadoresBD> victor = new ArrayList<>();
    private final Carro car = new Carro();
    private Boolean firtsgame=true;

 

    // Hacer players y la lista mirando los ids
    public void hacerJugador(JugadorId jugadorId, Nombre name, Color color) {
        Jugador jugador = new Jugador(Nombre, color, 0);
        Jugador put = players.put(jugadorId, jugador);
        hacerConductor(name);
    }

    // Asignar si el jugador es un conductor, crear el conductor y darle un carro.
    public void hacerConductor(Nombre nombre) {
        UUID id;            
        Scanner in = new Scanner(System.in);
        System.out.println("Quiere asignar el jugador con nombre: " + nombre.getNombre() + " sea el driver ? " + "SI/NO");
        while (!in.hasNext("[siSInoNO]")) {
            System.out.println("Solo se reciben como respuesta SI/NO ó si/no");
            in.next();
        }
        String consultaConductores = in.next();
        if (consultaConductores.equals("SI") || consultaConductores.equals("SI")) {
            Conductor conductor = new Conductor(nombre.getNombre());
            id = UUID.randomUUID();
            CarroId carroId = new CarroId(id);
            car.asignarConductor(carroId, conductor);
        }
    }

    /* 
        Asignar pistas segun el total de carros        
     */
    public void hacerPistas() {
        int kilometrosRandom;
        int numeroCarriles = car.numeroCarros();
        for (int i = 0; i < car.numeroCarros(); i++) {
            kilometrosRandom = (int) (Math.random() * 100 + 1);
            Pista pista = new Pista(kilometrosRandom, numeroCarriles);
            tracks.add(pista);
        }
    }

    public void darPrimerLugar(JugadorId jugadorId) {
        podium.asignarPrimerLugar(players.get(jugadorId));
        System.out.println("**********" + players.get(jugadorId).nombre().getNombre() + ": Primer Lugar" + "***********");

    }

    public void darSegundoLugar(JugadorId jugadorId) {
        podium.asignarSegundoLugar(players.get(jugadorId));
        System.out.println("**********" + players.get(jugadorId).nombre().getNombre() + ": Segundo Lugar" + "***********");
    }

    public void darTercerLugar(JugadorId jugadorId) {
        podium.asignarTercerLugar(players.get(jugadorId));
        System.out.println("**********" + players.get(jugadorId).nombre().getNombre() + ": Tercer Lugar" + "***********");

    }

    public void empezarJuego() {
        //La identificacion para jugar
        UUID id;
        id = UUID.randomUUID();
        JuegoId juegoId = new JuegoId(id);

        //Asignar pista
        Scanner in = new Scanner(System.in);
        System.out.println("Para iniciar el juego, elige la  pista (numero) en la que deseas jugar:  ");
        System.out.println("Pistas: ");
        int counter = 1;
        for (Pista p : tracks) {
            System.out.println(counter + "." + " Kilometros: " + p.kilometros() + " Número de carriles:  " + p.numeroDeCarriles());
            counter++;
        }
       while(!in.hasNextInt()) in.next();   
        int pistaElegida = in.nextInt();

        // Empezar con los carros en juego
        car.carros().forEach(new BiConsumer<CarroId, Conductor>() {
            @Override
            public void accept(CarroId key, Conductor value) {
                Carro carrosJuego = new Carro(value, 0, Color.yellow, juegoId);
                carsPlaying.add(carrosJuego);
                
                //añadir carros a los Carriles para jugar
                int kilometrosToMetros = tracks.get(pistaElegida - 1).kilometros() * 1000;
                Posicion posicion = new Posicion(0, kilometrosToMetros);
                Carril carriles = new Carril(key, juegoId, posicion, kilometrosToMetros, false);
                linesPlaying.add(carriles);
            }
        });

        //Empezar a jugar
        playing = true;
        Conductor conductor = new Conductor();
        System.out.println("Empezar carrera");

        //Tiene que haber tres ganadores para continuar
        while (playing) {
            int contador = 0;
            System.out.println("Ubicacion " + "Final: " + linesPlaying.get(contador).metros() + " metros");
            for (Carro carros : carsPlaying) {
                //Si el carro no ha ganado sigue jugando
                if (!yaGanoCarro(carros.conductor().nombre())) {
                    int mover = conductor.lanzarDado() * 100;
                    carros.setDistancia(carros.distancia() + mover);
                    linesPlaying.get(contador).moverCarro(linesPlaying.get(contador).posicion(), mover);
                    System.out.println(carros.conductor().nombre() + ":" + " mueve: " + mover + " Nueva posición: " + carros.distancia());

                    //Si el jugador llego a la final, asignarle la posición y el podio
                    if (linesPlaying.get(contador).desplazamientoFinal()) {
                        if (podium.primerLugar() == null) {
                            asignarPrimerLugar(jugadorID(carros.conductor().nombre()));
                        } else if (podium.segundoLugar() == null) {
                            asignarSegundoLugar(jugadorID(carros.conductor().nombre()));
                        } else if (podium.tercerLugar() == null) {
                            asignarTercerLugar(jugadorID(carros.conductor().nombre()));
                        }
                    }
                }
                contador++;
            }
            if (podium.completo()) {
                break;
            }
        }
        mostrarPodio();
        guardarRegistroBD();
        repetirJuego();
    }
    public Map<JugadorId, Jugador> players() {
        return players;
    }
    public Boolean jugando() {
        return playing;
    }
       // Devuelve el id del jugador dando el nombre del jugador
    public JugadorId jugadorID(String nombre) {
        JugadorId lookId = null;
        for (JugadorId keys : players.keySet()) {
            if (players.get(keys).nombre().getNombre().equals(nombre)) {
                lookId = keys;
            }
        }
        return lookId;
    }
    //Devuelve True si el carro en la carrera ya ganó
    public Boolean yaGanoCarro(String nombre) {
        boolean yaGano = false;
        if (podium.tercerLugar() == players.get(jugadorID(nombre))
                || podium.primerLugar() == players.get(jugadorID(nombre))
                || podium.segundoLugar() == players.get(jugadorID(nombre))) {
            yaGano = true;
        }
        return yaGano;
    }

    // Método para guardar el registro  en la base de datos 
    // Si es la primera partida crea el registro en la base de datos  y si no es la primera solo hace modificaciones.
    public void guardarRegistroBD() {
        int id = 1;
        int contador = 0;
        Persistencia controller = new Persistencia();
        for (Carro carros : carsPlaying) {
            int vecesPrimero = 0;
            int vecesSegundo = 0;
            int vecesTercero = 0;
            String nombreCondParticipantes = carros.conductor().nombre();
            if (!firtsgame) {
                vecesPrimero = victor.get(contador).getVecesPrimero();
                vecesSegundo = victor.get(contador).getVecesSegundo();
                vecesTercero = victor.get(contador).getVecesTercero();
            }
            if (podium.primerLugar().nombre().getNombre().equals(nombreCondParticipantes)) {
                vecesPrimero += 1;

            } else if (podium.segundoLugar().nombre().getNombre().equals(nombreCondParticipantes)) {
                vecesSegundo += 1;

            } else if (podium.tercerLugar().nombre().getNombre().equals(nombreCondParticipantes)) {
                vecesTercero += 1;
            }

            if (firtsgame) {
                GanadoresBD conductoresG = new GanadoresBD(id, nombreCondParticipantes, vecesPrimero, vecesSegundo, vecesTercero);
                victor.add(conductoresG);
                id++;
            } else {
                victor.get(contador).setVecesPrimero(vecesPrimero);
                victor.get(contador).setVecesSegundo(vecesSegundo);
                victor.get(contador).setVecesTercero(vecesTercero);
                contador++;
            }

        }

        for (GanadoresBD g : victor) {
            if (firtsgame) {
                controller.crearRegistro(g);
            } else {
                controller.modificarRegistro(g);
            }
        }
        firtsgame = false;
    }

    // Indicar si repetir el juego y resetear listas de juego
    public void repetirJuego() {
        Scanner in = new Scanner(System.in);
        System.out.println("Desea jugar otra carrera?  SI/NO");
        while (!in.hasNext("[siSInoNo]")) {
            System.out.println("Solo se reciben como respuesta SI/NO ó si/no");
            in.next();
        }
        String jugarOtro = in.next();
        if (jugarOtro.equals("SI") || jugarOtro.equals("si")) {
            carsPlaying.clear();
            linesPlaying.clear();
            Podio podioNuevo = new Podio();
            podium = podioNuevo;
            iniciarJuego();

        }

    }

    //Mostrar el podio
    public void mostrarPodio() {
        System.out.println("--------Podio--------");
        System.out.println("Primer Lugar:  " + podium.primerLugar().nombre().getNombre());
        System.out.println("Segundo Lugar:  " + podium.segundoLugar().nombre().getNombre());
        System.out.println("Tercer Lugar:  " + podium.tercerLugar().nombre().getNombre());
        System.out.println("----------------------");

    }

