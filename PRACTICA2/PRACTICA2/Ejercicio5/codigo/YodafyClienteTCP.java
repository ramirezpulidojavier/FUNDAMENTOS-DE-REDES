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
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @brief Clase Cliente la cual es encargada de gestionar la conexión del cliente al Servidor para comenzar el Juego.
 */
public class YodafyClienteTCP extends Thread{

	public static void main(String[] args) {
		
		if(args.length != 2){ //Hay que pasar dos argumentos, el host y el puerto
		
			//Mensaje de error y especificacion de ejecucion
			System.out.println("Error en el numero de argumentos. "+
			"Se ejecuta: java YodafyClienteTCP [host (localhost)] [nº_puerto]");
		
		}else{ //Ha metido el numero de argumentos correcto
		
			//Host que pasamos como argumento
			String host = args[0];
			//Puerto por el que escuchara el servidor
			int port = Integer.parseInt(args[1]);
			// Socket para la conexión TCP
			Socket socketServicio=null;
			//Entrada de teclado
			Scanner teclado = new Scanner(System.in);
			//Buffers para enviar y recibir informacion del procesador
			String buferEnvio;
			String buferRecepcion;
			//Cantidad de preguntas acertadas de cada cliente
			int acertados = 0;
			//Cantidad de preguntas falladas de cada cliente
			int fallados = 0;
			//Cantidad de preguntas respondidas de cada cliente
			int respondidas = 0;
			//Comprueba si se ha autentificado con exito.
			boolean autentificado = false;
			//Comprueba si se ha registrado con exito.
			boolean registrado=false; 
			//Enunciado de la pregunta para que la muestre por pantalla del cliente
			String pregunta;
			
			
			
			try {
			
				// Creamos un socket que se conecte a "host" y "port":
				socketServicio= new Socket(host, port);
							
				//Conectar con el buffer de escritura y lectura
				BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
                		PrintWriter outPrint    = new PrintWriter(socketServicio.getOutputStream(),true);
                		System.out.println("-------------------------------------------------------");
				System.out.println("CLIENTE CONECTADO AL SERVIDOR " + host + " CON EL PUERTO " + port);
				System.out.println("-------------------------------------------------------");
				
				
				int opcionMenu; //Numero que ingresara el cliente sobre el menu
				
				do{ //Menu de sesion, hacer esto hasta no escoger una opcion valida
				
				    //Muestro por pantalla el menu
				    System.out.println("-------------------------------------------------------");
				    System.out.println("-------------------------------------------------------");
				    System.out.println("\t#MENU#\n1. Iniciar sesion\n2. Registrase");
				    
				    System.out.print("\t-> : ");
				    
				    //Leo el numero introducido en terminal
				    opcionMenu = teclado.nextInt();
				    
				    //Envio la opcion al servidor
				    outPrint.println(Integer.toString(opcionMenu)); 
				    
				    //Si introduce una opcion incorrecta
				    if(opcionMenu != 1 && opcionMenu != 2)
				        System.out.println("¡Opcion del menu incorrecta, introduzca una opción valida!");		    
				   				    
				}while(opcionMenu != 1 && opcionMenu != 2); //Hasta que introduzca un valor correcto
				
				System.out.println("-------------------------------------------------------");
				System.out.println("-------------------------------------------------------");
				
				//Crea un usuario y contraseña para recibir los que meta el usuario
				String user=" ", pass=" ";
				
				switch (opcionMenu) {
				
				    case 1: //Haber escogido iniciar sesion
				        
				        System.out.println("#AUTENTIFICACION#");
				        
				        //Inicialmente no se ha autentificado
				        autentificado = false; 
				        
				        do{ //Mientras no este correctamente identificado le pedira nombre y contraseña
				            
				            //Pide el usuario
				            System.out.print("1. Introduce el usuario: ");
				            user = teclado.next();
				            
				            //Pide la contraseña
				            System.out.print("2. Introduce la contraseña: ");
				            pass = teclado.next();
				            
				            //Las envia al procesador para que lo compruebe
				            outPrint.println(user);
				            outPrint.println(pass);
				            
				            //Recibe la respuesta que sera un booleano que confirme si esta bien o no
				            autentificado = Boolean.parseBoolean(inReader.readLine());
				            
				            if(!autentificado){ //Error iniciando sesion
				            
				                System.out.println("¡FALLO, VUELVE A INTENTARLO!");
				                
				            }else{ //Inicio de sesion correcto
				            
				                System.out.println("¡AUTENTIFICADO CON EXITO " + user + "!");
				            
				            }
				            
				        }while(autentificado != true); //Mientras no se identifique bien
				        
				        break;
				        
				    case 2: //Haber escogido registrarse
				        
				        System.out.println("#RESGISTRO#");
				        
				        //Inicialmente no esta registrado
				        registrado = false;
				        //Inicialmente no esta autentificado (Despues del registro tendra que iniciar sesion)
				        autentificado = false;  
				        
				        do{ //Mientras no haya hecho ambas
				        
				        	if(!registrado){ //Por si falla iniciando sesion solo, que no se registre de nuevo
						       
						        //Pide el usuario
						        System.out.print("1. Introduce el nuevo usuario: ");
						        user = teclado.next();
						        
						        //Pide la contraseña
						        System.out.print("2. Introduce la contraseña: ");
						        pass = teclado.next();
						        
							 //Las envia procesador para que las registre
						        outPrint.println(user);
						        outPrint.println(pass);
						        
						        //Recibe la respuesta que sera un booleano que confirme si esta bien o no
						        registrado = Boolean.parseBoolean(inReader.readLine());
				                
				                }
				                
				                if(!registrado){ //Si el usuario ya existia
				                    
				                    System.out.println("¡El nombre usuario " + user + " ya existe, pruebe otro¡");
				                    
				                }else{ //Si se registra bien
				                
				                    System.out.println("¡REGISTRADO CON EXITO " + user + "!");
				                    System.out.println("¡AHORA INICIE SESION!");
				                    System.out.println("-------------------------------------------------------");
				                    System.out.println("#AUTENTIFICACION#");
				                    
				                    //Pide el usuario
						    System.out.print("1. Introduce el usuario: ");
						    user = teclado.next();
						    
						    //Pide la contraseña
						    System.out.print("2. Introduce la contraseña: ");
						    pass = teclado.next();
						    
						    //Las envia procesador
						    outPrint.println(user);
						    outPrint.println(pass);
						    
						    //Recibe la respuesta que sera un booleano que confirme si esta bien o no
						    autentificado = Boolean.parseBoolean(inReader.readLine());
						    
						    if(!autentificado){ //Error iniciando sesion
						    
						        System.out.println("¡Fallo en la autentificacion!");
						        
						    }else{ //Inicio de sesion correcto
						    
						        System.out.println("¡Autentificado con exito " + user + "!");
				            	    }
				            	    
				                }
				                
				        }while(registrado == false || autentificado == false); //Se registra e inicia sesion para salir
				        
				        break;
				        
				}

				//Si no se ha alcanzado el maximo numero de jugadores se me permite jugar
				buferRecepcion = inReader.readLine();
				
				if(!buferRecepcion.equals("maximo")){
				
				    //Recibe el numero de jugador que es (0,1,2,3)
				    int numeroJugador = Integer.parseInt(buferRecepcion); 
				    
				    // Se suma 1 para que empiece por el jugador 1 y no el 0
				    System.out.println("¡Soy JUGADOR " + (numeroJugador+1) + "!");

				    //Espera hasta que esten todos los jugadores					
				    System.out.println("¡Esperando a los demas jugadores para jugar!");
				    buferEnvio = inReader.readLine(); //JUGADORES PREPARADOS
				    System.out.println(buferEnvio); //Imprimo que todos los jugadores estan listos

				    int jugadorTocaJugar;
				    boolean estado = false;
				    do{
				    
				        // Leo al jugador que le toca
				        jugadorTocaJugar = Integer.parseInt(inReader.readLine());
				        

					//Si es el turno empieza el juego
				        if(numeroJugador == jugadorTocaJugar ){
				           
				           // System.out.println("TURNO JUGADOR" + Jugador.values()[jugadorTocaJugar]);
				           System.out.println("-------------------------------------------------------");
				           System.out.println("TURNO DE " + user);
				           System.out.println("-------------------------------------------------------");
				           
				           System.out.println("ACIERTOS: " + acertados);
				           System.out.println("FALLOS: " + fallados);
				           System.out.println("PASADAS: " + (respondidas-(acertados+fallados)));
				           System.out.println("QUEDAN: " + (26-respondidas));
				           
			           	    buferEnvio = String.valueOf(respondidas);
					    outPrint.println(buferEnvio);
					    
					    buferEnvio = String.valueOf(acertados);
					    outPrint.println(buferEnvio);
					    
					    buferEnvio = String.valueOf(fallados);
					    outPrint.println(buferEnvio);
					    
					    if(respondidas<26){
					    
					    	    pregunta = inReader.readLine();
					   	    System.out.print(pregunta+"\n");
					    
					    	    System.out.print("\t-> : ");
						    buferEnvio = teclado.next();
						    respondidas++;
						    
						    //Envia la respuesta que lee por pantalla a donde la comprueban
						    outPrint.println(buferEnvio);
						    buferRecepcion = inReader.readLine();
						    
						    //Interpreta la respues si ha acertado o no
						    //Interpretacion 0 = acertado, 1 = fallado, -1 = pasapalabra
						    if(buferRecepcion.equals("0")){
						    
							acertados++;
							System.out.println("¡Has acertado la pregunta!");
							
						    }else if(buferRecepcion.equals("1")){
						    
							System.out.println("¡Has fallado la pregunta!");
							String respuesta = inReader.readLine();
							System.out.println("\tLa respuesta era: " + respuesta);
							fallados++;
						       
						    }else{
						    
							System.out.println("¡Has pasado de pregunta!");
							String respuesta = inReader.readLine();
							System.out.println("\tLa respuesta era: " + respuesta);
						       
						    }
					
						}else{
						
							outPrint.println(numeroJugador);
							outPrint.println(user);
							outPrint.println(acertados);
							outPrint.println(fallados);
							estado= Boolean.valueOf(inReader.readLine());
							
							if(estado){
							
							   System.out.println("-------------------------------------------------------");
							   System.out.println("-------------------------------------------------------");
							   System.out.println("EL JUEGO HA TERMINADO, MIRA EL SERVIDOR PARA SABER QUIEN HA GANADO");							    	
								
							}
							
						}
					   				    
					}else{
					
            				    // System.out.println("TURNO JUGADOR" + Jugador.values()[jugadorTocaJugar]);
				            System.out.println("-------------------------------------------------------");
				            System.out.println("TURNO DEL JUGADOR " + jugadorTocaJugar+", ESPERE SU TURNO");
				            System.out.println("-------------------------------------------------------");
					
				            do{
				            
				                try {
				                    Thread.sleep(1000);
				                } catch (Exception e) {}
				                
				                jugadorTocaJugar = Integer.parseInt(inReader.readLine());
				                
				            }while(numeroJugador != jugadorTocaJugar); //Cuando pase turno sale de aqui
				            
				        }
				        

				    }while(!estado);
				    
				    socketServicio.close();
				    teclado.close();
				    
				}else{
				
				    System.out.println("¡Lo siento, maximo numero de jugadores conectados alcanzados!");
				    teclado.close();
				    socketServicio.close();
				    
				}
                    
			} catch (UnknownHostException e) {
				System.err.println("Error: Nombre de host no encontrado.");
			} catch (IOException e) {
				System.err.println("Error de entrada/salida al abrir el socket.");
			}catch (NumberFormatException e){
				System.out.println("-------------------------------------------------------");
				System.out.println("-------------------------------------------------------");
				System.err.println("EL JUEGO HA TERMINADO, MIRA EL SERVIDOR PARA SABER QUIEN HA GANADO");
			}
			
		}
		
	}
	
}

