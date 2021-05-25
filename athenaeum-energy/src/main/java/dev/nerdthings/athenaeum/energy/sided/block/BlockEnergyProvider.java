package dev.nerdthings.athenaeum.energy.sided.block;

import dev.nerdthings.athenaeum.energy.sided.SidedEnergyHandler;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implement on a block that will provide an {@link SidedEnergyHandler}.
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
    SidedEnergyHandler getEnergyHandler(World world, BlockPos pos, BlockState state);

}
