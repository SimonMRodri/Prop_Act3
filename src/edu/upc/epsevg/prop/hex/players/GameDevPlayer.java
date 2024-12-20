/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.hex.players;

import edu.upc.epsevg.prop.hex.HexGameStatus;
import edu.upc.epsevg.prop.hex.IAuto;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.MyStatus;
import edu.upc.epsevg.prop.hex.PlayerMove;
import edu.upc.epsevg.prop.hex.SearchType;
import java.awt.Point;
import java.util.Hashtable;

/**
 *
 * @author danip
 */
public class GameDevPlayer implements IPlayer, IAuto{
    private String name;
    private boolean timeOut;
    private int depth;
    private boolean timerOn;
    private int returnedDepth;
    private long numberOfNodesExplored;
    private int colorOfPlayer;
    private Hashtable HTable= new Hashtable<Long, MyStatus>(); 
    
    public GameDevPlayer(String nm, int depthMax, boolean modePlayer){
        name = nm;
        depth = depthMax;
        timerOn = modePlayer;
    }
    
    public int minimax(MyStatus t, int alpha, int beta, boolean maximizingPlayer, int aDepth) {
        numberOfNodesExplored++;
       
        if (t.getMoves().isEmpty() || timeOut || (aDepth == depth && !timerOn)){
            returnedDepth = aDepth;
            //int h = getHeuristica(t);
            //t.setHeuristica(h);
            //HTable.put(t.getHashCodeUpdated(j, k, 1, colorOfPlayer+1), t);
            return getHeuristica(t);
        }
        
        if (maximizingPlayer) { // Si és el torn del jugador maximitzador
            int maxEval = Integer.MIN_VALUE;
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k));
                        //com obtinc el OldState
                        //MyStatus aux = (MyStatus) HTable.get(t2.getCurrentHash()); //null
                        //HTable.put(t2.getHashCodeUpdated(j, k, aux.getPos(j, k)+1, colorOfPlayer+1), t2);//ni puta idea pero maybe te sentit???????
                        int eval = minimax(t2, alpha, beta, false, aDepth+1);
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        t2.setHeuristica(maxEval); //?????? no el podem guardar fora perque necesitem t2 
                        HTable.put(t2.getHashCodeUpdated(j, k, 1, colorOfPlayer+1), t2); // 1 perque anterior ha de ser un cella buit.
                        if (beta <= alpha) break; // Poda alfa-beta
                    }
                }
            }
            return maxEval;
        } else { // Si és el torn del jugador minimitzador
            int minEval = Integer.MAX_VALUE;
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){ 
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k));
                        int eval = minimax(t2, alpha, beta, true, aDepth+1);
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break; // Poda alfa-beta
                    }
                }
            }
            return minEval;
        }
    }
        
    @Override
    public PlayerMove move(HexGameStatus hgs) {
        colorOfPlayer = hgs.getCurrentPlayerColor();
        timeOut = false;
        Point millorMoviment = new Point(-1, -1);
        int millorValor = Integer.MIN_VALUE;
        
        for (int i = 0; i< hgs.getSize(); i++){
            for(int j = 0; j < hgs.getSize(); j++){
                if(hgs.getPos(i, j) == 0){
                    HexGameStatus t = new HexGameStatus(hgs);
                    
                    Point c = new Point(i,j);
                    t.placeStone(c);
                    MyStatus ms = new MyStatus(t.getSize(), t, millorValor);
                    int v = minimax(ms, Integer.MIN_VALUE, Integer.MAX_VALUE, false, 0);
                    ms.setHeuristica(v);
                    HTable.put(ms.currentHash, ms);
                    if (v > millorValor){
                        millorValor = v;
                        millorMoviment = c;
                    }
                }
            }
        }
        return new PlayerMove(millorMoviment, numberOfNodesExplored, returnedDepth, SearchType.MINIMAX);
        
    }
    

    @Override
    public void timeout() {
        if(timerOn){
            timeOut = true;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private int getHeuristica(HexGameStatus t) {
       return DijkstraHeuristic.calculateHeuristic(t, colorOfPlayer);
    }
    
}
