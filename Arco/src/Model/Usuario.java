/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author aluno
 */
public class Usuario extends Pessoa{
    public int id;
    public String consulta;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getConsulta() {
        return this.consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }
}
