package cz.cvut.fel.pjv.codenames.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    final static String NAME_FILE = "src/main/resources/cz/cvut/fel/pjv/codenames/Names.dck";
    private ArrayList<ArrayList<Card>> cards;
    private ArrayList<String> wordBuffer;

    public Deck() {
        initWordBuffer();
        Collections.shuffle(wordBuffer);
        cards = buildDeck(wordBuffer);
    }

   public Deck (ArrayList<String> loadedCardNames){
       cards =  buildDeck(loadedCardNames);
   }

   private void initWordBuffer()    {
        ArrayList<String> tempbuf = new ArrayList<String>();
       try (BufferedReader br = new BufferedReader(new FileReader(NAME_FILE))) {
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

    public ArrayList<ArrayList<Card>> getCards() {return cards;}

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
