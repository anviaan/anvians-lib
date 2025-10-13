package net.anvian.anvianslib.util;

import net.anvian.anvianslib.config.TelemetryConfigManager;
import net.anvian.anvianslib.platform.Services;
import net.minecraft.SharedConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class providing various helper methods for mod development.
 */
public class LibUtil {
    /**
     * Converts game ticks to seconds.
     *
     * @param cooldown The number of ticks to convert
     * @return The equivalent time in seconds as a float
     *
     * @deprecated This method is deprecated because it uses a fixed conversion rate.
     * Use {@link TimeUtil#ticksToSeconds(int)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.2")
    public static float ticksToSeconds(int cooldown) {
        return (float) cooldown / 20;
    }

    /**
     * Gets the current Minecraft version name.
     *
     * @return The current Minecraft version as a string
     */
    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().name();
    }

    /**
     * Creates a config directory for the specified mod if it doesn't exist.
     *
     * @param modId The ID of the mod
     * @param configPath The base config path to create the mod directory in
     * @throws RuntimeException if directory creation fails
     */
    public static void generateConfigPath(String modId, Path configPath) {
        Path configDir = configPath.resolve(modId);
        if (Files.notExists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config directory: " + configDir, e);
            }
        }
    }

    /**
     * Sets up telemetry for a mod by creating its config directory and initializing telemetry data.
     *
     * @param modId The ID of the mod
     * @param modVersion The version of the mod
     * @throws RuntimeException if directory creation fails
     */
    public static void setupTelemetry(String modId, String modVersion) {
        Path modConfigDir = Services.PLATFORM.getConfigPath().resolve(modId);
        if (Files.notExists(modConfigDir)) {
            try {
                Files.createDirectories(modConfigDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config directory: " + modConfigDir, e);
            }
        }

        TelemetryConfigManager.getInstance().initialize(modConfigDir.toFile());
        TelemetryConfigManager.getInstance().sendTelemetryData(modId, modVersion);
    }
}