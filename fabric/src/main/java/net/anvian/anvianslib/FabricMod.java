package net.anvian.anvianslib;

import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonMod.init();
    }
}
