import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class RegularExpressionTester {

    /*
        (aba*)(abb*)(a*b)*
        k[(bd)+(bbd)+(bbbddd)]*t
        (a+b)(a+b)(a+b)
        (0+1)(0+1)(0+1)(0+1)(0+1)*
        (a+b)(a+b)(a+b)(a+b)(a+b)*
    */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a regular expression: ");
            String input = scanner.nextLine();
            String pattern = generatePattern(input); // Your pattern definition here

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            System.out.println("Converted regular expression: " + pattern);

            if (!pattern.contains("*")) {
                System.out.println("The entered regular expression must contain at least one Kleene star...\n");
                continue;
            }

            long startTime = System.currentTimeMillis();
            long timeoutInMillis = 10 * 1000; // 10 seconds
            long intervalInMillis = 5 * 1000; // 5 seconds

            long nextPrintTime = startTime + intervalInMillis;

            Set<Character> uniqueLetters = findUniqueCharacters(pattern);
            Set<String> uniqueWords = new HashSet<>();

            while (uniqueWords.size() < 5) {
                String randomWord = generateRandomWord(uniqueLetters, 5);
                if (Pattern.matches(pattern, randomWord)) {
                    uniqueWords.add(randomWord);
                }

                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= timeoutInMillis) {
                    System.out.println("Timeout reached! Exiting loop.");
                    break;
                }

                // Print status at specific intervals
                if (System.currentTimeMillis() >= nextPrintTime) {
                    System.out.println("Processing...");
                    nextPrintTime += intervalInMillis;
                }
            }

            System.out.println("\nFound combinations: " + uniqueWords.size());
            System.out.println("------------------------------------------------------------------------------------------------");
            for (String word : uniqueWords) {
                System.out.println(word);
            }

            System.out.print("Press 'q' to quit, or any other key to continue: ");
        }

        scanner.close();
    }

    private static Set<Character> findUniqueCharacters(String str) {
        Set<Character> uniqueCharacters = new HashSet<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                uniqueCharacters.add(Character.toLowerCase(c));
            }
        }
        return uniqueCharacters;
    }

    private static String generateRandomWord(Set<Character> uniqueLetters, int length) {
        char[] lettersArray = new char[uniqueLetters.size()];
        int index = 0;
        for (Character letter : uniqueLetters) {
            lettersArray[index++] = letter;
        }
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char randomLetter = lettersArray[random.nextInt(lettersArray.length)];
            builder.append(randomLetter);
        }
        return builder.toString();
    }

    private static String generatePattern(String pattern) {
        // Replace special characters with regex equivalents
        return pattern
                .replaceAll("\\+", "|") // + becomes |
                .replaceAll("\\[", "(") // [ becomes (
                .replaceAll("]", ")"); // ] becomes )
    }
}
