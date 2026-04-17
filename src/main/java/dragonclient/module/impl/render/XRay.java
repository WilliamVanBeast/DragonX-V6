package dragonclient.module.impl.render;

import com.google.common.collect.ImmutableList;

import dragonclient.module.Category;
import dragonclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class XRay extends Module {
    public final ImmutableList<Block> xrayBlocks = ImmutableList.of(
        Blocks.COAL_ORE,
        Blocks.IRON_ORE,
        Blocks.GOLD_ORE,
        Blocks.REDSTONE_ORE,
        Blocks.LAPIS_ORE,
        Blocks.DIAMOND_ORE,
        Blocks.EMERALD_ORE,
        Blocks.QUARTZ_ORE,
        Blocks.CLAY,
        Blocks.GLOWSTONE,
        Blocks.CRAFTING_TABLE,
        Blocks.TORCH,
        Blocks.LADDER,
        Blocks.TNT,
        Blocks.COAL_BLOCK,
        Blocks.IRON_BLOCK,
        Blocks.GOLD_BLOCK,
        Blocks.DIAMOND_BLOCK,
        Blocks.EMERALD_BLOCK,
        Blocks.REDSTONE_BLOCK,
        Blocks.LAPIS_BLOCK,
        Blocks.FIRE,
        Blocks.MOSSY_COBBLESTONE,
        Blocks.MOB_SPAWNER,
        Blocks.END_PORTAL_FRAME,
        Blocks.ENCHANTING_TABLE,
        Blocks.BOOKSHELF,
        Blocks.CHEST,
        Blocks.TRAPPED_CHEST,
        Blocks.COMMAND_BLOCK,
        Blocks.LAVA,
        Blocks.FLOWING_LAVA,
        Blocks.WATER,
        Blocks.FLOWING_WATER,
        Blocks.FURNACE,
        Blocks.LIT_FURNACE
    );

    public XRay() {
        super("XRay", Category.RENDER);
    }

    @Override
    protected void onToggle(boolean enabled) {
        mc.renderGlobal.loadRenderers();
    }
}
