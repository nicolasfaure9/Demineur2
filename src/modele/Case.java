/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.ArrayList;

/**
 *
 * @author p1511086
 */
public class Case {
    private Status status;
    private ArrayList<Case> voisins;
    private int nbMinesAround;
    private boolean isAMine;

    public Case(int i, int j) {
        this.status = Status.HIDE;
        this.nbMinesAround = 0;
        this.voisins = new ArrayList<Case>();
    }
    
    public Status getStatus(){
        return this.status;
    }
    public int getNbMinesAround(){
        return this.nbMinesAround;
    }
    public void addVoisin(Case btn){
        this.voisins.add(btn);
    }
    public ArrayList<Case> getVoisins(){
        return this.voisins;
    }
    
    public void rightClick(){
        if(this.status == Status.FLAG){
            this.status = Status.UNDEFINED;
        }
        else if(this.status == Status.UNDEFINED){
            this.status = Status.HIDE;
        }
        else if(this.status == Status.HIDE){
            this.status = Status.FLAG;
        }
    }
    public void leftClick(){
        if(!(this.status == Status.FLAG)){
            this.status = Status.VISIBLE;
        }     
    }
    public boolean getIsAMine(){
        return this.isAMine;
    }
    public void setMine(){
        this.isAMine = true;
    }
    public void addNbMinesAround(){
        this.nbMinesAround+=1;
    }
}
