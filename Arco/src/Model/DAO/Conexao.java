/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
/**
 *
 * @author Luis Felipe
 */

//COMENTEI PQ TAVA DANDO ERRO NAO SEI PQ
//JOAO VÊ ISSO PF

public class Conexao {
        Connection conexao;
     
      
     public Connection conectar(){
       
        try{
            
           Class.forName("com.mysql.jdbc.Driver");
           String servidor ="jdbc:mysql://localhost:3306/arco";
           String usuario = "root";
           String senha = "root";
           
           return (Connection)DriverManager.getConnection(servidor,usuario,senha);
           
            
        }catch(Exception error){
            
            System.out.println("Erro:"+error.getMessage());
          
        }
       
        return null;
    }
     
     

}
