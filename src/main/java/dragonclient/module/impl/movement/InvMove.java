package dragonclient.module.impl.movement;


import net.lax1dude.eaglercraft.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;

public class InvMove extends Module {
    public BooleanSetting aacp = new BooleanSetting("InvMove_AACP", false);

    public InvMove() {
        super("InvMove", Category.MOVEMENT);
        addSettings(aacp);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }

    public void onUpdateEvent(UpdateEvent event) {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] key;
            KeyBinding[] array = key = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump};
            int length = array.length;
            int i2 = 0;
            while (i2 < length) {
                KeyBinding b2 = array[i2];
                KeyBinding.setKeyBindState(b2.getKeyCode(), Keyboard.isKeyDown(b2.getKeyCode()));
                ++i2;
            }
            if (this.aacp.get()) {
                mc.player.setSprinting(false);
            }
        }
    }
}
