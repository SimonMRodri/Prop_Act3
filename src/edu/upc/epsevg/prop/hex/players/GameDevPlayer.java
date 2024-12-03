/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.hex.players;

import edu.upc.epsevg.prop.hex.HexGameStatus;
import edu.upc.epsevg.prop.hex.IAuto;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.PlayerMove;

/**
 *
 * @author danip
 */
public class GameDevPlayer implements IPlayer, IAuto{

    
        public int minimax(HexGameStatus t, int depth, int alpha, int beta, boolean maximizingPlayer, int color, int pos) {

        if (maximizingPlayer) { // Si és el torn del jugador maximitzador
            int maxEval = Integer.MIN_VALUE;
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    HexGameStatus t2 = new HexGameStatus(t);
                    if (t2.movpossible(j)) {
                        t2.afegeix(j, color);
                        int eval = minimax(t2, depth + 1, alpha, beta, false, color, j);
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
                    if (t2.movpossible(j)) {
                        t2.afegeix(j, color == 1 ? -1 : 1);
                        int eval = minimax(t2, depth + 1, alpha, beta, true, color, j);
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void timeout() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
