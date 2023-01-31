package network;

import lombok.Getter;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NetworkRequest {
    @Getter
    String message;
    @Getter
    private Socket socket;
    public NetworkRequest(Socket s) throws IOException {
        this.socket = s;
        this.message = (new String(s.getInputStream().readAllBytes(), StandardCharsets.UTF_8)).split("\n")[0];
        if(!message.startsWith("GET /jintrasearch?")) {
            throw new IOException("Unknown request");
        }
    }

}
