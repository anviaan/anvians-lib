package net.anvian.anvianslib.fabric;

import net.anvian.anvianslib.CommonMod;
import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonMod.init();
    }
}
