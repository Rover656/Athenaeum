package dev.nerdthings.athenaeum.energy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An object that *stores* energy.
 *
 * @implNote When implementing a holder, if sided access is required, you should probably throw when {@link EnergySide#NONE} is passed.
 */
public interface EnergyHolder extends EnergyHandler {
    /**
     * Get the stored energy.  Implies access from no specific side.
     * @return The energy stored.
     */
    default @NotNull Energy getStoredEnergy() {
        return getStoredEnergy(EnergySide.NONE);
    }

    /**
     * Get the stored energy.
     * @param side The side to access from.
     * @return The energy stored.
     */
    @NotNull Energy getStoredEnergy(EnergySide side);

    /**
     * Set the stored energy. Implies access from no specific side.
     * @param energy The energy to be stored.
     */
    default void setStoredEnergy(@Nullable Energy energy) {
        setStoredEnergy(EnergySide.NONE, energy);
    }

    /**
     * Set the stored energy.
     * @param side The side to access from.
     * @param energy The energy to be stored.
     */
    void setStoredEnergy(EnergySide side, @Nullable Energy energy);

    /**
     * Get the energy capacity of the holder. Implies access from no specific side.
     * @return The capacity of the holder, including its preferred energyType.
     */
    default @NotNull Energy getEnergyCapacity() {
        return getEnergyCapacity(EnergySide.NONE);
    }

    /**
     * Get the energy capacity of the holder.
     * @param side The side being accessed.
     * @return The capacity of the holder, including its preferred energyType.
     */
    @NotNull Energy getEnergyCapacity(EnergySide side);

    /**
     * Test whether the Holder is full or not. Implies access from no specific side.
     * @return Whether the holder is full.
     */
    default boolean isFull() {
        return isFull(EnergySide.NONE);
    }

    /**
     * Test whether the Holder is full or not.
     * @param side The side being accessed.
     * @return Whether the holder is full.
     */
    default boolean isFull(EnergySide side) {
        return getStoredEnergy(side).quantity() >= getEnergyCapacity(side).quantity();
    }
}
