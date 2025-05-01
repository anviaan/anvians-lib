package net.anvian.anvianslib.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.anvian.anvianslib.Constants;
import net.anvian.anvianslib.platform.Services;
import net.anvian.anvianslib.util.LibUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Manages telemetry configuration and data sending for mods.
 * Handles loading, saving, and managing telemetry settings through a JSON configuration file.
 */
public class TelemetryConfigManager extends Config {
    /** GSON instance configured for telemetry config serialization/deserialization */
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
    /** Current telemetry configuration instance */
    private static TelemetryConfig config;

    /**
     * Initializes the telemetry configuration system.
     * @param configDir Directory where the telemetry configuration file should be stored
     */
    public static void initialize(File configDir) {
        configFile = new File(configDir, "telemetry.json");
        loadConfig();
    }

    /**
     * Loads the telemetry configuration from file.
     * Creates a new configuration with default values if the file doesn't exist.
     */
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

    /**
     * Saves the current telemetry configuration to file.
     */
    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save config");
        }
    }

    /**
     * Retrieves the current telemetry configuration.
     * @return The current TelemetryConfig instance
     */
    public static TelemetryConfig getConfig() {
        return config;
    }

    /**
     * Sends telemetry data with specified mod information and game version.
     * @param modId The ID of the mod
     * @param modVersion The version of the mod
     * @param game_version The Minecraft game version
     */
    public static void sendTelemetryData(String modId, String modVersion, String game_version) {
        if (config != null && config.isEnableTelemetry()) {
            sendTelemetryData(
                    modId,
                    modVersion,
                    game_version,
                    Services.PLATFORM.getPlatformName(),
                    !Services.PLATFORM.isDevelopmentEnvironment()
            );
        }
    }

    /**
     * Sends telemetry data with specified mod information and auto-detected game version.
     * @param modId The ID of the mod
     * @param modVersion The version of the mod
     */
    public static void sendTelemetryData(String modId, String modVersion) {
        if (config != null && config.isEnableTelemetry()) {
            sendTelemetryData(
                    modId,
                    modVersion,
                    LibUtil.getMinecraftVersion(),
                    Services.PLATFORM.getPlatformName(),
                    !Services.PLATFORM.isDevelopmentEnvironment()
            );
        }
    }

    /**
     * Sends detailed telemetry data to the configured endpoint.
     * @param modId The ID of the mod
     * @param modVersion The version of the mod
     * @param game_version The Minecraft game version
     * @param loader The mod loader being used
     * @param isProduction Whether the mod is running in a production environment
     *
     * @deprecated This method is deprecated and should not be used directly.
     * Use {@link #sendTelemetryData(String, String)} instead.
     */
    @Deprecated
    public static void sendTelemetryData(String modId, String modVersion, String game_version, String loader, Boolean isProduction) {
        if (config != null && config.isEnableTelemetry()) {
            try (HttpClient client = HttpClient.newHttpClient()) {
                URI url = (isProduction) ? URI.create("https://anvian.net/telemetry/data") : URI.create("http://localhost:8082/telemetry/data");

                JsonObject jsonInput = new JsonObject();
                jsonInput.addProperty("mod_id", modId);
                jsonInput.addProperty("mod_version", modVersion);
                jsonInput.addProperty("game_version", game_version);
                jsonInput.addProperty("loader", loader);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(url)
                        .header("content-type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonInput.toString()))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                Constants.LOG.info("Telemetry data sent: {}", response.statusCode());
            } catch (IOException | InterruptedException ignored) {
                Constants.LOG.error("Failed to send telemetry data from {}", modId);
            }
        }
    }

    /**
     * Configuration class for telemetry settings.
     */
    public static class TelemetryConfig {
        /** Flag to enable/disable telemetry data collection */
        public boolean enableTelemetry;

        /**
         * Creates a new telemetry configuration with default settings.
         * Telemetry is enabled by default.
         */
        public TelemetryConfig() {
            enableTelemetry = true;
        }

        /**
         * Checks if telemetry is enabled.
         * @return true if telemetry is enabled, false otherwise
         */
        public boolean isEnableTelemetry() {
            return enableTelemetry;
        }
    }
}