package net.anvian.anvianslib;

import net.neoforged.fml.common.Mod;

/** NeoForge entrypoint for Anvian's Lib. */
@Mod(Constants.MOD_ID)
public class NeoForgeMod {
    /** Initializes the common library code on NeoForge. */
    public NeoForgeMod() {
        CommonMod.init();
    }
}
