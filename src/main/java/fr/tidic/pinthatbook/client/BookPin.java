package fr.tidic.pinthatbook.client;

import net.minecraft.network.chat.Component;

import java.util.List;

public record BookPin(Component title, List<Component> pages) implements PinnedBook {
    @Override
    public int pageCount() {
        return Math.max(1, pages.size());
    }

    @Override
    public Component renderPage(int pageIndex) {
        if (pages.isEmpty()) return Component.translatable("pinthatbook.empty_book");
        return pages.get(Math.max(0, Math.min(pageIndex, pages.size() - 1)));
    }
}
