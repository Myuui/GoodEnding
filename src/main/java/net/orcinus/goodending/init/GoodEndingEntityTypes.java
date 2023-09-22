package net.orcinus.goodending.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.orcinus.goodending.GoodEnding;
import net.orcinus.goodending.entities.FireflyEntity;
import net.orcinus.goodending.entities.GoodEndingBoatEntity;
import net.orcinus.goodending.entities.GoodEndingChestBoatEntity;
import net.orcinus.goodending.entities.MarshEntity;
import net.orcinus.goodending.entities.WoodpeckerEntity;

public class GoodEndingEntityTypes {
    public static final EntityType<GoodEndingBoatEntity> BOAT = register("boat", FabricEntityTypeBuilder.<GoodEndingBoatEntity>create().entityFactory(GoodEndingBoatEntity::new).spawnGroup(MobCategory.MISC).dimensions(EntityDimensions.fixed(1.375f, 0.5625f)).trackRangeChunks(10), null);
    public static final EntityType<GoodEndingChestBoatEntity> CHEST_BOAT = register("chest_boat", FabricEntityTypeBuilder.<GoodEndingChestBoatEntity>create().entityFactory(GoodEndingChestBoatEntity::new).spawnGroup(MobCategory.MISC).dimensions(EntityDimensions.fixed(1.375f, 0.5625f)).trackRangeChunks(10), null);
    public static final EntityType<FireflyEntity> FIREFLY_SWARM = register(
            "firefly_swarm",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(FireflyEntity::new)
                    .defaultAttributes(FireflyEntity::createFireflyAttributes)
                    .spawnGroup(MobCategory.CREATURE)
                    .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, FireflyEntity::canSpawn)
                    .dimensions(EntityDimensions.scalable(1F, 1F))
                    .trackRangeBlocks(8),
            new int[]{ 0x000000, 0xFFF4A4 }
    );
    public static final EntityType<MarshEntity> MARSH = register(
            "marsh",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(MarshEntity::new)
                    .defaultAttributes(MarshEntity::createMarshAttributes)
                    .spawnGroup(MobCategory.CREATURE)
                    .dimensions(EntityDimensions.fixed(0.8F, 1F)),
            new int[]{ 3423006, 6979129 }
    );

    public static final EntityType<WoodpeckerEntity> WOODPECKER = register(
        "woodpecker",
        FabricEntityTypeBuilder.createMob()
                               .entityFactory(WoodpeckerEntity::new)
                               .defaultAttributes(WoodpeckerEntity::createWoodPeckerAttributes)
                               .spawnGroup(MobCategory.CREATURE)
                               .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, WoodpeckerEntity::canSpawn)
                               .dimensions(EntityDimensions.scalable(0.35F, 0.65F)),
        new int[]{ 0xCB4613, 0xFFFFFF }
    );

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType, int[] spawnEggColors) {
        if (spawnEggColors != null)
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(GoodEnding.MODID, id + "_spawn_egg"), new SpawnEggItem((EntityType<? extends Mob>) entityType, spawnEggColors[0], spawnEggColors[1], new Item.Properties().stacksTo(64)));

        return Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(GoodEnding.MODID, id), entityType);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, int[] spawnEggColors) {
        return register(id, entityType.build(), spawnEggColors);
    }
}
