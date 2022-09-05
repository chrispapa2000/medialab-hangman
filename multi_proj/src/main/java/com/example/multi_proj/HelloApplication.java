package com.example.multi_proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {
    private static String ret="";
    private static String word="";
    private static char[] found;
    private static HashSet<String> ActiveDict = null;
    private static int Points = 0;
    private static int SucGuesses = 0;
    private static int Guesses = 0;
    private static HashSet<String> HiddenWords;
    private static int mistakes = 0;
    private static float[] possibilities; //probability for each character of the hidden word, to calculate points
    private static String[] pastWords = new String[5];
    private static int[] pastTries = new int[5];
    private static String[] pastWinners = new String[5];
    private static boolean isWin = false;
    private static final String filePath = new File("").getAbsolutePath();
    private static List<Pair<Character, Float>>[] possible_chars; // pairs of (character,probability) for each char


    @Override
    /**
     * This method starts the application.
     * It loads the initial screen Using the file: hello-view.fxml
     * For this purpose it uses a scene object and a stage object
     * @param stage the stage object of the main application tab
     * @see Stage
     * @see Scene
     */
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("MediaLab Hangman");
        stage.setScene(scene);
        stage.show();
        //Dict.Creator();
    }

    /**
     * This method is called when we run the project.
     * It is the starting point of the application
     * @param args this parameter is given by default to the main method
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * This method is called from the Controller in order to load a dictionary
     * It tries to find a stored dictionary with the requested id,
     * it initializes the active dictionary accordingly.
     * Otherwise it throws an exception that is handled internally     *
     * @param id the requested Dictionary ID
     * @return 0 if the requested Dictionary is found. -1 otherwise
     */
    public static int loader(String id)
    {
        int ans = 0;
        String filename = "D:\\multimedia\\multi_proj\\src\\main\\java\\com\\example\\multi_proj\\medialab\\hangman_" + id + ".txt";
        Scanner sc2 = null;
        try {
            ActiveDict = new HashSet<String>();
            sc2 = new Scanner(new File(filename));
            while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    //System.out.println(s);
                    ActiveDict.add(s);
                }
            }
            //System.out.println(ActiveDict);
            ans = 0;
        }
        catch (Exception e) {
            //e.printStackTrace();
            ActiveDict = null;
            ans = -1;
        }
        finally
        {
            return ans;
        }
    }

    private static void calc_posib()
    {
        for (int i = 0; i < word.length(); ++i)
        {
            float posib = 0;
            for (String item : HiddenWords)
            {
                if ((item.charAt(i))==(word.charAt(i))) posib++;
            }
            posib = posib / HiddenWords.size();
            possibilities[i] = posib;

        }
        //System.out.println("Possibilities");
        //for (float num : possibilities) System.out.println(num);
        possible_chars = new ArrayList[word.length()];
        for (int i = 0; i < word.length(); ++i)
        {
            // find all distinct chars for this index
            HashSet<Character> distinct_letters = new HashSet<>();
            for (String item : HiddenWords) if (!distinct_letters.contains(item.charAt(i))) {distinct_letters.add(item.charAt(i));}
            System.out.println();
            possible_chars[i] = new ArrayList<>();
            for (char letter : distinct_letters)
            {
                float posib = 0;
                for (String item : HiddenWords) if (item.charAt(i)==letter) posib++;
                posib /= HiddenWords.size();
                possible_chars[i].add(new Pair<Character, Float>(letter, posib));
            }
            Collections.sort(possible_chars[i], new CustomComperator());
        }
    }

    /**
     * this method is called from the Controller
     * when the start menu option is chosen.
     * If there is an active dictionary, it chooses a random word
     * and initializes the state of the game accordingly.
     * It finds the set of hidden words and
     * calculates the probability of each possible character for each position.
     * It also initializes the number of mistakes done to 0
     * @return 0 if there is an active dictionary and -1 otherwise
     */
    public static int initialize()
    {
        // if no dictionary has been chosen we can't continue
        if (ActiveDict == null) return -1;
        // chose random word from current dict
        String[] Words = ActiveDict.toArray(new String[0]);
        Random rand = new Random();
        int ind = rand.nextInt(Words.length);
        word = Words[ind];
        // define the set of words that will be used to find the probability for each character
        HiddenWords = new HashSet<String>();
        for (String item : Words)
        {
            if (item.length()==word.length()) HiddenWords.add(item);
        }
        mistakes = 0;
        possible_chars = new ArrayList[word.length()];
        possibilities = new float[word.length()];
        calc_posib();
        found = new char[2*word.length()];
        for (int i = 0; i < 2*word.length(); ++i) {
            if (i % 2 == 0) found[i] = '_';
            else found[i] = ' ';
        }
        //System.out.println(word);
        return 0;
    }

    /**
     * This method is called from the Controller at the end of a game.
     * It stores the winner of the game the number of guesses and the hidden word.
     * This information is needed for the "rounds" menu option functionality.
     * Afterwards, the method resets all the game variables.
     */
    public static void reset()
    {
        for (int i = 3; i >= 0; --i)
        {
            pastTries[i+1] = pastTries[i];
            pastWinners[i+1] = pastWinners[i];
            pastWords[i+1] = pastWords[i];
        }
        if (isWin) pastWinners[0] = "You";
        else pastWinners[0] = "Computer";
        pastTries[0] = Guesses;
        pastWords[0] = word;

        ret="";
        word="";
        HiddenWords = null;
        mistakes = 0;
        SucGuesses = 0;
        Guesses = 0;
        found = null;
        Points = 0;
        isWin = false;
    }

    /**
     * This method is called from the Controller when the number
     * of words in the Active Dictionary is needed
     * @return The number of words in the Active Dictionary
     */
    public static int getWordNo(){ return ActiveDict.size(); }

    /**
     * This method is called from the Controller when the number
     * points in the current game is needed
     * @return the number of points in the current game
     */
    public static int getPoints() { return Points; }

    /**
     * @return the length of the hidden word
     */
    public static int getSize(){ return word.length(); }

    /*
    public static HashSet<Character> getCharCount(int ind)
    {
        //returns all possible characters for a specific index in the word
        HashSet<Character> res = new HashSet<Character>();
        for (String item:HiddenWords)
        {
            char letter = item.charAt(ind);
            if (res.contains(letter)) continue;;
            res.add(letter);
        }
        return res;
    }
    */

    /**
     * This method is called for the Controller when we need to display
     * the possible characters for a certain position in the word
     * @param ind the index of the position
     * @return a List of pairs containing the possible characters for the specified position and probability
     * that each character is the correct option
     */
    public static List<Pair<Character, Float>> AllChars(int ind){ return possible_chars[ind]; }

    /**
     * This method is called from the Controller when a new guess is made
     * (a new letter is chosen for a certain position)
     * If the guess is correct, it increases the number of successful guesses,
     * otherwise it increases the number of mistakes.
     * Afterwards, it updates the set of hidden words and recalculates the possibilities
     * of each character.
     * @param pos the position of the chosen letter
     * @param letter the letter/character that was chosen
     * @return 1 if the character for this position has already been guesses beforehand. 0 if the guess was correct. -1 if the guess was incorrect
     */
    public static int checkLetter(int pos, Character letter)
    {
        //System.out.println((Character) found[2*pos]);
        if (found[2*pos] != '_' ) return 1; // this letter has already been found
        Guesses++;
        Character c = (word.charAt(pos));
        //System.out.println(c + ' '+ letter);
        if (c.equals(letter))
        {
            SucGuesses++;
            HiddenWords.removeIf(item -> !c.equals(item.charAt(pos)));
            updatePoints(possibilities[pos]);
            calc_posib();
            found[2*pos] = (char) letter;

            String temp = "";
            for (int i = 0; i < found.length; ++i) if(i%2==0) temp+=found[i];
            //System.out.println(temp);
            if (word.equals(temp)) isWin = true;
            return 0;
        }
        else {
            mistakes++;
            Points = Math.max(0, Points-15);
            HiddenWords.removeIf(item -> letter.equals(item.charAt(pos)));
            calc_posib();
            /*
            if (mistakes > 5) // game over-reset everything
            {
                ret="";
                word="";
                HiddenWords = null;
                mistakes = 0;
            }*/
            return -1;
        }
    }

    /**
     * @return the number of mistakes in the current game
     */
    public static int getMistakes(){ return mistakes; }

    /**
     * This method is called from the Controller, when we need to display
     * the current status of the hidden word. This includes the correct character, in the positions where
     * it has been guessed correctly and underscores ("_") where the letter
     * hasn't been guessed yet.
     * @return an array of char that represents the status of the found word as described above
     */
    public static char[] getFoundWord() {return found;}
    private static void updatePoints(float posib)
    {
        if(posib >= 0.6) Points+=5;
        else if(posib >= 0.4) Points+=10;
        else if (posib >= 0.25) Points+=15;
        else Points+=30;
    }

    /**
     * This method is called from Controller when we need to display
     * the percentage of successful guesses of the current game.
     * The percentage is calculated by division of the number of successful guesses
     * by the total number of guesses
     * @return the percentage of successful guesses as a float number
     */
    public static float getPercentOfSucSuccess()
    {
        if (Guesses == 0) return 0;
        return (float) SucGuesses/Guesses;
    }

    /**
     * @return the hidden word
     */
    public static String getWord(){ return word; }

    /**
     * This method is called from the Controller when the "Dictionary"
     * menu option is chosen.
     * It calculates the percentage of words with 6 letters, of words with 7-9 letters
     * and of words with 10+ letters. Then it forms a string containing this information
     * @return the string that is formed as described above
     */
    public static String getDictionary()
    {
        if (ActiveDict==null) return "There is no Active Dictionary";
        float group1=0;
        float group2=0;
        float group3=0;
        for (String item : ActiveDict)
        {
            if (item.length()==6) group1++;
            else if (item.length() >= 7 && item.length() <= 9) group2++;
            else if (item.length() >= 10) group3++;
        }
        String s1 = String.format("%.2f", group1*100/ActiveDict.size())+"% words with 6 letters\r\n";
        String s2 = String.format("%.2f", group2*100/ActiveDict.size())+"% words with 7-9 letters\r\n";
        String s3 = String.format("%.2f", group3*100/ActiveDict.size())+"% words with 10 or more letters";
        return "The Current Dictionary Contains:\r\n"+s1+s2+s3;
    }

    /**
     * This method is called from the Controller when a game ends.
     * @return true if the player won and false if the computer won
     */
    public static boolean PlayerWins() { return isWin; }

    /**
     * This method is called when the "Rounds" menu option is chosen.
     * It forms a string containing information about the past rounds (5 at most).
     * @return the string that is formed as described above
     */
    public static String getRounds()
    {
        if (pastWords[0] == null) return "There is no information on past games yet";
        String ret = "";
        for (int i = 0; i < 5; ++i)
        {
            if (pastWords[i] == null || pastWords[i] == "") break;
            if (i == 0) ret += "1 game back:\r\n-Hidden Word: " + pastWords[i] + "\r\n-Winner: " + pastWinners[i] + "\r\n-Number of Guesses: " + pastTries[i];
            else ret +=  "\r\n\r\n" + String.valueOf(i+1) + " games back:\r\n-Hidden Word: " + pastWords[i] + "\r\n-Winner: " + pastWinners[i] + "\r\n-Number of Guesses: " + pastTries[i];
        }
        return ret;
    }
}

//this class is used when sorting the possible characters by probability
class CustomComperator implements Comparator<Pair<Character, Float>>{
    @Override
    public int compare(Pair<Character, Float> a, Pair<Character, Float> b)
    {
        return b.getValue().compareTo(a.getValue());
    }
}