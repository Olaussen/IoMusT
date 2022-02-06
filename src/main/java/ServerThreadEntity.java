import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadEntity extends Thread{

    private final ServerThread serverThread;
    private final Socket socket;
    private PrintWriter writer;

    public ServerThreadEntity(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    public void run() {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            while (true) serverThread.exchangeCommunication(reader.readLine());
        }catch (IOException e) {
            this.serverThread.getThreadEntities().remove(this);
        }
    }

    public PrintWriter getWriter() {
        return this.writer;
    }
}
