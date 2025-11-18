import java.util.Scanner;
import java.io.*;
import java.net.Socket;

public class Client {
    public static boolean getPassword(Scanner scnr) {
        System.out.println("Enter Password");
        String password = scnr.nextLine();

        return password.equals("password");
    }

    public static String encrypt(String message, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        // toCharArray creates a list of characters from a string
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c)? 'A' : 'a';
                // modulo operator handles the wrap around that is encountered at the end of the alphabet
                char encryptedChar = (char) (((c - base + shift) % 26) + base);
                encryptedText.append(encryptedChar);
            } else {
                // non letter characters are left unchanged
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        // establishing secure session
        boolean authenticated = getPassword(scnr);

        if (authenticated) {
            System.out.println("Enter a message");
            String message = scnr.nextLine();
            scnr.close();

            int shift = 3;
            String encrypted = encrypt(message, shift);

            // send data to the server
            try {
                Socket socket = new Socket("localhost", 1234);
                OutputStream outputStream = socket.getOutputStream();
                System.out.println("Output Stream Established");

                byte[] encryptedBytes = encrypted.getBytes();
                outputStream.write(encryptedBytes);
                outputStream.flush();
                System.out.println("Message sent!");

                outputStream.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Password!");
        }
    }
}
