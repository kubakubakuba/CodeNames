package cz.cvut.fel.pjv.codenames.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Deck implements Serializable {

    private String dckFile;
    private ArrayList<ArrayList<Card>> cards;
    private ArrayList<String> wordBuffer;

    /**
     * Constructor
     * @param loadedCardNames card names to use in the game
     */
   public Deck (ArrayList<String> loadedCardNames){
       Collections.shuffle(loadedCardNames);
       cards = buildDeck(loadedCardNames);

   }


   private void initWordBuffer()    {
        ArrayList<String> tempbuf = new ArrayList<String>();
       try (BufferedReader br = new BufferedReader(new FileReader(dckFile))) {
           String line;
           while ((line = br.readLine()) != null) {
               tempbuf.add(line);
           }
           wordBuffer =tempbuf;
       }catch (
               FileNotFoundException e) {
           System.out.println("File not found " + e.getMessage());
       } catch (IOException e) {
           System.out.println(e.getMessage());
       }
   }

    /**
     * Getter for cards
     * @return cards
     */
    public ArrayList<ArrayList<Card>> getCards() {return cards;}

    /**
     * Builds a 2D array of cards from a 1D array of card names
     * @param oneD 1D array of card names
     * @return 2D array of cards
     */
    private ArrayList<ArrayList<Card>> buildDeck(ArrayList<String> oneD){
        ArrayList<ArrayList<Card>> result = new ArrayList<>();
        int rows = 5;
        int cols = 5;
        for (int row = 0; row < rows; row++) {
            ArrayList<Card> rowList = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                int index = (row * cols) + col;
                rowList.add(new Card(oneD.get(index)));
            }
            result.add(rowList);
        }
        return result;
    }
}
