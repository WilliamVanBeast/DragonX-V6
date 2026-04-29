package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.PacketUtil;
import net.minecraft.network.play.client.CPacketPlayer;

public class Regen extends Module {

    private DescriptionSetting description = new DescriptionSetting("Description", "Regenerates your health faster.");
    
    private final ListSetting mode = new ListSetting("Mode", new String[]{
        "Normal",
        "Ghostly"
    }, "Normal");
    private final IntegerSetting health = new IntegerSetting("Health", 15, 0, 20);
    private final IntegerSetting speed = new IntegerSetting("Speed", 20, 1, 300);
    private final IntegerSetting tick = new IntegerSetting("Tick", 1, 1, 20);
    public Regen() {
        super("Regen", Category.COMBAT);
        addSettings(description, mode, health, speed, tick);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
    }

        @Override
    public void onPreMotionEvent(final PreMotionEvent event) {
        if (mc.player.ticksExisted > 10 && mc.player.getHealth() < health.get() && mc.player.ticksExisted % tick.get() == 0) {
            for (int i = 0; i < speed.get(); i++) {
                switch (mode.get()) {
                    case "Normal":
                        PacketUtil.sendPacket(new CPacketPlayer());
                        break;

                    case "Ghostly":
                        PacketUtil.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + 1E-9, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
                        break;
                }
            }
        }
    }
    
}
