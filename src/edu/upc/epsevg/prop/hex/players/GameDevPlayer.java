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
//treure aquests 3 només son per probes
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    private long bestHash;
    private int[][] weightMatrix;
    
    public GameDevPlayer(String nm, int depthMax, boolean modePlayer, int BS){
        this.HTable = new Hashtable<>();
        name = nm;
        depth = depthMax;
        timerOn = modePlayer;
        zh = new ZobristHashing(BS, 3); 
        int c = this.colorOfPlayer;
        createWeightMatrix(BS, c);
    }
    //ZobristTable es fa una i punt.
    public void createWeightMatrix(int size, int c){
        int[][] M = new int[size][size];
        for (int i = 0; i< size; i++){
            for(int j = 0; j < size; j++){
                
                M[i][j] = (size/(Math.abs(i-size/2)+ Math.abs(j-size/2)+1));
                if(i != size/2 && j == size/2) M[i][j] = 1;
                /*
                if (c == 1){
                    if(i != size/2 && j == size/2) M[i][j] = 1;   
                }
                else{
                    if(i != size/2 && j == size/2) M[j][i] = 1;
                }
                */
                //if(i != size/2 && j == size/2) M[i][j] = 1;
                //if (i == j && i != size/2) M[i][j] = 1;
            }
        }
        /*
        M[(size/2)-1][(size/2)] = M[(size/2)-2][(size/2)];
        M[(size/2)+1][(size/2)] = M[(size/2)+2][(size/2)];
        */        
        for (int i = 0; i< size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(M[i][j]+ " ");
            }
            System.out.println();
        }
        weightMatrix = M;
        
    }
    //S'ha de tenir en compte de que la matriu s'agafa al reves.
    
    public int minimax(MyStatus t, int alpha, int beta, boolean maximizingPlayer, int aDepth) {
        numberOfNodesExplored++;
        //System.out.println(aDepth);
        if (t.getMoves().isEmpty() || timeOut ||  aDepth == depth && !timerOn){
            if (returnedDepth< aDepth) returnedDepth = aDepth;
            //System.out.println(aDepth);
            int h = getHeuristica(t);
            t.setHeur(h);
            return h;
        } 
        
        
        
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
                            //si ja ha vist el tauler retrona la seva heuristica que es de les millors
                        }
                        else{
                            t2.currentHash = auxHash;
                        }

                        //OldState no sera sempre 1 ja que posem una pedra en una cella buida?
                        //MyStatus aux = HTable.get(t2.getCurrentHash(); //null
                        //HTable.put(zh.updateHash(t2.currentHash, j, k, 1, colorOfPlayer+1), colorOfPlayer+1), t2);//ni puta idea pero maybe te sentit???????
                        int eval = minimax(t2, alpha, beta, false, aDepth+1) ;
                        if (eval > maxEval){
                            maxEval = eval;
                            bestMov = new Point(j,k); 
                        }
                        //maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha){
                            //System.out.println("break");
                            break; // Poda alfa-beta
                        }
                    }
                }
            }
            if (bestMov != new Point(-1, -1)){
                MyStatus t3 = new MyStatus(t);
                int pos = t3.getPos(bestMov)+1;
                t3.setHeur(maxEval);
                
                t3.placeStone(bestMov);
                t3.currentHash = zh.updateHash(t3.currentHash, bestMov.x, bestMov.y, pos, t3.getPos(bestMov)+1); /////?????????
                HTable.put(t3.currentHash, t3);
            }
            /*
            if (maxEval >= -10 && maxEval <= 10){
            System.out.println(bestMov);
            System.out.println(maxEval);
            }
            */
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
            Point bestMov = new Point(-1, -1); 
            for (int j = t.getSize()-1; j >= 0; j--) {
                for (int k = t.getSize()-1; k>=0; k--){
                    MyStatus t2 = new MyStatus(t);
                    if (t2.getPos(j, k) == 0) {
                        t2.placeStone(new Point(j, k)); //potser no fa falta el place stone????????? bueno si que fara falta perque no tenim res guardat
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
                        
                        //minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha){
                            //System.out.println("break");
                            break;
                        } // Poda alfa-beta
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
        
    @Override
    public PlayerMove move(HexGameStatus hgs) {
        
        colorOfPlayer = hgs.getCurrentPlayerColor();
        timeOut = false;
        int size = hgs.getSize();
        
        //if(!HTable.isEmpty()) hgs = HTable.get(bestHash);

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
                    
                    MyStatus ms = new MyStatus(hgs.getSize(), hgs, millorValor, currentHash);
                    Point c = new Point(i,j);
                    ms.placeStone(c);
                    ms.currentHash = zh.updateHash(currentHash, i, j, 0+1, ms.getPos(i, j)+1);
                    
                    int v = minimax(ms, Integer.MIN_VALUE, Integer.MAX_VALUE, false, 0) + weightMatrix[i][j];// 
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
        System.out.println(returnedDepth);
        
            HexGameStatus t = new HexGameStatus(hgs);
            t.placeStone(millorMoviment);
        if (t.isGameOver()){
            writeSolutionToFile(t);
        }
        return new PlayerMove(millorMoviment, numberOfNodesExplored, returnedDepth, SearchType.MINIMAX);
        
    }
    public void writeSolutionToFile(HexGameStatus hgs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\UNI\\PROP\\Hex\\solucio.txt", true))) {
            writer.write(hgs.toString());
            writer.newLine(); // Nova línia per cada punt
            writer.write(hgs.GetWinner().name());// Escrivim la coordenada en format "x,y"
            writer.newLine(); // Nova línia per cada punt
        } catch (IOException e) {
            e.printStackTrace(); // Gestionem l'excepció
        }
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
