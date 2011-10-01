/*
 * UMLClase.java
 *
 * Created on 16 de junio de 2008, 10:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uml;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Hugo
 */
public class UMLClase extends UMLElemento{
    public static final byte CLASE      =   0;
    public static final byte INTERFAZ   =   1;
    public static final byte ABSTRACTA  =   2;
    
    protected Rectangle location;
    private byte tipo;
    
    private Hashtable< String , String > atributos;
    private ArrayList< Metodo > operaciones;
    
    /** Creates a new instance of UMLClase */
    public UMLClase( String nombre , byte tipo ) {
        super();
        setNombre( nombre );
        this.tipo = tipo;
        init();
    }
    
    public void init(){
        location = new Rectangle( posicion.x , posicion.y , nombre.length() * 10 , 20 * 3 );
        atributos = new Hashtable< String , String >();
        operaciones = new ArrayList< Metodo >();
    }
    
    /**
     * 
     * @return 
     */
    public Hashtable<String, String> getAtributos() {
        return atributos;
    }
    
    
    /**
     * 
     * @param acceso 
     * @param nombre 
     * @param tipo 
     */
    public void addAtributo( byte acceso , String nombre , String tipo ){
        if( !existeAtributo( nombre ) ){
            atributos.put( acceso + nombre , tipo );
            if( ( tipo.length() + nombre.length() + 1 ) * 10 > location.width  )
                location.width = ( tipo.length() + nombre.length() + 1 ) * 10;
        }
    }
    
    /**
     * 
     * @param nombre 
     */
    public void removeAtributo( String nombre ){
        atributos.remove( PRIVATE + nombre );
        atributos.remove( PUBLIC + nombre );
        atributos.remove( PROTECTED + nombre );
    }
    
    /**
     * 
     * @param acceso 
     * @param nombre 
     * @param retorno 
     */
    public void addOperacion( byte acceso, String nombre , String retorno ){
        if( retorno.trim().length() < 2 )
            retorno = "";        
        Metodo metodo = new Metodo( acceso,  nombre , retorno );
        operaciones.add( metodo );        
    }
    
    /**
     * 
     * @param nombre 
     */
    public void removeOperacion( String nombre ){
        operaciones.remove( getOperacion( nombre ) );
    }
    
    /**
     * 
     * @param nombre 
     * @return 
     */
    public Metodo getOperacion( String nombre ){
        for (Metodo elem : operaciones) {
            if( elem.getNombre().equals( nombre ) )
                return elem;
        }
        return null;
    }
    
    /**
     * 
     * @return 
     */
    public ArrayList<UMLClase.Metodo> getOperaciones() {
        return operaciones;
    }
    
    /**
     * 
     * @param atributos 
     */
    public void setAtributos(Hashtable<String, String> atributos) {
        this.atributos = atributos;
    }
    
    /**
     * 
     * @param nombre 
     * @return 
     */
    public boolean existeAtributo(String nombre){
        if( atributos.get( PRIVATE + nombre ) != null )
            return true;
        if( atributos.get( PUBLIC + nombre ) != null )
            return true;
        if( atributos.get( PROTECTED + nombre ) != null )
            return true;
        return false;
    }
    
    
    /**
     * 
     * @param x 
     * @param y 
     */
    @Override
    public void setPosicion(int x, int y) {
        location.setLocation( x , y );
    }
    
