package dev.nerdthings.athenaeum.registry;

// Based upon: https://github.com/TechReborn/RebornCore/blob/4be803f24979416c5dd3e0dbe81237786328f010/src/main/java/reborncore/RebornRegistry.java#L40

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * Based upon RebornRegistry by Gigabit101.
 * @author Reece Mackie
 * @since 0.1.0
 */
public class ForwardRegister {
    /**
     * Map an object to an Identifier.
     */
    private static final Map<Object, Identifier> IDENTITY_MAP = new HashMap<>();

    /**
     * Register an identity for an object so that it can be registered in the future.
     * @param obj The object to give an identity to.
     * @param identifier The identity for this object.
     */
    public static void identity(Object obj, Identifier identifier) {
        if (IDENTITY_MAP.containsKey(obj))
            throw new IllegalArgumentException("Object has already been given an identity!");
        IDENTITY_MAP.put(obj, identifier);
    }

    /**
     * Register a forward-identified {@link ItemConvertible}.
     * @param item The item to register.
     */
    public static void registerItem(ItemConvertible item) {
        registerItem(item.asItem());
    }

    /**
     * Register a forward-identified {@link Item}.
     * @param item The item to register.
     */
    public static void registerItem(Item item) {
        register(Registry.ITEM, item);
    }

    /**
     * Register a forward-identified {@link Block}
     * @param block The block to register.
     */
    public static void registerBlockOnly(Block block) {
        register(Registry.BLOCK, block);
    }

    /**
     * Register a forward-identified {@link Block} with a {@link BlockItem}.
     * @param block The block to register.
     * @param itemGroup The {@link ItemGroup} to register the {@link BlockItem} in.
     */
    public static void registerBlock(Block block, ItemGroup itemGroup) {
        registerBlock(block, new FabricItemSettings().group(itemGroup));
    }

    /**
     * Register a forward-identified {@link Block} with a {@link BlockItem}.
     * @param block The block to register.
     * @param itemSettings The {@link Item.Settings} used to create the {@link BlockItem}.
     */
    public static void registerBlock(Block block, Item.Settings itemSettings) {
        registerBlock(block, block1 -> new BlockItem(block1, itemSettings));
    }

    /**
     * Register a forward-identified {@link Block} with a {@link BlockItem}.
     * @param block The block to register.
     * @param blockItemFunction A function that takes the Block and outputs the BlockItem.
     */
    public static void registerBlock(Block block, Function<Block, BlockItem> blockItemFunction) {
        registerBlockOnly(block);
        registerBlockItem(block, blockItemFunction);
    }

    /**
     * Register a {@link BlockItem} for a forward-identified {@link Block}.
     * @param block The block whose identity will be used.
     * @param itemGroup The {@link ItemGroup} to register the {@link BlockItem} in.
     */
    public static void registerBlockItem(Block block, ItemGroup itemGroup) {
        registerBlockItem(block, new FabricItemSettings().group(itemGroup));
    }

    /**
     * Register a {@link BlockItem} for a forward-identified {@link Block}.
     * @param block The block whose identity will be used.
     * @param itemSettings The {@link Item.Settings} used to create the {@link BlockItem}.
     */
    public static void registerBlockItem(Block block, Item.Settings itemSettings) {
        registerBlockItem(block, block1 -> new BlockItem(block1, itemSettings));
    }

    /**
     * Register a {@link BlockItem} for a forward-identified {@link Block}.
     * @param block The block whose identity will be used.
     * @param blockItemFunction A function that takes the Block and outputs the BlockItem.
     */
    public static void registerBlockItem(Block block, Function<Block, BlockItem> blockItemFunction) {
        if (!IDENTITY_MAP.containsKey(block))
            throw new IllegalArgumentException("Must forward declare identity!");
        Registry.register(Registry.ITEM, IDENTITY_MAP.get(block), blockItemFunction.apply(block));
    }

    /**
     * Register an entry into a registry using a forward-declared identity.
     * @param registry The registry to register into.
     * @param entry The entry to register.
     * @param <T> The type of the registry and entry.
     */
    public static <T> void register(Registry<T> registry, T entry) {
        if (!IDENTITY_MAP.containsKey(entry))
            throw new IllegalArgumentException("Must forward declare identity!");
        Registry.register(registry, IDENTITY_MAP.get(entry), entry);
    }

}
