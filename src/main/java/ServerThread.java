import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread {

    private final Set<ServerThreadEntity> threadEntities = new HashSet<>();
    private final ServerSocket socket;

    public ServerThread(String port) throws IOException {
        this.socket = new ServerSocket(Integer.parseInt(port));
    }

    public void exchangeCommunication(String message) {
        for (ServerThreadEntity entity : threadEntities) {
            entity.getWriter().println(message);
        }
    }

    public void run() {
        try {
            while (true) {
                ServerThreadEntity entity = new ServerThreadEntity(this, socket.accept());
                threadEntities.add(entity);
                entity.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<ServerThreadEntity> getThreadEntities() {
        return this.threadEntities;
    }
}
