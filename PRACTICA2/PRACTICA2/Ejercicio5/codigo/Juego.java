//
// GONZALO DE LA TORRE MARTINEZ
// JAVIER RAMIREZ PULIDO
//

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Enum de jugadores, un total de 4 jugadores [0,1,2,3]
 */
 enum Jugador {
     JUGADOR1(0),
     JUGADOR2(1),
     JUGADOR3(2),
     JUGADOR4(3);

    private final int numJugador;

    /**
     * Constructor del enum
     * @param numJugador numero de jugador empieza en 0
     */
    Jugador(int numJugador) {
        this.numJugador = numJugador;
        
    }
    
    /**
     * Devuelve el numero del jugador
     * @return numero de jugador
     */
    public int getNumJugador() {
        return this.numJugador;
    }
 }

 /**
  * @brief Clase Juego con las pautas necesarias para el desarollo del juego, siguiendo
  * el modelo Singleton
  */
 public class Juego {
 
    //Turno del jugadorActual
    private Jugador jugadorActual;
    //Se crea la unica instancia del Juego.
    private static Juego juego = null; 
    //Jugadores creados 
    private static int numJugadoresCreados = 0; 
    // Estructura Map donde se guardan las preguntas con su respuesta correcta
    Map<String, String> PreguntaRespuesta; 
    // Fichero que contiene las preguntas y va cambiando conforme respondemos cada letra
    File fichero_leer;
    // Contendra la pregunta que toque en cada momento
    String pregunta;
    // Contendra la respuesta a la pregunta que toque en cada momento
    String respuesta;
    // Para le lectura 
    Scanner s;

    /**
     * Constructor de la clase Juego
     */
    private Juego(){
        this.jugadorActual = Jugador.JUGADOR1;
        this.PreguntaRespuesta = new HashMap<String, String>();
        this.fichero_leer = new File(" ");
    }
    
    /**
     * @brief Metodo el cual lee un fichero diferente con la pregunta de la letra por la que vamos
     */
    Map<String, String> SacarPreguntaRespuesta(int respondidas, int jugador){
    
        try {
        	
        	//para contar la letra por la que vamos
		char letra_que_toca;
		//la letra sera la 'a' + el numero de preguntas que hayamos respondido (Si hemos respondido 2 -> a + 2 = c)
		letra_que_toca = (char) ('a' + respondidas );
		//EL fichero que leeremos sera diferente segun el jugador (para que no se repitan) y la letra (para sacarlas por orden)
		fichero_leer=new File("./data/jugador"+jugador+"/"+letra_que_toca+".csv");
		
		
                // Leemos el contenido del fichero
		s = new Scanner(fichero_leer);

		//Codigo de internet para poder escoger lineas aleatorias de archivos .csv
		InputStream is = null;
		BufferedReader reader = null;
		
		try {
		
			is = new FileInputStream(fichero_leer);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fichero_leer)));

			//Creo una lista que almacene todas las lienas dentro del archivo que contiene las preguntas de una letra
			List<String> list = new ArrayList<String>();
			//Y un string donde se van a meter las lineas, inicialmente solo la primera
			String line = reader.readLine();
			
			//Mientras haya lineas, las va metiendo en el array que va a almacenar todas las lineas
			while (line != null) {
			
				list.add(line);
				line = reader.readLine();
			
			}

			//Se genera un numero random entre 0 y el numero de lineas que hay dentro
			SecureRandom random = new SecureRandom();
			int row = random.nextInt(list.size());

			//sacamos de la lista la linea en la posicion aleatoria 'row'
			pregunta = list.get(row); 
			//La respuesta es la ultima palabra de la frase, por lo que vamos al ultimo espacio y lo siguiente es la respuesta
			respuesta = pregunta.substring(pregunta.lastIndexOf(" ")+1); 
			//La pregunta es toda la linea menos lo que hay del ultimo espacio en adelante
			pregunta = pregunta.substring(0, pregunta.lastIndexOf(" ")); 
		
		}catch (Exception ex) {
            	
            		System.out.println("Error leyendo la pregunta aleatoria");
            	
            	}
            	
            	//Vacio para que no acumule mas preguntas que la actual
            	PreguntaRespuesta.clear();
            	//Guardo la pregunta y respuesta actual
		PreguntaRespuesta.put(pregunta, respuesta);
			
            
        } catch (Exception ex) {
        
            System.out.println("Mensaje: " + ex.getMessage());
            
        }finally {
        
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
	    try {
	    
                if (s != null)
                
                    s.close();
                    
           } catch (Exception ex2) {
           
		 System.out.println("Mensaje 2: " + ex2.getMessage());
	   
	   }
	   
        }
        
        return PreguntaRespuesta;
   
   }


     /**
     * @brief Metodo el cual lee un fichero con la estructura usuario (\n) contraseña y la introduce en el hash.
     */
     public String SacarPregunta(){
     
     	//Saca la pregunta del map con un iterator a la primera posicion (y unica)
     	Iterator<String> it = PreguntaRespuesta.keySet().iterator();
        Object key = it.next();
        
        //la devuelve
        return String.valueOf(key);
     }
    
     /**
     * @brief Metodo el cual lee un fichero con la estructura usuario (\n) contraseña y la introduce en el hash.
     */
     public String SacarRespuesta(){
     
     	//Devuelve la respuesta a la pregunta concreta
        return PreguntaRespuesta.get(pregunta);
        
     }


    /**
     * Meotodo devuelve los jugadores creados hasta el momento
     * @return numero de jugadores creados hasta el momento
     */
    public static int getNumJugadoresCreados(){
        return numJugadoresCreados;
    }

    /**
     * Metodo que devuelve la instancia unica del Juego
     * @return el juego actual
     */
    public static Juego getInstanceJuego(){
		if(juego == null){
			juego = new Juego();
		}
        return juego;
    }

  

    /**
     * Metodo que devuelve el jugador actual, como se asigna un jugador al cliente 
     * conectado al servidor, se va incrementado en todo momento los jugadores creados
     * @return devuelve el jugador actual
     */
    public Jugador getJugadorActual(){
        numJugadoresCreados++;
        return this.jugadorActual;
    }

    /**
     * Devuelve el numero del jugador actual
     * @return numero jugador Actual
     */
    public int getJugadorActualNumero(){
        return jugadorActual.getNumJugador();
    }

    /**
     * Devuelve el numero maximo de jugadores
     * @return numero max de jugadores
     */
    public int getNumMaxJugadores(){
        return Jugador.values().length;
    }

    /**
     * Cambia al siguiente jugador
     */
    public void cambiarJugador(){
        int numero = getJugadorActualNumero();
        jugadorActual = Jugador.values()[(numero+1)%Jugador.values().length];
    }

}

