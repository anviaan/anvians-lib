package net.anvian.anvianslib;

import net.anvian.anvianslib.util.LibUtil;

public class CommonMod {
    public static void init() {
        Constants.LOG.info("{} initialized in Minecraft {}", Constants.MOD_NAME, LibUtil.getMinecraftVersion());
    }
}