//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.Random;


public class ProcesadorYodafy {
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private DatagramSocket server;
        // Referencia a un paquete para recibir
        private DatagramPacket paqueteEntrada;
        // Referencia a un paquete para enviar
        private DatagramPacket paqueteSalida;
	// stream de lectura (por aquí se recibe lo que envía el cliente)
	private InputStream inputStream;
	// stream de escritura (por aquí se envía los datos al cliente)
	private OutputStream outputStream;
	
	// Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
	private Random random;
	
	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public ProcesadorYodafy(DatagramSocket socketServicio) {
		this.server=socketServicio;
		random=new Random();
	}
	
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){
		
		// Como máximo leeremos un bloque de 1024 bytes. Esto se puede modificar.
		byte [] datosRecibidos=new byte[1024];
		int bytesRecibidos=0;
		
		// Array de bytes para enviar la respuesta. Podemos reservar memoria cuando vayamos a enviarka:
		byte [] datosEnviar;
		
		
		try {
			// Obtiene los flujos de escritura/lectura
			paqueteEntrada = new DatagramPacket(datosRecibidos, datosRecibidos.length);
			server.receive(paqueteEntrada);
                        
			// Lee la frase a Yodaficar:
			String entrada = new String(datosRecibidos, 0, paqueteEntrada.getLength());
			
			// Yoda hace su magia:
                        String salida = yodaDo(entrada);
                        
			datosEnviar = salida.getBytes();
                        
                        // Enviamos la traducción de Yoda:
                        paqueteSalida = new DatagramPacket(datosEnviar, datosEnviar.length,
                                                            paqueteEntrada.getAddress(), 
                                                            paqueteEntrada.getPort());
                        
                        server.send(paqueteSalida);	
			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujso de entrada/salida.");
		}

	}

	// Yoda interpreta una frase y la devuelve en su "dialecto":
	private String yodaDo(String peticion) {
		// Desordenamos las palabras:
		String[] s = peticion.split(" ");
		String resultado="";
		
		for(int i=0;i<s.length;i++){
			int j=random.nextInt(s.length);
			int k=random.nextInt(s.length);
			String tmp=s[j];
			
			s[j]=s[k];
			s[k]=tmp;
		}
		
		resultado=s[0];
		for(int i=1;i<s.length;i++){
		  resultado+=" "+s[i];
		}
		
		return resultado;
	}
}