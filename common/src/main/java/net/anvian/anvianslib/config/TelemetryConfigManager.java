package net.anvian.anvianslib.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;

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

    public static void sendTelemetryData(String modId, String modVersion, String game_version, String loader , Boolean isProduction) {
        if (config.isEnableTelemetry()) {
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                URI url = (isProduction) ? URI.create("https://anvian.net/telemetry/data") : URI.create("http://localhost:5000/telemetry/data");
                HttpPost request = new HttpPost(url);
                JsonObject jsonInput = new JsonObject();
                jsonInput.addProperty("mod_id", modId);
                jsonInput.addProperty("mod_version", modVersion);
                jsonInput.addProperty("game_version", game_version);
                jsonInput.addProperty("loader", loader);
                request.addHeader("content-type", "application/json");
                StringEntity params = new StringEntity(jsonInput.toString());
                request.setEntity(params);
                HttpResponse response = client.execute(request);
                System.out.println(response);
            } catch (IOException e) {
                Constants.LOG.error("Failed to send telemetry data from {}", modId);
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
