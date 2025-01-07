package net.anvian.anvianslib.util;

import net.minecraft.SharedConstants;

public class LibUtil {
    public static float ticksToSeconds(int cooldown) {
        return (float) cooldown / 20;
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getName();
    }
}
