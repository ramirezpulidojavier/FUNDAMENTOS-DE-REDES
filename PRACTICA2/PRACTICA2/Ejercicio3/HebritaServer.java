
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gonzalo
 */
public class HebritaServer extends Thread{
    private Socket cliente;

    HebritaServer(Socket c) {
	cliente = c;
    }
    
    @Override
    public void run() {
	ProcesadorYodafy procesador=new ProcesadorYodafy(cliente);
	procesador.procesa();
    }
}
