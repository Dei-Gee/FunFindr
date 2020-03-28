package com.example.funfindr.utilites;

public class FunFindrUtils {

    /**
     * Checks if a string is a numeric integer
     * @param s The string to be checked
     * @return whether the string is numeric or not
     */
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    /**
     * Checks if a string is a numeric integer
     * @param s The string to be checked
     * @param radix The base of the number i.e. Binary, Hexadecimal, Decimal/Denary etc.
     * @return whether the string is numeric or not
     */
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
