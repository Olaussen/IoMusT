import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerThread extends Thread{

    private final BufferedReader reader;
    private Socket socket;

    public PeerThread(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
    }

    public void run() {
        while (true) {
            try {
                JsonObject json = Json.createReader(this.reader).readObject();
                if (json.containsKey("username") && json.containsKey("message")) {
                    String username = json.getString("username");
                    String message = json.getString("message");
                    System.out.println("[" + username + "]: " + message);
                }
            } catch (Exception e) {
                System.out.println("-- An error occurred. Interrupting --");
                interrupt();
                break;
            }
        }
    }

    public Socket getSocket() {
        return this.socket;
    }
}
