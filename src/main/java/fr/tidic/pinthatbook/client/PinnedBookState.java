package fr.tidic.pinthatbook.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PinnedBookState {
    private static PinnedBook pinned;
    private static ItemStack pinnedStack = ItemStack.EMPTY;
    private static int page;
    private static boolean visible = true;
    private static boolean progressTracking = true;

    public static Optional<PinnedBook> get() {
        return Optional.ofNullable(pinned);
    }

    public static ItemStack pinnedStack() {
        return pinnedStack;
    }

    public static int page() {
        return page;
    }

    public static boolean isVisible() {
        return visible;
    }

    public static boolean isProgressTracking() {
        return progressTracking;
    }

    public static void toggleVisible() {
        if (pinned == null) return;
        visible = !visible;
        PinPersistence.save();
    }

    public static void toggleProgressTracking() {
        progressTracking = !progressTracking;
        PinPersistence.save();
    }

    public static void nextPage() {
        if (pinned != null && page < pinned.pageCount() - 1) {
            page++;
            PinPersistence.save();
        }
    }

    public static void prevPage() {
        if (pinned != null && page > 0) {
            page--;
            PinPersistence.save();
        }
    }

    public static void unpin() {
        pinned = null;
        pinnedStack = ItemStack.EMPTY;
        page = 0;
        PinPersistence.save();
    }

    public static void pinFrom(ItemStack stack) {
        Optional<PinnedBook> book = readBook(stack);
        if (book.isEmpty()) {
            unpin();
            return;
        }
        pinned = book.get();
        pinnedStack = stack.copy();
        page = 0;
        visible = true;
        PinPersistence.save();
    }

    public static void restoreFrom(ItemStack stack, int savedPage, boolean savedVisible, boolean savedProgress) {
        Optional<PinnedBook> book = readBook(stack);
        if (book.isEmpty()) return;
        pinned = book.get();
        pinnedStack = stack.copy();
        page = Math.max(0, Math.min(savedPage, pinned.pageCount() - 1));
        visible = savedVisible;
        progressTracking = savedProgress;
    }

    private static Optional<PinnedBook> readBook(ItemStack stack) {
        if (stack.is(Items.WRITTEN_BOOK)) {
            WrittenBookContent content = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
            if (content == null) return Optional.empty();
            Component title = Component.literal(content.title().raw());
            List<Component> pages = new ArrayList<>();
            content.pages().forEach(f -> pages.add(f.raw()));
            return Optional.of(new BookPin(title, pages));
        }
        if (stack.is(Items.WRITABLE_BOOK)) {
            WritableBookContent content = stack.get(DataComponents.WRITABLE_BOOK_CONTENT);
            if (content == null) return Optional.empty();
            List<Component> pages = new ArrayList<>();
            content.pages().forEach(f -> pages.add(Component.literal(f.raw())));
            return Optional.of(new BookPin(Component.translatable("pinthatbook.draft_title"), pages));
        }
        return SchematicReaders.INSTANCE.read(stack);
    }
}
