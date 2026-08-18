package net.anvian.anvianslib.platform;

import net.anvian.anvianslib.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

/** Fabric implementation of the common platform helper. */
public class FabricPlatformHelper implements IPlatformHelper {

    /** Creates the Fabric platform helper. */
    public FabricPlatformHelper() {}

    /** {@inheritDoc} */
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    /** {@inheritDoc} */
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
