package edu.upc.epsevg.prop.hex;

import edu.upc.epsevg.prop.hex.players.HumanPlayer;
import edu.upc.epsevg.prop.hex.players.RandomPlayer;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.players.GameDevPlayer;
import edu.upc.epsevg.prop.hex.players.H_E_X_Player;



import javax.swing.SwingUtilities;

/**
 * Checkers: el joc de taula.
 * @author bernat
 */
public class Game {
        /**
     * @param args
     */
    public static void main(String[] args) { 
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int boardSize = 4;
                IPlayer player1 = new HumanPlayer("Human");
                
                IPlayer player2 = new GameDevPlayer("GameDev", 5, true, boardSize);
                        //H_E_X_Player(2/*GB*/);
                        //HumanPlayer("Human");
                        //RandomPlayer("SISI");
                        
                                
                new Board(player1 , player2, boardSize /*mida*/,  10/*s*/, false);
             }
        });
    }
}
