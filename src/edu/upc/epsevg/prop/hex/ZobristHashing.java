package edu.upc.epsevg.prop.hex;
import java.util.Random;

public class ZobristHashing {
    private long[][][] zobristTable; // Tabla Zobrist: [fila][columna][estado]
    private int boardSize;
    private MyStatus ms; 
    //Vale suposo que el zobristHashing l'hem de cridar desde el GameDevPlayer no des del MyStatus.
    public ZobristHashing(int boardSize, int numStates) { //, MyStatus hg
        this.boardSize = boardSize;
        zobristTable = new long[boardSize][boardSize][numStates];
        //ms = hg;
        initializeZobristTable();
    }

    // Genera números aleatorios para cada celda y estado
    private void initializeZobristTable() {
        Random random = new Random();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int k = 0; k < zobristTable[i][j].length; k++) {
                    zobristTable[i][j][k] = random.nextLong();
                    //ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
                }
            }
        }
    }

    // Calcula el hash inicial para un tablero dado
    public long calculateInitialHash(int[][] board) {
        long hash = 0;
        /*
        for (int i = 0; i < ms.getSize(); i++) {
            for (int j = 0; j < ms.getSize(); j++) {
                if (ms.getPos(i, j) != 0) { // Si no está vacío
                    hash ^= zobristTable[i][j][ms.getPos(i, j)+1];
                }
            }
        }
        */
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 1) { // Si no está vacío
                    hash ^= zobristTable[i][j][board[i][j]];
                }
            }
        }
        
        return hash;
    }

    // Actualiza el hash tras un movimiento
    public long updateHash(long currentHash, int x, int y, int oldState, int newState) {
        if (oldState != 1) {  //si aquest es abans de fer el moviment em sembla redundant perque sempre sera 1 que realment es el 0 la cella buida mai pot ser ni 0, enemic, ni 2, fitxa meva.
            currentHash ^= zobristTable[x][y][oldState];
        }
        if (newState != 1) {
            currentHash ^= zobristTable[x][y][newState];
        }
        return currentHash;
    }
}
