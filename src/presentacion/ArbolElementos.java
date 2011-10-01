/*
 * ArbolElementos.java
 *
 * Created on 10 de julio de 2008, 22:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package presentacion;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import uml.UMLComposite;
import uml.UMLElemento;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class ArbolElementos extends JTree implements VistaDiagrama{
    private UMLComposite diagrama;
    private DefaultTreeModel modeloTree;
    private DefaultMutableTreeNode nodoRoot = new DefaultMutableTreeNode( "Diagrama de clases" );
    private DefaultMutableTreeNode nodoClases = new DefaultMutableTreeNode( "Clases" );
    private DefaultMutableTreeNode nodoRelaciones = new DefaultMutableTreeNode( "Relaciones" );
    
    /** Creates a new instance of ArbolElementos */
    public ArbolElementos() {
        super();
        modeloTree = new DefaultTreeModel( nodoRoot );
        modeloTree.insertNodeInto( nodoClases , nodoRoot , 0 );
        modeloTree.insertNodeInto( nodoRelaciones , nodoRoot , 1 );
        this.setModel( modeloTree );
    }
    
    public void setDiagrama(UMLComposite diagrama) {
        this.diagrama = diagrama;
    }
    
    public void actualizar() {        
        nodoClases.removeAllChildren();        
        nodoRelaciones.removeAllChildren();
        
        int indexRelaciones = 0;
        int indexClases = 0;
        for (UMLElemento elem : diagrama.getElementos()) {
            if( !elem.isRelacion() ){
                nodoClases.insert( new DefaultMutableTreeNode( elem.getNombre() ) ,indexClases  );
                indexClases++;
            }else{
                nodoRelaciones.insert( new DefaultMutableTreeNode( elem.getNombre() ) , indexRelaciones  );
                indexRelaciones++;
            }
        }
        modeloTree.reload();
    }    
            
    
}
