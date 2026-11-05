package dev.pandafurnace;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class PandaFurnaceClient implements ClientModInitializer {
    private static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {
        PandaFurnaceConfig.load();
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.FURNACE, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(BlockEntityType.FURNACE, PandaFurnaceRenderer::new);
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.pandafurnace.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y,
            "category.pandafurnace"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new MobSelectScreen());
            }
        });
    }
}
