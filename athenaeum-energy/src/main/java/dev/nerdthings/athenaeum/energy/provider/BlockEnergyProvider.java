package dev.nerdthings.athenaeum.energy.provider;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Implement on a block that will provide an {@link EnergyHandler}.
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface BlockEnergyProvider {

    /**
     * Get the EnergyHolder from this block.
     * @param world The current world.
     * @param pos The block position.
     * @param state The {@link BlockState}.
     * @return
     */
    EnergyHandler getEnergyHandler(World world, BlockPos pos, BlockState state);

}
