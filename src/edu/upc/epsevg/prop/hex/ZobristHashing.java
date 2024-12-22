package edu.upc.epsevg.prop.hex;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        //Random random = new Random();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int k = 0; k < zobristTable[i][j].length; k++) {
                    zobristTable[i][j][k] = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
                    //ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
                    //random.nextLong();
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
    /*
    Ara ho entenc. 
    Si el oldState es 0 o 2, enemic o jo, fes XOR per tornar al tauler original, 
    llavors si newState es 0 o 2, enemic o jo, fes XOR per calcular el hash del nou moviment.
    Aquesta es una opcio de la funció.
    Si oldState es buit no facis res, si newState no es buit calcula nou Hash.
    Si oldState no es buit, retorna al tauler original. Si newState es buit no facis res.
    I si oldState i newState son buits retorna el currentHash sense canvis.
    */
    /*
    Aquesta funcio agafa el Hash d'un tauler i li fa XOR amb oldState si no es buit 
    obtenint el Hash de la taula amb el moviment anterior/abans de fer el moviment????
    Sino, agafa el Hash de la taula actual li fa XOR del NewState obtenint la taula del moviment fet. 
    Es te en compte que si cap es buit estic fent XOR del oldState i despres del NewState 
    La major pregunta es quan la crido??
    Quan faci el moviment i li dic a la taula el teu nou hash es igual a la funcio amb el teu hash
    el estat abans de fer el moviment i color del jugador que ha fet el moviment???.
    */
    public long getAux(long currentHash, long nextMov){
        long aux = currentHash ^ nextMov;
        return aux; 
        //si torno a crida aquesta funcio posant el aux com a currentHash i el nextMov igual 
        //puc tornar a obtenir el currentHash Anterior, el tauler abans de fer el moviment.
    }
}
