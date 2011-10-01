/*
 * UMLComposite.java
 *
 * Created on 16 de junio de 2008, 12:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uml;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Hugo
 */
public class UMLComposite extends UMLElemento implements Cloneable{
    private ArrayList< UMLElemento > elementos;
    
    /** Creates a new instance of UMLComposite */
    public UMLComposite() {
        super();
        elementos = new ArrayList< UMLElemento >();
    }

    /**
     * 
     * @return 
     */
    public ArrayList<UMLElemento> getElementos() {
        return elementos;
    }
    
    /**
     * 
     * @param ele 
     * @return 
     */
    public boolean addElemento( UMLElemento ele ){
        if( ele != null && getElementoByName( ele.getNombre() ) == null){
            elementos.add( ele );
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @param ele 
     */
    public void removeElemento( UMLElemento ele ){
        elementos.remove( ele );
    }
    
    /**
     * 
     * @param index 
     */
    public void removeElemento( int index ){
        elementos.remove( index );
    }
    
    /**
     * 
     * @param p 
     * @return 
     */
    public UMLElemento getElementoEn( Point p ){
        for (UMLElemento elem : elementos ) {
            if( elem.isContenido( p ) )
                return elem;
        }
        return null;
    }
    
    /**
     * 
     * @param nombre 
     * @return 
     */
    public UMLElemento getElementoByName( String nombre ){
        for (UMLElemento elem : elementos ) {
            if( elem.getNombre().equals( nombre ) )
                return elem;
        }
        return null;
    }
    
    /**
     * 
     * @param g 
     */
    @Override
    public void pintar(Graphics g) {
        for (UMLElemento elem :elementos ){
            if( !elem.isRelacion() )
                elem.pintar( g );
        }
        for (UMLElemento elem :elementos ){
            if( elem.isRelacion() )
                elem.pintar( g );
        }
    }
    
    /**
     * 
     * @param p 
     * @return 
     */
    public boolean isContenido(Point p) {
        return false;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isRelacion() {
        return false;
    }
    
    /**
     * 
     * @return 
     */
    public Rectangle getArea() {
        return null;
    }
    
    /**
     * 
     * @throws java.lang.CloneNotSupportedException 
     * @return 
     */
    protected Object clone() throws CloneNotSupportedException {
        /*UMLComposite temp = new UMLComposite();
        temp.elementos = (ArrayList<UMLElemento>) this.elementos.clone();
        //return temp;
         **/
        Object temp = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( this );
            oos.flush();
            byte[] data = baos.toByteArray();
            oos.close();
            baos.close();
            
            ByteArrayInputStream bais = new ByteArrayInputStream( data );
            ObjectInputStream ois = new ObjectInputStream( bais );
            temp = ois.readObject();
            ois.close();
            bais.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }
    
    /**
     * 
     * @return 
     */
    public int length() {
        return this.elementos.size();
    }
    
    
    
}
