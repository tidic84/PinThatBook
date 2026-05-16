package fr.tidic.pinthatbook.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    public static final String CATEGORY = "key.categories.pinthatbook";

    public static final KeyMapping PIN = new KeyMapping(
            "key.pinthatbook.pin",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_P),
            CATEGORY
    );

    public static final KeyMapping NEXT_PAGE = new KeyMapping(
            "key.pinthatbook.next_page",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_RIGHT_BRACKET),
            CATEGORY
    );

    public static final KeyMapping PREV_PAGE = new KeyMapping(
            "key.pinthatbook.prev_page",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_LEFT_BRACKET),
            CATEGORY
    );

    public static final KeyMapping TOGGLE_VISIBILITY = new KeyMapping(
            "key.pinthatbook.toggle_visibility",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_H),
            CATEGORY
    );

    private KeyBindings() {}
}
