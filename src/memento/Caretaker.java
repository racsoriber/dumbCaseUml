/*
 * Caretaker.java
 *
 * Created on 30 de junio de 2008, 09:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package memento;

import java.util.LinkedList;
import uml.UMLComposite;

/**
 * Clase que manipula los estados del modelo
 * @author Juan TImoteo Ponce Ortiz
 */
public class Caretaker {
    /**
     * indice actual de estados
     */
    private int index;
    /**
     * lista de estados
     */
    private LinkedList< UMLComposite > estados;
    
    /** Creates a new instance of Caretaker */
    public Caretaker() {
        estados = new LinkedList< UMLComposite >();
        index = -1;
    }
    
    /**
     * limpiar estados previos
     */
    public void clean(){
        index = -1;
        estados.clear();
        System.gc();
    }
    
    /**
     * Agregar una estado
     * @param estado st
     */
    public void addMemento( final UMLComposite estado ){
        System.out.println("admemento");
        index ++;
        if( index == estados.size() )
            estados.addLast( estado );
        else
            estados.add( index , estado );
    }
    
    /**
     * Verifica si existe un estado previo
     * @return bool
     */
    public boolean hasPrevious(){
        return ( index > 0);
    }
    
    /**
     * Verifica si existe un estado posterior
     * @return bool
     */
    public boolean hasMext(){
        return ( !estados.isEmpty() && index + 1 < estados.size() );
    }
    
    /**
     * retorna al estado inmediato inferior
     * @return estado
     */
    public UMLComposite undo(){
        if( !estados.isEmpty() && index > 0){
            index--;
            return estados.get( index );
        }
        System.out.println("no undo");
        return null;
    }
    
    /**
     * retorna al estado inmediato inferior
     * @return estado
     */
    public UMLComposite redo(){
        if( index + 1 < estados.size() ){
            index++;
            return estados.get( index );
        }
        System.out.println("no redo");
        return null;
    }
    
}
