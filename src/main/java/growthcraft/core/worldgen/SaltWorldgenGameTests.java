package growthcraft.core.worldgen;

import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftGameRules;
import net.minecraft.world.level.GameRules;
import net.minecraft.nbt.NbtOps;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Uses the loaded worldgen registries and config; restores every edited setting. */
@GameTestHolder(Reference.MODID)
@PrefixGameTestTemplate(false)
public final class SaltWorldgenGameTests {
    private SaltWorldgenGameTests() {}

    @GameTest(template = "fruit_growth_test")
    public static void saltRespectsRuntimeConfig(GameTestHelper helper) {
        Map<ModConfigSpec.ConfigValue<Object>, Object> saved = new LinkedHashMap<>();
        var server = helper.getLevel().getServer();
        var rules = helper.getLevel().getGameRules();
        boolean previousRule = rules.getBoolean(GrowthcraftGameRules.SALT_GENERATION);
        try {
            rules.getRule(GrowthcraftGameRules.SALT_GENERATION).set(true, server);
            var defaults = new GameRules();
            helper.assertTrue(defaults.getBoolean(GrowthcraftGameRules.SALT_GENERATION),
                    "New worlds and worlds without a saved salt rule must default to enabled");
            defaults.getRule(GrowthcraftGameRules.SALT_GENERATION).set(false, null);
            var tag = defaults.createTag();
            helper.assertTrue("false".equals(tag.getString("growthcraftSaltGeneration")),
                    "World NBT must contain the chosen salt rule");
            var loaded = new GameRules(new Dynamic<>(NbtOps.INSTANCE, tag));
            helper.assertTrue(!loaded.getBoolean(GrowthcraftGameRules.SALT_GENERATION)
                    && !loaded.copy().getBoolean(GrowthcraftGameRules.SALT_GENERATION),
                    "Disabled rule must survive saving, loading and world-creation copies");
            set(saved, "saltOreGenEnabled", true);
            set(saved, "saltOreGenNetherEnabled", true);
            set(saved, "saltOreGenTheEndEnabled", true);
            set(saved, "saltOreGenDeepslateEnabled", true);
            set(saved, "saltOreGenSpreadAmount", 3);
            set(saved, "saltOreGenHeightMin", 20);
            set(saved, "saltOreGenHeightMax", 22);
            var level = helper.getLevel();
            var context = new PlacementContext(level, level.getChunkSource().getGenerator(), Optional.empty());
            var origin = new BlockPos(0, 0, 0);
            var positions = SaltOrePlacement.INSTANCE.getPositions(context, RandomSource.create(42), origin).toList();
            helper.assertTrue(positions.size() == 3, "Runtime spread must control attempt count");
            helper.assertTrue(positions.stream().allMatch(p -> p.getY() >= 20 && p.getY() <= 22
                    && p.getX() >= 0 && p.getX() < 16 && p.getZ() >= 0 && p.getZ() < 16), "Runtime height and horizontal bounds");
            set(saved, "saltOreGenSpreadAmount", 1);
            helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(context, RandomSource.create(42), origin).count() == 1,
                    "Reducing spread must apply without rebuilding resources");
            var key = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "salt_ore");
            ConfiguredFeature<?, ?> feature = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(key);
            helper.assertTrue(feature != null && feature.feature() instanceof SaltOreFeature, "Loaded resource must use runtime salt feature");
            var placed = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE)
                    .get(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "salt_ore_placed"));
            helper.assertTrue(placed != null && placed.placement().getFirst() instanceof SaltOrePlacement,
                    "Loaded placed feature must use runtime config placement");
            var original = (OreConfiguration) feature.config();
            helper.assertTrue(original.targetStates.stream().anyMatch(t -> t.state.is(GrowthcraftBlocks.SALT_ORE_NETHER.get())
                    && t.target.test(net.minecraft.world.level.block.Blocks.NETHERRACK.defaultBlockState(), RandomSource.create(42))),
                    "Nether target must match netherrack using an existing tag");
            for (String biomeName : java.util.List.of("plains", "nether_wastes", "end_highlands")) {
                var biome = level.registryAccess().registryOrThrow(Registries.BIOME)
                        .get(ResourceLocation.withDefaultNamespace(biomeName));
                helper.assertTrue(biome.getGenerationSettings().features().stream()
                        .flatMap(set -> set.stream()).anyMatch(holder -> holder.value() == placed),
                        "Salt placed feature must be attached to biome: " + biomeName);
            }

            set(saved, "saltOreGenVeinSize", 2);
            set(saved, "saltOreGenDeepslateEnabled", false);
            var config = SaltOreFeature.runtimeConfiguration(original);
            helper.assertTrue(config.size == 2 && config.targetStates.stream().noneMatch(t -> t.state.is(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get())),
                    "Runtime vein size and deepslate switch");
            helper.assertTrue(config.targetStates.stream().anyMatch(t -> t.state.is(GrowthcraftBlocks.SALT_ORE.get())),
                    "Disabling deepslate must preserve stone salt");
            set(saved, "saltOreGenDeepslateEnabled", true);
            helper.assertTrue(SaltOreFeature.runtimeConfiguration(original).targetStates.size() == 4, "All targets can be re-enabled");
            // Positive control: the loaded feature actually replaces stone when enabled.
            for (BlockPos p : BlockPos.betweenClosed(0, 0, 0, 4, 4, 4)) {
                helper.setBlock(p, net.minecraft.world.level.block.Blocks.STONE);
            }
            // The ore feature expects generation heightmaps, which completed test chunks lack.
            var center = new net.minecraft.world.level.ChunkPos(helper.absolutePos(new BlockPos(2, 3, 2)));
            for (int x = center.x - 1; x <= center.x + 1; x++) {
                for (int z = center.z - 1; z <= center.z + 1; z++) {
                    net.minecraft.world.level.levelgen.Heightmap.primeHeightmaps(level.getChunk(x, z),
                            java.util.EnumSet.of(net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR_WG));
                }
            }
            set(saved, "saltOreGenVeinSize", 10);
            boolean generated = false;
            for (int seed = 0; seed < 20 && !generated; seed++) {
                generated = feature.place(level, context.generator(), RandomSource.create(seed),
                        helper.absolutePos(new BlockPos(2, 3, 2)));
            }
            helper.assertTrue(generated, "Enabled loaded feature must generate salt in stone");
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(),
                    "gamerule growthcraftSaltGeneration false");
            helper.assertTrue(!rules.getBoolean(GrowthcraftGameRules.SALT_GENERATION),
                    "The gamerule command must change this world's setting");
            helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(context, RandomSource.create(42), origin).findAny().isEmpty(),
                    "World rule must block placement with config enabled");
            helper.assertTrue(!feature.place(level, context.generator(), RandomSource.create(42), helper.absolutePos(new BlockPos(2, 2, 2))),
                    "World rule must also block direct feature placement");
            for (var dimension : java.util.List.of(Level.NETHER, Level.END)) {
                var other = server.getLevel(dimension);
                var otherContext = new PlacementContext(other, other.getChunkSource().getGenerator(), Optional.empty());
                helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(otherContext, RandomSource.create(42), origin).findAny().isEmpty(),
                        "World rule must block generation in dimension " + dimension);
            }
            rules.getRule(GrowthcraftGameRules.SALT_GENERATION).set(true, server);
            helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(context, RandomSource.create(42), origin).count() == 1,
                    "Re-enabling the world rule must permit placement");
            set(saved, "saltOreGenEnabled", false);
            helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(context, RandomSource.create(42), origin).findAny().isEmpty(), "Master disable must prevent all attempts");
            helper.assertTrue(!feature.place(level, context.generator(), RandomSource.create(42), helper.absolutePos(new BlockPos(2, 2, 2))),
                    "Direct feature placement must also respect master disable");
            set(saved, "saltOreGenEnabled", true);
            set(saved, "saltOreGenHeightMin", 30);
            helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(context, RandomSource.create(42), origin).findAny().isEmpty(), "Inverted heights must safely skip generation");
            set(saved, "saltOreGenHeightMin", 20);
            for (var dimension : java.util.List.of(Level.NETHER, Level.END)) {
                var other = level.getServer().getLevel(dimension);
                var otherContext = new PlacementContext(other, other.getChunkSource().getGenerator(), Optional.empty());
                String flag = dimension.equals(Level.NETHER) ? "saltOreGenNetherEnabled" : "saltOreGenTheEndEnabled";
                set(saved, flag, false);
                helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(otherContext, RandomSource.create(42), origin).findAny().isEmpty(), "Dimension disable: " + dimension);
                set(saved, flag, true);
                helper.assertTrue(SaltOrePlacement.INSTANCE.getPositions(otherContext, RandomSource.create(42), origin).count() == 1, "Dimension re-enable: " + dimension);
            }
        } finally {
            saved.forEach(ModConfigSpec.ConfigValue::set);
            rules.getRule(GrowthcraftGameRules.SALT_GENERATION).set(previousRule, server);
        }
        helper.succeed();
    }

    private static void set(Map<ModConfigSpec.ConfigValue<Object>, Object> saved, String name, Object value) {
        ModConfigSpec.ConfigValue<Object> setting = GrowthcraftConfig.SPEC.getValues().get("worldgen." + name);
        saved.putIfAbsent(setting, setting.get());
        setting.set(value);
    }
}
