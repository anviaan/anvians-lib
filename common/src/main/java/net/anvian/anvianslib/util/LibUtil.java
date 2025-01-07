package net.anvian.anvianslib.util;

import net.minecraft.SharedConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LibUtil {
    public static float ticksToSeconds(int cooldown) {
        return (float) cooldown / 20;
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getName();
    }

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
}
