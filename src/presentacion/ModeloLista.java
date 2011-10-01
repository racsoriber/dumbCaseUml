/*
 * ModeloLista.java
 *
 * Created on 19 de mayo de 2008, 10:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package presentacion;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Juan TImoteo Ponce Ortiz
 */
public class ModeloLista extends AbstractListModel{
    ArrayList data;
    
    public ModeloLista(){
        data = new ArrayList();
    }
    
    @Override
    public int getSize() {
        return data.size();
    }
    
    @Override
    public Object getElementAt(int index) {
        return data.get( index );
    }
    
    public void add( Object obj ){
        data.add( obj);
        fireContentsChanged( this , 0 , data.size() -1  );
    }
    
    public void remove( int index ){
        data.remove( index );
        fireContentsChanged( this , 0 , data.size() -1  );
    }
    
    public void clear() {
        data.clear();
    }    

    public void set(int index, Object object) {
        data.set( index, object );
        fireContentsChanged( this , 0 , data.size() -1  );
    }
    
}