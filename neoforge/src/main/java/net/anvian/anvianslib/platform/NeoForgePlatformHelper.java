package net.anvian.anvianslib.platform;

import net.anvian.anvianslib.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

/** NeoForge implementation of the common platform helper. */
public class NeoForgePlatformHelper implements IPlatformHelper {

    /** Creates the NeoForge platform helper. */
    public NeoForgePlatformHelper() {}

    /** {@inheritDoc} */
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    /** {@inheritDoc} */
    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
