package main;

import queryprocessor.QueryParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static boolean isDistributed = false;
    public static void main(String[] args)  {
        isDistributed = false;
        QueryParser queryParser = new QueryParser();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String query = ""; ;
        do {
            try {
                System.out.println("Enter input:");

                query = br.readLine();
                if (!query.equalsIgnoreCase("exit")) {
                    queryParser.parseQuery(query);
                    System.out.println("completed...");
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        while (!query.equalsIgnoreCase("exit"));

    }
}
