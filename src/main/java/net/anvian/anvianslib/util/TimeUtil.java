package net.anvian.anvianslib.util;

/**
 * Utility class for converting between game ticks and seconds.
 * In Minecraft, 1 second equals 20 ticks.
 */
public class TimeUtil {
    /**
     * Converts game ticks to seconds.
     *
     * @param cooldown The number of ticks to convert
     * @return The equivalent time in seconds as a float
     */
    public static float ticksToSeconds(int cooldown) {
        return (float) cooldown / 20;
    }

    /**
     * Converts seconds to game ticks.
     *
     * @param seconds The number of seconds to convert
     * @return The equivalent time in ticks as an integer
     */
    public static int secondsToTicks(float seconds) {
        return (int) (seconds * 20);
    }
}
