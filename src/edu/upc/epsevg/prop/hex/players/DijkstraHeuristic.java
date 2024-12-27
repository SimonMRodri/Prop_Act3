
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
        //return (calculateShortestPath(t, -colorOfPlayer) - calculateShortestPath(t, colorOfPlayer));
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
        
        if (color == 1) { // Jugador conecta de arriba a abajo
            for (int i = 0; i < n; i++) {
                if (t.getPos(0, i) != -1) { // No bloqueado por el oponente
                    distances[0][i] = (t.getPos(0, i) == -1) ? 0 : 1;
                    pq.add(new PointDistance(new Point(0, i), distances[0][i]));
                }
            }
        } else { // Jugador conecta de izquierda a derecha
            for (int i = 0; i < n; i++) {
                if (t.getPos(i, 0) != 1) { // No bloqueado por el oponente
                    distances[i][0] = (t.getPos(i, 0) == 1) ? 0 : 1;
                    pq.add(new PointDistance(new Point(i, 0), distances[i][0]));
                }
            }
        }
        /*
        Un punt per sobre del tauler que estigui conectat a tots així nomes el tirem una vegada.
        if (0, n-1) llavors si miro la direccio 0, -1 la estic mirant per qualsevol
        */
        // Dijkstra
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        while (!pq.isEmpty()) {
            PointDistance current = pq.poll();
            Point p = current.point;

            if (visited[p.x][p.y]) continue;
            visited[p.x][p.y] = true;
            //Ha d'haver en algun moment un if t.getPos(i+1, j) == -1 and t.getPos(i, j+1) == -1 no pots passar
            for (int[] dir : directions) {
                int nx = p.x + dir[0], ny = p.y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < n && ny < n && !visited[nx][ny]) {
                    int cost = (t.getPos(nx, ny) == color) ? 0 : 1;
                    if (t.getPos(nx, ny) == -color){
                        continue; // Bloqueado por el oponente
                    } 
                    if (distances[p.x][p.y] + cost < distances[nx][ny]) {
                        distances[nx][ny] = distances[p.x][p.y] + cost;
                        pq.add(new PointDistance(new Point(nx, ny), distances[nx][ny]));
                    }
                }
            }
        }//add out to hashtable.contains

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
