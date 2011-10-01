/*
 * UMLRelacion.java
 *
 * Created on 17 de junio de 2008, 0:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uml;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class UMLRelacion extends UMLElemento{
    public static final byte RELACION               =   0;
    public static final byte ASOCIACION             =   1;
    public static final byte ASOCIACION_AGREGACION  =   2;
    public static final byte ASOCIACION_COMPOSICION =   3;
    public static final byte GENERALIZACION         =   4;
    public static final byte IMPLEMENTACION         =   5;
    public static final byte DEPENDENCIA            =   6;
    
    protected byte tipo;
    protected String cardinalidad_origen;
    protected String cardinalidad_destino;
    protected UMLElemento origen;
    protected UMLElemento destino;
    /** Creates a new instance of UMLRelacion */
    public UMLRelacion() {
        super();
        this.tipo = ASOCIACION;
    }
    
    /**
     * 
     * @param origen 
     * @param destino 
     */
    public UMLRelacion( UMLElemento origen , UMLElemento destino ) {
        super();
        nombre = "relacion";
        this.tipo = RELACION;
        this.origen = origen;
        this.destino = destino;
    }
    
    /**
     * 
     * @return 
     */
    public UMLElemento getDestino() {
        return destino;
    }
    
    /**
     * 
     * @return 
     */
    public UMLElemento getOrigen() {
        return origen;
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
     * @param destino 
     */
    public void setDestino(UMLElemento destino) {
        this.destino = destino;
    }
    
    /**
     * 
     * @param origen 
     */
    public void setOrigen(UMLElemento origen) {
        this.origen = origen;
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
     * @return 
     */
    public String getCardinalidad_origen() {
        return cardinalidad_origen;
    }
    
    /**
     * 
     * @return 
     */
    public String getCardinalidad_destino() {
        return cardinalidad_destino;
    }
    
    /**
     * 
     * @param cardinalidad_origen 
     */
    public void setCardinalidad_origen(String cardinalidad_origen) {
        this.cardinalidad_origen = cardinalidad_origen;
    }
    
    /**
     * 
     * @param cardinalidad_destino 
     */
    public void setCardinalidad_destino(String cardinalidad_destino) {
        this.cardinalidad_destino = cardinalidad_destino;
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
        
        if( origen != null && destino != null ){
            if( isMarcado() )
                g2d.setColor( Color.GREEN );
            else
                g2d.setColor( Color.RED );
            int x1 = origen.getArea().x;
            int y1 = origen.getArea().y;
            int x2 = destino.getArea().x;
            int y2 = destino.getArea().y;
            g2d.drawString( getNombre() , x1 + ( x2-x1 )/2 , y1 + ( y2-y1)/2 );
            g2d.drawString( "<<" + estereotipo + ">>" , x1 + ( x2-x1 )/2 , y1 + ( y2-y1)/2 + 10 );
            if( cardinalidad_origen != null && cardinalidad_destino != null ){
                g2d.drawString( cardinalidad_origen , x1 + 5 , y1 );
                g2d.drawString( cardinalidad_destino , x2 + 5 , y2 );
            }
            switch ( tipo ){
                case RELACION:
                    g2d.drawLine( x1 , y1 , x2 , y2 );
                    break;
                case GENERALIZACION:
                    g2d.drawPolygon( getArrow( x1 , y1 , x2 , y2 , 15, 0 , 0.4 , true ) );
                    break;
                case DEPENDENCIA:
                    drawDashedLine( (Graphics2D) g2d , x1 , y1 , x2 , y2 );
                    g2d.drawPolygon( getArrow( x1 , y1 , x2 , y2 , 15 , 15 , 0.4 , false ) );
                    break;
                case ASOCIACION:
                    g2d.drawPolygon( getArrow( x1 , y1 , x2 , y2 , 15, 15, 0.4 , true ) );
                    break;
                case ASOCIACION_AGREGACION:
                    g2d.drawPolygon( getArrow( x2 , y2 , x1 , y1 , 15 , -10 , 0.4 , true ) );
                    break;
                case ASOCIACION_COMPOSICION:
                    g2d.drawPolygon( getArrow( x2 , y2 , x1 , y1 , 15 , -10 , 0.4 , true ) );
                    g2d.fillPolygon( getArrow( x2 , y2 , x1 , y1 , 15 , -10 , 0.4 , false ) );
                    break;
            }
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
        return true;
    }
    
    /**
     * 
     * @return 
     */
    public Rectangle getArea() {
        return null;
    }
    //************************************************************************//
    /**
     * 
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     * @param headsize 
     * @param difference 
     * @param factor 
     * @param withLine 
     * @return 
     */
    protected Polygon getArrow(int x1, int y1, int x2, int y2, int headsize, int difference, double factor, boolean withLine) {
        int[] crosslinebase = getArrowHeadLine(x1, y1, x2, y2, headsize);
        int[] headbase = getArrowHeadLine(x1, y1, x2, y2, headsize - difference);
        int[] crossline = getArrowHeadCrossLine(crosslinebase[0], crosslinebase[1], x2, y2, factor);
        
        Polygon head = new Polygon();
        
        head.addPoint(headbase[0], headbase[1]);
        head.addPoint(crossline[0], crossline[1]);
        head.addPoint(x2, y2);
        head.addPoint(crossline[2], crossline[3]);
        head.addPoint(headbase[0], headbase[1]);
        if(withLine)
            head.addPoint(x1, y1);
        
        return head;
        
        
    }
    
    /**
     * 
     * @param xsource 
     * @param ysource 
     * @param xdest 
     * @param ydest 
     * @param distance 
     * @return 
     */
    protected int[] getArrowHeadLine(int xsource, int ysource,int xdest,int ydest, int distance) {
        int[] arrowhead = new int[2];
        int headsize = distance;
        
        double stretchfactor = 0;
        stretchfactor = 1 - (headsize/(Math.sqrt(((xdest-xsource)*(xdest-xsource))+((ydest-ysource)*(ydest-ysource)))));
        
        arrowhead[0] = (int) (stretchfactor*(xdest-xsource))+xsource;
        arrowhead[1] = (int) (stretchfactor*(ydest-ysource))+ysource;
        
        return arrowhead;
    }
    
    /**
     * 
     * @param x1 
     * @param x2 
     * @param b1 
     * @param b2 
     * @param factor 
     * @return 
     */
    protected int[] getArrowHeadCrossLine(int x1, int x2, int b1, int b2, double factor) {
        int [] crossline = new int[4];
        
        int x_dest = (int) (((b1-x1)*factor)+x1);
        int y_dest = (int) (((b2-x2)*factor)+x2);
        
        crossline[0] = (int) ((x1+x2-y_dest));
        crossline[1] = (int) ((x2+x_dest-x1));
        crossline[2] = crossline[0]+(x1-crossline[0])*2;
        crossline[3] = crossline[1]+(x2-crossline[1])*2;
        return crossline;
    }
    
    /**
     * 
     * @param g2d 
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     */
    protected void drawDashedLine(Graphics2D g2d, int x1, int y1, int x2, int y2 ) {
        float[] dashpattern = {3.5f};
        BasicStroke bs = new BasicStroke(1.1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashpattern, 0.0f);
        g2d.setStroke(bs);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.setStroke(new BasicStroke());
    }
    
    
}
