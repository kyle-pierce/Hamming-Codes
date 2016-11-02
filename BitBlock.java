import java.io.*;
import java.util.*;

public class BitBlock {
   
   public static ArrayList<Integer> bits;
   
   public BitBlock(String currentBit) {
      bits = new ArrayList<Integer>();
      for (int i = 0; i < currentBit.length(); i++) {
         bits.add(Character.getNumericValue(currentBit.charAt(i)));
      }
   }
   
   public void addParity() {
      bits.add(0, (bits.get(0) + bits.get(1) + bits.get(3) + bits.get(4) + bits.get(6)) % 2);
      bits.add(1, (bits.get(1) + bits.get(3) + bits.get(4) + bits.get(6) + bits.get(7)) % 2);
      bits.add(3, (bits.get(3) + bits.get(4) + bits.get(5) + bits.get(9)) % 2);
      bits.add(7, (bits.get(7) + bits.get(8) + bits.get(9) + bits.get(10)) % 2);
   }
   
   public String toString() {
      String currentBlock = "";
      for (int i = 0; i < bits.size(); i++) {
         currentBlock += bits.get(i);
      }
      return currentBlock;
   }
   
   public boolean testParity() {
      boolean corrupt = false;
      int test = -1;
      if (testParityHelper(0, 2, 4, 6, 8, 10) != 0) { 
         test += 1;
         corrupt = true;
      }
      if (testParityHelper(1, 2, 5, 6, 9, 10) != 0) {
         test += 2;
         corrupt = true;
      }
      if (testParityHelper(3, 4, 5, 6, 11, -1) != 0) {
         test += 4;
         corrupt = true;
      }
      if (testParityHelper(7, 8, 9, 10, 11, -1) != 0) {
         test += 8;
         corrupt = true;
      }
      if (test >= 0) {
         bits.set(test, (bits.get(test) + 1) % 2);
      }
      return corrupt;
   }
   
   private int testParityHelper(int a, int b, int c, int d, int e, int f) {
      int i = (bits.get(a) + bits.get(b) + bits.get(c) + bits.get(d) + bits.get(e));
      if (f > 0) {
         i += bits.get(f);
      }
      return i % 2;
   }
   
   public int size() {
      return bits.size();
   }
   
   public void corruptBit() {
      int index = ((int)Math.round(Math.random() * 11));
      for (int i = 0; i < this.size(); i++) {
         if (i == index) {
            bits.set(i, ((int)Math.round(Math.random())));
         } 
      }
   }
   
   public void removeParity() {
      bits.remove(0);
      bits.remove(0);
      bits.remove(1);
      bits.remove(4);
   }

}