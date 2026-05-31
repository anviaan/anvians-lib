package net.anvian.anvianslib;

import net.anvian.anvianslib.util.LibUtil;

/**
 * Common initialization point for Anvian's Lib across all platforms.
 *
 * <p>This class provides initialization logic shared by both Fabric and NeoForge loaders.
 */
public class CommonMod {
    /**
     * Initialize the Anvian's Lib common module.
     *
     * <p>This method logs the initialization message and is called by platform-specific
     * mod initializers during the mod loading process.
     */
    public static void init() {
        Constants.LOG.info("{} initialized in Minecraft {}", Constants.MOD_NAME, LibUtil.getMinecraftVersion());
    }
}