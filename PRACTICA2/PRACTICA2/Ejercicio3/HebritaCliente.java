
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gonzalo
 */
public class HebritaCliente extends Thread{
    
    private Socket socketServicio;
    private String buferRecepcion;
    private String buferEnvio;
    private int nHebra;

    HebritaCliente(Socket socket, String envio, int nH) {
	socketServicio = socket;
        buferRecepcion = "";
        buferEnvio = envio;
        nHebra = nH;
    }

    @Override
    public void run() {		
		
        try{
        
            InputStream inputStream = socketServicio.getInputStream();
            OutputStream outputStream = socketServicio.getOutputStream();

            InputStreamReader inputStreamR = new InputStreamReader(inputStream);

            BufferedReader buffer_lectura = new BufferedReader(inputStreamR);
            PrintWriter printer = new PrintWriter(outputStream, true);


            // Enviamos el mensaje
            //////////////////////////////////////////////////////
            //System.out.println("\nEnviando datos...");
            printer.println(buferEnvio);
            //System.out.println("Datos enviados");
            //////////////////////////////////////////////////////

            // Recibimos
            //////////////////////////////////////////////////////
            //System.out.println("\nLeyendo datos...");
            buferRecepcion = buffer_lectura.readLine();
            //System.out.println("Recibido: ");
            System.out.println("Respuesta a cliente " + nHebra + ": " + buferRecepcion);
            /////////////////////////////////////////////////////

            // Una vez terminado el servicio, cerramos el socket
            //////////////////////////////////////////////////////
            socketServicio.close();
            
            //Thread.yield();
            
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }        
    }
}
