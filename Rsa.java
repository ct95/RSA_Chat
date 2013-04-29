package rsa;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Rsa {

    public int nInput, mInput, nthPrime, mthPrime, c, m;
    public int e; // e is a random number coprime to m
    public int d; // the modular inverse of e mod m

    public int coprime(int x){
    // using a java.util.Random number generator, pick a random integer that is coprime to the given input parameter x
        int rdn = new Random().nextInt(1000);
        while(GCD(rdn, x) != 1){
            rdn = new Random().nextInt(1000);
        }
        return rdn;
    }

    public int endecrypt(int msg_or_cypher, int key, int c){
    // given an integer representing an ASCII character value, encrypt it via the RSA crypto algorithm
        //System.out.println("msg_or_cypher = " + msg_or_cypher + ", key = " + key + " , c = " + c);
        int temp;
        int modInt = 1;
        while(key > 2){
            temp = (int)(Math.pow(msg_or_cypher, 2)) % c;
            key = key-2;
            modInt = (int)(modInt * temp) % c;
            //System.out.println("temp = " + temp);
        }

        temp = (int)(Math.pow(msg_or_cypher, key)) % c;
        modInt = (int)(modInt * temp) % c;
        //System.out.println("temp = " + temp);
        //System.out.println("modInt = " + modInt);
        return modInt;
    }

    public int GCD(int a, int b){ // computes the GCD of two numbers a and b
        if(b == 0) return a;
        return GCD(b, a % b);
    }

    public int mod_inverse(int base, int m){ // compute the modular inverse base ^-1 % m
        int n = 1;
        while((m*n+1) % base != 0){
            n++;
        }
        return ((m*n+1) / base);
    }

    public int modulo(int a, int b, int c){ // computes Math.mod(Math.pow(a, b), c), for large values of a, b, and c
        double temp = Math.pow(a, b);
        return (int)(temp % c);
    }

    public int totient(int n){ // computes Euler's Totient
        int counter = 0;
        for(int i=1; i<n; i++){
            if(GCD(i, n) == 1){
                counter++;
            }
        }
        return counter;
    }

    public static int nthPrime(int n){
        int counter = 0;
        int num = 1;
        while(counter < n){
            num++;
            if(isPrime(num)){
                counter++;
            }
        }
        return num;
    }

    /** Return true iff n is a prime number. */
    private static boolean isPrime(int n) {
        if (n == 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        int limit = (int)(Math.sqrt(n)+ 0.5);
        for (int i = 3; i <= limit; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public void askPrimes(){
        System.out.println("Enter the n-th and m-th prime to compute : ");
        Scanner scanIn = new Scanner(System.in);
        String sInput = scanIn.nextLine();
        String [] split = sInput.split(" ");
        nInput = Integer.parseInt(split[0]);
        mInput = Integer.parseInt(split[1]);
        this.nmPrimeFinder(nInput, mInput);
    }

    public void nmPrimeFinder(int nInput, int mInput){
        nthPrime = nthPrime(nInput);
        mthPrime = nthPrime(mInput);
        //System.out.println(nthPrime + ", " + mthPrime);
        c = nthPrime * mthPrime;
        m = (nthPrime-1) * (mthPrime-1);
        e = this.coprime(m);
        d = this.mod_inverse(e, m);
        System.out.println(nInput + "th prime = " + nthPrime + ", " + mInput + "th prime = " + mthPrime + " , c = " + c + " , m = " +
                m + " , e = " + e + " , d = " + d + " , Public Key = (" + e + ", " + c + "), Private Key = (" + d + ", " + c + ")");
    }


    public String encryptFunction(String strToEncrypt, int eOutside, int cOutside){
        int [] encryptedStr = new int[strToEncrypt.length()];
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<strToEncrypt.length(); i++){
            int tempChar = (int)strToEncrypt.charAt(i);
            //System.out.println("tempChar at " + i + " = " + tempChar);
            encryptedStr[i] = endecrypt(tempChar, eOutside, cOutside);
            //System.out.println(encryptedStr[i]);
            if(i < strToEncrypt.length()-1){
                sb.append(encryptedStr[i] + " ");
            }else{
                sb.append(encryptedStr[i] + "") ;
            }
        }

        return sb.toString();
    }

    public void decryptFunction(){
        Scanner scanIn = new Scanner(System.in);
        System.out.println("Please enter the private key (d, c): first d, then c");
        String privateKey = scanIn.nextLine();
        String [] prikeysplit = privateKey.split(" ");
        d = Integer.parseInt(prikeysplit[0]);
        c = Integer.parseInt(prikeysplit[1]);
        while(true){
            System.out.println("Enter next char cipher value as an int, type quit to quit");
            String strToDecrypt = scanIn.nextLine();
            if(strToDecrypt.charAt(0) == 'q' || strToDecrypt.charAt(0) == 'Q') break;
            int toDecrypt = Integer.parseInt(strToDecrypt);
            int decryptedInt = this.endecrypt(toDecrypt, d, c);
            char decryptedChar = (char)decryptedInt;
            System.out.println(decryptedChar + " " + decryptedInt);
        }
    }

    public String decryptFunction(String strToDecrypt, int d, int c){
        String [] split = strToDecrypt.split(" ");
        int [] decryptedStr = new int[split.length];
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<split.length; i++){
            int toDecrypt = Integer.parseInt(split[i]);
            decryptedStr[i] = endecrypt(toDecrypt, d, c);
            char temp = (char)decryptedStr[i];
            sb.append(temp);
        }
        return sb.toString();
    }

//    public int decryptFun_individual(String input, int d, int c){
//        int toDecrypt = Integer.parseInt(input);
//        int decryptedInt = this.endecrypt(toDecrypt, d, c);
//        return decryptedInt;
//    }

    public void crackFunction(){
        Scanner scanIn = new Scanner(System.in);
        System.out.println("Enter the public key value (e, c): first e, then c");
        String publicKey = scanIn.nextLine();
        String [] pubkeysplit = publicKey.split(" ");
        e = Integer.parseInt(pubkeysplit[0]);
        c = Integer.parseInt(pubkeysplit[1]);
        int [] rtn = this.bruteForceRSA(e, c);
        System.out.println("a was " + rtn[0] + " , b was " + rtn[1]);
        System.out.println("The totient is " + (rtn[0]-1)*(rtn[1]-1));
        System.out.println("d was found to be " + rtn[2]);
        while(true){
            System.out.println("Enter next char cipher value as an int, type quit to quit");
            String strToDecrypt = scanIn.nextLine();
            if(strToDecrypt.charAt(0) == 'q' || strToDecrypt.charAt(0) == 'Q') break;
            int toDecrypt = Integer.parseInt(strToDecrypt);
            int decryptedInt = this.endecrypt(toDecrypt, d, c);
            char decryptedChar = (char)decryptedInt;
            System.out.println("This char decrypted to " + decryptedInt);
            System.out.println("The letter is " + decryptedChar);
        }

        scanIn.close();
    }

//    public static void main(String[] args) {
//        Rsa rsa = new Rsa();
//
//
//
//        System.out.println("Please enter the public key (e, c): first e, then c");
//        String publicKey = scanIn.nextLine();
//        String [] pubkeysplit = publicKey.split(" ");
//        e = Integer.parseInt(pubkeysplit[0]);
//        c = Integer.parseInt(pubkeysplit[1]);
//        System.out.println("Please enter a sentence to encrypt");
//        String strToEncrypt = scanIn.nextLine();
//        rsa.encryptFunction(strToEncrypt);
//
//    }

    //public HashMap<Integer, Integer> factors = new HashMap<Integer, Integer>();

    public int[] bruteForceRSA(int e, int c){
        int [] rtn = new int [3];

        for(int i=2; i <= ((c+1)/2); i++){
            if((c % i) == 0 && isPrime(i) && isPrime(c/i)){
                int m = (i-1)*((c/i)-1);
                if(this.GCD(e, m) == 1){
                    rtn[0] = i;
                    rtn[1] = (c/i);
                    rtn[2] = this.mod_inverse(e, m);
                    break;
                }
            }
        }
        return rtn;
    }

}
