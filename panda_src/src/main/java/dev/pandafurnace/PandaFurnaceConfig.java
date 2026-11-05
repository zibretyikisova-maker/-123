package dev.pandafurnace;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PandaFurnaceConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("panda-furnace.json");
    private static final String[] IDS = {"minecraft:panda", "minecraft:shulker", "minecraft:magma_cube", "minecraft:creeper", "minecraft:silverfish", "minecraft:blaze"};
    private static final String[] NAMES = {"Панда", "Шалкер", "Магма-куб", "Крипер", "Чешуйница", "Ифрит"};
    private static int selected = 0;

    private PandaFurnaceConfig() {}
    public static int selected() { return selected; }
    public static String name(int i) { return NAMES[i]; }
    public static EntityType<?> entityType() {
        Identifier id = Identifier.of(IDS[selected]);
        return Registries.ENTITY_TYPE.get(id);
    }
    public static void select(int index) {
        if (index < 0 || index >= IDS.length) return;
        selected = index;
        save();
    }
    public static void load() {
        try {
            if (Files.exists(FILE)) {
                String s = Files.readString(FILE, StandardCharsets.UTF_8).trim();
                selected = Math.max(0, Math.min(IDS.length - 1, Integer.parseInt(s)));
            }
        } catch (Exception ignored) { selected = 0; }
    }
    private static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, Integer.toString(selected), StandardCharsets.UTF_8);
        } catch (IOException ignored) { }
    }
}
