package net.anvian.anvianslib.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public static void sendTelemetryData(String modId, String modVersion, String game_version) {
        if (config.isEnableTelemetry()) {
            try {
                URL url = URI.create("https://anvian.net/telemetry/data").toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setDoOutput(true);

                JsonObject jsonInput = new JsonObject();
                jsonInput.addProperty("mod_id", modId);
                jsonInput.addProperty("mod_version", modVersion);
                jsonInput.addProperty("game_version", game_version);
                String jsonInputString = jsonInput.toString();

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            } catch (IOException e) {
                Constants.LOG.error("Failed to send telemetry data from {}", modId);
            }
        }
    }

    public static class TelemetryConfig {
        private boolean enableTelemetry = true;

        public boolean isEnableTelemetry() {
            return enableTelemetry;
        }

        public void setEnableTelemetry(boolean enableTelemetry) {
            this.enableTelemetry = enableTelemetry;
        }
    }
}
