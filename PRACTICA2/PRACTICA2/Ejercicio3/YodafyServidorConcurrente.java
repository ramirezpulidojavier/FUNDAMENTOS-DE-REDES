
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorConcurrente
// (CC) jjramos, 2012
//

public class YodafyServidorConcurrente {

	public static void main(String[] args) {
	
		// Puerto de escucha
		final int PORT=8989;
		
		
		try {
			
			//////////////////////////////////////////////////
                        ServerSocket serverSocket = new ServerSocket(PORT);
			//////////////////////////////////////////////////
			
			// Mientras ... siempre!
			while(true){
                            
                            try {
                                HebritaServer h = new HebritaServer(serverSocket.accept());
                                h.start();
                            
                            } catch (IOException e) {
                                System.out.println("Error: no se pudo aceptar la conexión solicitada.");
                            }
                            
			}
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto " + PORT);
		}
	}
}
