package net.anvian.anvianslib.config;

import net.anvian.anvianslib.Constants;
import net.anvian.anvianslib.platform.Services;
import net.anvian.anvianslib.telemetry.TelemetrySender;
import net.anvian.anvianslib.util.LibUtil;

import java.io.File;
import java.util.Optional;

/**
 * Manager for telemetry configuration and sending telemetry data.
 *
 * <p>This class is a singleton that holds a {@link TelemetryConfig} instance loaded from a
 * JSON file. It provides convenience methods to send telemetry data when telemetry is enabled.
 */
public class TelemetryConfigManager extends Config<TelemetryConfigManager.TelemetryConfig> {
    private static final TelemetryConfigManager INSTANCE = new TelemetryConfigManager();

    private TelemetryConfigManager() {
        super(TelemetryConfig.class, Constants.LOG);
    }

    /**
     * Returns the singleton instance of the manager.
     *
     * @return the TelemetryConfigManager singleton
     */
    public static TelemetryConfigManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected TelemetryConfig createDefaultConfig() {
        return new TelemetryConfig();
    }

    /**
     * Returns the loaded telemetry configuration wrapped in an Optional.
     *
     * @return Optional containing the TelemetryConfig if present
     */
    public Optional<TelemetryConfig> getTelemetryConfig() {
        return Optional.ofNullable(config);
    }

    /**
     * Send telemetry data for a mod if telemetry is enabled in the config.
     *
     * @param modId       the mod identifier
     * @param modVersion  the mod version string
     * @param gameVersion the game (Minecraft) version string
     */
    public void sendTelemetryData(String modId, String modVersion, String gameVersion) {
        getTelemetryConfig().filter(TelemetryConfig::isEnableTelemetry).ifPresent(cfg ->
                TelemetrySender.send(
                        modId,
                        modVersion,
                        gameVersion,
                        Services.PLATFORM.getPlatformName(),
                        !Services.PLATFORM.isDevelopmentEnvironment()
                )
        );
    }

    /**
     * Convenience overload that uses the current Minecraft version as the game version.
     *
     * @param modId      the mod identifier
     * @param modVersion the mod version string
     */
    public void sendTelemetryData(String modId, String modVersion) {
        sendTelemetryData(modId, modVersion, LibUtil.getMinecraftVersion());
    }

    /**
     * Initialize the telemetry config manager using the provided config directory. The config
     * filename will be derived from the fixed id "telemetry".
     *
     * @param configDir the directory where the telemetry config file will be stored
     */
    public void initialize(File configDir) {
        super.initialize(configDir, "telemetry");
    }

    /**
     * Simple POJO representing the telemetry configuration options serialized to JSON.
     */
    public static class TelemetryConfig {
        public boolean enableTelemetry;

        public TelemetryConfig() {
            enableTelemetry = true;
        }

        /**
         * Returns whether telemetry is enabled. If false, telemetry will not be sent.
         *
         * @return true when telemetry is enabled
         */
        public boolean isEnableTelemetry() {
            return enableTelemetry;
        }
    }
}