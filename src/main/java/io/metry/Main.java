package io.metry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length < 2) throw new IllegalArgumentException("Board and letters file paths need to provided as arguments");
        Board board = buildBoard(args[0], args[1]);
        System.out.println(new Scorer(board).calculateScores());
        System.out.println(new Scorer(board).calculateWinner());
    }

    //I have to parse through the json nodes because the input looks a little bit non-standard. It could have been a list of objects.
    private static Board buildBoard(String boardFilePath, String lettersFilePath) throws IOException {
        Map<Character, Integer> lettersWithValue = buildLettersMap(lettersFilePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node =  objectMapper.readTree(new File(boardFilePath));
        Iterator<JsonNode> iterator = node.elements();
        Board board = new Board();
        while (iterator.hasNext()){
            JsonNode squareNode = iterator.next();
            Iterator<JsonNode> squareNodeIterator = squareNode.iterator();
            int index = 0;
            int xCoordinate = 0, yCoordinate = 0;
            char letter = 0;
            Set<String> players = new HashSet<>();
            while (squareNodeIterator.hasNext()) {
                JsonNode jsonNode = squareNodeIterator.next();
                switch (index) {
                    case 0:
                        yCoordinate = jsonNode.asInt();
                        index ++;
                        break;
                    case 1:
                        xCoordinate = jsonNode.asInt();
                        index ++;
                        break;
                    case 2:
                        letter = jsonNode.textValue().charAt(0);
                        index ++;
                        break;
                    case 3:
                        jsonNode.elements().forEachRemaining(playersNode -> {
                            players.add(playersNode.textValue());
                        });
                        index ++;
                        break;
                    default:
                        break;
                }
            }
            board.placeTile(new Tile(letter, lettersWithValue.get(letter), players), xCoordinate - 1, yCoordinate - 1);
        }
        return board;
    }




    //I have to parse through the json nodes because the input looks a little bit non-standard. It could have been a Map of Character and Integer.
    private static Map<Character, Integer> buildLettersMap(String lettersFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node =  objectMapper.readTree(new File(lettersFilePath));
        Iterator<JsonNode> iterator = node.elements();
        Map<Character, Integer> letters = new HashMap<>();
        while (iterator.hasNext()){
            JsonNode jsonNode = iterator.next();
            int value = jsonNode.get("value").intValue();
            jsonNode.get("letters").elements().forEachRemaining(letterNode -> letters.put(letterNode.textValue().charAt(0), value));
        }
        return letters;
    }


}
