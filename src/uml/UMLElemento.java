package uml;
/*
 * UMLElemento.java
 *
 * Created on 16 de junio de 2008, 10:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;


/**
 *
 * @author Hugo
 */
public abstract class UMLElemento implements Cloneable,Serializable{
    public static final byte PRIVATE        =   0;
    public static final byte PUBLIC         =   1;
    public static final byte PROTECTED      =   2;
    public static final byte ABSTRACT       =   3;
    
    protected String nombre;
    protected String estereotipo;
    protected byte visibilidad;
    protected boolean marcado;
    protected Point posicion;
    
    /** Creates a new instance of UMLElemento */
    public UMLElemento() {
        this.nombre = "";
        this.posicion = new Point();
        this.visibilidad = PUBLIC;
        this.marcado = false;
    }
    
    public abstract Rectangle getArea();
    public abstract void pintar( Graphics g );
    public abstract boolean isContenido( Point p );
    public abstract boolean isRelacion( );
    
    public String getEstereotipo() {
        return estereotipo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public Point getPosicion() {
        return posicion;
    }
    
    public void setEstereotipo(String estereotipo) {
        this.estereotipo = estereotipo;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }
    
    public void setPosicion(int x , int y) {
        this.posicion.x = x;
        this.posicion.y = y;
    }

    public byte getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(byte visibilidad) {
        this.visibilidad = visibilidad;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public boolean isMarcado() {
        return marcado;
    }    

    /*public static void main(String[] args) {
        Object obj = new UMLClase( "alog" , (byte)0 );
        Object dos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( obj );
            oos.flush();
            byte[] data = baos.toByteArray();
            oos.close();
            baos.close();
            
            ByteArrayInputStream bais = new ByteArrayInputStream( data );
            ObjectInputStream ois = new ObjectInputStream( bais );
            dos = ois.readObject();
            ois.close();
            bais.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println( dos.getClass() );
    }*/
}
