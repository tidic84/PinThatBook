package fr.tidic.pinthatbook.client;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface SchematicReader {
    Optional<PinnedBook> read(ItemStack stack);
}
