
package edu.upc.epsevg.prop.hex;

import java.util.concurrent.ThreadLocalRandom;
import java.awt.Point;

/** 
 * Classe MyStatus que extén la classe HexGameStatus per afegit noves
 * funcionalitats per a representar l'estat d'un tauler d'Hex
 */
public class MyStatus extends HexGameStatus {
    
    public HexGameStatus hgs;
    public int heur;
    public long currentHash;
    
    /**
     * Constructora de la classe MyStatus
     * @param hg El tauler de l'estat actual
     * @param he Heuristica del tauler
     * @param CH El hash actual
     */
    public MyStatus(HexGameStatus hg, int he, long CH){
       super(hg);
       hgs = hg;
       heur = he;
       currentHash = CH;
    }
    
    /**
     * Constructor per copia superficial d'un estat
     * @param other 
     */
    public MyStatus(MyStatus other){
        super(other); 
        this.hgs = other.hgs; 
        this.heur = other.heur;
        this.currentHash = other.currentHash;
    }
    
    /**
     * Funció per a obtenir el tauler de l'estat
     * @return El tauler
     */
    public HexGameStatus getBoard(){
        return hgs;
    }
    /**
     * Funció per a obtenir l'heurística de l'estat
     * @return L'heurística
     */
    public int getHeur(){
        return heur;
    }
    /**
     * Funció per establir una heurística
     * @param he L'heuristica a establir
     */
    public void setHeur(int he){
        heur = he;
    }
    /**
     * Funció per a obtenir el Hash actual
     * @return El hash actual
     */
    public long getCurrentHash(){
        return currentHash;
    }
}
