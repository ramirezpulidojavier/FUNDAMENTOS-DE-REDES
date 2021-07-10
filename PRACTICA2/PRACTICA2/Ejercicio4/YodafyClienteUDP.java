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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class YodafyClienteUDP {

	public static void main(String[] args) {
		
		byte []buferEnvio;
		byte []buferRecepcion=new byte[256];
		int bytesLeidos=0;
		
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		final int PORT = 8989;
		
		DatagramSocket cliente;
                String mensajeRecibido;
		
		try {
                        // Establecer conexión
			InetAddress direccion = InetAddress.getByName(host);
                        cliente = new DatagramSocket();
			
			buferEnvio="Al monte del volcán debes ir sin demora".getBytes();
			
			// Enviamos el array
                        DatagramPacket paqueteSalida = new DatagramPacket(buferEnvio, buferEnvio.length, direccion, PORT);
                        cliente.send(paqueteSalida);
			
			// Leemos la respuesta del servidor
                        DatagramPacket paquete = new DatagramPacket(buferRecepcion, buferRecepcion.length);
			cliente.receive(paquete);
			
                        mensajeRecibido = new String(paquete.getData(), 0, paquete.getLength());
                        System.out.println("Respuesta Yoda: " + mensajeRecibido);
			
			
			// Una vez terminado el servicio, cerramos el socket
                        cliente.close();
			
			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}