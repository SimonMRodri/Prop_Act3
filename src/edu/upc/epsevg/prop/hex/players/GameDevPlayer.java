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
 * Implementació del jugador GameDevPlayer
 * @author DanielFiSimonM
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
    private long bestHash;
    private int[][] weightMatrix;
    
    
    /**
     * Constructora del jugador
     * 
     * @param nm El nom del jugador creat
     * @param depthMax La profunditat màxima a la que arribarà el minimax
     * @param modePlayer Indicador per saber si va per TimeOut o per IDS
     * @param BS Grandària del tauler
     * @param pColor Indica si es el jugador 1 o el 2
     */
    public GameDevPlayer(String nm, int depthMax, boolean modePlayer, int BS, int pColor){
        this.HTable = new Hashtable<>();
        name = nm;
        depth = depthMax;
        timerOn = modePlayer;
        zh = new ZobristHashing(BS, 3); 
        createWeightMatrix(BS, pColor);
    }
    
    /**
     * Funció per generar una matriu de pes per donar prioritat a les caselles
     * centrals del tauler.
     * 
     * @param size Grandària del tauler
     * @param c Color del jugador propi
     */
    public void createWeightMatrix(int size, int c){
        int[][] M = new int[size][size];
        for (int i = 0; i< size; i++){
            for(int j = 0; j < size; j++){
                M[i][j] = (size/(Math.abs(i-size/2)+ Math.abs(j-size/2)+1));
                if(c != 1){
                    if(i != size/2 && j == size/2) M[i][j] = 1;
                }
                else{
                    if(i == size/2 && j != size/2) M[i][j] = 1;
                }
            }
        }      
        for (int i = 0; i< size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(M[i][j]+ " ");
            }
            System.out.println();
        }
        weightMatrix = M;
        
    }

    
    /**
     * Funció MiniMax per trobar la jugada més adecuada
     * 
     * @param t Tauler actual
     * @param alpha Variable alpha per al MiniMax
     * @param beta Variable beta per al MiniMax
     * @param maximizingPlayer Indicador per saber si estem maximitzant o minimitzant el jugador.
     * @param aDepth Profunditat actual a l'arbre
     * @return Retorna el valor del millor moviment
     */
    public int minimax(MyStatus t, int alpha, int beta, boolean maximizingPlayer, int aDepth) {
        numberOfNodesExplored++;
        if (t.getMoves().isEmpty() || timeOut ||  aDepth == depth && !timerOn){
            if (returnedDepth< aDepth) returnedDepth = aDepth;
            int h = getHeuristica(t);
            t.setHeur(h);
            return h;
        }              
        if (maximizingPlayer) { 
            int maxEval = Integer.MIN_VALUE;
            Point bestMov = new Point(-1, -1);
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k));
                        long auxHash = zh.updateHash(t2.currentHash, j, k, 1, t2.getPos(j, k)+1);//o getPos(j,k)+1 pero crec que amb color es mes rapid.
                        if (HTable.containsKey(auxHash)){
                            returnedDepth = aDepth;
                            return HTable.get(auxHash).getHeur();
                        }
                        else{
                            t2.currentHash = auxHash;
                        }
                        int eval = minimax(t2, alpha, beta, false, aDepth+1) ;
                        if (eval > maxEval){
                            maxEval = eval;
                            bestMov = new Point(j,k); 
                        }
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha){
                            break;
                        }
                    }
                }
            }
            if (bestMov != new Point(-1, -1)){
                MyStatus t3 = new MyStatus(t);
                int pos = t3.getPos(bestMov)+1;
                t3.setHeur(maxEval);
                
                t3.placeStone(bestMov);
                t3.currentHash = zh.updateHash(t3.currentHash, bestMov.x, bestMov.y, pos, t3.getPos(bestMov)+1); 
                HTable.put(t3.currentHash, t3);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            Point bestMov = new Point(-1, -1); 
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k)); 
                        long auxHash = zh.updateHash(t2.currentHash, j, k, 1, t2.getPos(j,k)+1);
                        if (HTable.containsKey(auxHash)){
                            returnedDepth = aDepth;
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
                        beta = Math.min(beta, eval);
                        if (beta <= alpha){
                            break;
                        } 
                    }
                }
            }
                       
            if (bestMov != new Point(-1, -1)){
                MyStatus t3 = new MyStatus(t);
                int pos = t3.getPos(bestMov)+1;
                t3.setHeur(minEval); 
                t3.placeStone(bestMov);
                t3.currentHash = zh.updateHash(t3.currentHash, bestMov.x, bestMov.y, pos, t3.getPos(bestMov)+1);
                HTable.put(t3.currentHash, t3);
            }            
            return minEval;
        }
    }
        
    /**
     * Busca el millor moviment possible amb el tauler actual i actualitza la taula hash
     * 
     * @param hgs El tauler actual
     * @return El millor moviment trobat
     */
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
        
        for (int i = 0; i< hgs.getSize(); i++){
            for(int j = 0; j < hgs.getSize(); j++){
                if(hgs.getPos(i, j) == 0){
                    
                    MyStatus ms = new MyStatus(hgs, millorValor, currentHash);
                    Point c = new Point(i,j);
                    ms.placeStone(c);
                    ms.currentHash = zh.updateHash(currentHash, i, j, 0+1, ms.getPos(i, j)+1);
                    
                    int v = minimax(ms, Integer.MIN_VALUE, Integer.MAX_VALUE, false, 0) + weightMatrix[i][j];
                    HTable.put(ms.currentHash, ms);
                    if (v > millorValor){
                        millorValor = v;
                        ms.setHeur(v);
                        millorMoviment = c;
                    }
                }
            }
        }        
        HexGameStatus t = new HexGameStatus(hgs);
        t.placeStone(millorMoviment);
        return new PlayerMove(millorMoviment, numberOfNodesExplored, returnedDepth, SearchType.MINIMAX);
    }
    
    
    /**
     * Aquesta funció s'activa quan s'acaba el temps del jugador per escollir el moviment
     */
    @Override
    public void timeout() {
        if(timerOn){
            timeOut = true;
        }
    }

    /**
     * Funció per a obtenir el nom del jugador
     * 
     * @return un string amb el nom del jugador
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Funció per a obtenir la heurística donat un tauler
     * 
     * @param t El tauler del que es vol saber l'heurística
     * @return L'heurística obtinguda
     */
    public int getHeuristica(HexGameStatus t) {
       return DijkstraHeuristic.calculateHeuristic(t, colorOfPlayer);
    }
    
}
