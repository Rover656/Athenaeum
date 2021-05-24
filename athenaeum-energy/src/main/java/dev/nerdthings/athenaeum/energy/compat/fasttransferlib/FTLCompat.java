package dev.nerdthings.athenaeum.energy.compat.fasttransferlib;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import dev.nerdthings.athenaeum.energy.EnergySide;
import dev.technici4n.fasttransferlib.api.energy.EnergyApi;
import net.minecraft.block.entity.BlockEntityType;

/**
 * Provides one-way compatibility with fasttransferlib.
 * Duplex support may come in the future.
 * Right now only outgoing compatibility is supported.
 */
public class FTLCompat {
    public static void registerSelf(BlockEntityType<?>... blockEntities) {
        EnergyApi.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof EnergyHandler) {
                return new HandlerToEnergyIo(((EnergyHandler) blockEntity), EnergySide.fromDirection(direction));
            }
            return null;
        }, blockEntities);
    }
}
