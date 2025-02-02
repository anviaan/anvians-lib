package net.anvian.anvianslib.forge;

import net.anvian.anvianslib.CommonMod;
import net.anvian.anvianslib.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class ForgeMod {
    public ForgeMod() {
        CommonMod.init();
    }
}
