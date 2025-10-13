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

    public Config(Class<T> configClass, Logger logger) {
        this.configClass = configClass;
        this.logger = logger;
    }

    public T getConfig() {
        return config;
    }

    public void initialize(File configDir, String modId) {
        this.modId = modId;
        configFile = new File(configDir, modId + "-config.json");
        loadConfig();
    }

    protected abstract T createDefaultConfig();

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

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            logger.error("Failed to save config for {}", modId, e);
        }
    }
}
