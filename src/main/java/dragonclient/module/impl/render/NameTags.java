package dragonclient.module.impl.render;

import dragonclient.Dragon;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.event.Events.RenderNametagEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.ColorSetting;
import dragonclient.util.RenderUtil;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class NameTags extends Module {
    private ColorSetting  color = new ColorSetting("Color", 32,32,32,1);
    private BooleanSetting showHealth = new BooleanSetting("Show Health", true);
    private BooleanSetting showArmor = new BooleanSetting("Show Armor", true);
    private BooleanSetting showEnchantments = new BooleanSetting("Show Enchants", true, () -> showArmor.get());
    public NameTags() {
        super("NameTags", Category.RENDER);
        Dragon.eventManager.registerListener(this, RenderNametagEvent.class);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
        addSettings(color, showHealth, showArmor, showEnchantments);
    }
    
    
    public static void renderItem(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y + 8);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, stack, x - 1, y + 10, null);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    public void onRenderNametagEvent(RenderNametagEvent event) {
        event.cancelEvent();
    }

    public void onRender3DEvent(Render3DEvent event) {
           FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

            for (EntityPlayer entity : mc.world.playerEntities) {

                if (entity.isInvisible() || entity == mc.player)
                    continue;

                GlStateManager.pushMatrix();


                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.field_194147_b - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.field_194147_b - mc.getRenderManager().renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.field_194147_b - mc.getRenderManager().renderPosZ;
                //float distance = mc.player.getDistanceToEntity(entity);


                GlStateManager.translate(x, y + entity.getEyeHeight() + 1.7, z);
                EaglercraftGPU.glNormal3f(0, 1, 0);
                if (mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
                    GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1, 0, 0);
                } else {
                    GlStateManager.rotate(-mc.player.rotationYaw, 0, 1, 0);
                    GlStateManager.rotate(mc.player.rotationPitch, 1, 0, 0);
                }
                float distance = mc.player.getDistanceToEntity(entity),
                        scaleConst_1 = 0.02672f, scaleConst_2 = 0.10f;
                double maxDist = 7.0;


                float scaleFactor = (float) (distance <= maxDist ? maxDist * scaleConst_2 : (double) (distance * scaleConst_2));
                scaleConst_1 *= scaleFactor;

                float scaleBet = (float) (5 * 10E-3);
                scaleConst_1 = Math.min(scaleBet, scaleConst_1);


                GlStateManager.scale(-scaleConst_1, -scaleConst_1, .2f);

                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();


                String colorCode = entity.getHealth() > 15 ? "\247a" : entity.getHealth() > 10 ? "\247e" : entity.getHealth() > 7 ? "\2476" : "\247c";
                int colorrectCode = entity.getHealth() > 15 ? 0xff4DF75B : entity.getHealth() > 10 ? 0xffF1F74D : entity.getHealth() > 7 ? 0xffF7854D : 0xffF7524D;
                String thing = entity.getName() + " " + colorCode + (int) entity.getHealth();
                float namewidth = (float) fr.getStringWidth(thing);


                RenderUtil.drawRect(-namewidth / 2 - 35, 42, namewidth / 2 + 2, 40, 0x90080808);
                    RenderUtil.drawRect(-namewidth / 2 - 35, 42, namewidth / 2 + 15 - (1 - (entity.getHealth() / entity.getMaxHealth())) * (namewidth + 4), 40, colorrectCode);
                    RenderUtil.drawRect(-namewidth / 2 - 35, 20, namewidth / 2 + 15, 40, color.get());


                fr.drawStringWithShadow(entity.getName(), -50, 23, -1);
                if(showHealth.get()) {
                    fr.drawStringWithShadow(colorCode + (int) entity.getHealth(), namewidth / 2, 23, -1);
                }
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();


                double movingArmor = 1.2;

                if (namewidth <= 65) {
                    movingArmor = 2;
                }
                if (namewidth <= 85) {
                    movingArmor = 1.2;
                }

                if (namewidth <= 100) {
                    movingArmor = 1.1;
                }
                    for (int index = 0; index < 5; index++) {

                        if (entity.getItemStackFromSlot(EntityEquipmentSlot.values()[index]) == null)
                            continue;

                        ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.values()[index]);
                        if(showArmor.get()) {
                            renderItem(stack, (int) (index * 19 / movingArmor) - 30, -10);
                        }

                        java.util.Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                        if (showArmor.get()) {
                        if (!enchantments.isEmpty()) {
                            int enchantY = -22;
                            for (java.util.Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                Enchantment enchant = entry.getKey();
                                int level = entry.getValue();
                                String enchantName = enchant.getTranslatedName(level).toString();
                                String shortName = getShortEnchantName(enchantName);
                                String enchantText =(shortName + level);
                                int itemX = (int) (index * 19 / movingArmor) - 30;
                                
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                                int textWidth = fr.getStringWidth(enchantText);
                                fr.drawStringWithShadow(enchantText, itemX * 2 - textWidth / 2, enchantY * 2, 0xFFFFFF);
                                GlStateManager.popMatrix();
                                
                                enchantY -= 6;
                            }
                        }
                    }

                }

                GlStateManager.popMatrix();

            }
        }

    private String getShortEnchantName(String fullName) {
        String lower = fullName.toLowerCase();
        if (lower.contains("protection")) return "prot";
        if (lower.contains("sharpness")) return "sharp";
        if (lower.contains("knockback")) return "kb";
        if (lower.contains("fire aspect")) return "fire";
        if (lower.contains("looting")) return "loot";
        if (lower.contains("efficiency")) return "eff";
        if (lower.contains("unbreaking")) return "unb";
        if (lower.contains("fortune")) return "fort";
        if (lower.contains("respiration")) return "resp";
        if (lower.contains("aqua affinity")) return "aqua";
        if (lower.contains("thorns")) return "thorn";
        if (lower.contains("depth strider")) return "depth";
        if (lower.contains("frost walker")) return "frost";
        if (lower.contains("power")) return "pow";
        if (lower.contains("punch")) return "punch";
        if (lower.contains("flame")) return "flame";
        if (lower.contains("infinity")) return "inf";
        if (lower.contains("mending")) return "mend";
        if (lower.contains("sweeping")) return "sweep";
        if (lower.contains("silk touch")) return "silk";
        if (lower.contains("smite")) return "smite";
        if (lower.contains("bane of arthropods")) return "bane";
        if (lower.contains("curse of vanishing")) return "cv";
        if (lower.contains("curse of binding")) return "cb";
        return fullName.substring(0, Math.min(4, fullName.length()));
    }
}
