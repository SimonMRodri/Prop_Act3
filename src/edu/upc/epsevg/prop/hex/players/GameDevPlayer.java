/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.hex.players;

import edu.upc.epsevg.prop.hex.HexGameStatus;
import edu.upc.epsevg.prop.hex.IAuto;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.PlayerMove;
import edu.upc.epsevg.prop.hex.SearchType;
import java.awt.Point;

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
    
    public GameDevPlayer(String nm, int depthMax, boolean modePlayer){
        name = nm;
        depth = depthMax;
        timerOn = modePlayer;
    }
    public int minimax(HexGameStatus t, int alpha, int beta, boolean maximizingPlayer, Point pos, int aDepth) {
        int color = t.getCurrentPlayerColor();
        numberOfNodesExplored++;
        
        if (t.getMoves().isEmpty() || timeOut || (aDepth == depth && !timerOn)){
            returnedDepth = aDepth;
            return getHeuristica(t);
        }
        
        if (maximizingPlayer) { // Si és el torn del jugador maximitzador
            int maxEval = Integer.MIN_VALUE;
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    HexGameStatus t2 = new HexGameStatus(t);
                    if (t.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k));
                        int eval = minimax(t2, alpha, beta, false, new Point(j, k), aDepth+1);
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break; // Poda alfa-beta
                    }
                }
            }
            return maxEval;
        } else { // Si és el torn del jugador minimitzador
            int minEval = Integer.MAX_VALUE;
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){ 
                    HexGameStatus t2 = new HexGameStatus(t);
                    if (t.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k));
                        int eval = minimax(t2, alpha, beta, true, new Point(j,k), aDepth+1);
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
        timeOut = false;
        Point millorMoviment = new Point(1, 1);
        int millorValor = Integer.MIN_VALUE;

        for (int i = 0; i< hgs.getSize(); i++){
            for(int j = 0; j < hgs.getSize(); j++){
                if(hgs.getPos(i, j) == 0){
                    HexGameStatus t = new HexGameStatus(hgs);
                    int v = minimax(t, millorValor, Integer.MAX_VALUE, false, new Point(i, j), 0);
                    if (v > millorValor){
                        millorValor = v;
                        millorMoviment = new Point(i, j);
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
        return 1;
    }
    
}
