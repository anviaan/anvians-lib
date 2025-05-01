package net.anvian.anvianslib.config;

import java.io.File;

/**
 * Abstract base class for handling configuration files.
 * Provides basic structure for loading and saving configuration data.
 */
public abstract class Config {
    /**
     * The configuration file that will be read from and written to.
     */
    protected static File configFile;

    /**
     * Loads the configuration from the config file.
     * This method must be implemented by concrete subclasses.
     *
     * @throws UnsupportedOperationException if the method is not implemented
     */
    public static void loadConfig() {
        throw new UnsupportedOperationException("loadConfig no está implementado");
    }

    /**
     * Saves the current configuration to the config file.
     * This method must be implemented by concrete subclasses.
     *
     * @throws UnsupportedOperationException if the method is not implemented
     */
    public static void saveConfig() {
        throw new UnsupportedOperationException("saveConfig no está implementado");
    }
}
