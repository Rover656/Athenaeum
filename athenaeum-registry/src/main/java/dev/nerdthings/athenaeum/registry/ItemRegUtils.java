package dev.nerdthings.athenaeum.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ItemRegUtils {
    public static Item of(Identifier identifier, ItemGroup itemGroup) {
        return of(identifier, Item::new, 64, itemGroup);
    }

    public static Item of(Identifier identifier, int maxCount, ItemGroup itemGroup) {
        return of(identifier, Item::new, maxCount, itemGroup);
    }

    public static <T extends Item> T of(Identifier identifier, Function<Item.Settings, T> constructor, ItemGroup itemGroup) {
        return of(identifier, constructor, 64, itemGroup);
    }

    public static <T extends Item> T of(Identifier identifier, Function<Item.Settings, T> constructor, int maxCount, ItemGroup itemGroup) {
        T item = constructor.apply(new FabricItemSettings().maxCount(maxCount).group(itemGroup));
        return register(identifier, item);
    }

    public static <T extends Item> T register(Identifier identifier, T item) {
        ForwardRegister.identity(item, identifier);
        return item;
    }
}
