
package almacenamiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author lucero
 */
public class Almacenamiento {

    /**
     * guarda el objeto de manera persistente en un archivo
     *
     * @param objeto objeto a almacenar
     * @param nombre_archivo
     * @return
     */
    public static boolean guardarObjeto(Object objeto, String nombre_archivo) {
        FileOutputStream fos;
        try {
            File fileUsuario = new File(nombre_archivo);

            if (!fileUsuario.exists()) {
                fos = new FileOutputStream(fileUsuario, true);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeUnshared(objeto);
                fos.close();
                oos.close();

                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * busca un objeto de tipo {@link Object} a partir de su nombre completo.
     *
     * @param archivo nombre de archivo buscado
     * @return
     */
    public static Object obtenerObjeto(String archivo) {
        FileInputStream fis = null;
        ObjectInputStream ois;
        Object objeto;
        File file;

        try {
            file = new File(archivo);
            if (file.exists()) {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);

                // recuperando el objeto guardados
                objeto = ois.readObject();
                // cerrar fichero
                fis.close();
                ois.close();

                return objeto;
            }
        } catch (IOException e) {
            try {
                fis.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Elimina un archivo.
     *
     * @param nombreArchivo nombre completodelarchivo a eliminar
     * @return el objeto eliminado, de lo contrario null.
     */
    public static Object eliminarObjeto(String nombreArchivo) {
        Object obj = obtenerObjeto(nombreArchivo);

        // comprobar si existe el archivo de lista de logins
        if (obj != null) {
            File file = new File(nombreArchivo);
            // eliminar archivo
            file.delete();
            return obj;
        }
        return null;
    }
}
