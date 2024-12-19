
package edu.upc.epsevg.prop.hex;

import java.util.concurrent.ThreadLocalRandom;

public class MyStatus extends HexGameStatus {
    
    public HexGameStatus hgs;
    public int heur;
    
    public MyStatus(int i, HexGameStatus hg, int he){
       super(i);
       hgs = hg;
       heur = he;
    }
    
    public HexGameStatus getBoard(){
        return hgs;
    }
    
    public int getHeuristica(){
        return heur;
    }
    
    public long getHashCode(int x, int y){
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
    }
    
}
