package Modules;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CoinGame  {



    static int[][] table;
    static int[][] usedCoins;
    static int max, min;

    // Returns optimal value possible
    // that a player can collect from
    // an array of coins of size n.
    // Note than n must be even
    static int  optimalStrategyOfGame(int[] arr, int n) {
        // Create a table to store
        // solutions of subproblems
        table = new int[n][n];
        int gap, i, j, x, y, z;

        // Fill table using above recursive formula.
        // Note that the table is filled in diagonal
        // fashion (similar to http:// goo.gl/PQqoS),
        // from diagonal elements to table[0][n-1]
        // which is the result.
        for (gap = 0; gap < n; ++gap) {
            for (i = 0, j = gap; j < n; ++i, ++j) {

                // Here x is value of F(i+2, j),
                // y is F(i+1, j-1) and z is
                // F(i, j-2) in above recursive formula
                x = ((i + 2) <= j) ? table[i + 2][j] : 0;

                y = ((i + 1) <= (j - 1)) ? table[i + 1][j - 1] : 0;

                z = (i <= (j - 2)) ? table[i][j - 2] : 0;

                table[i][j] = Math.max(arr[i] + Math.min(x, y), arr[j] + Math.min(y, z));
//               usedCoins[i][0] = Math.max(arr[i] + Math.min(x, y), arr[j] + Math.min(y, z));

                if(arr[i] + Math.min(x, y) >  arr[j] + Math.min(y, z))
                    usedCoins[i][0] =  arr[i];
                else
                    usedCoins[i][0] =  arr[j];


                if (arr[i] + Math.min(x, y) > arr[j] + Math.min(y, z)) {
                    max = arr[i];
                    min = Math.min(x, y);

                } else {
                    max = arr[j];
                    min = Math.min(y, z);
                }

//                if (i == n - 1)
//                    System.out.print("  " + table[i][j] + "\n");
//                else {
//                    System.out.print("  " + table[i][j]);
//
//                }

            }
        }
//        for (i = 0; i < table.length; i++) {
//            System.out.println();
//            for (j = 0; j < table[i].length; j++) {
//                System.out.print(table[i][j] + " ");
//            }
//        }
        return table[0][n - 1];

    }

    static int[][] getTable() {
        return table;

    }

    static int[][] getUsedCoins() {
        return usedCoins;

    }
}




