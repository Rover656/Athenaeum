package dev.nerdthings.athenaeum.energy.util;

import dev.nerdthings.athenaeum.energy.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;

/**
 * A wrapped quantity holder, useful for wrapping a sided block entity.
 * Can allow or deny extraction to an underlying holder.
 * @author Reece Mackie
 * @since 0.1.0
 * @deprecated Should be reimplemented with a sided wrapper, supporting the option of having multiple allowed sides etc. Must be thought about. For now still use this.
 */
@Deprecated
public class WrappedEnergyHandler implements EnergyHandler {

    /**
     * The wrapped holder.
     */
    private final EnergyHandler handler;

    /**
     * Whether extraction from the holder is allowed.
     */
    private final boolean allowExtract;

    /**
     * Whether insertion to the holder is allowed.
     */
    private final boolean allowInsert;

    public WrappedEnergyHandler(EnergyHandler handler, boolean allowExtract, boolean allowInsert) {
        // They shouldn't be the same, its a waste of time using this otherwise
        if (allowInsert == allowExtract) {
            // TODO: Logger instead maybe?
            throw new InvalidParameterException("If you are not allowing insertion or extraction or are allowing both, just return null or the original holder instead!");
        }

        this.handler = handler;
        this.allowExtract = allowExtract;
        this.allowInsert = allowInsert;
    }

    @Override
    public boolean consumeEnergy(@NotNull Energy energy, boolean simulate) {
        return handler.consumeEnergy(energy, simulate);
    }

    @Override
    public @NotNull Energy insertEnergy(EnergySide side, @NotNull Energy energy, boolean simulate) {
        if (!allowInsert) return energy;
        return handler.insertEnergy(side, energy, simulate);
    }

    @Override
    public @NotNull Energy extractEnergy(EnergySide side, @NotNull Energy maxAmount, boolean simulate) {
        if (!allowExtract) return maxAmount.energyType().zero();
        return handler.extractEnergy(side, maxAmount, simulate);
    }

    @Override
    public boolean canInsert(EnergySide side, @NotNull EnergyType type) {
        return allowInsert && handler.canInsert(side, type);
    }

    @Override
    public boolean canExtract(EnergySide side, @NotNull EnergyType type) {
        return allowExtract && handler.canExtract(side, type);
    }
}
