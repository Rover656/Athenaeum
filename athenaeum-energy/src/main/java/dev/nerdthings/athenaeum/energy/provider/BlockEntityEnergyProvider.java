package dev.nerdthings.athenaeum.energy.provider;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Implement on a {@link BlockEntity} that will provide an {@link EnergyHandler}.
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface BlockEntityEnergyProvider {
    @Nullable
    EnergyHandler getEnergyHandler();
}