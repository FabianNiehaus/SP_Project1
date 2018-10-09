import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Cracker {

    public static void main(String args[]){
        LinkedList<ShadowEntry> shadowEntries = new LinkedList<>();
        LinkedList<String> commonPasswords = new LinkedList<>();

        System.out.println("Starting Cracker!");

        //Read shadow entries
        try {
            System.out.println("Reading shadow entries...");
            BufferedReader shadowReader = new BufferedReader(new FileReader("shadow"));

            String shadowLine;
            while(( shadowLine = shadowReader.readLine() )!=null){
                String username, salt, hash, shash;
                username = shadowLine.substring(0, shadowLine.indexOf(":"));
                shash = shadowLine.substring(shadowLine.indexOf(":"), shadowLine.indexOf(":", shadowLine.indexOf(":") + 1));
                shash = shash.substring(shash.indexOf("$") + 2);
                salt = shash.substring(shash.indexOf("$") + 1, shash.indexOf("$", shash.indexOf("$")+1));
                hash = shash.substring(shash.indexOf("$", shash.indexOf("$")+1) + 1);

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
                String hashedPassword = MD5Shadow.crypt(password ,shadowEntry.salt);
                /*System.out.println(cycle + ": " + hashedPassword + " - " + shadowEntry.hash);
                cycle++;*/
                if (hashedPassword.equals(shadowEntry.hash)){
                    System.out.println(shadowEntry.username + ":" + password);
                    passwordsFound++;
                }
            }
        }
        System.out.println("Found " + passwordsFound + " passwords!");
        System.out.println("Finished looking for collisions, exiting SimpleCracker");
    }

}