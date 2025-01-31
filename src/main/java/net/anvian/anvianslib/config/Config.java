package net.anvian.anvianslib.config;

import java.io.File;

public abstract class Config {
    protected static File configFile;

    public static void loadConfig() {
        throw new UnsupportedOperationException("loadConfig no está implementado");
    }

    public static void saveConfig() {
        throw new UnsupportedOperationException("saveConfig no está implementado");
    }
}
