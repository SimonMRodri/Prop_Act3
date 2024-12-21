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
import edu.upc.epsevg.prop.hex.ZobristHashing;
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
    private Hashtable<Long, MyStatus> HTable;
    private ZobristHashing zh;
    
    public GameDevPlayer(String nm, int depthMax, boolean modePlayer, int BS){
        this.HTable = new Hashtable<>();
        name = nm;
        depth = depthMax;
        timerOn = modePlayer;
        zh = new ZobristHashing(BS, 3); // aquesta inicialitzacio esta bé
        //ZobristTable es fa una i punt.
    }
    
    public int minimax(MyStatus t, int alpha, int beta, boolean maximizingPlayer, int aDepth) {
        numberOfNodesExplored++;
       
        if (t.getMoves().isEmpty() || timeOut || (aDepth == depth && !timerOn)){
            returnedDepth = aDepth;
            return getHeuristica(t);
        } 
        /*
        int h = getHeur(t);
        t.setHeur(h);
        HTable.put(t.currentHash, t);
        */
        
        
        if (maximizingPlayer) { // Si és el torn del jugador maximitzador
            int maxEval = Integer.MIN_VALUE;
            /*
            Posarlo aqui es com fer un recursive leaf
            Crec que he de mirar-ho abans la crida recursiva amb el fill generat.
            if (HTable.containsKey(t.currentHash)){
                return HTable.get(t.currentHash).getHeur();
            }
            */
                        // Si segueix sense funcionar placeStone tornar a posar el 
                        // MyStatus t2 = new MyStatus(t); dins del bucle.
                        // O posar un check perque no posi fora dels limits.
            Point bestMov = new Point(0,0);
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k));
                        long auxHash = zh.updateHash(t2.currentHash, j, k, 0+1, t2.getCurrentPlayerColor()+1);//o getPos(j,k)+1 pero crec que amb color es mes rapid.
                        if (HTable.containsKey(auxHash)){
                            return HTable.get(auxHash).getHeur();
                            //si ja ha vist el tauler retrona la seva heuristica que es de les millors
                        }
                        else{
                            t2.currentHash = auxHash;
                        }

                        //OldState no sera sempre 1 ja que posem una pedra en una cella buida?
                        //MyStatus aux = HTable.get(t2.getCurrentHash(); //null
                        //HTable.put(zh.updateHash(t2.currentHash, j, k, 1, colorOfPlayer+1), colorOfPlayer+1), t2);//ni puta idea pero maybe te sentit???????
                        int eval = minimax(t2, alpha, beta, false, aDepth+1);
                        if (eval > maxEval){
                            maxEval = eval;
                            bestMov = new Point(j,k); 
                        }
                        //maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break; // Poda alfa-beta
                    }
                }
            }
            
            t.setHeur(maxEval);
            t.placeStone(bestMov);
            t.currentHash = zh.updateHash(t.currentHash, bestMov.x, bestMov.y, t.getPos(bestMov)+1, t.getCurrentPlayerColor()+1);
            HTable.put(t.currentHash, t);
            
            return maxEval;
        } else { // Si és el torn del jugador minimitzador
            int minEval = Integer.MAX_VALUE;
            /*
            MyStatus t2 = new MyStatus(t);
            long cHash = t2.currentHash;
            HTable.put(cHash, t2); 
            // La posem de forma temporal per poder torna a agafar el tauler 
            // sense crear una copia cada per cada iteració del bucle.
            // Al acabar el bucle la esborrem.
            int newState = t2.getCurrentPlayerColor()+1;
            */
            Point bestMov = new Point(0,0);
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k)); //potser no fa falta el place stone????????? bueno si que fara falta perque no tenim res guardat
                        long auxHash = zh.updateHash(t2.currentHash, j, k, 0+1, t2.getCurrentPlayerColor()+1);
                        if (HTable.containsKey(auxHash)){
                            return HTable.get(auxHash).getHeur();
                        }
                        else{
                            t2.currentHash = auxHash;
                        }

                        int eval = minimax(t2, alpha, beta, true, aDepth+1);
                        if (eval<minEval){
                            minEval = eval;
                            bestMov = new Point(j,k);
                        }
                        //minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break; // Poda alfa-beta
                    }
                }
            }
            /*
            t.setHeur(minEval); 
            t.placeStone(bestMov);
            t.currentHash = zh.updateHash(t.currentHash, bestMov.x, bestMov.y, t.getPos(bestMov)+1, t.getCurrentPlayerColor()+1);
            HTable.put(t.currentHash, t);
            */
            return minEval;
        }
    }
        
    @Override
    public PlayerMove move(HexGameStatus hgs) {
        colorOfPlayer = hgs.getCurrentPlayerColor();
        timeOut = false;
        int size = hgs.getSize();
        
        
        int[][] board = new int[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = hgs.getPos(x, y)+1;
            }
        }
        long currentHash = zh.calculateInitialHash(board);
        
        Point millorMoviment = new Point(-1, -1);
        int millorValor = Integer.MIN_VALUE;
        MyStatus ms = new MyStatus(hgs.getSize(), hgs, millorValor, currentHash);
        for (int i = 0; i< hgs.getSize(); i++){
            for(int j = 0; j < hgs.getSize(); j++){
                if(hgs.getPos(i, j) == 0){
                    HexGameStatus t = new HexGameStatus(hgs);
                    
                    Point c = new Point(i,j);
                    t.placeStone(c);
                    ms.currentHash = zh.updateHash(currentHash, i, j, 0+1, t.getCurrentPlayerColor()+1);
                    
                    int v = minimax(ms, Integer.MIN_VALUE, Integer.MAX_VALUE, false, 0);
                    //MyStatus ms = new MyStatus(hgs.getSize(), hgs, millorValor, currentHash);
                    HTable.put(ms.currentHash, ms);
                    if (v > millorValor){
                        millorValor = v;
                        ms.setHeur(v);
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
