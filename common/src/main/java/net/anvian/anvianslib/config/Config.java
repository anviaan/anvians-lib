package net.anvian.anvianslib.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Generic configuration manager that loads and saves a single JSON-backed configuration object.
 *
 * <p>This abstract class handles reading and writing a configuration file using Gson. Subclasses
 * must provide the concrete configuration type and implement {@link #createDefaultConfig()} to
 * supply defaults when the file is missing or unreadable.
 *
 * @param <T> the configuration POJO type that will be serialized/deserialized
 */
public abstract class Config<T> {

    protected File configFile;
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    protected T config;
    private final Class<T> configClass;
    private final Logger logger;
    private String modId;

    /**
     * Create a new Config manager for the given config class and logger.
     *
     * @param configClass the concrete class of the configuration POJO
     * @param logger      a logger used for error reporting
     */
    public Config(Class<T> configClass, Logger logger) {
        this.configClass = configClass;
        this.logger = logger;
    }

    /**
     * Returns the currently loaded configuration instance. May be null until {@link #initialize(File, String)}
     * or {@link #loadConfig()} has been called.
     *
     * @return the loaded configuration object or null
     */
    public T getConfig() {
        return config;
    }

    /**
     * Initialize the manager by setting the directory and mod id used to derive the config filename
     * and then loading the configuration from disk (creating defaults if necessary).
     *
     * @param configDir the directory where the config file will be stored
     * @param modId     an identifier used to name the config file (creates "{modId}-config.json")
     */
    public void initialize(File configDir, String modId) {
        this.modId = modId;
        configFile = new File(configDir, modId + "-config.json");
        loadConfig();
    }

    /**
     * Create a default instance of the configuration. Implementations should return a fully
     * initialized configuration object with sensible defaults.
     *
     * @return a default configuration instance
     */
    protected abstract T createDefaultConfig();

    /**
     * Load the configuration from disk. If the file does not exist, a default config is created
     * and immediately saved. If an IO error occurs while reading, the default is created and
     * the error is logged.
     */
    public void loadConfig() {
        if (!configFile.exists()) {
            config = createDefaultConfig();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, configClass);
            } catch (IOException e) {
                logger.error("Failed to load config for {}", modId, e);
                config = createDefaultConfig();
            }
        }
    }

    /**
     * Persist the current configuration to disk. Any IO errors are logged but not rethrown.
     */
    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            logger.error("Failed to save config for {}", modId, e);
        }
    }
}
