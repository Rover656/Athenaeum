package dev.nerdthings.athenaeum.energy.sided.blockentity;

import dev.nerdthings.athenaeum.energy.sided.SidedEnergyHandler;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Implement on a {@link BlockEntity} that will provide an {@link SidedEnergyHandler}.
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface EnergyProvider {
    @Nullable
    SidedEnergyHandler getEnergyHandler();
}
