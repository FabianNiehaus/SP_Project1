import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class SimpleCracker {

    static class ShadowEntry{
        String username;
        String salt;
        String hash;

        ShadowEntry(String username, String salt, String hash) {
            this.username = username;
            this.salt = salt;
            this.hash = hash;
        }
    }

    public static void main(String args[]){
        LinkedList<ShadowEntry> shadowEntries = new LinkedList<>();
        LinkedList<String> commonPasswords = new LinkedList<>();

        System.out.println("Starting SimpleCracker!");

        //Read shadow entries
        try {
            System.out.println("Reading shadow entries...");
            BufferedReader shadowReader = new BufferedReader(new FileReader("shadow-simple"));

            String shadowLine;
            while(( shadowLine = shadowReader.readLine() )!=null){
                String username, salt, hash;
                username = shadowLine.substring(0, shadowLine.indexOf(":")).replace(":", "");
                salt = shadowLine.substring(shadowLine.indexOf(":"), shadowLine.lastIndexOf(":")).replace(":", "");
                hash = shadowLine.substring(shadowLine.lastIndexOf(":")).replace(":", "");

                shadowEntries.add(new ShadowEntry(username, salt, hash));
            }

            shadowReader.close();
            System.out.println("Done!");
        } catch (FileNotFoundException e) {
            System.out.println("File \"shadow-simple\" not found!");
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        // Read common passwords
        try {
            System.out.println("Reading common passwords...");
            BufferedReader passwordsReader = new BufferedReader(new FileReader("common-passwords.txt"));

            String passwordsLine;
            while(( passwordsLine = passwordsReader.readLine() )!=null){
              commonPasswords.add(passwordsLine);
            }

            passwordsReader.close();
            System.out.println("Done!");
        } catch (FileNotFoundException e) {
            System.out.println("File \"common-passwords.txt\" not found!");
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Looking for collisions...");
        /*int cycle = 0;*/
        int passwordsFound = 0;
        for (ShadowEntry shadowEntry: shadowEntries) {
            for(String password: commonPasswords){
                String hashedPassword = generateHash(shadowEntry.salt + password);
                /*System.out.println(cycle + ": " + hashedPassword + " - " + shadowEntry.hash);
                cycle++;*/
                if (hashedPassword.equals(shadowEntry.hash)){
                    /*System.out.println("!!!!!");
                    System.out.println("Password found for user " + shadowEntry.username);
                    System.out.println("Hash is " + hashedPassword);
                    System.out.println("Password is " + password);
                    System.out.println("Salt is " + shadowEntry.salt);
                    System.out.println("-----");*/
                    System.out.println(shadowEntry.username + ":" + password);
                    passwordsFound++;
                }
            }
        }
        System.out.println("Found " + passwordsFound + " passwords!");
        System.out.println("Finished looking for collisions, exiting SimpleCracker");
    }

    private static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md5.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F' };
            for (byte b : hashedBytes) {
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash.toString();
    }

}
