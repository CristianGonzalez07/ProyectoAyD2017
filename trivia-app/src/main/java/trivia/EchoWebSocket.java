package trivia;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class EchoWebSocket {
    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + App.nextUserNumber++;
        App.userUsernameMap.put(user, username);
        App.broadcastMessage(sender = "server", msg = "build");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = App.userUsernameMap.get(user);
        App.userUsernameMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        App.broadcastMessage(sender = App.userUsernameMap.get(user), msg = message);
    }
}