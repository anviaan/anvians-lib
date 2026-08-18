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
public final class LibUtil {
    private LibUtil() {}

    /**
     * Gets the current Minecraft version name.
     *
     * @return The current Minecraft version as a string
     */
    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().name();
    }

    /**
     * Creates a directory if it doesn't exist.
     *
     * @param dirPath The path to create
     * @throws RuntimeException if directory creation fails
     */
    private static void createDirectoryIfNotExists(Path dirPath) {
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + dirPath, e);
        }
    }

    /**
     * Creates a config directory for the specified mod if it doesn't exist.
     *
     * @param modId      The ID of the mod (must not be null)
     * @param configPath The base config path to create the mod directory in (must not be null)
     * @throws NullPointerException if modId or configPath is null
     * @throws RuntimeException if directory creation fails
     */
    public static void generateConfigPath(String modId, Path configPath) {
        if (modId == null) {
            throw new NullPointerException("modId must not be null");
        }
        if (configPath == null) {
            throw new NullPointerException("configPath must not be null");
        }
        Path configDir = configPath.resolve(modId);
        createDirectoryIfNotExists(configDir);
    }

    /**
     * Sets up telemetry for a mod by creating its config directory and initializing telemetry data.
     *
     * @param modId      The ID of the mod (must not be null)
     * @param modVersion The version of the mod (must not be null)
     * @throws NullPointerException if modId or modVersion is null
     * @throws RuntimeException if directory creation fails
     */
    public static void setupTelemetry(String modId, String modVersion) {
        if (modId == null) {
            throw new NullPointerException("modId must not be null");
        }
        if (modVersion == null) {
            throw new NullPointerException("modVersion must not be null");
        }
        Path modConfigDir = Services.PLATFORM.getConfigPath().resolve(modId);

        TelemetryConfigManager.getInstance().initialize(modConfigDir.toFile());
        TelemetryConfigManager.getInstance().sendTelemetryData(modId, modVersion);
    }
}
