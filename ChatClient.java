package rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) throws IOException {
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        final int ePublicOutside, cPublicOutside;
        String ipv4 = "127.0.0.1"; // local host

        try{

            echoSocket = new Socket(ipv4, 8000);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch(UnknownHostException e){
            System.err.println("Don't know about host: " + ipv4);
            System.exit(1);
        } catch(IOException e){
            System.err.println("Couldn't get I/O for the connection to: " + ipv4);
            System.exit(1);
        }

        Scanner scanIn = new Scanner(System.in);
        System.out.println("Please enter your friend's public key (e, c): first e, then c");
        String publicKey = scanIn.nextLine();
        String [] publicKeyArray = publicKey.split(" ");
        ePublicOutside = Integer.parseInt(publicKeyArray[0]);
        cPublicOutside = Integer.parseInt(publicKeyArray[1]);
        //System.out.println("Readin e = " + ePublicOutside + ", c = " + cPublicOutside);

        Rsa rsa = new Rsa();
        rsa.askPrimes();

        System.out.println("You may now type in messages");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        String rtnOutput;

//        if((rtnOutput = in.readLine()) != null){
//            String decryptedStr = rsa.decryptFunction(rtnOutput, rsa.d, rsa.c);
//            System.out.println(decryptedStr);
//        }



        while((userInput = stdIn.readLine()) != null){
            //System.out.println("You typed in : " + userInput);
            //System.out.println("Readin e = " + ePublicOutside + ", c = " + cPublicOutside);
            String writeout = rsa.encryptFunction(userInput, ePublicOutside, cPublicOutside);
            out.println(writeout);

            String decryptedStr = rsa.decryptFunction(in.readLine(), rsa.d, rsa.c);

            System.out.println("Server respond : " + decryptedStr);
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }

}
