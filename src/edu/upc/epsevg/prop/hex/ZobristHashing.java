import java.util.Random;

public class ZobristHashing {
    private long[][][] zobristTable; // Tabla Zobrist: [fila][columna][estado]
    private int boardSize;

    public ZobristHashing(int boardSize, int numStates) {
        this.boardSize = boardSize;
        zobristTable = new long[boardSize][boardSize][numStates];
        initializeZobristTable();
    }

    // Genera números aleatorios para cada celda y estado
    private void initializeZobristTable() {
        Random random = new Random();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int k = 0; k < zobristTable[i][j].length; k++) {
                    zobristTable[i][j][k] = random.nextLong();
                }
            }
        }
    }

    // Calcula el hash inicial para un tablero dado
    public long calculateInitialHash(int[][] board) {
        long hash = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) { // Si no está vacío
                    hash ^= zobristTable[i][j][board[i][j]];
                }
            }
        }
        return hash;
    }

    // Actualiza el hash tras un movimiento
    public long updateHash(long currentHash, int x, int y, int oldState, int newState) {
        if (oldState != 0) {
            currentHash ^= zobristTable[x][y][oldState];
        }
        if (newState != 0) {
            currentHash ^= zobristTable[x][y][newState];
        }
        return currentHash;
    }
}
