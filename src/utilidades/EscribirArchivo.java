
package utilidades;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author debian
 */
public class EscribirArchivo {

    private static FileWriter fw;
    private static PrintWriter pw;

    public static void abrir(String nombreArchivo) {
        try {
            fw = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fw);
        } catch (IOException ex) {
            Logger.getLogger(EscribirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void escribir(String cadena) {

        pw.println(cadena);
    }
    
   public static void escribir(List lista) {
       lista.forEach((object) -> {
           escribir(object.toString());
        });
    }
    public static void terminar() {
        if (pw != null) {
            pw.close();
        }
    }
}
