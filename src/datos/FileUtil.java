package datos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Clase que manipula la lectura y persistencia de datos en archivos
 */
public class FileUtil {

/*    public static String abrirTexto(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder bufferTexto = new StringBuilder();
            String linea = reader.readLine();
            while (linea != null) {
                bufferTexto.append(linea);
                bufferTexto.append(System.getProperty("line.separator"));
                linea = reader.readLine();
            }
            reader.close();
            return bufferTexto.toString();
        } catch (Exception e) {
            crearTexto(path);
            return abrirTexto(path);
        }
    }
*/
    /**
     * Almacena una secuencia de caracteres a un archivo de texto plano
     * @param path Direccion del fichero
     * @param contenido Contenido del fichero
     */
    public static void guardarTexto(String path, String contenido) {
        try {
            File f = new File( path );
            if( !f.exists() )
                f.createNewFile();
            f = null;
            FileWriter file = new FileWriter( path );
            BufferedWriter Br = new BufferedWriter( file );
            Br.write( contenido );
            Br.flush();
            Br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
/*
    public static void crearTexto(String path) {
        try {
            FileWriter file = new FileWriter(path);
            BufferedWriter Br = new BufferedWriter(file);
            Br.flush();
            Br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	*/
}
