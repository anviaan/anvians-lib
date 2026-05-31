package net.anvian.anvianslib.telemetry;

import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Sends telemetry data to the telemetry endpoint.
 *
 * <p>This class constructs a small JSON payload and posts it to either the production
 * telemetry endpoint or a local development endpoint depending on the provided flag.
 * Requests have a default timeout of 10 seconds to prevent hanging.
 */
public class TelemetrySender {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Send telemetry information about a mod.
     *
     * @param modId        the mod identifier - used by the server to group events (must not be null)
     * @param modVersion   the version string of the mod (must not be null)
     * @param gameVersion  the game (Minecraft) version running (must not be null)
     * @param loader       the loader or platform name, e.g., Fabric, Forge (must not be null)
     * @param isProduction when true, send to the production endpoint; when false, send to a local dev server
     * @throws NullPointerException if any required parameter is null
     */
    public static void send(String modId, String modVersion, String gameVersion, String loader, boolean isProduction) {
        if (modId == null || modVersion == null || gameVersion == null || loader == null) {
            throw new NullPointerException("modId, modVersion, gameVersion, and loader must not be null");
        }
        URI url = isProduction ? URI.create("https://anvian.net/telemetry/data") : URI.create("http://localhost:8082/telemetry/data");
        JsonObject jsonInput = new JsonObject();
        jsonInput.addProperty("mod_id", modId);
        jsonInput.addProperty("mod_version", modVersion);
        jsonInput.addProperty("game_version", gameVersion);
        jsonInput.addProperty("loader", loader);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .timeout(REQUEST_TIMEOUT)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput.toString()))
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            Constants.LOG.info("Telemetry data sent: {}", response.statusCode());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            Constants.LOG.error("Failed to send telemetry data from {}", modId, e);
        }
    }
}

