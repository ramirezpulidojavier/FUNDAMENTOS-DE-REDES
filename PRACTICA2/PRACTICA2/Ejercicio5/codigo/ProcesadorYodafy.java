//
// GONZALO DE LA TORRE MARTINEZ
// JAVIER RAMIREZ PULIDO
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;



/**
 * @brief Clase procesador la cual hereda de Thread para procesar mas de un cliente
 */
public class ProcesadorYodafy extends Thread{
	//Creacion de un juego que sera la partida en si
	Juego juego;
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private Socket socketServicio;
	//Instancia de la clase autentificacion que se encargar del registro y el inicio de sesion
	Autentificacion auth = new Autentificacion();
	//Para comprobar que se han identificado correctamente
    	boolean autentificacion = false;
    	//Para cuando se hayan registrado comprobar si es correcta la operacion
    	boolean registro = false;
    	//Indica el final del juego, incialmente a false
    	static boolean terminado = false;
    	//Cantidad de clientes que tenemos conectados al servidor
    	static int jugadores_Conectados = 0;
    	//El indice que identifica al jugador concreto (cada cliente tiene su jugadorActual)
    	int jugadorActual;
    	//El indice que identifica al jugador al que le toca jugar ahora. Inicialmente empieza siempre el mismo
   	static int jugadorTocaJugar = 0;
   	//El numero de preguntas que lleva un cliente respondidas
    	int respondidas = 0;
    	//El numero de preguntas que lleva un cliente acertadas
    	int acertadas = 0;
    	//El numero de preguntas que lleva un cliente falladas
    	int falladas = 0;
    	//Para guardar el enunciado de la pregunta mandada por el procesador y mostrarlo por pantalla
    	String pregunta; 
    	//Para guardar la respuesta de la pregunta mandada por el procesador y comprobarla
    	String respuesta;
    	//Para guardar el nombre del jugador 1
    	static String jug1;
    	//Para guardar el nombre del jugador 2
    	static String jug2;
    	//Vector que tiene en la posicion 0 el numero de aciertos y en la posicion 1 el numero de fallos del primer jugador
    	static int jugador1[] = new int[2];
    	//Vector que tiene en la posicion 0 el numero de aciertos y en la posicion 1 el numero de fallos del segundo jugador
    	static int jugador2[] = new int[2];
    
	
	
	/**
	   * Constructor de la clase Procesaodr
	   * @param socketServicio socketservicio a conectar
	   * @param juego unica instancia del juego
	*/
	public ProcesadorYodafy(Socket socketServicio, Juego juego) {
		this.socketServicio=socketServicio;
		this.juego=Juego.getInstanceJuego();
		
	}
	
	
	/**
	 * Metodo devuelve el total de jugadores conectados
	 * @return jugadores conectados
	 */
	public static int getjugadores_Conectados(){
		return jugadores_Conectados;
	}
	
	/**
        * Metodo devuelve el jugador que le toca jugar
        * @return numero jugador que le toca
        */
        public static int getJugadorTocaJugar(){
    	    return jugadorTocaJugar;
    	}
    	
