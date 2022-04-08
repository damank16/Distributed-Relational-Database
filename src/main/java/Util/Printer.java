package Util;

import java.util.Arrays;

public class Printer {

    public  static void printTitle(String title)
    {
        System.out.println(repeatChar('-',140));
        System.out.format("%100s" ,title);
        System.out.println();
        System.out.println(repeatChar('-',140));

    }

    public static void printContent(String content)
    {
        System.out.println(content);
    }

    private static  String repeatChar(char c, int length) {
        char[] data = new char[length];
        Arrays.fill(data, c);
        return new String(data);
    }
}
