package trivia;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

@WebSocket
public class EchoWebSocket {
    private String sender, msg;
    static Map<String,String> userUsernameMap = new HashMap<String,String>();
    static Map<Session,String> usernameMap = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "user"+App.nextUserNumber++;
        System.out.println("------"+userUsernameMap.get(username));
        usernameMap.put(user,username);
        App.broadcastMessage(sender = "server", msg = "build", user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = usernameMap.get(user);
        usernameMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("=======?="+message);
        App.broadcastMessage(sender = usernameMap.get(user), msg = message, user);
    }
}