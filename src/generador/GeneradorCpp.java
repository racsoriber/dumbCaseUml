/*
 * GeneradorCpp.java
 *
 * Created on 11 de julio de 2008, 23:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generador;

import java.util.Enumeration;
import uml.UMLClase;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class GeneradorCpp extends GeneradorCodigo{
    
    /** Creates a new instance of GeneradorCpp */
    public GeneradorCpp() {
        init();
    }
    
    public String generarCodigo() {
        StringBuilder buffer = new StringBuilder();
        Enumeration en;
        String key,temp;
        //las cabeceras
        buffer.append( "#ifndef " + clase.getNombre().toUpperCase() + "_H" + EOLN );
        buffer.append( "#define " + clase.getNombre().toUpperCase() + "_H" + EOLN);
        //las dependencias
        for (String elem : dependencias) 
            buffer.append( "<" + elem + ".h>" + EOLN );
        buffer.append( EOLN );        
        //nombre de clase
        buffer.append( "class " + clase.getNombre() + "{" + EOLN );
        //atributos y metodos privados
        buffer.append( "\tprivate:" + EOLN );
        en = clase.getAtributos().keys();
        while( en.hasMoreElements() ){
            key = (String) en.nextElement();
            buffer.append( "\t\t" + clase.getAtributos().get( key ) + " " + key.substring( 1 , key.length() ) + EOLN );
        }
        buffer.append( "\tpublic:" + EOLN );
        for (UMLClase.Metodo elem : clase.getOperaciones()) {         
                buffer.append( "\t\t" + elem.toString() + EOLN);
        }
        buffer.append( "};" + EOLN );
        return buffer.toString();
    }    
    public String getPath() {
        return clase.getNombre() + ".h";
    }
    
}
