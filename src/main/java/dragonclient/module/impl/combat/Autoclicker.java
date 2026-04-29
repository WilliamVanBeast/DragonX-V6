package dragonclient.module.impl.combat;

import java.util.Random;

import dragonclient.Dragon;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.IntegerSetting;
import net.lax1dude.eaglercraft.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class Autoclicker extends Module{
    private DescriptionSetting description = new DescriptionSetting("Description", "Automatically clicks for you.");
   private final IntegerSetting minCps = new IntegerSetting("Min CPS", 5, 0, 15);
    private final IntegerSetting maxCps = new IntegerSetting("Max CPS", 10, 1, 30);
    public Autoclicker(){
        super("AutoClicker", Category.COMBAT);
        addSettings(minCps, maxCps, description);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
    }


    @Override
    public void onUpdateEvent(UpdateEvent event){
        if(this.maxCps.get() < this.minCps.get()){
            this.maxCps.set(this.minCps.get());
        }
    }

    Random r = new Random();
    @Override
    public void onRender3DEvent(Render3DEvent e) {
            if (mc.currentScreen == null) {
                if (Mouse.isButtonDown(0) && Math.random() * 50 <= minCps.get() + (r.nextDouble() * (maxCps.get() - minCps.get()))) {
                    sendClick(0, true);
                    sendClick(0, false);
                }
            }
        }

    private void sendClick(final int button, final boolean state) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode(), state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
}
}