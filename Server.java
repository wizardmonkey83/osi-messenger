import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
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

    public static String decrypt(String message, int shift) {
        return encrypt(message, 26 - shift);
    }

    public static void main(String[] args) {
        String message = "";

        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            message = new String(buffer, 0, bytesRead);
            
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int shift = 3;
        String decryptedMessage = decrypt(message, shift);
        System.out.println("Client said: " + decryptedMessage);
        
    }
}
