package trivia;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.json.JSONObject;

@WebSocket
public class EchoWebSocket {
    private String sender, msg;
    static BiMap<String, Session> biMapSession = HashBiMap.create();
    static BiMap<String, String>  biMapUsername = HashBiMap.create();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "user"+App.nextUserNumber++;
        biMapSession.put(username,user);
        App.broadcastMessage(sender = "server", msg = "build", user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = biMapSession.inverse().get(user);
        biMapSession.remove(user);
        biMapUsername.remove(username);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        App.broadcastMessage(sender = biMapSession.inverse().get(user), msg = message, user);
    }
}