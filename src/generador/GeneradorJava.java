/*
 * GeneradorJava.java
 *
 * Created on 8 de julio de 2008, 23:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generador;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import uml.UMLClase;
import uml.UMLElemento;

/**
 * Generador de codigo para Java
 * @author Juan Timoteo Ponce Ortiz
 */
public class GeneradorJava extends GeneradorCodigo{    
    
    /** Creates a new instance of GeneradorJava */
    public GeneradorJava() {
        init();
    }
    
    /**
     * Generar el codigo en una cadena
     * @return codigo
     */
    @Override
    public String generarCodigo() {
        StringBuilder buffer = new StringBuilder();
        //dependencias
        buffer.append( getDependencias() );
        //visibilidad
        switch( clase.getVisibilidad() ){
            case UMLClase.PRIVATE:
                buffer.append( "private " );
                break;
            case UMLClase.PUBLIC:
                buffer.append( "public " );
                break;
            case UMLClase.PROTECTED:
                buffer.append( "protected " );
                break;
        }
        //nombre
        switch( clase.getTipo() ){
            case UMLClase.CLASE:
                buffer.append( "class " );
                break;
            case UMLClase.ABSTRACTA:
                buffer.append( "abstract class " );
                break;
            case UMLClase.INTERFAZ:
                buffer.append( "interface " );
                break;
        }
        buffer.append(  clase.getNombre() + " "  );
        //padre
        if( !padre.isEmpty() )
            buffer.append( "extends " + padre + " " );
        //implementaciones
        buffer.append( getRealizaciones() );
        buffer.append( '{' + EOLN );
        //atributos
        buffer.append( getAtributos() );
        //constructor
        buffer.append( EOLN + "\tpublic " + clase.getNombre() + "(){} " + EOLN + EOLN );
        //metodos
        //selectores y modificadores
        if( crearAccesos ){
            buffer.append( getAccesos() );
        }
        //metodos
        buffer.append( getMetodos() );
        
        buffer.append( "}" );
        return buffer.toString();
    }
    
    
    
    
    /**
     * Retorna el nombre de la clase, con su extension
     * @return nombre de clase
     */
    @Override
    public String getPath() {
        return clase.getNombre() + ".java";
    }
    
    /**
     * Retorna las dependencias de la clase(otras clases)
     * @return dependencias
     */    
    private String getDependencias() {
        String str = "";
        for (String elem : dependencias) {
            str = str + ( "import " + elem + ";" +  EOLN );
        }
        return str;
    }
    
    /**
     * Retorna las clases a implementar
     * @return interfaces
     */
    private Object getRealizaciones() {
        String str = "";
        if( realizaciones.size() > 0 ){
            str = str + "implements " ;
            for (int i=0 ; i < realizaciones.size() ; i++ ) {
                str = str + ( realizaciones.get( i ).toString() );
                if( i + 1 < realizaciones.size() )
                    str = str + ( "," );
            }
        }
        return str;
    }
    
    /**
     * Retorna los atributos de la clase
     * @return texto
     */
    private String getAtributos() {
        String str = "";
        Hashtable<String,String> atributos = clase.getAtributos();
        Enumeration en = atributos.keys();
        String var;
        String temp;
        while( en.hasMoreElements() ){
            var = (String) en.nextElement();
            temp = var.substring( 1 , var.length() );
            str = str + ( "\tprivate " +   atributos.get( var )
            + " " + temp + ";" + EOLN );
        }
        return str;
    }
    
    /**
     * Retorna los atributos de la clase
     * @return texto
     */
    private String getAccesos() {
        String str = "";
        Hashtable<String,String> atributos = clase.getAtributos();
        Enumeration en = atributos.keys();
        String var;
        String temp;
        while( en.hasMoreElements() ){
            var = (String) en.nextElement();
            temp = var.substring( 1 , var.length() );
            str = str + ( "\tpublic " + atributos.get( var ) + " get" + temp + "(){" + EOLN );
            str = str + ( "\t\treturn this." + temp + ";" + EOLN + "\t}" + EOLN );
            
            str = str + ( "\tpublic void set" + temp + "( " + atributos.get( var ) +" value ){" + EOLN );
            str = str + ( "\t\tthis." + temp + " = value;" + EOLN + "\t}" + EOLN );
        }
        return str;
    }
    
    /**
     * Retorna los metodos de una clase
     * @return texto
     */
    private Object getMetodos() {
        StringBuilder buffer = new StringBuilder();
        ArrayList< UMLClase.Metodo > metodos =  clase.getOperaciones();
        Hashtable< String , String > parametros;
        Enumeration en;
        String temp = null;
        
        for (UMLClase.Metodo elem : metodos) {
            switch ( elem.getAcceso() ){
                case UMLElemento.PUBLIC:
                    buffer.append( "\tpublic ");
                    break;
                case UMLElemento.PRIVATE:
                    buffer.append( "\tprivate ");
                    break;
                case UMLElemento.PROTECTED:
                    buffer.append( "\tprotected ");
                    break;
            }
            temp = elem.getRetorno();
            if( temp.trim().isEmpty() )
                temp = "void";
            buffer.append( temp + " " + elem.getNombre() + "( " );
            parametros = elem.getParametros();
            en = parametros.keys();
            temp = null;
            while( en.hasMoreElements() ){
                if( temp != null )
                    buffer.append( " , " );
                temp = en.nextElement().toString();
                buffer.append( parametros.get( temp ) + " " + temp );
            }
            buffer.append( ")" );
            temp = null;
            
            buffer.append( "{\n \t}\n" );
        }
        return buffer.toString();
        
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        UMLClase clase = new UMLClase( "clase_1",UMLClase.CLASE );
        clase.addAtributo( UMLElemento.PRIVATE , "attr1" , "String" );
        clase.addAtributo( UMLElemento.PRIVATE ,"attr2" , "int" );
        clase.addOperacion( clase.PROTECTED , "metodo1" , "int" );
        clase.getOperacion( "metodo1" ).addParametro( "param1" , "String" );
        clase.getOperacion( "metodo1" ).addParametro( "param2" , "String" );
        
        GeneradorCodigo gen = new GeneradorJava();
        gen.addDependencia( clase );
        gen.addRealizacion( clase );
        gen.setClase( clase );
        gen.setPadre( clase );
        gen.crearAccesos( true );
        
        System.out.println( gen.generarCodigo() );
    }
    
}
