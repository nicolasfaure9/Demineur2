/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

/**
 *
 * @author p1511086
 */
public class Plateau extends Observable{
    private int nbLignes;
    private int nbColonnes;
    private int nbMines;
    private boolean end;

    private boolean win;
    private HashMap<String,Case> boutons;
    private ArrayList<int[]> indicesMines;
    
    public Plateau(int nbLignes, int nbColonnes, int nbMines){
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.nbMines = nbMines;
        this.end = false;
        this.win = true;
        boutons = new HashMap<String,Case>();
        indicesMines = new ArrayList<int[]>();

    }
    
     public int getNbMines() {
        return nbMines;
    }

   
    public boolean isEnd() {
        return end;
    }
    
    public int getNbLignes(){
        return this.nbLignes;
    }
    public int getNbColonnes(){
        return this.nbColonnes;
    }
    public int[] getPosButton(String pos){
        int [] coordonnees = {0,0};
        String[] parts = pos.split(",");
        coordonnees[0] = Integer.parseInt(parts[0]);
        coordonnees[1] = Integer.parseInt(parts[1]);
        return coordonnees;
    }
    public HashMap<String,Case> getListBoutons(){
        return this.boutons;
    }
    public void initPlateau(){
        initBoutons();
        initMines(nbMines);
        initVoisins();
        
        this.setChanged();
        this.notifyObservers(); 
    };
    public void initBoutons(){
        for(int i = 0; i<this.nbLignes;i++){
            for(int j = 0; j<this.nbColonnes;j++){
               String key = ""+i+","+j+"" ;
               this.boutons.put(key, new Case(i,j));
            }
        }
    }
    
    public void genererMines(int nbMines){
        
        
        boolean allReadyIn;
        while (this.indicesMines.size() < nbMines){
            int random[] = {(int)(Math.random() * (this.nbLignes)), (int)(Math.random() * (this.nbColonnes))};
            allReadyIn = false;
            for(int mine[] : this.indicesMines){
                if(mine[0]==random[0] && mine[1]==random[1])
                    allReadyIn = true;
            }
            if(!allReadyIn)
                this.indicesMines.add(random);
        };
        
    }
    

    public Case findCas(String pos){
        Case bouton = null;
        for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            String coordonnee = currentEntry.getKey();
            if (coordonnee.equalsIgnoreCase(pos)){
                bouton = currentEntry.getValue();
            }
        }
        return bouton; 
    }
    
    public String findPose(Case btn){
    String pos = null;
    for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            Case val = currentEntry.getValue();
            if (btn == val){
                pos = currentEntry.getKey();
            }
    }
    return pos;  
}
    
    public void initMines(int nbMines){
        genererMines(nbMines);
        for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            Case bouton = currentEntry.getValue();
            for(int[] indiceMine : this.indicesMines){
                String str = ""+indiceMine[0]+","+indiceMine[1]+"";
                if(this.findCas(str)==bouton){
                    bouton.setMine();
                }                       
            }
        }
        
        for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            String coordonnees = currentEntry.getKey();
            Case bouton = currentEntry.getValue();       
            for(Case boutonVoisin : bouton.getVoisins()){
                if(boutonVoisin.getIsAMine()){
                    bouton.addNbMinesAround();
                }              
            }
        }
    }
    public void initVoisins(){
        for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            String coordonnees = currentEntry.getKey();
            Case bouton = currentEntry.getValue();
            int x = getPosButton(coordonnees)[0];
            int y = getPosButton(coordonnees)[1];
            String CorVoisin ="";
            if(x-1 >= 0){
                CorVoisin = ""+(x-1)+","+y+"";             
                bouton.addVoisin(findCas(CorVoisin));
                if(y-1 >= 0){
                    CorVoisin = ""+(x-1)+","+(y-1)+""; 
                    bouton.addVoisin(findCas(CorVoisin));
                }
                if(y+1 < this.nbLignes){
                    CorVoisin = ""+(x-1)+","+(y+1)+""; 
                    bouton.addVoisin(findCas(CorVoisin));
                }
            }
            if(x+1 < this.nbColonnes){
                CorVoisin = ""+(x+1)+","+y+"";              
                bouton.addVoisin(findCas(CorVoisin));
                if(y-1 >= 0){
                CorVoisin = ""+(x+1)+","+(y-1)+""; 
                bouton.addVoisin(findCas(CorVoisin));
                }
                if(y+1 < this.nbLignes){
                    CorVoisin = ""+(x+1)+","+(y+1)+""; 
                    bouton.addVoisin(findCas(CorVoisin));
                }
            }
            if(y-1 >= 0){
                CorVoisin = ""+x+","+(y-1)+""; 
                bouton.addVoisin(findCas(CorVoisin));
            }
            if(y+1 < this.nbLignes){
                CorVoisin = ""+x+","+(y+1)+""; 
                bouton.addVoisin(findCas(CorVoisin));
            }
        }
        
        for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            Case bouton = currentEntry.getValue();       
            for(Case boutonVoisin : bouton.getVoisins()){
                if(boutonVoisin.getIsAMine()){
                    bouton.addNbMinesAround();
                }              
            }
        }
    }
    public void rightClickOn(Case c){
        c.rightClick();
        this.setChanged();
        this.notifyObservers();
    }
    public void leftClickOn(Case c){
        c.leftClick(false);
        if(c.getNbMinesAround()==0 && c.getStatus()!=Status.FLAG){
            showNeighbour(c);
        }
        if(c.getIsAMine() && c.getStatus()!=Status.FLAG){
            showAll();
            this.end = true;
            this.win = false;
        }
        this.setChanged();
        this.notifyObservers();
    }
    public void showAll(){
        for(Entry<String, Case> currentEntry : this.boutons.entrySet()) {
            Case bouton = currentEntry.getValue();       
            bouton.leftClick(true);
        }
    }
    public void showNeighbour(Case c){
        for(Case voisin : c.getVoisins()){
            if(voisin.getStatus()!=Status.VISIBLE && voisin.getStatus()!=Status.FLAG){
                voisin.leftClick(false);
                if(voisin.getNbMinesAround() == 0){
                    showNeighbour(voisin);
                }    
            }
        }
    }
}
