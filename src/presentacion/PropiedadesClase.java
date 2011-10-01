/*
 * PropiedadesClase.java
 *
 * Created on 10 de julio de 2008, 22:47
 */

package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import uml.UMLClase;

/**
 *
 * @author  Juan Timoteo Ponce Ortiz
 */
public class PropiedadesClase extends javax.swing.JDialog {
    private boolean accept;
    private UMLClase objetivo;
    /** Creates new form PropiedadesClase */
    public PropiedadesClase(java.awt.Frame parent, boolean modal , UMLClase objetivo) {
        super(parent, modal);
        if( objetivo != null ){
            this.objetivo = objetivo;
            initComponents();
            init();
            setLocationRelativeTo( null );
        }
        return;
    }
    
    private void init(){
        listaAtributos.setModel( new ModeloLista() );
        listaMetodos.setModel( new ModeloLista() );
        listaParametros.setModel( new ModeloLista() );
        //general
        setGenerales();
        //atributos
        setAtributos();
        //metodos
        setMetodos();
        
        setListeners();
    }
    
    private void setGenerales(){
        txtNombre.setText( objetivo.getNombre() );
        comboTipo.setSelectedIndex( objetivo.getTipo() );
        txtEstereotipo.setText( objetivo.getEstereotipo() );
        switch( objetivo.getVisibilidad() ){
            case UMLClase.PRIVATE:
                setAccesoGeneral( true , false , false );
                break;
            case UMLClase.PUBLIC:
                setAccesoGeneral( false , true , false );
                break;
            default:
                setAccesoGeneral( false , false , true );
        }
    }
    
    private void setAtributos(){
        Enumeration en = objetivo.getAtributos().keys();
        String key,attr;
        while( en.hasMoreElements() ){
            key = (String) en.nextElement();
            attr = "" + objetivo.getCharAcceso(  Byte.parseByte( key.charAt( 0 ) + "" ) ) + ' ';
            attr += key.substring( 1 , key.length() ) + " : " + objetivo.getAtributos().get( key );
            ((ModeloLista)listaAtributos.getModel()).add( attr );
        }
    }
    
    private void setMetodos(){
        Enumeration en ;
        String key = null;
        String temp = "";
        for (UMLClase.Metodo elem : objetivo.getOperaciones()) {
            temp += objetivo.getCharAcceso( elem.getAcceso() ) + " ";
            temp += objetivo.getNombre() + '(';
            en = elem.getParametros().keys();
            while( en.hasMoreElements() ){
                if( key != null)
                    temp += " , ";
                key = (String) en.nextElement();
                temp += key + ':' + elem.getParametros().get( key );
                key = null;
            }
            temp+= "):" + elem.getRetorno();
            ((ModeloLista)listaMetodos.getModel()).add( temp );
            temp = "";
        }
    }
    
    private void setListeners(){
        EstadoListener listenesEst = new EstadoListener();
        PropiedadesListener listenerProp = new PropiedadesListener();
        btnAceptar.addActionListener( listenerProp );
        btnCancelar.addActionListener( listenerProp );
        //general
        radioPrivado.addMouseListener( listenesEst );
        radioProtected.addMouseListener( listenesEst );
        radioPublico.addMouseListener( listenesEst );
        //atributos
        radioAtPrivado.addMouseListener( listenesEst );
        radioAtProtected.addMouseListener( listenesEst );
        radioAtPublico.addMouseListener( listenesEst );
        
        btnAddAt.addActionListener( listenerProp );
        btnModAt.addActionListener( listenerProp );
        btnDelAt.addActionListener( listenerProp );
        //metodos
        radioMetPriv.addMouseListener( listenesEst );
        radioMetProt.addMouseListener( listenesEst );
        radioMetPub.addMouseListener( listenesEst );
        
        btnAddMet.addActionListener( listenerProp );
        btnModMet.addActionListener( listenerProp );
        btnDelMet.addActionListener( listenerProp );
        btnAddPar.addActionListener( listenerProp );
        btnDelPar.addActionListener( listenerProp );
    }
    
    public boolean isAccept() {
        return accept;
    }
    
    public boolean showDialog(){
        this.setVisible( true );
        return accept;
    }
    