    /**
     * 
     * @param g 
     */
    @Override
    public void pintar(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color fondo = Color.ORANGE;
        //nombre
        if( isMarcado() )
            fondo = Color.LIGHT_GRAY ;
        g2d.setColor( fondo );
        g2d.fillRect( location.x , location.y , location.width , 40 );
        
        g2d.setColor( Color.RED );
        g2d.drawRect( location.x , location.y , location.width , 40 );
        g2d.drawString( "<<" + estereotipo + ">>" , location.x + 5 , location.y + 15 );
        g2d.drawString( nombre , location.x + 5 , location.y + 30 );
        
        //atributos
        g2d.setColor( fondo );
        g2d.fillRect( location.x , location.y + 40 , location.width , atributos.size() * 20 );
        g2d.setColor( Color.RED );
        g2d.drawRect( location.x , location.y + 40 , location.width , atributos.size() * 20 );
        
        Enumeration< String > en = atributos.keys();
        String key;
        int y = location.y + 40 ;
        while( en.hasMoreElements() ){
            key = en.nextElement();
            g2d.drawString( getCharAcceso( Byte.parseByte( key.charAt( 0 )+"" ) ) +
                    key.substring( 1 , key.length() ) +
                    ':' + atributos.get( key ) , location.x + 5 , y + 15 );
            y += 20;
        }
        
        //metodos
        g2d.setColor( fondo );
        g2d.fillRect( location.x , y , location.width , operaciones.size() * 20 );
        g2d.setColor( Color.RED );
        g2d.drawRect( location.x , y , location.width , operaciones.size() * 20 );
        
        for (Metodo elem : operaciones) {
            g2d.drawString( elem.toString() , location.x + 5 , y + 15 );
            y += 20;
        }
        location.height = y - 20;
        
    }
    
    /**
     * 
     * @param p 
     * @return 
     */
    @Override
    public boolean isContenido(Point p) {
        return location.contains( p );
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public boolean isRelacion() {
        return false;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public Rectangle getArea() {
        return this.location;
    }
    
    /**
     * 
     * @return 
     */
    public byte getTipo() {
        return tipo;
    }
    
    /**
     * 
     * @param tipo 
     */
    public void setTipo(byte tipo) {
        this.tipo = tipo;
    }
    
    /**
     * 
     * @param acceso 
     * @return 
     */
    public char getCharAcceso( byte acceso ){
        switch ( acceso ){
            case PRIVATE:
                return '-';
            case PROTECTED:
                return '#';
            case PUBLIC:
                return '+';
        }
        return '+';
    }
    
    
    public class Metodo implements Serializable{
        private byte acceso;
        //private byte comportamiento;
        private String nombre;
        private String retorno;
        private Hashtable< String , String > parametros;
        
        /**
         * 
         * @param acceso 
         * @param nombre 
         * @param retorno 
         */
        public Metodo( byte acceso, /*byte comportamiento ,*/ String nombre , String retorno){
            this.acceso = acceso;
            //this.comportamiento = comportamiento;
            this.nombre = nombre;
            this.retorno = retorno;
            this.parametros = new Hashtable< String , String >();
        }
        
        /**
         * 
         * @param nombre 
         * @param tipo 
         */
        public void addParametro( String nombre , String tipo ){
            this.parametros.put( nombre , tipo );            
        }
        
        /**
         * 
         * @param nombre 
         */
        public void removeParametro( String nombre ){
            this.parametros.remove( nombre );
        }
        
        /**
         * 
         * @return 
         */
        public Hashtable<String, String> getParametros() {
            return parametros;
        }
        
        /**
         * 
         * @return 
         */
        public byte getAcceso() {
            return acceso;
        }
        
        /**
         * 
         * @return 
         */
        public String getNombre() {
            return nombre;
        }
        
        /*public byte getComportamiento() {
            return comportamiento;
        }*/
        
        /**
         * 
         * @return 
         */
        public String getRetorno() {
            return retorno;
        }
        
        /**
         * 
         * @param acceso 
         */
        public void setAcceso(byte acceso) {
            this.acceso = acceso;
        }
        
        /**
         * 
         * @param nombre 
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        
        /*public void setComportamiento(byte comportamiento) {
            this.comportamiento = comportamiento;
        }*/
        
        /**
         * 
         * @param retorno 
         */
        public void setRetorno(String retorno) {
            this.retorno = retorno;
        }
        
        /**
         * 
         * @return 
         */
        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();
            buffer.append( getCharAcceso( acceso ) );
            buffer.append( ' ' + nombre + "(" );
            Enumeration en = parametros.keys();
            String temp = null;
            while( en.hasMoreElements() ){
                if( temp != null)
                    buffer.append( ',' );
                temp = en.nextElement().toString();
                buffer.append( temp + ':' + parametros.get( temp ) );
            }
            buffer.append( "):" + ' ' + retorno );
            if( ( buffer.toString().length() +1 ) *10 > location.width  )
                location.width = ( buffer.toString().length() +1 ) *10;
            return buffer.toString();
        }
        
    }
}