//UTIL PARA LA PRUEBA------------------------------
			//byte []buferEnvio;
			//byte []buferRecepcion=new byte[256];
			//int bytesLeidos=0;
			//UTIL PARA LA PRUEBA------------------------------
			
			
			//ESTO ES LO QUE NOS DAN, PERO QUIERO PODER ELEGIR EL HOST Y EL PUERTO
			// Nombre del host donde se ejecuta el servidor:
			//String host="localhost";
			// Puerto en el que espera el servidor:
			//int port=8989;
//---------------------------------
		

//UTIL PARA LA PRUEBA------------------------------------
				/*
				// Si queremos enviar una cadena de caracteres por un OutputStream, hay que pasarla primero
				// a un array de bytes:
				buferEnvio="Al monte del volcán debes ir sin demora".getBytes();
				
				// Enviamos el array por el outputStream;			
				outputStream.write(buferEnvio);
				
				// Aunque le indiquemos a TCP que queremos enviar varios arrays de bytes, sólo
				// los enviará efectivamente cuando considere que tiene suficientes datos que enviar...
				// Podemos usar "flush()" para obligar a TCP a que no espere para hacer el envío:
				outputStream.flush(); 
				
				
				// Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que intentará
				// rellenar. El método "read(...)" devolverá el número de bytes leídos.
				bytesLeidos=inputStream.read(buferRecepcion);
				
				
				// MOstremos la cadena de caracteres recibidos:
				System.out.println("Recibido: ");
				for(int i=0;i<bytesLeidos;i++){
					System.out.print((char)buferRecepcion[i]);
				}
				
				// Una vez terminado el servicio, cerramos el socket (automáticamente se cierran
				// el inpuStream  y el outputStream)
				socketServicio.close();
//UTIL PARA LA PRUEBA------------------------------------*/
				