    /**
     * @brief Override del metodo run, el cual crea las hebras cuando se llama con .start()
     */
    @Override
    public void run(){
    
	String palabraRecibida; //Palabra que envia el cliente para responder a la pregunta
	int opcionMenu;	//Eleccion del cliente de una de las opciones del menu
	
	try {
	
	    //Declaracion de los flujos por los que enviaremos y recibiremos la informacion con el cliente
	    BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
	    PrintWriter outPrint = new PrintWriter(socketServicio.getOutputStream(), true);
	    
	    //Mientras no se escoja una opcion valida seguir haciendo bucle.
	    do{
	    
	        opcionMenu = Integer.parseInt(inReader.readLine()); //Lee opcion elegida por el usuario
	    
	    }while(opcionMenu != 1 && opcionMenu != 2);

	    //Creo un usuario y una contrasenia generica hasta recibir la de usuario
	    String user="", pass="";
	    
	    //Cuando escoge una opcion correcta hay varias opciones
	    switch (opcionMenu) {
	        case 1: //Autentificacion si ha escogido iniciar sesion
	            
	            do{
	            
	            	 //Lee el usuario
	                user = inReader.readLine(); 
	                //Lee la contraseña
	                pass = inReader.readLine(); 
	                //llama al metodo de otra clase que comprueba que son datos correctos y devuelve un booleano
	                autentificacion = auth.iniciarSesion(user, pass); 
	                //Manda al cliente si se ha identificado ya o no
	                outPrint.println(autentificacion); 
	                
	            }while(autentificacion != true); //Mientras falle en el logueo
	            
	            break;
	            
	        case 2://Haber elegido la opcion de registrarse desde 0
	        
		    do{
		    
		        //Introduce un usuario
			user = inReader.readLine(); 
			//Introduce una contrasenia
			pass = inReader.readLine(); 
			//Llama a la funcion que dira si el registro es correcto y devuelve un booleano
			registro = auth.registrarUsuario(user, pass); 
			
			outPrint.println(registro); 
			
	            }while(registro != true); //Mientras realice mal la operacion de registro
	            
	            do{ //Ahora tiene que iniciar sesion
	            	
	            	//Lee el usuario
	                user = inReader.readLine(); 
	                //Lee la contraseña
	                pass = inReader.readLine(); 
	                //llama al metodo de otra clase que comprueba que son datos correctos y devuelve un booleano
	                autentificacion = auth.iniciarSesion(user, pass); 
	                //Envia si se ha identificado al cliente
	                outPrint.println(autentificacion); 
	                
	            }while(autentificacion != true); //Mientras falle en el logueo
	            
	            break;
	            
	        default: //opcion por defecto por si llega al switch sin que hayan metido 1 o 2
	        
	            break;
	            
	    } //FIN DEL SWITCH DE OPCIONMENU

	    //Determina si se ha alcanzado el numero maximo de jugadores para pasar a jugar o esperar a otro
	    if(Juego.getNumJugadoresCreados() <= juego.getNumMaxJugadores()-3){  //juego.GetnumMaxjugadores devuelve 4, para que juguen solo 2, le resto 3 y asi entra si aun queda otro
	    
	    	//Se ha registrado un usuario y le asigna un numero
	        System.out.println("¡¡¡¡¡BIENVENIDO " + user + " ERES " + juego.getJugadorActual() + "!!!!!"); 
	        
	        //envia el numero actual de jugador a ese cliente concreto...
	        outPrint.println(Integer.toString(juego.getJugadorActualNumero())); 
	        
	        //...y luego pasa de jugador	
	        juego.cambiarJugador(); 	
	        					
	    }else{ //Si se conectan jugadores de mas
	    
	    	//Si se han conectado mas jugadores del maximo
	        System.out.println("¡ERROR AL CONECTAR, MAXIMO NUMERO DE JUGADORES: " + juego.getNumMaxJugadores() + "!"); 
	        //Envia la palabra clave "maximo"
	        outPrint.println("maximo"); 
	        //Cierra el socket
	        socketServicio.close(); 
	        
	    }

	    //No empieza el juego hasta que se conecten todos los jugadores
	    
	    //Si no ha cerrado el socket es porque no se ha pasado del numero de jugadores y su conexion suma una mas a las que habia
	    jugadores_Conectados++; //1, 2, 3, 4 
	    
	    //Nombro al jugador actual conforme se conecte (resto uno para que vaya de 0-3)
	    jugadorActual = jugadores_Conectados-1; 
	    
	    //Doy nombre a la hebra segun el jugador que sea
	    setName(Integer.toString(jugadorActual)); 
	    
	    
	    do{ //Esperara mientras no esten todos los jugadores que quiero, para la prueba quiero 2
	        
	        //Recibe cuantos jugadores hay
	        jugadores_Conectados = ProcesadorYodafy.getjugadores_Conectados();
	        
                try { //Pone a dormir a la hebra
                    Thread.sleep(1000);
                } catch (Exception e) {}
                
	    }while(jugadores_Conectados != juego.getNumMaxJugadores()-2); 


	    //Sale del while anterior por lo que estan todos los que quiero conectados
	    //Solo quiero que el servidor imprima una vez el mensaje, ya que si no cada hebra lo haria, asi que lo hara el primer jugador
	    if(getName().equals("0")){ 
	    
	        System.out.println("-------------------------------------------------------");
               System.out.println("-------------------------------------------------------");
	        System.out.println("------------------¡JUGADORES LISTOS!-------------------");
	        System.out.println("-------------------------------------------------------");
		System.out.println("-------------------------------------------------------");
		
	    }
	    
	    //Envio esto para que lo reciban los jugadores por pantalla
	    outPrint.println("¡Jugadores listos, el juego va a comenzar!");

	    do{
	    	
	        //Envio al jugador que le toca jugar
	        outPrint.println(Integer.toString(jugadorTocaJugar));
	        
	        if(jugadorTocaJugar == jugadorActual){
	            
			    respondidas = Integer.parseInt(inReader.readLine());
			    acertadas = Integer.parseInt(inReader.readLine());
			    falladas = Integer.parseInt(inReader.readLine());
			    
			    if(respondidas<26){
			    
			    	    juego.SacarPreguntaRespuesta(respondidas, jugadorActual+1);
				    pregunta = juego.SacarPregunta();
				    respuesta = juego.SacarRespuesta();
				    outPrint.println(pregunta);
				    
				    System.out.println("-------------------------------------------------------");
				    System.out.println(user+" LLEVA: " + acertadas + " ACIERTOS Y "+falladas+" FALLOS");
				    
				    palabraRecibida = inReader.readLine(); //Leo la palabra enviada por el cliente 
				    System.out.println("RESPUESTA DE "+user+" : " + palabraRecibida);
				    
				    //Interpretacion 0 = acertado, 1 = fallado, -1 = pasapalabra
				    if(palabraRecibida.equals(respuesta)){
				    	
					outPrint.println("0");
					System.out.println("!ACERTASTE¡");
					
				    }else if(palabraRecibida.equals("pasapalabra")){
				    	
					outPrint.println("-1");
					System.out.println("!Pasas turno¡");
					outPrint.println(respuesta);
					System.out.println("LA RESPUESTA CORRECTA ERA: " + respuesta);
					
				    }else{
				    	
					outPrint.println("1");
					System.out.println("!FALLASTE¡");
					outPrint.println(respuesta);
					System.out.println("LA RESPUESTA CORRECTA ERA: " + respuesta);
					
				    }
				    
				    System.out.println("-------------------------------------------------------");
				    
				    //Paso turno
				    jugadorTocaJugar = (jugadorTocaJugar+1)%Juego.getNumJugadoresCreados(); 			    
			    
			    }else {
				   
				   int jugador = Integer.parseInt(inReader.readLine());
				   
				   if(jugador==0){
				   
				   	   jug1 = inReader.readLine();
				   	   jugador1[0]=Integer.parseInt(inReader.readLine());
					   jugador1[1]=Integer.parseInt(inReader.readLine());
					   outPrint.println(terminado);
					   jugadorTocaJugar = 1;
				   
				   }else if(jugador==1){
				   
				   	   jug2 = inReader.readLine();
				   	   jugador2[0]=Integer.parseInt(inReader.readLine());
					   jugador2[1]=Integer.parseInt(inReader.readLine());
					   terminado=true;
					   outPrint.println(terminado);
				   
				   }
				   
				   if(terminado){
				   
					   if(jugador1[0]>jugador2[0]){
					   
						   System.out.println("-------------------------------------------------------");
						   System.out.println("-------------------------------------------------------");
					    	   System.out.println("¡EL JUEGO HA JUEGO TERMINADO Y EL GANADOR ES EL JUGADOR 1: "+jug1+"!");
					    	   System.out.println("-------------------------------------------------------");
						   System.out.println("-------------------------------------------------------");
						   
				    	   }else if(jugador1[0]<jugador2[0]){
				    	   
				    	   	  System.out.println("-------------------------------------------------------");
						  System.out.println("-------------------------------------------------------");
				    	    	  System.out.println("¡EL JUEGO HA JUEGO TERMINADO Y EL GANADOR ES EL JUGADOR 2: "+jug2+"!");
				    	    	  System.out.println("-------------------------------------------------------");
						  System.out.println("-------------------------------------------------------");
						   
				    	   }else{
				    	    
				    	 	  if(jugador1[1]<jugador2[1]){
				    	 	  
				    	 	     System.out.println("-------------------------------------------------------");
						     System.out.println("-------------------------------------------------------");
				    	    	     System.out.println("¡EL JUEGO HA JUEGO TERMINADO Y EL GANADOR ES EL JUGADOR 1: "+jug1+"!");
				    	    	     System.out.println("-------------------------------------------------------");
						     System.out.println("-------------------------------------------------------");
				    	    	  
				    	    	  }else if(jugador1[1]>jugador2[1]){
				    	    	  
				    	    	     System.out.println("-------------------------------------------------------");
						     System.out.println("-------------------------------------------------------");
				    	    	     System.out.println("¡EL JUEGO HA JUEGO TERMINADO Y EL GANADOR ES EL JUGADOR 2:  "+jug2+"!");
				    	    	     System.out.println("-------------------------------------------------------");
						     System.out.println("-------------------------------------------------------");
						   
				    	    	  }else{
				    	    	  
				    	    	     System.out.println("-------------------------------------------------------");
						     System.out.println("-------------------------------------------------------");
				    	    	     System.out.println("¡EL JUEGO HA JUEGO TERMINADO Y HABEIS EMPATADO!");
				    	    	     System.out.println("-------------------------------------------------------");
						     System.out.println("-------------------------------------------------------");
					   	  
					   	  }
			    	    				    	    	            
			    		   }
			    		   
			    		   socketServicio.close();
				   
				   }
			    	   
			    }
			    
	        }else{
	        
	            do{
	            
	                try {
	                    Thread.sleep(1000);
	                } catch (Exception e) {}
	                
	                jugadorTocaJugar = ProcesadorYodafy.getJugadorTocaJugar();
	                outPrint.println(Integer.toString(jugadorTocaJugar));
	                
	                if(terminado)
	                    socketServicio.close();
	                
	            }while(jugadorActual != jugadorTocaJugar);
	            
	        }
	        
	    }while(terminado != true);
	    	    
	    	socketServicio.close();
	        
	    } catch (IOException e) {
	        System.err.println("Error al obtener los flujos de entrada/salida.");
	    }
	    
    }

}








//UTIL PARA EL EJEMPLO------------------------------
		/*// stream de lectura (por aquí se recibe lo que envía el cliente)
		private InputStream inputStream;
		// stream de escritura (por aquí se envía los datos al cliente)
		private OutputStream outputStream;
		
		// Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
		private Random random;*/
	//UTIL PARA EL EJEMPLO------------------------------











