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
public class UsuarioDAO {
        Conexao conexao;
    
    
    
    public UsuarioDAO(){
        
    conexao = new Conexao();
    
    
   }
    
    public void insereUsuario(Usuario usuario){
        ver depois
 String script = "Insert Into usuarios(nome,idade,altura,peso,fuma,alcool,sexo,imc,usuario,senha,grupoderiscoid) value (?,?,?,?,?,?,?,?,?,?,?)";
    
   try{
       
      Connection con = conexao.conectar();
        PreparedStatement ps = con.prepareStatement(script); 
        
        /*ps.setString(1,usuario.getNome());
        ps.setInt(2,usuario.getIdade());
        ps.setDouble(3,usuario.getAltura());
        ps.setDouble(4,usuario.getPeso());
        ps.setBoolean(5,usuario.isFuma());
        ps.setBoolean(6,usuario.isConsomealcool());
        ps.setString(7,usuario.getSexo()); 
        ps.setDouble(8,usuario.getImc());
        ps.setString(9,usuario.getNickname());
        ps.setString(10,usuario.getSenha());
        ps.setInt(11,usuario.getIdgrupoderisco());
        ps.execute();
       */
        
        
        }catch(Exception error){
            
            System.out.println("Erro: "+error.getMessage()); 
        }

        
        
        
        
    }
    
    public void pegaDadosUser(){
  
  String script = "Select * from usuarios";

    try{
       
      Connection con = conexao.conectar();
      PreparedStatement ps = con.prepareStatement(script); 
      ResultSet rs = ps.executeQuery();
      
       
       
        
        
        }catch(Exception error){
            
            System.out.println("Erro: "+error.getMessage()); 
        }
        
        
        
        
        
    }
    
    
    public void EditaUsuario(Usuario usuario){
      /*  
        String script = "Update usuarios set nome = ?, idade = ?, peso = ?, usuario = ?, senha = ?, altura = ?, sexo = ?,"
                + " fuma = ? , alcool = ? , imc = ? where idusuario = ?";
        
        try{
       
      Connection con = conexao.conectar();
      PreparedStatement ps = con.prepareStatement(script); 
     
       ps.setString(1,usuario.getNome());
       ps.setInt(2,usuario.getIdade());
       ps.setDouble(3,usuario.getPeso());
       ps.setString(4,usuario.getNickname() );
       ps.setString(5,usuario.getSenha());
       ps.setDouble(6,usuario.getAltura());
       ps.setString(7,usuario.getSexo());
       ps.setBoolean(8,usuario.isFuma());
       ps.setBoolean(9,usuario.isConsomealcool());
       ps.setDouble(10,usuario.getImc());
       ps.setInt(11,usuario.getId());
       ps.executeUpdate();
     */
        }catch(Exception error){
            
            System.out.println("Erro: "+error.getMessage()); 
        }    
    }
}
