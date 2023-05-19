package cz.cvut.fel.pjv.codenames.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Key implements Serializable {
    public enum KeyType{
        RED,
        BLUE,
        ASSASSIN,
        CIVILIAN,
        EMPTY
    }
    private int[] RBCACardCountRStarts = {9,8,7,1};
    private int[] RBCACardCountBStarts = {8,9,7,1};
    private ArrayList<ArrayList<KeyType>> solution;

    private KeyType[][] test = {
            {KeyType.RED, KeyType.RED, KeyType.RED, KeyType.RED, KeyType.RED},
            {KeyType.RED, KeyType.RED, KeyType.RED, KeyType.RED, KeyType.BLUE},
            {KeyType.BLUE, KeyType.BLUE, KeyType.BLUE, KeyType.BLUE, KeyType.BLUE},
            {KeyType.BLUE, KeyType.BLUE, KeyType.ASSASSIN, KeyType.CIVILIAN, KeyType.CIVILIAN},
            {KeyType.CIVILIAN, KeyType.CIVILIAN, KeyType.CIVILIAN, KeyType.CIVILIAN, KeyType.CIVILIAN}
    };

    public Key(Player.PlayerTeam startingTeam)    {

//        ArrayList<ArrayList<KeyType>> testArray = new ArrayList<ArrayList<KeyType>>();
//        //for testing purposes
//        for (KeyType[] row : test) {
//            ArrayList<KeyType> arrayListRow = new ArrayList<>(Arrays.asList(row));
//            testArray.add(arrayListRow);
//        }
//        solution=testArray;
        //for game
        solution = generateKey(startingTeam);
    }
    public ArrayList<ArrayList<KeyType>> getSolution() {
        return solution;
    }

    private ArrayList<ArrayList<KeyType>> generateKey(Player.PlayerTeam startingTeam)   {
        List<KeyType> generatedSolutionUnmod = new ArrayList<>();
        int[] CardTypeCounts;

        if (startingTeam == Player.PlayerTeam.RED)
            CardTypeCounts = RBCACardCountRStarts;
        else
            CardTypeCounts = RBCACardCountBStarts;

        for (int i = 0; i < CardTypeCounts[0]; i++) {
            generatedSolutionUnmod.add(KeyType.RED);
        }
        for (int i = 0; i < CardTypeCounts[1]; i++) {
            generatedSolutionUnmod.add(KeyType.BLUE);
        }
        for (int i = 0; i < CardTypeCounts[2]; i++) {
            generatedSolutionUnmod.add(KeyType.CIVILIAN);
        }
        generatedSolutionUnmod.add(KeyType.ASSASSIN);
        // Shuffle the available KeyTypes
        Collections.shuffle(generatedSolutionUnmod);

        return convert1Dto2D(generatedSolutionUnmod);
    }

    private ArrayList<ArrayList<KeyType>> convert1Dto2D(List<KeyType> array)   {
        ArrayList<ArrayList<KeyType>> result = new ArrayList<>();

        int rows = 5;
        int cols = 5;
        for (int row = 0; row < rows; row++) {
            ArrayList<KeyType> rowList = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                int index = (row * cols) + col;
                rowList.add(array.get(index));
            }
            result.add(rowList);
        }
        return result;
    }


}
