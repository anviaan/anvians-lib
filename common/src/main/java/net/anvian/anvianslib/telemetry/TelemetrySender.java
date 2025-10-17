package net.anvian.anvianslib.telemetry;

import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Sends telemetry data to the telemetry endpoint.
 *
 * <p>This class constructs a small JSON payload and posts it to either the production
 * telemetry endpoint or a local development endpoint depending on the provided flag.
 */
public class TelemetrySender {
    private final HttpClient client;

    public TelemetrySender() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Send telemetry information about a mod.
     *
     * @param modId        the mod identifier (used by the server to group events)
     * @param modVersion   the version string of the mod
     * @param gameVersion  the game (Minecraft) version running
     * @param loader       the loader or platform name (e.g., Fabric, Forge)
     * @param isProduction when true, send to the production endpoint; when false, send to a local dev server
     */
    public void send(String modId, String modVersion, String gameVersion, String loader, boolean isProduction) {
        URI url = isProduction ? URI.create("https://anvian.net/telemetry/data") : URI.create("http://localhost:8082/telemetry/data");
        JsonObject jsonInput = new JsonObject();
        jsonInput.addProperty("mod_id", modId);
        jsonInput.addProperty("mod_version", modVersion);
        jsonInput.addProperty("game_version", gameVersion);
        jsonInput.addProperty("loader", loader);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Constants.LOG.info("Telemetry data sent: {}", response.statusCode());
        } catch (IOException | InterruptedException e) {
            Constants.LOG.error("Failed to send telemetry data from {}", modId, e);
        }
    }
}

