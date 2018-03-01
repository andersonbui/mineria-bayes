package utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author debian
 */
public class LeerArchivo {

    static File archivo = null;
    static FileReader fr = null;
    static BufferedReader br = null;

    public static void abrir(String nombreArchivo) {

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File(nombreArchivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    /**
     *
     * @param numeroLineas
     * @return
     */
    public static List<String> leer(int numeroLineas) {
        List<String> listaCadenas = new ArrayList();
        // Lectura del fichero
        String linea;
        try {
            while ((linea = br.readLine()) != null && numeroLineas > 0) {
                listaCadenas.add(linea);
                numeroLineas--;
            }
        } catch (IOException ex) {
            Logger.getLogger(LeerArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCadenas;
    }

    public static List<String> leer() {
        List<String> listaCadenas = new ArrayList();
        // Lectura del fichero
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                listaCadenas.add(linea);
            }
        } catch (IOException ex) {
            Logger.getLogger(LeerArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCadenas;
    }

    public static void terminar() {
        try {
            if (null != fr) {
                fr.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
