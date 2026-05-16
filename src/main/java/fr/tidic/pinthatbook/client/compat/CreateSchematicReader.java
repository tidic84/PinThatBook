package fr.tidic.pinthatbook.client.compat;

import com.simibubi.create.AllDataComponents;
import fr.tidic.pinthatbook.PinThatBook;
import fr.tidic.pinthatbook.client.PinnedBook;
import fr.tidic.pinthatbook.client.SchematicPin;
import fr.tidic.pinthatbook.client.SchematicReader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CreateSchematicReader implements SchematicReader {

    @Override
    public Optional<PinnedBook> read(ItemStack stack) {
        if (!isCreateSchematic(stack)) return Optional.empty();

        String file = stack.get(AllDataComponents.SCHEMATIC_FILE);
        if (file == null || file.isEmpty()) return Optional.empty();

        Path dir = FMLPaths.GAMEDIR.get().resolve("schematics").normalize();
        Path target = dir.resolve(file).normalize();
        if (!target.startsWith(dir) || !Files.exists(target)) {
            PinThatBook.LOGGER.warn("Schematic not found: {}", target);
            return Optional.empty();
        }

        try {
            CompoundTag root = NbtIo.readCompressed(target, NbtAccounter.unlimitedHeap());
            return Optional.of(buildPin(file, root));
        } catch (IOException e) {
            PinThatBook.LOGGER.warn("Failed to read schematic {}", target, e);
            return Optional.empty();
        }
    }

    private static boolean isCreateSchematic(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getNamespace().equals("create") && id.getPath().equals("schematic");
    }

    private static SchematicPin buildPin(String filename, CompoundTag root) {
        Map<Block, Integer> counts = countBlocks(root);
        List<SchematicPin.Entry> entries = counts.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(e -> new SchematicPin.Entry(e.getKey(), e.getValue()))
                .toList();

        String title = filename;
        if (title.endsWith(".nbt")) title = title.substring(0, title.length() - 4);
        int slash = Math.max(title.lastIndexOf('/'), title.lastIndexOf('\\'));
        if (slash >= 0) title = title.substring(slash + 1);

        return new SchematicPin(Component.literal(title), entries);
    }

    private static Map<Block, Integer> countBlocks(CompoundTag root) {
        ListTag palette = root.getList("palette", Tag.TAG_COMPOUND);
        ListTag blocks = root.getList("blocks", Tag.TAG_COMPOUND);

        int[] paletteCounts = new int[palette.size()];
        for (int i = 0; i < blocks.size(); i++) {
            int state = blocks.getCompound(i).getInt("state");
            if (state >= 0 && state < paletteCounts.length) paletteCounts[state]++;
        }

        Map<Block, Integer> result = new HashMap<>();
        for (int i = 0; i < palette.size(); i++) {
            int count = paletteCounts[i];
            if (count == 0) continue;
            CompoundTag stateTag = palette.getCompound(i);
            ResourceLocation id = ResourceLocation.tryParse(stateTag.getString("Name"));
            if (id == null) continue;
            Block block = BuiltInRegistries.BLOCK.get(id);
            if (block == Blocks.AIR) continue;
            result.merge(block, count, Integer::sum);
        }
        return result;
    }
}
