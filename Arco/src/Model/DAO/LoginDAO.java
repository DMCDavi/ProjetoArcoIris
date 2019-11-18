/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Luis Felipe
 */
public class LoginDAO {
        Conexao conexao = new Conexao();
    
    
    
    public boolean verificUsuario(String usuario, String senha){
    int verificador = 0;  
    String script = "Select * from usuarios where usuario = '"+usuario+"' and senha = '"+senha+"' ;";
       
    try{
            
      Connection con = conexao.conectar();
      PreparedStatement ps = con.prepareStatement(script); 
  
       ResultSet rs = ps.executeQuery();
       while(rs.next()){
          
          verificador++; 
                   
       }
       if(verificador > 0){
           
          return true; 
           
       }else {
           
           return false;
       }
       
        // return true;
        
        }catch(Exception error){
           
           // System.out.println("Erro: "+error.getMessage()); 
             return false;
        }
   
    }
    
    public Usuario PegaUsuario(String usuario, String senha){
        Usuario user = new Usuario();   
        String script = "Select * from usuarios where usuario = '"+usuario+"' and senha = '"+senha+"';";
            
        try{
            
      Connection con = conexao.conectar();
      PreparedStatement ps = con.prepareStatement(script); 
      //  ps.setString(1, cafe.getTipo());
       ResultSet rs = ps.executeQuery();
       
       while(rs.next()){
          
           user.setNome(rs.getString("nome"));
           user.setEmail(rs.getString("email"));
           user.setCPF(rs.getString("cpf"));
           user.setConsulta(rs.getString("consulta"));
           user.setGenero(rs.getString("genero"));
           user.setId(rs.getInt("idusuario"));
       }
      

        
        }catch(Exception error){
            
            System.out.println("Erro: "+error.getMessage()); 
        }
        
        
      return user;  
    }
    
  
}
