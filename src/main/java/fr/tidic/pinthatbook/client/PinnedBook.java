package fr.tidic.pinthatbook.client;

import net.minecraft.network.chat.Component;

public sealed interface PinnedBook permits BookPin, SchematicPin {
    Component title();
    int pageCount();
    Component renderPage(int pageIndex);
}
