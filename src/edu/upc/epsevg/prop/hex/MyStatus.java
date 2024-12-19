
package edu.upc.epsevg.prop.hex;

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
    
}
