package dev.nerdthings.athenaeum.energy.sided;

import dev.nerdthings.athenaeum.energy.Energy;
import dev.nerdthings.athenaeum.energy.EnergySide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An object that *stores* energy.
 */
public interface SidedEnergyHolder extends SidedEnergyHandler {
    /**
     * Get the stored energy.
     * @param side The side to access from.
     * @return The energy stored.
     */
    @NotNull Energy getStoredEnergy(EnergySide side);

    /**
     * Set the stored energy.
     * @param side The side to access from.
     * @param energy The energy to be stored.
     */
    void setStoredEnergy(EnergySide side, @Nullable Energy energy);

    /**
     * Get the energy capacity of the holder.
     * @param side The side being accessed.
     * @return The capacity of the holder, including its preferred energyType.
     */
    @NotNull Energy getEnergyCapacity(EnergySide side);

    /**
     * Test whether the Holder is full or not.
     * @param side The side being accessed.
     * @return Whether the holder is full.
     */
    default boolean isFull(EnergySide side) {
        return getStoredEnergy(side).quantity() >= getEnergyCapacity(side).quantity();
    }
}
