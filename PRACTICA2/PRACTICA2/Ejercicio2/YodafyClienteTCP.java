//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class YodafyClienteTCP {

	public static void main(String[] args) {
		
		String buferEnvio;
		String buferRecepcion;
		int bytesLeidos=0;
		
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		final int PORT=8989;
		
		// Socket para la conexión TCP
		Socket socketServicio=null;
		
		try {
			// Creamos un socket que se conecte a "host" y "port":
			//////////////////////////////////////////////////////
                       System.out.println("Abriendo puerto...");		
			socketServicio = new Socket (host,PORT);
			System.out.println("Puerto listo");
                        //////////////////////////////////////////////////////			
			
			InputStream inputStream = socketServicio.getInputStream();
			OutputStream outputStream = socketServicio.getOutputStream();
                        
                       InputStreamReader inputStreamR = new InputStreamReader(inputStream);
                        
                       BufferedReader buffer_lectura = new BufferedReader(inputStreamR);
			PrintWriter printer = new PrintWriter(outputStream, true);
                        
			
			buferEnvio = "Al monte del volcan debes ir sin demora";
			
			// Enviamos el mensaje
			//////////////////////////////////////////////////////
                       System.out.println("\nEnviando datos...");
			printer.println(buferEnvio);
                       System.out.println("Datos enviados");
			//////////////////////////////////////////////////////
			
			
			//////////////////////////////////////////////////////
                       System.out.println("\nLeyendo datos...");
			buferRecepcion = buffer_lectura.readLine();
                       System.out.println("Recibido: ");
                       System.out.println(buferRecepcion);
			/////////////////////////////////////////////////////
			
			// Una vez terminado el servicio, cerramos el socket
			//////////////////////////////////////////////////////
			socketServicio.close();
			System.out.println("\n\nConexión cerrada");

			//////////////////////////////////////////////////////
			
			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
