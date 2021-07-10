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
import java.util.Scanner;

public class YodafyClienteTCP {

	public static void main(String[] args) throws IOException {
		
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		final int PORT=8989;
                
                final String mensaje = "Al monte del volcan debes ir sin demora";
		
		// Socket para la conexión TCP
		Socket cliente;
                
                System.out.println("\n¿Cuántos clientes quieres generar? \n(introduce un numero entero): ");
                Scanner in = new Scanner(System.in);
                int nClientes = in.nextInt();
		
                try{
                    int nH = 1; // saber que cliente es al23 que se le responde
                    for(int i=0; i<nClientes; i++){
                        
                        //Iniciamos una hebra dado un socket y el mensaje a enviar al servidor
                        (new HebritaCliente(new Socket(host, PORT), mensaje, nH)).start();
                        /*HebritaCliente hebra = new HebritaCliente(cliente, "Al monte del volcan debes ir sin demora");
                        hebra.start();*/
                        nH++;
                    }
                    
                } catch (UnknownHostException e) {
                    System.err.println("Error: Nombre de host no encontrado.");
                } catch (IOException e) {
                    System.err.println("Error de entrada/salida al abrir el socket.");
                }
                
	}
}