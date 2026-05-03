package dragonclient;

import net.lax1dude.eaglercraft.profile.EaglerProfile;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.json.JSON;

import org.teavm.jso.websocket.WebSocket;

import dragonclient.irc.IRCUtils;
import dragonclient.irc.OpCodes;

import java.util.Timer;
import java.util.TimerTask;

public class IRCServer implements IRCUtils {
    private WebSocket socket;
    private String userKey;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    public IRCServer() {
        // Initialize the WebSocket connection
        socket = WebSocket.create("ws://82.170.41.118:8080/");

        // Set up the event listeners
        socket.onOpen(evt -> onOpen());
        socket.onMessage(evt -> onMessage(convertToString(evt.getData())));
        socket.onClose(evt -> onClose(evt.getReason(), evt.getCode()));
    }

    public void onOpen() {
        System.out.println("[IRC] Connected to IRC");

        JSONObject object = JSONObject.create();
        object.put("op", OpCodes.CONNECT);
        object.put("d", EaglerProfile.getName());

        sendMessage(object.stringify());

        // Create a task timeout for pinging the IRC server
        timer.schedule(timerTask = new TimerTask() {
            @Override
            public void run() {
                JSONObject ping = JSONObject.create();
                ping.put("op", OpCodes.PING);
                ping.put("d", "");

                sendMessage(ping.stringify());
            }
        }, 20L * 1000L);

        sendIRCMessage("Connected to IRC");
    }

    private void onMessage(String message) {
        JSONObject object = JSON.parse(message).cast();

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

    public void onClose(String reason, int code) {
        sendIRCMessage("You were disconnected from the IRC chat.");
        System.out.println("[IRC] Disconnected. Code: " + code + ", reason: " + reason);
    }

    public void disconnect() {
        if (timerTask != null) {
            timerTask.cancel();
        }

        timer.cancel();

        if (socket != null) {
            socket.close();
        }
    }

    public void sendMessage(String msg) {
        if (socket.getReadyState() == 1) {
            socket.send(msg);
        } else {
            sendIRCMessage("You are not currently connected to the IRC server");
        }
    }

    public void sendIRCChatMessage(String message) {
        JSONObject object = JSONObject.create();
        object.put("op", OpCodes.MESSAGE_SEND);
        object.put("d", message);

        sendMessage(object.stringify());
    }
    // Utility method to convert JSObject to String
    @JSBody(params = "obj", script = "return obj.toString();")
    private static native String convertToString(JSObject obj);

    private interface JSONObject extends JSObject {
        @JSBody(script = "return {};")
        static JSONObject create() {
            return null; // This method will return a new JavaScript object
        }

        @JSBody(params = {"key", "value"}, script = "this[key] = value;")
        void put(String key, String value);

        @JSBody(params = {"key", "value"}, script = "this[key] = value;")
        void put(String key, int value);

        @JSBody(params = "key", script = "return this[key];")
        String getString(String key);

        @JSBody(params = "key", script = "return this[key];")
        int getInt(String key);

        @JSBody(params = "key", script = "return this.hasOwnProperty(key);")
        boolean has(String key);

        @JSBody(script = "return JSON.stringify(this);")
        String stringify();
    }
}
