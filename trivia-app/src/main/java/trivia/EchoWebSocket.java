package trivia;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class EchoWebSocket {
    private String sender, msg;
    static Map<Session, String> usernameMap = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = usernameMap.get(user);
        App.userUsernameMap.put(user, username);
        App.broadcastMessage(sender = "server", msg = "build",user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = App.userUsernameMap.get(user);
        App.userUsernameMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        App.broadcastMessage(sender = App.userUsernameMap.get(user), msg = message,user);
    }
}