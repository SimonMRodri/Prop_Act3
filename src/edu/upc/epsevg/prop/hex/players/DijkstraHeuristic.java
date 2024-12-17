
package edu.upc.epsevg.prop.hex.players;
import edu.upc.epsevg.prop.hex.HexGameStatus;
import java.awt.Point;
import java.util.PriorityQueue;

public class DijkstraHeuristic {
    
    public static int calculateHeuristic(HexGameStatus t, int colorOfPlayer) {
        int playerColor = colorOfPlayer;
        int opponentColor = -colorOfPlayer;

        int playerScore = calculateShortestPath(t, playerColor);
        int opponentScore = calculateShortestPath(t, opponentColor);

        // La heurística será una diferencia entre el puntaje del oponente y del jugador.
        return opponentScore - playerScore;
    }

    private static int calculateShortestPath(HexGameStatus t, int color) {
        int n = t.getSize();
        int[][] distances = new int[n][n];
        boolean[][] visited = new boolean[n][n];
        PriorityQueue<PointDistance> pq = new PriorityQueue<>();

        // Inicializar distancias con un valor grande
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        // Agregar puntos iniciales (borde superior o izquierdo según el color)
        for (int i = 0; i < n; i++) {
            if (color == 1) { // Jugador conecta de arriba a abajo
                if (t.getPos(0, i) != -1) { // No bloqueado por el oponente
                    distances[0][i] = (t.getPos(0, i) == -1) ? 0 : 1;
                    pq.add(new PointDistance(new Point(0, i), distances[0][i]));
                }
            } else { // Jugador conecta de izquierda a derecha
                if (t.getPos(i, 0) != 1) { // No bloqueado por el oponente
                    distances[i][0] = (t.getPos(i, 0) == 1) ? 0 : 1;
                    pq.add(new PointDistance(new Point(i, 0), distances[i][0]));
                }
            }
        }

        // Dijkstra
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        while (!pq.isEmpty()) {
            PointDistance current = pq.poll();
            Point p = current.point;

            if (visited[p.x][p.y]) continue;
            visited[p.x][p.y] = true;

            for (int[] dir : directions) {
                int nx = p.x + dir[0], ny = p.y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < n && ny < n && !visited[nx][ny]) {
                    int cost = (t.getPos(nx, ny) == color) ? 0 : 1;
                    if (t.getPos(nx, ny) == -color) continue; // Bloqueado por el oponente
                    if (distances[p.x][p.y] + cost < distances[nx][ny]) {
                        distances[nx][ny] = distances[p.x][p.y] + cost;
                        pq.add(new PointDistance(new Point(nx, ny), distances[nx][ny]));
                    }
                }
            }
        }

        // Buscar la distancia mínima a los bordes opuestos
        int minDistance = Integer.MAX_VALUE;
        if (color == 1) { // Arriba a abajo
            for (int i = 0; i < n; i++) {
                minDistance = Math.min(minDistance, distances[n - 1][i]);
            }
        } else { // Izquierda a derecha
            for (int i = 0; i < n; i++) {
                minDistance = Math.min(minDistance, distances[i][n - 1]);
            }
        }
        return minDistance;
    }

    static class PointDistance implements Comparable<PointDistance> {
        Point point;
        int distance;

        PointDistance(Point p, int d) {
            this.point = p;
            this.distance = d;
        }

        @Override
        public int compareTo(PointDistance o) {
            return Integer.compare(this.distance, o.distance);
        }
    }
}
