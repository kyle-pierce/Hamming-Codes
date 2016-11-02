import java.util.*;
import java.io.*;

public class BitFileMaker {

   private static final String TEXT_FILE = "binaryStrings.txt";
   private static final String TEXT_FILE_PARITY = "binaryStringsParity.txt";
   private static final String TEXT_FILE_CORRUPT = "binaryStringsCorrupt.txt";
   private static final String TEXT_FILE_CORRECT = "binaryStringsCorrect.txt";
   private static final String TEXT_FILE_FINAL = "binaryStringsFinal.txt";
   private static final int maxBlocks = 100;
   private static final int minBlocks = 10;
   
   private static Scanner console;
   private static int numBlocks;

   public static void main(String[] args) throws FileNotFoundException {
      console = new Scanner(System.in);
      if (playIntro()) {
         System.out.println();
         System.out.print("How many codes would you like to generate? " 
                          + minBlocks + " - " + maxBlocks + "): ");
         writeCodes(new PrintStream(TEXT_FILE), verifyNumber(console.next()));
         addParityBits(new PrintStream(TEXT_FILE_PARITY));
         corruptBits(new PrintStream(TEXT_FILE_CORRUPT));
         fixBits(new PrintStream(TEXT_FILE_CORRECT));
         removeParityBits(new PrintStream(TEXT_FILE_FINAL));
      }
   }
   
   public static boolean playIntro() {
      System.out.println("Hello, this is a generator for filling a text file named ");
      System.out.println(TEXT_FILE + " with random String of 8-bit binary codes.");
      System.out.print("Would you like me to make this file for you? (y/n): ");
      String response = verifyResponse(console.next().toLowerCase());
      if (response.startsWith("n")) {
         System.out.println("Oh...that's ok, I'll just close then.");
         return false;
      } else if (new File(TEXT_FILE).exists()) {
         System.out.println();
         System.out.println("I see you already have a file by the name " + TEXT_FILE + ".");
         System.out.print("You're sure you'd like to write over this file? (y/n): ");
         response = verifyResponse(console.next().toLowerCase());
         if (response.startsWith("y")) {
            System.out.println("Thank you, your old file is gone forever.");
         } else {
            System.out.println("Your file escaped destruction...this time.");
            return false;
         }
      }
      return true;
   }  
   
   public static String verifyResponse(String response) {
      while(!response.startsWith("y") && !response.startsWith("n")) {
         clearScanner();
         System.out.println();
         System.out.print("Cannot decipher response, try again. (y/n): ");
         response = console.next();
      }
      return response;
   }
   
   public static int verifyNumber(String response) {
      System.out.println();
      int value = 0;
      boolean valid = false;
      while (!valid) {
         try {
            value = Integer.parseInt(response);
            if (value >= minBlocks && value <= maxBlocks) {
               valid = true;
            } else {
               throw new NumberFormatException();
            }
         } catch (NumberFormatException e) {
            clearScanner();
            System.out.print("Invalid response.  Pick a number between " 
                             + minBlocks + " and " + maxBlocks + ": ");
            response = console.next();
            System.out.println();
         }
      }
      return value;
   }
   
   public static void writeCodes(PrintStream stream, int codes) {
      numBlocks = codes;
      for (int i = 0; i < codes; i++) {
         System.out.println("Generating code " + (i + 1) + " of " + codes + "...");
         stream.println(makeCode());
      }
      System.out.println("Done creating codes!");
   }
   
   public static String makeCode() {
      String code = "";
      for (int i = 0; i < 8; i++) {
         code += (int)(Math.round(Math.random()));
      }
      return code;
   }
   
   public static void clearScanner() {
      console = new Scanner(System.in);
   }
   
   public static void addParityBits(PrintStream stream) throws FileNotFoundException {
      String[] bits = readInData(TEXT_FILE, numBlocks);
      for (int i = 0; i < bits.length; i++) {
         BitBlock block = new BitBlock(bits[i]);
         block.addParity();
         stream.println(block.toString());
      }
      System.out.println();
      System.out.println("Parity bits added!");
   }
   
   public static void corruptBits(PrintStream stream) throws FileNotFoundException {
      String[] bits = readInData(TEXT_FILE_PARITY, numBlocks);
      for (String data : bits) {
         BitBlock block = new BitBlock(data);
         block.corruptBit();
         stream.println(block.toString());
      }
      System.out.println();
      System.out.println("Bits corrupted in transfer!");
   }
   
   public static void fixBits(PrintStream stream) throws FileNotFoundException {
      int corrected = 0;
      String[] bits = readInData(TEXT_FILE_CORRUPT, numBlocks);
      for (String data : bits) {
         BitBlock block = new BitBlock(data);
         if (block.testParity()) {
            corrected++;
         }
         stream.println(block.toString());
      }
      System.out.println();
      System.out.println(corrected + " bits corrupt, " + corrected + " now corrected.");
   }
   
   public static void removeParityBits(PrintStream stream) throws FileNotFoundException {
      String[] bits = readInData(TEXT_FILE_CORRECT, numBlocks);
      for (String data : bits) {
         BitBlock block = new BitBlock(data);
         block.removeParity();
         stream.println(block.toString());
      }
      System.out.println();
      System.out.println("Original data restored!");
   }
   
   public static String[] readInData(String fileName, int length) throws FileNotFoundException {
      String[] data = new String[length];
      int index = 0;
      Scanner input = new Scanner(new File(fileName));
      while (input.hasNext()) {
         data[index] = input.next();
         index++;
      }
      return data;
   }
}