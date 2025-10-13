package net.anvian.anvianslib.telemetry;

import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Encapsula la lógica de envío de datos de telemetría.
 */
public class TelemetrySender {
    private final HttpClient client;

    public TelemetrySender() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Envía los datos de telemetría al endpoint configurado.
     * @param modId ID del mod
     * @param modVersion Versión del mod
     * @param gameVersion Versión de Minecraft
     * @param loader Loader utilizado
     * @param isProduction true si es entorno de producción
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

