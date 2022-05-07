import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Wordle {
    public static final String RESET  = "\u001B[0m";
    public static final String RED_BRIGHT = "\u001B[091m";
    public static final String YELLOW_BRIGHT = "\u001B[093m";
    public static final String GREEN_BRIGHT = "\u001B[092m";

    public static int getDictionaryLength() throws Exception {
        File dictionary = new File("dic.txt");
        Scanner in = new Scanner(dictionary);
        int counterWords = 0;

        while(in.hasNext()) {
            in.next();
            counterWords++;
        }
        in.close();
        return counterWords;
    } 

    public static int getRandomNumber() throws Exception {
        Random rand = new Random();
        int dictionaryLength = getDictionaryLength();
        int num = rand.nextInt(dictionaryLength);
        
        return num;
    }

    public static String getRandomWord() throws Exception {
        int randomNumber = getRandomNumber();

        String randomWord = Files.readAllLines(Paths.get("dic.txt")).get(randomNumber);
        return randomWord;
    }

    public static boolean validInput(String guess) {
        boolean isValid = true;

        for(int i = 0; i < guess.length(); i++) {
            if(!Character.isLetter(guess.charAt(i)) || guess.length() != 5) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static boolean inDictionary(String guess) throws Exception {
        boolean isReal = false;
        int max = getDictionaryLength();
        int i = 0;
        File dictionary = new File("dic.txt");
        Scanner in = new Scanner(dictionary);

		while (i < max) {
			String word = in.nextLine().toUpperCase();
            int compare = word.compareToIgnoreCase(guess);
            if(compare == 0) {
                isReal = true;
            } 
            i++;
		}

		in.close();
        return isReal;
    }
    
    public static void playGame() throws Exception {
        System.out.println("Please enter a 5-letter word: \nFor instructions, type :help \nTo give up, type :giveup");
        Scanner sc = new Scanner(System.in);
        String answer = getRandomWord().toUpperCase();
        int guessCount = 0;
        boolean gameOver = false;
        String temp = " ";

        //System.out.println(answer); //for testing game

        while(gameOver == false) {  
            String userGuess = sc.next().toUpperCase();
            if(userGuess.equalsIgnoreCase(":help")) {
                System.out.println("\nTo play the game, try to guess the secret 5-letter word.");
                System.out.println("If your letter comes back " + GREEN_BRIGHT + "green" + RESET + ", it's correct!");
                System.out.println("If it comes back " + YELLOW_BRIGHT + "yellow" + RESET + ", it's correct but in the wrong place.");
                System.out.println("If it comes back " + RED_BRIGHT + "red" + RESET + ", it's incorrect.");
                System.out.println("To give up, type :giveup");
                System.out.println("To keep playing, enter a 5-letter word: ");
                continue;
            } else if(userGuess.equalsIgnoreCase(":giveup")) {
                System.out.println("\nThe answer is " + answer);
                break;
            } else if(validInput(userGuess) == false) {
                System.out.println("\nInvalid input! Please enter a 5-letter word: ");
                continue;
            } else if(inDictionary(userGuess) == false) {
                System.out.println("\nThat word isn't in our dictionary! Please enter another 5-letter word: ");
                continue;
            }

            temp += "\n"; 

            for(int i = 0; i < userGuess.length(); i++) {
                if(userGuess.charAt(i) == answer.charAt(i)) {
                    temp = temp + GREEN_BRIGHT + userGuess.charAt(i) + RESET;
                }
                else if(userGuess.charAt(i) != answer.charAt(i)) {
                    int j = answer.indexOf(userGuess.charAt(i));
                    if (j >= 0) {
                        temp = temp + YELLOW_BRIGHT + userGuess.charAt(i) + RESET;
                    } else {
                        temp = temp + RED_BRIGHT + userGuess.charAt(i) + RESET;
                    }
                }
            }
            guessCount ++; 
            System.out.println(temp);  

            if(userGuess.equalsIgnoreCase(answer)) {
                gameOver = true;
                System.out.println("\nYou win! You guessed the answer in " + guessCount + " tries!");
                sc.close();
            } else {
                System.out.println("\nMake another guess: ");
            }
        } 
    }

   

    public static void main(String[] args) throws Exception {
       playGame();
    }

}