    private void setValores(){
        //general
        objetivo.setNombre( txtNombre.getText() );
        objetivo.setTipo( (byte)comboTipo.getSelectedIndex() );
        objetivo.setEstereotipo( txtEstereotipo.getText() );
        if( radioPrivado.isSelected() )
            objetivo.setVisibilidad( UMLClase.PRIVATE );
        else if( radioPublico.isSelected() )
            objetivo.setVisibilidad( UMLClase.PUBLIC );
        else
            objetivo.setVisibilidad( UMLClase.PROTECTED );
        //atributos
        ModeloLista modelo = (ModeloLista) listaAtributos.getModel();
        String temp;
        for (int i = 0; i < modelo.getSize(); i++) {
            temp = modelo.getElementAt( i ).toString();
            objetivo.addAtributo( getAcceso( temp ) , getAtVar( temp ) , getAtTipo( temp ) );
        }
        //metodos
        modelo = (ModeloLista) listaMetodos.getModel();
        String nombre,retorno;
        for (int i = 0; i < modelo.getSize(); i++) {
            temp = modelo.getElementAt( i ).toString();
            nombre = temp.substring( 1 , temp.indexOf( '(' ) ).trim();
            retorno = temp.substring( temp.indexOf( ')' ) + 2 , temp.length() ).trim();
            objetivo.addOperacion( getAcceso( temp ) , nombre , retorno );
            setParametros( nombre , temp.substring( temp.indexOf( '(' ) + 1 , temp.indexOf( ')' ) ) );
        }
    }
    
    private void setParametros( String op,String str ){
        StringTokenizer stoken = new StringTokenizer( str , "," );
        String temp;
        while( stoken.hasMoreTokens() ){
            temp = stoken.nextToken();
            objetivo.getOperacion( op ).addParametro( getAtVar( str ) , getAtTipo( str ) );
        }
    }
    
    private byte getAcceso( String str ){
        char c = str.charAt( 0 );
        if( c == '-' )
            return UMLClase.PRIVATE;
        else if( c == '+' )
            return UMLClase.PUBLIC;
        else
            return UMLClase.PROTECTED;
    }
    private String getAtVar( String str ){
        return str.substring( 1 , str.indexOf( ":" )).trim() ;
    }
    
    private String getAtTipo( String str ){
        return str.substring( str.indexOf( ":" ) + 1 , str.length() ).trim() ;
    }
    
