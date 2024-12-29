package edu.upc.epsevg.prop.hex;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe per a implementar el métode de Hashing Zobrist
 */
public class ZobristHashing {
    private long[][][] zobristTable;
    private int boardSize;
    private MyStatus ms; 
    
    /**
     * Constructora de la classe ZobristHashing
     * @param boardSize Grandària del tauler
     * @param numStates Quantitat d'estats de cada cassella
    */
    public ZobristHashing(int boardSize, int numStates) { //, MyStatus hg
        this.boardSize = boardSize;
        zobristTable = new long[boardSize][boardSize][numStates];
        initializeZobristTable();
    }
    
    /**
     * Funció per a inicialitzar la taula Hash
    */
    public void initializeZobristTable() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int k = 0; k < zobristTable[i][j].length; k++) {
                    zobristTable[i][j][k] = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
                }
            }
        }
    }

    /**
     * Funció per a calcular el Hash inicial d'un tauler
     * @param board El tauler per al que es vol calcular el Hash inicial
     * @return 
     */
    public long calculateInitialHash(int[][] board) {
        long hash = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 1) { // Si no está vacío
                    hash ^= zobristTable[i][j][board[i][j]];
                }
            }
        } 
        return hash;
    }

    /** 
     * Funció per a actualitzar el Hash d'un tauler
     * @param currentHash El hash actual del tauler
     * @param x Posició x de la pedra que es coloca
     * @param y Posició y de la pedra que es coloca
     * @param oldState Estat antic de la posició
     * @param newState Nou estat de la posició
     * @return 
    */
    public long updateHash(long currentHash, int x, int y, int oldState, int newState) {
        if (oldState != 1) { 
            currentHash ^= zobristTable[x][y][oldState];
        }
        if (newState != 1) {
            currentHash ^= zobristTable[x][y][newState];
        }
        return currentHash;
    }
    
    /**
     * Calcula un hash auxiliar a partir del Hash actual i el següent moviment
     * @param currentHash Hash actual
     * @param nextMov Següent moviment
     * @return El hash auxiliar
     */
    public long getAux(long currentHash, long nextMov){
        long aux = currentHash ^ nextMov;
        return aux; 
    }
}
