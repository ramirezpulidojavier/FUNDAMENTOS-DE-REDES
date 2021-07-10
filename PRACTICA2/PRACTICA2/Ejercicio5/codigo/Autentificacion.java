//
// GONZALO DE LA TORRE MARTINEZ
// JAVIER RAMIREZ PULIDO
//
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;

/**
 * @brief Clase Autentificación encargada de administrar la autenticación de los usuarios.
 */
 class Autentificacion {
 
    // Estructura Map donde se guardan los datos de los usuarios, <Nombre><Contraseña>
    Map<String, String> usuariosRegistrados; 
    // Fichero de donde se va a leer los datos
    File fichero_leer; 
    // Fichero donde se va a escribir usuarios a registrar
    FileWriter fichero_escribir; 
    //Entrada por teclado
    Scanner s; 

    /**
     * @brief Constructor por defecto, lee el fichero ./usuarios.txt e introduce la información en el HashMap
     */
    public Autentificacion(){
        this.usuariosRegistrados = new HashMap<String, String>();
        this.fichero_leer = new File("./data/usuarios.txt");
        this.fichero_escribir = null;
        this.s = null;
        leerFichero();
    }

    /**
     * @brief Metodo el cual lee un fichero con la estructura usuario (\n) contraseña y la introduce en el hash.
     */
    private void leerFichero(){
        try {
            // Leemos el contenido del fichero
			s = new Scanner(fichero_leer);

            // Leemos linea a linea el fichero
			while (s.hasNextLine()) {
                String user = s.nextLine(); 	// Guardamos la linea en un String
                String pass = s.nextLine();
                //System.out.println(linea);      // Imprimimos la linea
                usuariosRegistrados.put(user, pass);
            }
        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
			try {
                if (s != null)
                    s.close();
			} catch (Exception ex2) {
				System.out.println("Mensaje 2: " + ex2.getMessage());
			}
        }
    }

    /**
     * @brief Metodo el cual escribe en el fichero ./usuarios.txt un nuevo usuario con su contraseña
     * @param user Nombre de usuario a guardar
     * @param pass Contraseña del usuario a guardar
     */
    private void escribirFichero(String user, String pass){
        try {
			fichero_escribir = new FileWriter("./data/usuarios.txt",true);

            // Escribimos linea a linea en el fichero
            fichero_escribir.write(user + "\n");
            fichero_escribir.write(pass + "\n");

			fichero_escribir.close();

		} catch (Exception ex) {
			System.out.println("Mensaje de la excepción: " + ex.getMessage());
		}
    }

    /**
     * @brief Imprime todos los usuarios y sus claves
     */
    public void Imprimir(){
        Iterator<String> it = usuariosRegistrados.keySet().iterator();
        while(it.hasNext()){
        Object key = it.next();
        System.out.println("Usuario: " + key + " -> Password: " + usuariosRegistrados.get(key));
        }
    }
    
    /**
     * @brief Metodo el cual comprueba el inicio de sesión
     * @param user Nombre usuario a comprobar
     * @param pass Contraseña del usuario a comprobar
     * @return Devuelve true si el usuario existe y la clave es correcta, false en caso contrario.
     */
    public boolean iniciarSesion(String user, String pass){
        if(usuariosRegistrados.containsKey(user)){
            String truePass = usuariosRegistrados.get(user);    
            return truePass.equals(pass);
        }else{
            return false;
        }
    }

    /**
     * @brief Metodo el cual registra un nuevo usuario, y carga de nuevo el fichero en el
     * @param user Nombre de usuario a registrar
     * @param pass Contraseña del usuario a registrar
     * @return Devuelve true si se ha podido registrar, devuelve false en caso contrario, por si existe otro usuario con el mimso nombre.
     */
    public boolean registrarUsuario(String user, String pass){
        
        if(usuariosRegistrados.containsKey(user)){
            System.out.println("¡ERROR: El nombre de usuario ya existe!");
            return false;
        }else{
            //usuariosRegistrados.put(user, pass);
            this.usuariosRegistrados = null;
            this.usuariosRegistrados = new HashMap<String, String>();
            
            
            escribirFichero(user, pass);
            leerFichero();
            return true;
        }
    }
    
    /**
     * @brief Pequeña prueba de la clase Autentificacion
     * @param args
     */
    public static void main(String[] args) {
        
    }


 }
