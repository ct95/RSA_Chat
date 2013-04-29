package rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer implements Runnable{
    protected static boolean serverContinue = true;
    protected Socket clientSocket;
    public static PrintWriter out;
    public static BufferedReader in;

    //outputLine;
    public static int dServer, cServer;
    public static Rsa rsa;

    public static void main(String[] args) throws IOException {
        rsa = new Rsa();
        rsa.askPrimes();
        dServer = rsa.d;
        cServer = rsa.c;

        ServerSocket serverSocket = null;
        int port = 8000;

        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Connection Socket Created");
            try{
                while(serverContinue){
                    System.out.println("Waiting for Connection");
                    new ChatServer(serverSocket.accept());
                }
            }catch(IOException e){
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }catch(IOException e){
            System.err.println("Cound not listen on port: " + port);
            System.exit(1);
        }finally{
            try{
                serverSocket.close();
            }catch(IOException e){
                System.err.println("Could not close port: " + port);
                System.exit(1);
            }
        }


    }
//        ServerSocket serverSocket = null;
//        try{
//            serverSocket = new ServerSocket(8000);
//        } catch (IOException e){
//            System.err.println("Could not listen on port: 8000");
//            System.exit(1);
//        }
//
//        Socket clientSocket = null;
//        System.out.println("Waiting for connection ...");
//
//        try{
//            clientSocket = serverSocket.accept();
//
//        }catch(IOException e){
//            System.err.println("Accept failed.");
//            System.exit(1);
//        }
//        System.out.println("Connection successful");
//        System.out.println("Waiting for input ...");
//
//        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        ChatServer chatsrvr = new ChatServer();
//        Thread myThread = new Thread(chatsrvr);
//        myThread.start();

    private ChatServer(Socket clientSoc){
        clientSocket = clientSoc;
        Thread myThread = new Thread(this);
        myThread.start();
    }



    @Override
    public void run(){
        System.out.println("New Communication Thread Started");
        Scanner scanIn = new Scanner(System.in);
        int ePublicOutside, cPublicOutside;
        System.out.println("Please enter your friend's public key (e, c): first e, then c");
        String publicKey = scanIn.nextLine();
        String [] publicKeyArray = publicKey.split(" ");
        ePublicOutside = Integer.parseInt(publicKeyArray[0]);
        cPublicOutside = Integer.parseInt(publicKeyArray[1]);
        //System.out.println("Readin e = " + ePublicOutside + ", c = " + cPublicOutside);

        try{
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //PrintWriter out2 = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //BufferedReader in2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null){

                //System.out.println("d = " + dServer + " , c = " + cServer);
                //System.out.println("Server received: " + inputLine);
                String decryptedStr = rsa.decryptFunction(inputLine, dServer, cServer);
                System.out.println("Client sent: " + decryptedStr);

                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                String outputStr = rsa.encryptFunction(stdIn.readLine(), ePublicOutside, cPublicOutside);
                out.println(outputStr);

            }
            out.close();
            in.close();
            clientSocket.close();
        } catch(IOException e){
            System.err.println("Problem with communication server");
            System.exit(1);
        }
//        try {
//            //synchronized (this){
//                String inputLine;
//                if(in == null) System.out.println("in is NULL !!");
//                while((inputLine = in.readLine()) != null){
//                    out.println(inputLine);
//            //    }
//            }
//        } catch (IOException e) {
//            System.err.println("in.readLine() generates IOException");
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
    }

}
