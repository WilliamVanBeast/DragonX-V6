package dragonclient;

import net.lax1dude.eaglercraft.profile.EaglerProfile;
import org.glassfish.tyrus.client.ClientManager;
import org.json.JSONObject;

import dragonclient.irc.IRCUtils;
import dragonclient.irc.OpCodes;

import javax.websocket.*;
import javax.websocket.ClientEndpointConfig.Builder;
import javax.websocket.ClientEndpointConfig.Configurator;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


@ClientEndpoint
public class IRCServer extends Endpoint implements IRCUtils{
    private Session session;
    private String userKey;

    private final Timer timer = new Timer();
    private TimerTask timerTask;

    public IRCServer() {

        ClientManager manager = ClientManager.createClient();

        Builder builder = ClientEndpointConfig.Builder.create();
        builder.configurator(new Configurator() {
            @Override
            public void beforeRequest(Map<String, List<String>> headers) {
                headers.put("User-Agent", Collections.singletonList("Custom WS client"));
            }
        });

        try {
            // TODO: i'll use different hosting soon xd
            manager.connectToServer(this, builder.build(), new URI("ws://82.170.41.118:8080/"));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;

        session.addMessageHandler(String.class, this::onMessage);

        System.out.println("[IRC] Connected to IRC");

        JSONObject object = new JSONObject();
        object.put("op", OpCodes.CONNECT);
        object.put("d", EaglerProfile.getName());

        sendMessage(object.toString());

        // create task timeout for pinging the IRC server
        timer.schedule(timerTask = new TimerTask() {
            @Override
            public void run() {
                JSONObject ping = new JSONObject();

                ping.put("op", OpCodes.PING);
                ping.put("d", "");

                sendMessage(ping.toString());
            }
        }, 20L * 1000L);

        sendIRCMessage("Connected to IRC");
    }

    private void onMessage(String message) {
        JSONObject object = new JSONObject(message);

        if (object.has("op") && object.has("d")) {
            int opCode = object.getInt("op");
            String data = object.getString("d");

            switch (opCode) {
                case OpCodes.ID:
                    userKey = data;
                    System.out.println("[IRC] User key received.");
                    break;

                case OpCodes.MESSAGE_RECEIVE:
                    sendIRCMessage(data);
                    break;

                case OpCodes.DISCONNECTED:
                    sendIRCMessage("Disconnected from IRC: " + data);
                    break;
            }
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        sendIRCMessage("You were disconnected from the IRC chat.");
        System.out.println("[IRC] Disconnected. " + closeReason.getCloseCode() + ", reason: " + closeReason.getReasonPhrase());
    }

    public void disconnect() {
        try {

            if (timerTask != null) {
                timerTask.cancel();
            }

            timer.cancel();
            timer.purge();

            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        if (session.isOpen()) {
            session.getAsyncRemote().sendText(msg);
        }

        else {
            sendIRCMessage("You are not currently connected to the IRC server");
        }
    }

    public void sendIRCChatMessage(String message) {
        JSONObject object = new JSONObject();
        object.put("op", OpCodes.MESSAGE_SEND);
        object.put("d", message);

        sendMessage(object.toString());
    }
}
