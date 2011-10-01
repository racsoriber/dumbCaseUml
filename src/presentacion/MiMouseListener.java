/*
 * MiMouseListener.java
 *
 * Created on 19 de junio de 2008, 12:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package presentacion;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import uml.*;
import uml.UMLRelacion;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class MiMouseListener implements MouseListener,MouseMotionListener{
    protected UMLPizarra pizarra;
    protected byte relacion;
    protected UMLElemento origen;
    protected UMLElemento destino;    
    
    public MiMouseListener( UMLPizarra pizarra ){
        this.pizarra = pizarra;
        this.relacion = -1;
        this.origen = null;
        this.destino = null;
    }
    
    public void setRelacion(byte relacion) {
        this.relacion = relacion;
    }
    
    public void setOrigen(UMLElemento origen) {
        this.origen = origen;
    }
    
    public void setDestino(UMLElemento destino) {
        this.destino = destino;
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        System.out.println( "mousePressed" );
        origen = this.pizarra.getElementoEn( e.getPoint() );
        if( origen != null )
            origen.setMarcado( true );        
        this.pizarra.repaint();
        //testing
        //System.out.println(" es shift " + e.isShiftDown() );
        //if( e.isShiftDown() )
        //relacion = UMLRelacion.RELACION;
    }
    
    public void mouseReleased(MouseEvent e) {
        System.out.println( "mouseReleased" );
        //testing
        if( relacion != -1 ){
            destino = this.pizarra.getElementoEn( e.getPoint() );
            if( destino != null ){
                System.out.println("destino no es null");
                UMLRelacion rel = null;
                switch ( relacion ){
                    case UMLRelacion.RELACION:
                        rel = new UMLRelacion( origen , destino );
                        break;
                    case UMLRelacion.ASOCIACION:
                        rel = new UMLRelacion( origen , destino );
                        rel.setTipo( UMLRelacion.ASOCIACION );
                        break;
                    case UMLRelacion.ASOCIACION_AGREGACION:
                        rel = new UMLRelacion( origen , destino );
                        rel.setTipo( UMLRelacion.ASOCIACION_AGREGACION );
                        break;
                    case UMLRelacion.ASOCIACION_COMPOSICION:
                        rel = new UMLRelacion( origen , destino );
                        rel.setTipo( UMLRelacion.ASOCIACION_COMPOSICION );
                        break;
                    case UMLRelacion.GENERALIZACION:
                        rel = new UMLRelacion( origen , destino );
                        rel.setTipo( UMLRelacion.GENERALIZACION );
                        break;
                    case UMLRelacion.DEPENDENCIA:
                        rel = new UMLRelacion( origen , destino );
                        rel.setTipo( UMLRelacion.DEPENDENCIA );
                        break;
                }
                if( rel != null ){
                    rel.setNombre( "relacion" + pizarra.getDiagrama().length() );
                    this.pizarra.addElemento( rel );
                    this.pizarra.repaint();
                }
            }
        }
        relacion = -1;
        destino = null;
        //
        if( origen != null )
            origen.setMarcado( false );
        origen = null;
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
        //System.out.println( "mouseDragged" );
        //print( "dragging" );
        if( origen != null && relacion == -1 ){
            origen.setPosicion( e.getX() , e.getY() );
            this.pizarra.repaint();
        }/*else{            
            this.pizarra.repaint();
            pintarLinea( e.getPoint() );
        }*/
    }
    
    public void mouseMoved(MouseEvent e) {
    }
/*
    private void pintarLinea( Point p2) {
        this.pizarra.getGraphics().drawLine( origen.getArea().x , origen.getArea().y , p2.x , p2.y );
    }
*/    
    
}