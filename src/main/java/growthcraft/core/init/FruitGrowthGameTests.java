package growthcraft.core.init;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.config.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Server regression coverage for fruit spawning and maturity-gated loot. */
@GameTestHolder(Reference.MODID)
@PrefixGameTestTemplate(false)
public final class FruitGrowthGameTests {
    private FruitGrowthGameTests() {}

    @GameTest(template = "fruit_growth_test")
    public static void fruitGrowthAndLoot(GameTestHelper helper) {
        BlockPos leaf = new BlockPos(1, 3, 1);
        BlockPos neighbor = leaf.east();
        BlockState leaves = GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get().defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true);
        helper.setBlock(leaf, leaves);
        helper.setBlock(neighbor, leaves);
        RandomSource random = RandomSource.create(88L);
        for (int attempt = 0; attempt < 32; attempt++) {
            helper.setBlock(leaf.below(), Blocks.AIR);
            helper.setBlock(neighbor.below(), Blocks.AIR);
            // Neighbor-update ticks must update leaf distance without spawning fruit.
            helper.getBlockState(leaf).tick(helper.getLevel(), helper.absolutePos(leaf), random);
            helper.assertBlockPresent(Blocks.AIR, leaf.below());
            leaves.randomTick(helper.getLevel(), helper.absolutePos(leaf), random);
            helper.assertBlockPresent(GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get(), leaf.below());
            helper.assertBlockPresent(Blocks.AIR, neighbor.below());
        }
        helper.setBlock(leaf.below(), GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get());
        helper.setBlock(neighbor.below(), GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get());
        BlockPos third = leaf.south();
        helper.setBlock(third, leaves);
        leaves.randomTick(helper.getLevel(), helper.absolutePos(third), random);
        helper.assertBlockPresent(Blocks.AIR, third.below());
        // A decaying leaf must not spawn fruit beneath another surviving leaf.
        helper.setBlock(leaf.below(), Blocks.AIR);
        helper.setBlock(neighbor.below(), Blocks.AIR);
        BlockState decaying = leaves.setValue(LeavesBlock.PERSISTENT, false).setValue(LeavesBlock.DISTANCE, 7);
        helper.setBlock(leaf, decaying);
        decaying.randomTick(helper.getLevel(), helper.absolutePos(leaf), random);
        helper.assertBlockPresent(Blocks.AIR, neighbor.below());
        helper.assertBlockPresent(Blocks.AIR, third.below());

        verifyFruitLoot(helper, GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get(), Items.APPLE);
        verifyFruitLoot(helper, GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get());
        verifyFruitLoot(helper, GrowthcraftCellarBlocks.RED_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_RED.get());
        verifyFruitLoot(helper, GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_WHITE.get());
        helper.succeed();
    }

    private static void verifyFruitLoot(GameTestHelper helper, Block fruit, Item item) {
        for (int age = 0; age <= 7; age++) {
            BlockState state = fruit.defaultBlockState().setValue(BlockStateProperties.AGE_7, age);
            var drops = Block.getDrops(state, helper.getLevel(), helper.absolutePos(new BlockPos(1, 2, 1)), null);
            int count = drops.stream().filter(stack -> stack.is(item)).mapToInt(ItemStack::getCount).sum();
            helper.assertTrue(count == (age == 7 ? 1 : 0), fruit + " age " + age + " has incorrect fruit drops");
        }
    }

}
