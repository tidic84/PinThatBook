package fr.tidic.pinthatbook.client;

import fr.tidic.pinthatbook.PinThatBook;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PinPersistence {
    private static final Path FILE = FMLPaths.CONFIGDIR.get()
            .resolve("pinthatbook")
            .resolve("pinned.nbt");

    private PinPersistence() {}

    public static void save() {
        HolderLookup.Provider registries = registries();
        if (registries == null) return;
        try {
            CompoundTag root = new CompoundTag();
            ItemStack stack = PinnedBookState.pinnedStack();
            if (!stack.isEmpty()) {
                root.put("stack", stack.save(registries));
                root.putInt("page", PinnedBookState.page());
                root.putBoolean("visible", PinnedBookState.isVisible());
            }
            Files.createDirectories(FILE.getParent());
            NbtIo.writeCompressed(root, FILE);
        } catch (IOException e) {
            PinThatBook.LOGGER.warn("Failed to save pinned book", e);
        }
    }

    public static void load() {
        if (!Files.exists(FILE)) return;
        HolderLookup.Provider registries = registries();
        if (registries == null) return;
        try {
            CompoundTag root = NbtIo.readCompressed(FILE, NbtAccounter.unlimitedHeap());
            if (!root.contains("stack")) return;
            Tag stackTag = root.get("stack");
            ItemStack.parse(registries, stackTag).ifPresent(stack -> {
                int page = root.getInt("page");
                boolean visible = !root.contains("visible") || root.getBoolean("visible");
                PinnedBookState.restoreFrom(stack, page, visible);
            });
        } catch (IOException e) {
            PinThatBook.LOGGER.warn("Failed to load pinned book", e);
        }
    }

    private static HolderLookup.Provider registries() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) return mc.level.registryAccess();
        if (mc.getConnection() != null) return mc.getConnection().registryAccess();
        return null;
    }
}
