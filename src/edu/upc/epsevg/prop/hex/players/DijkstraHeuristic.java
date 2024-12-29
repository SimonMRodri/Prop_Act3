
package edu.upc.epsevg.prop.hex.players;
import edu.upc.epsevg.prop.hex.HexGameStatus;
import java.awt.Point;
import java.util.PriorityQueue;


/**
 * Aquesta clase ens serveix per a poder calcular el camí més curt
 * d'una punta a l'altre d'un tauler de Hex per a un jugador donat.
 */
public class DijkstraHeuristic {
    
    /**
     * Funció per a obtenir l'heurística en base a l'algorisme de Dijkstra
     * 
     * @param t Tauler del que es vol saber l'heurística
     * @param colorOfPlayer Color del jugador pel que es vol saber el camí més curt
     * @return La diferència dels camins dels dos jugadors
     */
    public static int calculateHeuristic(HexGameStatus t, int colorOfPlayer) {
        int playerColor = colorOfPlayer;
        int opponentColor = -colorOfPlayer;

        int playerScore = calculateShortestPath(t, playerColor);
        int opponentScore = calculateShortestPath(t, opponentColor);

        return opponentScore - playerScore;
    }

    /**
     * Funció per a calcular el camí més curt d'una punta d'un tauler de Hex a l'altre per a un color donat
     * 
     * @param t El tauler del que es vol saber el camí més curt
     * @param color El jugador pel que es vol saber el camí més curt
     * @return El camí més curt per al jugador en el tauler
     */
    public static int calculateShortestPath(HexGameStatus t, int color) {
        int n = t.getSize();
        int[][] distances = new int[n][n];
        boolean[][] visited = new boolean[n][n];
        PriorityQueue<PointDistance> pq = new PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        if (color == 1) {
            for (int i = 0; i < n; i++) {
                if (t.getPos(0, i) != -1) {
                    distances[0][i] = (t.getPos(0, i) == -1) ? 0 : 1;
                    pq.add(new PointDistance(new Point(0, i), distances[0][i]));
                }
            }
        } else { 
            for (int i = 0; i < n; i++) {
                if (t.getPos(i, 0) != 1) {
                    distances[i][0] = (t.getPos(i, 0) == 1) ? 0 : 1;
                    pq.add(new PointDistance(new Point(i, 0), distances[i][0]));
                }
            }
        }
        
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
                    if (t.getPos(nx, ny) == -color){
                        continue;
                    } 
                    if (distances[p.x][p.y] + cost < distances[nx][ny]) {
                        distances[nx][ny] = distances[p.x][p.y] + cost;
                        pq.add(new PointDistance(new Point(nx, ny), distances[nx][ny]));
                    }
                }
            }
        }
        int minDistance = Integer.MAX_VALUE;
        if (color == 1) {
            for (int i = 0; i < n; i++) {
                minDistance = Math.min(minDistance, distances[n - 1][i]);
            }
        } else {
            for (int i = 0; i < n; i++) {
                minDistance = Math.min(minDistance, distances[i][n - 1]);
            }
        }
        return minDistance;
    }

   
    /**
     * Aquesta clase ens serveix per a definir punts amb distàncies per
     * a facilitar la tasca de trobar el camí més curt usant Dijkstra.
     */
    static class PointDistance implements Comparable<PointDistance> {
        Point point;
        int distance;

        /**
         * Constructora de la classe PointDistance
         * @param p El punt al que es vol assignar una distància
         * @param d La distància
         */
        PointDistance(Point p, int d) {
            this.point = p;
            this.distance = d;
        }
        
        /**
         * Funció per a comparar les distàncies del punt implícit i un donat
         * @param o El punt amb el que es vol comparar la distància propia
         * @return Un valor que ens indica si els valor son iguals o diferents
         */
        @Override
        public int compareTo(PointDistance o) {
            return Integer.compare(this.distance, o.distance);
        }
    }
}