    /**Atributos**/
    private void addAtributo(){
        if( txtNombreAt.getText().length() > 0 ){
            ModeloLista temp = (ModeloLista) listaAtributos.getModel();
            String ele = "";
            if( radioAtPrivado.isSelected() )
                ele += '-';
            else if( radioAtPublico.isSelected() )
                ele += '+';
            else
                ele += '#';
            ele += ' ' + txtNombreAt.getText() + " : " + comboTipoAt.getSelectedItem().toString();
            temp.add( ele );
            txtNombreAt.setText( "" );
        }
    }
    private void modAtributo(){
        int index = listaAtributos.getSelectedIndex();
        if( txtNombreAt.getText().length() > 0 && index != -1){
            ModeloLista temp = (ModeloLista) listaAtributos.getModel();
            String ele = "";
            if( radioAtPrivado.isSelected() )
                ele += '-';
            else if( radioAtPublico.isSelected() )
                ele += '+';
            else
                ele += '#';
            ele += ' ' + txtNombreAt.getText() + " : " + comboTipoAt.getSelectedItem().toString();
            temp.set( index , ele );
            txtNombreAt.setText( "" );
        }
    }
    private void delAtributo(){
        int index = listaAtributos.getSelectedIndex();
        if( index != -1){
            ModeloLista temp = (ModeloLista) listaAtributos.getModel();
            temp.remove( index );
        }
    }
    /**Metodos**/
    private void addParametro(){
        if( !txtNombreMetodo.getText().isEmpty() ){
            Object[] mensaje = new Object[ 3 ];
            mensaje[ 0 ] = "Ingrese el parametro y su tipo";
            JTextField txt = new JTextField( "metodo" );
            mensaje[ 1 ] = txt;
            JComboBox combo = new JComboBox();
            combo.setEditable( true );
            combo.addItem( "byte" );
            combo.addItem( "int" );
            combo.addItem( "long" );
            combo.addItem( "float" );
            combo.addItem( "double" );
            combo.addItem( "char" );
            combo.addItem( "String" );
            mensaje[ 2 ] = combo;
            String[] opciones = new String[]{ "Aceptar" , "Cancelar" } ;
            int result = JOptionPane.showOptionDialog( this ,
                    mensaje ,
                    "Parametros",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[ 0 ]
                    );
            if( result == 0 && !txt.getText().isEmpty() ){
                ( ( ModeloLista ) listaParametros.getModel() )
                .add( ' ' + txt.getText() + " : " + combo.getSelectedItem() );
            }
        }
    }
    private void delParametro(){
        int index = listaParametros.getSelectedIndex();
        if( index != -1 ){
            ( ( ModeloLista ) listaParametros.getModel())
            .remove( index );
        }
    }
    private void addMetodo(){
        if( !txtNombreMetodo.getText().isEmpty() ){
            String ele = "";
            if( radioMetPriv.isSelected() )
                ele += '-';
            else if( radioMetPub.isSelected() )
                ele += '+';
            else
                ele += '#';
            ele += ' ' + txtNombreMetodo.getText() + '(';
            ModeloLista temp = (ModeloLista) listaParametros.getModel();
            String param = null;
            for (int i = 0; i < temp.getSize(); i++) {
                if( param != null )
                    ele += ",";
                param = temp.getElementAt( i ).toString();
                ele += ' ' + getAtVar( param ) + ":" + getAtTipo( param );
            }
            ele += "):" + comboRetornoMet.getSelectedItem().toString();
            ( ( ModeloLista ) listaMetodos.getModel() ).add( ele );
            
            temp.clear();
            txtNombreMetodo.setText( "" );
        }
    }
    private void modMetodo(){
        int index = listaMetodos.getSelectedIndex();
        if( index != -1 && !txtNombreMetodo.getText().isEmpty() ){
            String ele = "";
            if( radioMetPriv.isSelected() )
                ele += '-';
            else if( radioMetPub.isSelected() )
                ele += '+';
            else
                ele += '#';
            ele += ' ' + txtNombreMetodo.getText() + '(';
            ModeloLista temp = (ModeloLista) listaParametros.getModel();
            String param = null;
            for (int i = 0; i < temp.getSize(); i++) {
                if( param != null )
                    ele += ",";
                param = temp.getElementAt( i ).toString();
                ele += getAtVar( param ) + ":" + getAtTipo( param );
            }
            ele += "):" + comboRetornoMet.getSelectedItem().toString();
            ( ( ModeloLista ) listaMetodos.getModel() ).set( index , ele );
            
            temp.clear();
            txtNombreMetodo.setText( "" );
        }
    }
    private void delMetodo(){
        int index = listaMetodos.getSelectedIndex();
        if( index != -1 ){
            ( ( ModeloLista) listaMetodos.getModel() ).remove( index );
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Código Generado  ">//GEN-BEGIN:initComponents
    private void initComponents() {
        panelTabs = new javax.swing.JTabbedPane();
        panelGeneral = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        radioPrivado = new javax.swing.JRadioButton();
        radioPublico = new javax.swing.JRadioButton();
        radioProtected = new javax.swing.JRadioButton();
        comboTipo = new javax.swing.JComboBox();
        txtNombre = new javax.swing.JTextField();
        txtEstereotipo = new javax.swing.JTextField();
        panelAtributos = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        radioAtPrivado = new javax.swing.JRadioButton();
        radioAtPublico = new javax.swing.JRadioButton();
        radioAtProtected = new javax.swing.JRadioButton();
        txtNombreAt = new javax.swing.JTextField();
        comboTipoAt = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaAtributos = new javax.swing.JList();
        btnAddAt = new javax.swing.JButton();
        btnModAt = new javax.swing.JButton();
        btnDelAt = new javax.swing.JButton();
        panelMetodos = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaParametros = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaMetodos = new javax.swing.JList();
        btnAddMet = new javax.swing.JButton();
        btnModMet = new javax.swing.JButton();
        btnDelMet = new javax.swing.JButton();
        txtNombreMetodo = new javax.swing.JTextField();
        comboRetornoMet = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        radioMetPriv = new javax.swing.JRadioButton();
        radioMetPub = new javax.swing.JRadioButton();
        radioMetProt = new javax.swing.JRadioButton();
        btnAddPar = new javax.swing.JButton();
        btnDelPar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Propiedades clase");
        jLabel1.setText("Nombre:");

        jLabel2.setText("Tipo:");

        jLabel3.setText("Estereotipo");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Acceso"));
        radioPrivado.setSelected(true);
        radioPrivado.setText("Privado");
        radioPrivado.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioPrivado.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioPublico.setText("Publico");
        radioPublico.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioPublico.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioProtected.setText("Protegido");
        radioProtected.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioProtected.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioPrivado)
                    .addComponent(radioProtected)
                    .addComponent(radioPublico))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioPrivado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioPublico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioProtected)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        comboTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Clase", "Interfaz", "Abstracta" }));

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(48, 48, 48)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEstereotipo)
                            .addComponent(txtNombre)
                            .addComponent(comboTipo, 0, 145, Short.MAX_VALUE))))
                .addContainerGap(161, Short.MAX_VALUE))
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtEstereotipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        panelTabs.addTab("General", panelGeneral);

        jLabel4.setText("Nombre:");

        jLabel5.setText("Tipo:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Acceso"));
        radioAtPrivado.setSelected(true);
        radioAtPrivado.setText("Privado");
        radioAtPrivado.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioAtPrivado.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioAtPublico.setText("Publico");
        radioAtPublico.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioAtPublico.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioAtProtected.setText("Protegido");
        radioAtProtected.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioAtProtected.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioAtPublico)
                    .addComponent(radioAtProtected)
                    .addComponent(radioAtPrivado))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioAtPrivado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAtPublico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAtProtected))
        );

        comboTipoAt.setEditable(true);
        comboTipoAt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "int", "boolean", "String", "float", "byte", "long", "char", "double", "" }));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Atributos"));
        jScrollPane1.setViewportView(listaAtributos);

        btnAddAt.setText("Agregar");

        btnModAt.setText("Modiicar");

        btnDelAt.setText("Eliminar");

        javax.swing.GroupLayout panelAtributosLayout = new javax.swing.GroupLayout(panelAtributos);
        panelAtributos.setLayout(panelAtributosLayout);
        panelAtributosLayout.setHorizontalGroup(
            panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAtributosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAtributosLayout.createSequentialGroup()
                        .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(27, 27, 27)
                        .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboTipoAt, 0, 138, Short.MAX_VALUE)
                            .addComponent(txtNombreAt, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelAt)
                    .addComponent(btnModAt)
                    .addComponent(btnAddAt))
                .addGap(125, 125, 125))
        );
        panelAtributosLayout.setVerticalGroup(
            panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtributosLayout.createSequentialGroup()
                .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAtributosLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtNombreAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(comboTipoAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelAtributosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(panelAtributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAtributosLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(btnAddAt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModAt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelAt))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelTabs.addTab("Atributos", panelAtributos);

        jLabel6.setText("Nombre:");

        jLabel7.setText("Retorno:");

        jLabel8.setText("Parametros:");

        jScrollPane2.setViewportView(listaParametros);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Metodos"));
        jScrollPane3.setViewportView(listaMetodos);

        btnAddMet.setText("Agregar");

        btnModMet.setText("Modificar");

        btnDelMet.setText("Eliminar");

        comboRetornoMet.setEditable(true);
        comboRetornoMet.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "byte", "int", "long", "char", "float", "String", "double" }));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Acceso"));
        radioMetPriv.setText("Privado");
        radioMetPriv.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioMetPriv.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioMetPub.setSelected(true);
        radioMetPub.setText("Publico");
        radioMetPub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioMetPub.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioMetProt.setText("Protegido");
        radioMetProt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioMetProt.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioMetPriv)
                    .addComponent(radioMetPub)
                    .addComponent(radioMetProt))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(radioMetPriv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioMetPub)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioMetProt))
        );

        btnAddPar.setText("Agregar");

        btnDelPar.setText("Eliminar");

        javax.swing.GroupLayout panelMetodosLayout = new javax.swing.GroupLayout(panelMetodos);
        panelMetodos.setLayout(panelMetodosLayout);
        panelMetodosLayout.setHorizontalGroup(
            panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMetodosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMetodosLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddMet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelMet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnModMet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelMetodosLayout.createSequentialGroup()
                        .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMetodosLayout.createSequentialGroup()
                                .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8))
                                .addGap(19, 19, 19)
                                .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                                    .addComponent(comboRetornoMet, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNombreMetodo, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddPar)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelPar))))
                .addGap(26, 26, 26))
        );
        panelMetodosLayout.setVerticalGroup(
            panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMetodosLayout.createSequentialGroup()
                .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMetodosLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtNombreMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(comboRetornoMet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMetodosLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(panelMetodosLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelMetodosLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddPar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelPar)))
                .addGroup(panelMetodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMetodosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMetodosLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(btnAddMet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModMet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelMet)))
                .addGap(21, 21, 21))
        );
        panelTabs.addTab("Metodos", panelMetodos);

        btnAceptar.setText("Aceptar");

        btnCancelar.setText("Cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(btnAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnAceptar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    class PropiedadesListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if( e.getSource() == btnAceptar ){
                accept = true;
                setValores();
                dispose();
                return;
            }
            
            if( e.getSource() == btnCancelar ){
                accept = false;
                dispose();
                return;
            }
            //atributos
            if( e.getSource() == btnAddAt ){
                addAtributo();
                return;
            }
            if( e.getSource() == btnModAt ){
                modAtributo();
                return;
            }
            if( e.getSource() == btnDelAt ){
                delAtributo();
                return;
            }
            //metodos
            if( e.getSource() == btnAddPar ){
                addParametro();
                return;
            }
            if( e.getSource() == btnDelPar ){
                delParametro();
                return;
            }
            if( e.getSource() == btnAddMet ){
                addMetodo();
                return;
            }
            if( e.getSource() == btnModMet ){
                modMetodo();
                return;
            }
            if( e.getSource() == btnDelMet ){
                delMetodo();
                return;
            }
            
        }
    }
    
    class EstadoListener implements MouseListener{
        
        @Override
        public void mouseClicked(MouseEvent e) {
            //general
            if( e.getSource() == radioPrivado ){
                setAccesoGeneral( true , false , false );
                return;
            }
            if( e.getSource() == radioProtected ){
                setAccesoGeneral( false , false , true );
                return;
            }
            if( e.getSource() == radioPublico ){
                setAccesoGeneral( false , true , false );
                return;
            }
            
            //atributo
            if( e.getSource() == radioAtPrivado ){
                setAtAccesoGeneral( true , false , false );
                return;
            }
            if( e.getSource() == radioAtProtected ){
                setAtAccesoGeneral( false , false , true );
                return;
            }
            if( e.getSource() == radioAtPublico ){
                setAtAccesoGeneral( false , true , false );
                return;
            }
            
            //metodos
            if( e.getSource() == radioMetPriv ){
                setMetAccesoGeneral( true , false , false );
                return;
            }
            if( e.getSource() == radioMetProt ){
                setMetAccesoGeneral( false , false , true );
                return;
            }
            if( e.getSource() == radioMetPub ){
                setMetAccesoGeneral( false , true , false );
                return;
            }
        }
        
        public void mousePressed(MouseEvent e) {        }
        public void mouseReleased(MouseEvent e) {        }
        public void mouseEntered(MouseEvent e) {        }
        public void mouseExited(MouseEvent e) {        }
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PropiedadesClase(new javax.swing.JFrame(), true, new UMLClase( "clase",(byte)0 ) ).showDialog();
            }
        });
        
    }
    
    private void setAccesoGeneral( boolean prim,boolean seg,boolean terc ){
        radioPrivado.setSelected( prim );
        radioPublico.setSelected( seg );
        radioProtected.setSelected( terc );
    }
    private void setAtAccesoGeneral( boolean prim,boolean seg,boolean terc ){
        radioAtPrivado.setSelected( prim );
        radioAtPublico.setSelected( seg );
        radioAtProtected.setSelected( terc );
    }
    private void setMetAccesoGeneral( boolean prim,boolean seg,boolean terc ){
        radioMetPriv.setSelected( prim );
        radioMetPub.setSelected( seg );
        radioMetProt.setSelected( terc );
    }
    
    // Declaración de varibales -no modificar//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnAddAt;
    private javax.swing.JButton btnAddMet;
    private javax.swing.JButton btnAddPar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDelAt;
    private javax.swing.JButton btnDelMet;
    private javax.swing.JButton btnDelPar;
    private javax.swing.JButton btnModAt;
    private javax.swing.JButton btnModMet;
    private javax.swing.JComboBox comboRetornoMet;
    private javax.swing.JComboBox comboTipo;
    private javax.swing.JComboBox comboTipoAt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listaAtributos;
    private javax.swing.JList listaMetodos;
    private javax.swing.JList listaParametros;
    private javax.swing.JPanel panelAtributos;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelMetodos;
    private javax.swing.JTabbedPane panelTabs;
    private javax.swing.JRadioButton radioAtPrivado;
    private javax.swing.JRadioButton radioAtProtected;
    private javax.swing.JRadioButton radioAtPublico;
    private javax.swing.JRadioButton radioMetPriv;
    private javax.swing.JRadioButton radioMetProt;
    private javax.swing.JRadioButton radioMetPub;
    private javax.swing.JRadioButton radioPrivado;
    private javax.swing.JRadioButton radioProtected;
    private javax.swing.JRadioButton radioPublico;
    private javax.swing.JTextField txtEstereotipo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreAt;
    private javax.swing.JTextField txtNombreMetodo;
    // Fin de declaración de variables//GEN-END:variables
    
}
