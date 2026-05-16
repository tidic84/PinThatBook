package fr.tidic.pinthatbook.client;

import net.minecraft.network.chat.Component;

import java.util.List;

public record PinnedBook(Component title, List<Component> pages) {
    public int pageCount() {
        return pages.size();
    }
}
