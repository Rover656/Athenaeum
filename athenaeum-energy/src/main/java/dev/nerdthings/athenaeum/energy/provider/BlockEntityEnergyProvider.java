package dev.nerdthings.athenaeum.energy.provider;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Implement on a {@link BlockEntity} that will provide an {@link EnergyHandler}.
 * @param <T> The type of handler attached. This helps cut down casts.
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface BlockEntityEnergyProvider<T extends EnergyHandler> {
    @Nullable T getEnergyHandler();
}
