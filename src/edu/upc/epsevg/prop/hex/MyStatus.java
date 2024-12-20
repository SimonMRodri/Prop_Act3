
package edu.upc.epsevg.prop.hex;

import java.util.concurrent.ThreadLocalRandom;
import java.awt.Point;

public class MyStatus extends HexGameStatus {
    
    public HexGameStatus hgs;
    public int heur;
    public long currentHash;
    public ZobristHashing zh;
    
    public MyStatus(int sizeH, HexGameStatus hg, int he){
       super(sizeH); //o hg | constructor que crea un estat buit a partir del size o el constructor per copia al que li pases el HGS
       //crec que seria super(hg);
       hgs = hg;
       heur = he;
       int size = hgs.getSize();
       zh = new ZobristHashing(size, 3);
       
       int[][] board = new int[size][size];
       for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = hgs.getPos(x, y)+1;
            }
       }
       currentHash = zh.calculateInitialHash(board);
    }
    public MyStatus(MyStatus other){
        super(other); //getId() me ponia// Llama al constructor de la clase padre con el mismo identificador
        this.hgs = other.hgs; // Copia superficial (si quieres copia profunda, necesitarás clonar hgs)
        this.heur = other.heur;
        this.currentHash = other.currentHash;
        this.zh = other.zh;
    }
    
    public HexGameStatus getBoard(){
        return hgs;
    }
    
    public int getHeuristica(){
        return heur;
    }
    public void setHeuristica(int he){
        heur = he;
    }
    public long getCurrentHash(){
        return currentHash;
    }
    
    //es crida despres de fer un moviment
    public long getHashCodeUpdated(int x, int y, int preMovement, int colorStoneSeted){
        long newHash = zh.updateHash(currentHash, x, y, this.getPos(x, y)+1, colorStoneSeted);
        return newHash;
        
        /*
        int n = this.getSize();
        long[][][] zobrist = new long[n][n][3];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0; k < n; k++){
                    zobrist[i][j][k] = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
                }    
            }
        }
        long hash = 0;
        
        int valor = this.getPos(x, y);
        hash ^= zobrist[x][y][valor+1];
        return hash;
        */
    }
    
}
