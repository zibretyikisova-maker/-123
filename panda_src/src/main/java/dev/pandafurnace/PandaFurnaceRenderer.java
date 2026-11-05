package dev.pandafurnace;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.MobSpawnerBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/** Draws a client-only vanilla spawner mob over the real furnace block entity (visual only). */
public final class PandaFurnaceRenderer implements BlockEntityRenderer<FurnaceBlockEntity> {
    private final MobSpawnerBlockEntityRenderer renderer;
    private MobSpawnerBlockEntity preview;
    private EntityType<?> lastType = null;
    private World lastWorld = null;
    private long lastTick = Long.MIN_VALUE;

    public PandaFurnaceRenderer(BlockEntityRendererFactory.Context context) {
        renderer = new MobSpawnerBlockEntityRenderer(context);
    }

    @Override
    public void render(FurnaceBlockEntity furnace, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider consumers, int light, int overlay) {
        World world = furnace.getWorld();
        if (world == null) return;

        EntityType<?> type = PandaFurnaceConfig.entityType();
        // The spawner caches its rendered mob, so build a fresh preview whenever the mob or world changes.
        if (preview == null || type != lastType || world != lastWorld) {
            preview = new MobSpawnerBlockEntity(BlockPos.ORIGIN, Blocks.SPAWNER.getDefaultState());
            preview.setWorld(world);
            preview.setEntityType(type, Random.create());
            lastType = type;
            lastWorld = world;
            lastTick = Long.MIN_VALUE;
        }
        // Advance the spawner logic once per game tick (not once per frame / per furnace).
        long now = world.getTime();
        if (now != lastTick) {
            preview.getLogic().clientTick(world, furnace.getPos());
            lastTick = now;
        }
        // The furnace is an opaque block (light 0 inside), so always draw the mob fully lit.
        renderer.render(preview, tickDelta, matrices, consumers, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);
    }
}
