package net.anvian.anvianslib.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TelemetryConfigManager extends Config {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
    private static TelemetryConfig config;

    public static void initialize(File configDir) {
        configFile = new File(configDir, "telemetry.json");
        loadConfig();
    }

    public static void loadConfig() {
        if (!configFile.exists()) {
            config = new TelemetryConfig();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, TelemetryConfig.class);
            } catch (IOException e) {
                Constants.LOG.error("Failed to load config");
                config = new TelemetryConfig();
            }
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save config");
        }
    }

    public static TelemetryConfig getConfig() {
        return config;
    }

    public static void sendTelemetryData(String modId, String modVersion, String game_version, String loader, Boolean isProduction) {
        if (config.isEnableTelemetry()) {
            try {
                URI url = URI.create("http://localhost:5000/telemetry/data");

                JsonObject jsonInput = new JsonObject();
                jsonInput.addProperty("mod_id", modId);
                jsonInput.addProperty("mod_version", modVersion);
                jsonInput.addProperty("game_version", game_version);
                jsonInput.addProperty("loader", loader);

                HttpClient client = HttpClient.newHttpClient();

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

    public static class TelemetryConfig {
        public boolean enableTelemetry;

        public TelemetryConfig() {
            enableTelemetry = true;
        }

        public boolean isEnableTelemetry() {
            return enableTelemetry;
        }
    }
}
