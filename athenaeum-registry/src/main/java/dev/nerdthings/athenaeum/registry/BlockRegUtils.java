package dev.nerdthings.athenaeum.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegUtils {
    public static Block of(Identifier identifier, AbstractBlock.Settings settings) {
        return of(identifier, Block::new, settings);
    }

    public static <T extends Block> T of(Identifier identifier, Function<AbstractBlock.Settings, T> constructor, AbstractBlock.Settings settings) {
        return of(identifier, () -> constructor.apply(settings));
    }

    public static <T extends Block> T of(Identifier identifier, Supplier<T> supplier) {
        T block = supplier.get();
        ForwardRegister.identity(block, identifier);
        return block;
    }
}
