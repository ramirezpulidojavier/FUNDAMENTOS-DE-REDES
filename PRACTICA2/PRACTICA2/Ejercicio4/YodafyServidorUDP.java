import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.DatagramSocket;
//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorUDP {

	public static void main(String[] args) {
               
		// Puerto de escucha
		final int PORT = 8989;
		
		try {
                    
                    DatagramSocket servidor = new DatagramSocket(PORT);
                    
                    while(true){

                            // Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
                            // argumento el nuevo socket, para que realice el procesamiento
                            ProcesadorYodafy procesador = new ProcesadorYodafy(servidor);
                            procesador.procesa();

                    }
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+PORT);
		}

	}

}