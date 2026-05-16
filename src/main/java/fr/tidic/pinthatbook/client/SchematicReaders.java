package fr.tidic.pinthatbook.client;

import fr.tidic.pinthatbook.PinThatBook;
import net.neoforged.fml.ModList;

import java.util.Optional;

public final class SchematicReaders {
    private static final SchematicReader NOOP = stack -> Optional.empty();
    public static final SchematicReader INSTANCE = resolve();

    private SchematicReaders() {}

    private static SchematicReader resolve() {
        if (!ModList.get().isLoaded("create")) return NOOP;
        try {
            Class<?> cls = Class.forName("fr.tidic.pinthatbook.client.compat.CreateSchematicReader");
            return (SchematicReader) cls.getDeclaredConstructor().newInstance();
        } catch (Throwable t) {
            PinThatBook.LOGGER.warn("Failed to initialize Create schematic support", t);
            return NOOP;
        }
    }
}
