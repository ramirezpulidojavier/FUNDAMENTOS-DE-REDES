//
// GONZALO DE LA TORRE MARTINEZ
// JAVIER RAMIREZ PULIDO
//

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class YodafyServidorIterativo {

	public static void main(String[] args) {
	
		//El puerto se pueda meter por terminal, por lo que debe haber al menos un argumento
		if(args.length != 1){
		
			//Mensaje de error y de cómo ejecutar
			System.out.println("Error al pasar los argumentos. Forma de ejecutar: java YodafyServidorIterativo [nº_puerto]"); 
			
		}else{ // Numero de argumentos correcto
			
			//Asigno el numero de puerto. Si quiero alguno fijo puedo poner aqui (por ejemplo port = 8989)		
			int port = Integer.parseInt(args[0]); 
			
			//Creamos un socket para que escuche por el puerto "port"
			ServerSocket serverSocket; 
			
			//Creamos un juego que es una partida nueva
			Juego juego = Juego.getInstanceJuego(); 
			
			//Indicara que el juego ha terminado
			boolean terminado = false; 
			
			
			
			try {
				
				// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
				serverSocket=new ServerSocket(port);
				System.out.println("-------------------------------------------------------");
				System.out.println("Servidor iniciado escuchando en el puerto: " + port);
		        	System.out.println("Esperando conexion de los jugadores...");
		        	System.out.println("-------------------------------------------------------");
				
				//Creamos un socket
				Socket socketServicio = null;
				
				do {// Mientras el juego no termine
					
					// Aceptamos una nueva conexión con accept()
					try {
					
		                		socketServicio = serverSocket.accept(); //Espera hasta aceptar conexion
		            		
		            		} catch (IOException e) {
		                		
		                		System.out.println("ERROR: no se pudo aceptar la conexion solicitada.");
		            		
		            		}
		            		
					// Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
					// argumento el nuevo socket, para que realice el procesamiento y el juego para que lo gestione
					// Este esquema permite que se puedan usar hebras más fácilmente.
					ProcesadorYodafy procesador=new ProcesadorYodafy(socketServicio , juego);
					
					//activar las hebras
					procesador.start(); 
					
				} while (terminado != true); //hasta que el juego termine
			
			} catch (IOException e) {
			
				System.err.println("Error al escuchar en el puerto "+port);
				
			}

		}

	}
	
}
//UTIL PARA EL EJEMPLO SOLO-----------------------
			// array de bytes auxiliar para recibir o enviar datos.
			//byte []buffer=new byte[256];
			// Número de bytes leídos
			//int bytesLeidos=0;
		//UTIL PARA EL EJEMPLO SOLO-----------------------
