package growthcraft.core.init;

import growthcraft.core.config.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Exercises the current rope loot table in a real server level. */
@GameTestHolder(Reference.MODID)
@PrefixGameTestTemplate(false)
public final class RopeLootGameTests {
    private RopeLootGameTests() {}

    @GameTest(template = "rope_loot_test")
    public static void currentRopeDropsOneItem(GameTestHelper helper) {
        BlockPos pos = new BlockPos(2, 2, 2);
        var state = GrowthcraftBlocks.ROPE_LINEN2.get().defaultBlockState();
        helper.setBlock(pos, state);
        for (ItemStack tool : new ItemStack[]{ItemStack.EMPTY, new ItemStack(Items.SHEARS)}) {
            var drops = Block.getDrops(state, helper.getLevel(), helper.absolutePos(pos), null, null, tool);
            helper.assertTrue(drops.size() == 1 && drops.getFirst().is(GrowthcraftItems.ROPE_LINEN2.get())
                    && drops.getFirst().getCount() == 1, "Current rope must drop exactly one rope with tool " + tool);
        }
        helper.getLevel().destroyBlock(helper.absolutePos(pos), true);
        helper.assertBlockPresent(Blocks.AIR, pos);
        helper.assertItemEntityCountIs(GrowthcraftItems.ROPE_LINEN2.get(), pos, 1.0, 1);
        helper.succeed();
    }
}
