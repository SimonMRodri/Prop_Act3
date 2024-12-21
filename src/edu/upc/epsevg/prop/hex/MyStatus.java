
package edu.upc.epsevg.prop.hex;

import java.util.concurrent.ThreadLocalRandom;
import java.awt.Point;

public class MyStatus extends HexGameStatus {
    
    public HexGameStatus hgs;
    public int heur;
    public long currentHash;
    
    public MyStatus(int sizeH, HexGameStatus hg, int he, long CH){
       super(sizeH); //o hg | constructor que crea un estat buit a partir del size o el constructor per copia al que li pases el HGS
       //crec que seria super(hg);
       hgs = hg;
       heur = he;
       currentHash = CH;
    }
    public MyStatus(MyStatus other){
        super(other); //getId() me ponia// Llama al constructor de la clase padre con el mismo identificador
        this.hgs = other.hgs; // Copia superficial (si quieres copia profunda, necesitarás clonar hgs)
        this.heur = other.heur;
        this.currentHash = other.currentHash;
    }
    
    public HexGameStatus getBoard(){
        return hgs;
    }
    public int getHeur(){
        return heur;
    }
    public void setHeur(int he){
        heur = he;
    }
    public long getCurrentHash(){
        return currentHash;
    }
}
