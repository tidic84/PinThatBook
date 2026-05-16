package fr.tidic.pinthatbook.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class HudOverlay implements LayeredDraw.Layer {
    private static final int WIDTH = 130;
    private static final int PADDING = 5;
    private static final int BG_COLOR = 0xC0101010;
    private static final int BORDER_COLOR = 0xFF707070;
    private static final int TITLE_COLOR = 0xFFFFE070;
    private static final int META_COLOR = 0xFFAAAAAA;

    @Override
    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (!PinnedBookState.isVisible()) return;
        PinnedBookState.get().ifPresent(book -> draw(graphics, book));
    }

    private void draw(GuiGraphics graphics, PinnedBook book) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int screenW = graphics.guiWidth();
        int screenH = graphics.guiHeight();

        int page = PinnedBookState.page();
        Component pageContent = book.renderPage(page);

        int innerWidth = WIDTH - PADDING * 2;
        List<FormattedCharSequence> titleLines = font.split(book.title(), innerWidth);
        List<FormattedCharSequence> bodyLines = font.split(pageContent, innerWidth);

        int lineH = font.lineHeight + 1;
        int contentH = PADDING * 2
                + titleLines.size() * lineH
                + lineH
                + bodyLines.size() * lineH;
        int height = Math.min(contentH, screenH - 8);

        int x = screenW - WIDTH - 4;
        int y = 4;

        graphics.fill(x, y, x + WIDTH, y + height, BG_COLOR);
        graphics.renderOutline(x, y, WIDTH, height, BORDER_COLOR);

        int textY = y + PADDING;
        for (FormattedCharSequence line : titleLines) {
            graphics.drawString(font, line, x + PADDING, textY, TITLE_COLOR, false);
            textY += lineH;
        }

        Component pageInfo = Component.literal((page + 1) + "/" + book.pageCount());
        graphics.drawString(font, pageInfo, x + WIDTH - PADDING - font.width(pageInfo), textY, META_COLOR, false);
        textY += lineH;

        for (FormattedCharSequence line : bodyLines) {
            if (textY + font.lineHeight > y + height - PADDING) break;
            graphics.drawString(font, line, x + PADDING, textY, -1, false);
            textY += lineH;
        }
    }
}
