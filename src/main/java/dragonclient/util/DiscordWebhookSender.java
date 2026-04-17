package dragonclient.util;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhookSender {

    public static void sendMessage(String webhookUrl, String... lines) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            StringBuilder contentBuilder = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                contentBuilder.append(lines[i]);
                if (i < lines.length - 1) {
                    contentBuilder.append("\\n"); // newline for Discord
                }
            }

            String jsonPayload = "{\"content\":\"" + escapeJson(contentBuilder.toString()) + "\"}";

            OutputStream os = connection.getOutputStream();
            os.write(jsonPayload.getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Example usage
    public static void main() {
        String webhook = "https://discord.com/api/webhooks/1491166979195801652/Wfh430U_8GkmGEPA6TUjMQvxg2wSmS4PHsRNShgUCSpXK-AltJ6vzntKtWVsr4BNFLFo";

        sendMessage(webhook,
                "Hello from TeaVM!",
                "This is line 2",
                "This is line 3",
                "Custom multi-line message!");
    }
}
