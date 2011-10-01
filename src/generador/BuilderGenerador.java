/*
 * BuilderJava.java
 *
 * Created on 11 de julio de 2008, 22:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generador;

import java.util.ArrayList;
import uml.UMLClase;
import uml.UMLElemento;
import uml.UMLRelacion;

/**
 * Clase que inicializa y manipula un generador de código 
 * de manera que este procese un modelo
 * @author Juan Timoteo Ponce Ortiz
 */
public class BuilderGenerador{
    /**
     * Generador de codigo fuente
     */
    private GeneradorCodigo generador;
    
    /**
     * Creates a new instance of BuilderJava
     * @param generador Generador a construir
     */
    public BuilderGenerador( GeneradorCodigo generador ) {
        this.generador = generador;
    }
    
    /**
     * Retorna el generador de código establecido
     * @return GeneradorCodigo
     */
    public GeneradorCodigo getGenerador() {
        return generador;
    }
    
    /**
     * Asigna el nombre de clase a procesar, esto se hace para generar
     * codigo para diferentes fichero
     * @param clase Clase a generar
     */
    public void setClase(UMLClase clase) {
        generador.init();
        generador.setClase( clase );
    }
    
    /**
     * Proceso que identifica y construye el generador de código segun 
     * lo indique el modelo
     * @param relaciones Relaciones del modelo
     */
    public void procesarRelaciones(ArrayList<UMLElemento> relaciones) {
        UMLRelacion cursor;
        for ( UMLElemento elem : relaciones ) {
            if( elem.isRelacion() ){
                cursor = (UMLRelacion) elem;
                if( cursor.getOrigen().equals( generador.getClase() ) ){
                    switch( cursor.getTipo() ){
                        case UMLRelacion.ASOCIACION:
                            //no hacemos nada
                            break;
                        case UMLRelacion.ASOCIACION_AGREGACION:
                            generador.addDependencia( (UMLClase) cursor.getDestino() );
                            generador.getClase().addAtributo( UMLClase.PRIVATE ,
                                    cursor.getNombre() ,
                                    cursor.getDestino().getNombre() );
                            break;
                        case UMLRelacion.ASOCIACION_COMPOSICION:
                            generador.addDependencia( (UMLClase) cursor.getDestino() );
                            generador.getClase().addAtributo( UMLClase.PRIVATE ,
                                    cursor.getNombre() ,
                                    cursor.getDestino().getNombre() );
                            break;
                        case UMLRelacion.DEPENDENCIA:
                            generador.addDependencia( (UMLClase) cursor.getDestino() );
                            break;
                        case UMLRelacion.GENERALIZACION:
                            generador.addDependencia( (UMLClase) cursor.getDestino() );
                            generador.setPadre( (UMLClase) cursor.getDestino() );
                            break;
                        case UMLRelacion.IMPLEMENTACION:
                            generador.addDependencia( (UMLClase) cursor.getDestino() );
                            generador.addRealizacion( (UMLClase) cursor.getDestino() );
                            break;
                    }
                }
            }
        }
        
    }
    
}
