package dev.nerdthings.athenaeum.energy;

import dev.nerdthings.athenaeum.energy.exceptions.SideRequiredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An object that *stores* energy.
 */
public interface EnergyHolder extends EnergyHandler {
    /**
     * Get the stored energy.  Implies access from no specific side.
     * @return The energy stored.
     */
    default @NotNull Energy getStoredEnergy() {
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
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
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
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
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
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
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
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
