package net.anvian.anvianslib;

import net.fabricmc.api.ModInitializer;

/**
 * Fabric entrypoint for Anvian's Lib.
 */
public class FabricMod implements ModInitializer {
    /**
     * Creates the Fabric entrypoint.
     */
    public FabricMod() {}

    /**
     * Initializes the common library code on Fabric.
     */
    @Override
    public void onInitialize() {
        CommonMod.init();
    }
}
