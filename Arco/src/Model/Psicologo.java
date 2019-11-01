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
public class Psicologo extends Pessoa{
    public int id;
    public Curriculo curriculo;
    public FaculdadeAtendimento faculdade;    

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Curriculo getCurriculo() {
        return this.curriculo;
    }

    public void setCurriculo(Curriculo curriculo) {
        this.curriculo = curriculo;
    }

    public FaculdadeAtendimento getFaculdade() {
        return this.faculdade;
    }

    public void setFaculdade(FaculdadeAtendimento faculdade) {
        this.faculdade = faculdade;
    }
    
}
