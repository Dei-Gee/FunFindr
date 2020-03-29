package com.example.funfindr.utilites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

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

    /**
     * Converts a Bitmap image to a byte array
     * @param bitmap The bitmap image
     * @param originalFormat the original format of the bitmap image
     * @return returns a byte array of the bitmap image
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, String originalFormat)
    {
        byte[] byteArray = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(originalFormat.toLowerCase() == "png")
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        }
        else if(originalFormat.toLowerCase() == "jpg" || originalFormat.toLowerCase()== "jpeg")
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        }
        else if(originalFormat.toLowerCase() == "webp" || originalFormat.toLowerCase()== "jpeg")
        {
            bitmap.compress(Bitmap.CompressFormat.WEBP, 0, stream);
        }
        byteArray = stream.toByteArray();
        return byteArray;
    }

    /**
     * Converts a byte arrayto a Bitmap image
     * @param byteArray The byte imag to be converted
     * @return returns a Bitmap image from the byte array
     */
    public static Bitmap getImage(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

}
