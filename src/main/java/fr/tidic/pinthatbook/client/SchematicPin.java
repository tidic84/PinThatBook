package fr.tidic.pinthatbook.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public final class SchematicPin implements PinnedBook {
    public static final int ENTRIES_PER_PAGE = 8;

    private final Component title;
    private final List<Entry> entries;

    public SchematicPin(Component title, List<Entry> entries) {
        this.title = title;
        this.entries = entries;
    }

    public record Entry(Block block, int needed) {}

    @Override
    public Component title() {
        return title;
    }

    @Override
    public int pageCount() {
        return Math.max(1, (entries.size() + ENTRIES_PER_PAGE - 1) / ENTRIES_PER_PAGE);
    }

    @Override
    public Component renderPage(int pageIndex) {
        if (entries.isEmpty()) return Component.translatable("pinthatbook.empty_schematic");
        boolean tracking = PinnedBookState.isProgressTracking();
        Map<Item, Integer> have = tracking ? InventoryCache.get() : Map.of();

        int start = pageIndex * ENTRIES_PER_PAGE;
        int end = Math.min(start + ENTRIES_PER_PAGE, entries.size());
        MutableComponent page = Component.empty();
        for (int i = start; i < end; i++) {
            Entry entry = entries.get(i);
            int need = entry.needed();
            page.append(entry.block().getName().copy().withStyle(ChatFormatting.WHITE));
            page.append(Component.literal(" "));
            if (tracking) {
                int got = have.getOrDefault(entry.block().asItem(), 0);
                ChatFormatting color = got >= need ? ChatFormatting.GREEN : ChatFormatting.YELLOW;
                page.append(Component.literal(got + "/" + need).withStyle(color));
            } else {
                page.append(Component.literal("× ").withStyle(ChatFormatting.GRAY));
                page.append(Component.literal(String.valueOf(need)).withStyle(ChatFormatting.YELLOW));
            }
            if (i < end - 1) page.append(Component.literal("\n"));
        }
        return page;
    }
}
