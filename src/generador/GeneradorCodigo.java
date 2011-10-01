/*
 * GeneradorCodigo.java
 *
 * Created on 8 de julio de 2008, 23:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generador;

import java.util.ArrayList;
import uml.UMLClase;

/**
 * Clase base paralos generadores de codigo fuente(patron template)
 * @author Juan Timoteo Ponce Ortiz
 */
public abstract class GeneradorCodigo {
    /**
     * 
     */
    protected boolean crearAccesos;
    protected UMLClase clase;
    protected String padre;
    protected ArrayList<String> realizaciones;
    protected ArrayList<String> dependencias;
    
    public static final String EOLN = System.getProperty( "line.separator" );
    
    /**
     * Inicializa las variables y datos preliminares de la clase
     */
    public void init() {
        this.crearAccesos = false;
        this.clase = null;
        this.padre = "";
        if( this.realizaciones == null )
            this.realizaciones = new ArrayList<String>();
        else
            this.realizaciones.clear();
        if( this.dependencias == null )
            this.dependencias = new ArrayList<String>();
        else
            this.dependencias.clear();
    }
    
    
    /**
     * Asigna la clase a procesar
     * @param clase clase
     */
    public void setClase(UMLClase clase) {
        this.clase = clase;
    }
    
    /**
     * Retorna la clase en proceso
     * @return clase
     */
    public UMLClase getClase() {
        return this.clase;
    }
    
    /**
     * Asigna el indicador del padre de clase
     * @param clase clase
     */
    public void setPadre(UMLClase clase) {
        this.padre = clase.getNombre();
    }
    
    /**
     * Agrega una condicion de implementacion de una interfaz
     * @param clase interfaz
     */
    public void addRealizacion(UMLClase clase) {
        this.realizaciones.add( clase.getNombre() );
    }
    
    /**
     * Eliminar la implementacion de una interfaz
     * @param clase interfaz
     */
    public void removeRealizacion(UMLClase clase) {
        this.realizaciones.remove( clase.getClass().getName() );
    }
    
    /**
     * Agrega una dependencia de clase
     * @param clase clase
     */
    public void addDependencia(UMLClase clase) {
        this.dependencias.add( clase.getNombre() );
    }
    
    /**
     * Elimina una dependencia de clase
     * @param clase clase
     */
    public void removeDependencia(UMLClase clase) {
        this.dependencias.remove( clase.getClass().getName() );
    }
    
    /**
     * Asigna la bandera para crear selectores y modificadores(accesos)
     * de una clase
     * @param value boolean
     */
    public void crearAccesos(boolean value) {
        this.crearAccesos = value;
    }
    
    /**
     * Proceso que genera el codigo de una clase en una secuencua
     * de caracteres
     * @return codigo fuente
     */
    public abstract String generarCodigo();
    /**
     * Retorna la direccion de almacenaje
     * @return path
     */
    public abstract String getPath();
}
