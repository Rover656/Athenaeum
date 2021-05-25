package dev.nerdthings.athenaeum.energy.compat.fasttransferlib;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import dev.nerdthings.athenaeum.energy.EnergyHolder;
import dev.nerdthings.athenaeum.energy.EnergySide;
import dev.nerdthings.athenaeum.energy.provider.BlockEnergyProvider;
import dev.nerdthings.athenaeum.energy.provider.BlockEntityEnergyProvider;
import dev.nerdthings.athenaeum.energy.provider.ItemEnergyProvider;
import dev.technici4n.fasttransferlib.api.energy.EnergyApi;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

/**
 * Provides one-way compatibility with fasttransferlib.
 * This allows the FTL API to interact with Athenaeum's.
 * Athenaeum cannot interact with FTL however.
 */
public class FTLCompat {
    public static void registerFallbacks() {
        EnergyApi.SIDED.registerFallback(((world, pos, state, blockEntity, direction) -> {
            EnergyHandler handler = null;
            if (blockEntity != null) {
                if (blockEntity instanceof BlockEntityEnergyProvider<?> provider) {
                    handler = provider.getEnergyHandler();
                } else if (blockEntity instanceof EnergyHolder holder) {
                    handler = holder;
                }
            } else if (state.getBlock() instanceof BlockEnergyProvider<?> provider) {
                handler = provider.getEnergyHandler(world, pos, state);
            }

            if (handler == null)
                return null;
            return new HandlerToEnergyIo(handler, EnergySide.fromDirection(direction));
        }));

        EnergyApi.ITEM.registerFallback((itemStack, context) -> {
            if (itemStack.getItem() instanceof ItemEnergyProvider<?> provider) {
                EnergyHandler handler = provider.getEnergyHandler(itemStack.toStack());
                if (handler != null)
                    return new HandlerToEnergyIo(handler, EnergySide.NONE);
            }
            return null;
        });
    }

    public static void registerForBlockEntities(BlockApiLookup.BlockEntityApiProvider<EnergyHandler, Void> provider, BlockEntityType<?>... blockEntityTypes) {
        EnergyApi.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity != null) {
                EnergyHandler handler = provider.find(blockEntity, null);
                if (handler != null)
                    return new HandlerToEnergyIo(handler, EnergySide.fromDirection(direction));
            }
            return null;
        }, blockEntityTypes);
    }

    public static void registerForBlocks(BlockApiLookup.BlockApiProvider<EnergyHandler, Void> provider, Block... blocks) {
        EnergyApi.SIDED.registerForBlocks((world, pos, state, blockEntity, direction) -> {
            EnergyHandler handler = provider.find(world, pos, state, blockEntity, null);
            if (handler != null)
                return new HandlerToEnergyIo(handler, EnergySide.fromDirection(direction));
            return null;
        }, blocks);
    }
}
