import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

public class Peer {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-- Enter username and port for this peer: --");
        String[] config = reader.readLine().split(" "); // [0] - username, [1] - port

        ServerThread serverThread = new ServerThread(config[1]);
        serverThread.start();
        Peer peer = new Peer();
        peer.updatePeerListener(reader, config[0], serverThread);
    }

    public void updatePeerListener(BufferedReader reader, String username, ServerThread serverThread) throws IOException {
        System.out.println("-- Enter peers (host:port) to recieve from space seperated: --");
        String[] otherPeers = reader.readLine().split(" ");
        for (String otherPeer : otherPeers) {
            String[] hostPort = otherPeer.split(":");
            Socket socket = new Socket(hostPort[0], Integer.parseInt(hostPort[1]));

            PeerThread peerThread = new PeerThread(socket);
            peerThread.start();
        }
        this.enterCommunication(reader, username, serverThread);
    }

    public void enterCommunication(BufferedReader reader, String username, ServerThread serverThread) throws IOException {
        System.out.println("-- You can now communicate with your peers --");
        while (true) {
            String message = reader.readLine();
            if (message.equals("exit")) {
                break;
            }else if (message.equals("change")){
                this.updatePeerListener(reader, username, serverThread);
            }
            StringWriter writer = new StringWriter();
            JsonObject json = Json.createObjectBuilder().add("username", username).add("message", message).build();
            Json.createWriter(writer).writeObject(json);
            serverThread.exchangeCommunication(writer.toString());
        }
        System.exit(0);
    }
}
