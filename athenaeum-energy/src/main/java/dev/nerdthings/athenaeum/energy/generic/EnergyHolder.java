package dev.nerdthings.athenaeum.energy.generic;

import dev.nerdthings.athenaeum.energy.Energy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EnergyHolder extends EnergyHandler {

    /**
     * Get the stored energy.
     * @return The energy stored.
     */
    @NotNull Energy getStoredEnergy();

    /**
     * Set the stored energy.
     * @param energy The energy to be stored.
     */
    void setStoredEnergy(@Nullable Energy energy);

    /**
     * Get the energy capacity of the holder.
     * @return The capacity of the holder, including its preferred energyType.
     */
    @NotNull Energy getEnergyCapacity();

    /**
     * Test whether the Holder is full or not.
     * @return Whether the holder is full.
     */
    default boolean isFull() {
        return getStoredEnergy().quantity() >= getEnergyCapacity().quantity();
    }

}
