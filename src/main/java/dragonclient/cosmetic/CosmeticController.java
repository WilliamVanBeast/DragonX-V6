package dragonclient.cosmetic;

import dragonclient.Dragon;
import dragonclient.module.impl.render.Cosmetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticController {

    public static boolean renderTopHat(AbstractClientPlayer player) {
        return Dragon.moduleManager.getModule(Cosmetics.class).isEnabled() && Dragon.moduleManager.getModule(Cosmetics.class).show.get() && shouldRender(player) && Dragon.moduleManager.getModule(Cosmetics.class).tophat.get();
    }

    public static boolean renderBlaze(AbstractClientPlayer player) {
        return Dragon.moduleManager.getModule(Cosmetics.class).isEnabled() && Dragon.moduleManager.getModule(Cosmetics.class).show.get() && shouldRender(player) && Dragon.moduleManager.getModule(Cosmetics.class).blaze.get();
    }

    public static boolean renderCrystalWings(AbstractClientPlayer player) {
        return Dragon.moduleManager.getModule(Cosmetics.class).isEnabled() && Dragon.moduleManager.getModule(Cosmetics.class).show.get() && shouldRender(player) && Dragon.moduleManager.getModule(Cosmetics.class).crystal.get();
    }

    public static boolean renderHalo(AbstractClientPlayer player) {
        return Dragon.moduleManager.getModule(Cosmetics.class).isEnabled() && Dragon.moduleManager.getModule(Cosmetics.class).show.get() && shouldRender(player) && Dragon.moduleManager.getModule(Cosmetics.class).halo.get();
    }

    public static boolean renderGlasses(AbstractClientPlayer player) {
        return Dragon.moduleManager.getModule(Cosmetics.class).isEnabled() && Dragon.moduleManager.getModule(Cosmetics.class).show.get() && shouldRender(player) && Dragon.moduleManager.getModule(Cosmetics.class).glasses.get();
    }

    public static boolean renderDogPet(AbstractClientPlayer player) {
        return Dragon.moduleManager.getModule(Cosmetics.class).isEnabled() &&Dragon.moduleManager.getModule(Cosmetics.class).show.get() && shouldRender(player) && Dragon.moduleManager.getModule(Cosmetics.class).dogPet.get();
    }


    public static float[] getTopHatColor(AbstractClientPlayer player) {
        return new float[] { 1, 0, 0 };
    }

    public static float[] getCrystalWingsColor(AbstractClientPlayer player) {
        return new float[] { 1, 0, 0 };
    }


    public static boolean shouldRender(AbstractClientPlayer player) {
        switch (Dragon.moduleManager.getModule(Cosmetics.class).who.get()) {
            case "Only you":
                return player == Minecraft.getMinecraft().player;
            case "Everyone":
                return true;
            case "Everyone else":
                return player != Minecraft.getMinecraft().player;
        }
        return false;
    }
}