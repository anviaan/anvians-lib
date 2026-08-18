package net.anvian.anvianslib.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import net.anvian.anvianslib.platform.Services;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

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

    /**
     * The JSON file used to persist the configuration.
     */
    protected File configFile;

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    /**
     * The currently loaded configuration object.
     */
    protected T config;

    private final Class<T> configClass;
    private final Logger logger;
    private String modId;

    /**
     * Create a new Config manager for the given config class and logger.
     *
     * @param configClass the concrete class of the configuration POJO
     * @param logger      a logger used for error reporting
     * @throws NullPointerException if configClass or logger is null
     */
    public Config(Class<T> configClass, Logger logger) {
        this.configClass = Objects.requireNonNull(configClass, "configClass must not be null");
        this.logger = Objects.requireNonNull(logger, "logger must not be null");
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
     * @throws NullPointerException if configDir or modId is null
     */
    public void initialize(File configDir, String modId) {
        Objects.requireNonNull(configDir, "configDir must not be null");
        Objects.requireNonNull(modId, "modId must not be null");
        this.modId = modId;

        try {
            Files.createDirectories(configDir.toPath());
        } catch (IOException | SecurityException e) {
            logger.error("Failed to create config directory: {}", configDir.getAbsolutePath(), e);
        }

        configFile = new File(configDir, modId + "-config.json");
        loadConfig();
    }

    /**
     * Initialize the manager in this mod's platform config directory.
     *
     * @param modId the mod identifier used for the config directory and filename
     * @throws NullPointerException if modId is null or the platform config path is unavailable
     */
    public void initialize(String modId) {
        initialize(Services.PLATFORM.getConfigPath().resolve(modId).toFile(), modId);
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
     * the error is logged. Malformed JSON is treated the same way.
     *
     * @throws IllegalStateException if the manager has not been initialized
     */
    public void loadConfig() {
        ensureInitialized();

        if (!configFile.exists()) {
            config = createDefaultConfig();
            saveConfig();
        } else {
            try (Reader reader = Files.newBufferedReader(configFile.toPath(), StandardCharsets.UTF_8)) {
                T loadedConfig = GSON.fromJson(reader, configClass);
                config = loadedConfig != null ? loadedConfig : createDefaultConfig();
                if (loadedConfig == null) {
                    saveConfig();
                }
            } catch (IOException | JsonParseException | IllegalStateException e) {
                logger.error("Failed to load config for {}", modId, e);
                config = createDefaultConfig();
            }
        }
    }

    /**
     * Persist the current configuration to disk. Any IO errors are logged but not rethrown.
     *
     * @throws IllegalStateException if the manager has not been initialized
     */
    public void saveConfig() {
        ensureInitialized();

        try (Writer writer = Files.newBufferedWriter(configFile.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(config, writer);
        } catch (IOException | JsonIOException e) {
            logger.error("Failed to save config for {}", modId, e);
        }
    }

    private void ensureInitialized() {
        if (configFile == null) {
            throw new IllegalStateException("Config manager has not been initialized");
        }
    }
}
