/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.hex;

import edu.upc.epsevg.prop.hex.HexGameStatus;
import edu.upc.epsevg.prop.hex.PlayerType;
import edu.upc.epsevg.prop.hex.players.DijkstraHeuristic;
import edu.upc.epsevg.prop.hex.players.ProfeGameStatus2;
import edu.upc.epsevg.prop.hex.players.ProfeGameStatus3;
import edu.upc.epsevg.prop.hex.players.ProfeGameStatus3.Result;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author bernat
 */
public class UnitTesting {
    
    
    
    public static void main(String[] args) {
    
        List<byte[][]> boards = new ArrayList<>();
        boards.add(new byte[][]{
        //X   0  1  2  3  4
            { 0, 0, 0, 0},                      // 0   Y
              { 0, -1, 1, 1},                    // 1
                { 0, -1, 1, 0},                  // 2
                  { 0, 1, 0, 0}                 // 4  
        
        });
        boards.add(new byte[][]{
        //X   0  1  2  3  4
            { 0, 0, 0, 0},                      // 0   Y
              { 0, -1, 1, 1},                    // 1
                { 0, -1, 1, 0},                  // 2
                  { 0, 1, 0, 0}                 // 4  
        
        });
        boards.add(new byte[][]{
        //X   0  1  2  3  4
            { 0, 0, 0, 0},                      // 0   Y
              { 0, -1, 1, 1},                    // 1
                { 0, -1, 1, 0},                  // 2
                  { 0, 1, 0, 0}                 // 4  
        
        });
        
        for (byte[][] board : boards){
            HexGameStatus gs = new HexGameStatus(board, PlayerType.PLAYER1);        
            int h = DijkstraHeuristic.calculateHeuristic(gs, -1);
            System.out.println("H="+h);
        }
        
 
    }
    
}
