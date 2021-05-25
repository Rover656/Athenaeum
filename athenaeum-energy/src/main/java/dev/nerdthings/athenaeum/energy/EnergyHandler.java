package dev.nerdthings.athenaeum.energy;

import dev.nerdthings.athenaeum.energy.exceptions.SideRequiredException;
import org.jetbrains.annotations.NotNull;

/**
 * A handler of Energy input and output.
 * Not necessarily an object that *stores* energy, but it handles it in some way.
 */
public interface EnergyHandler {

    /**
     * Determine whether or not the handler is sided.
     * If this is a sided handler, then accessing the handler from {@link EnergySide#NONE} is perfect valid.
     * Otherwise you must provide a valid side.
     * @return Whether the handler is sided.
     */
    default boolean isSided() {
        return false;
    }

    /**
     * Attempt to consume quantity from the handler.
     * @param energy The energy to consume.
     * @param simulate Whether or not this is a simulated action.
     * @return Whether or not the quantity was consumed.
     */
    boolean consumeEnergy(@NotNull Energy energy, boolean simulate);

    /**
     * Insert quantity into the holder from an external source. Implies access from no specific side.
     * @param energy The energy to insert.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity left after insertion.
     */
    default @NotNull Energy insertEnergy(@NotNull Energy energy, boolean simulate) {
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
        return insertEnergy(EnergySide.NONE, energy, simulate);
    }

    /**
     * Insert quantity into the holder from an external source.
     * @param side The side to insert from.
     * @param energy The energy to insert.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity left after insertion.
     */
    @NotNull Energy insertEnergy(EnergySide side, @NotNull Energy energy, boolean simulate);

    /**
     * Extract energy from the holder from an external source. Implies access from no specific side.
     * @param maxAmount The maximum quantity of energy to extract.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity successfully extracted.
     */
    default @NotNull Energy extractEnergy(@NotNull Energy maxAmount, boolean simulate) {
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
        return extractEnergy(EnergySide.NONE, maxAmount, simulate);
    }

    /**
     * Extract energy from the holder from an external source.
     * @param side The side to extract from.
     * @param maxAmount The maximum quantity of energy to extract.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity successfully extracted.
     */
    @NotNull Energy extractEnergy(EnergySide side, @NotNull Energy maxAmount, boolean simulate);

    /**
     * Whether or not the given quantity energyType can be inserted. Implies access from no specific side.
     * @param type The quantity energyType to be inserted.
     */
    default boolean canInsert(@NotNull EnergyType type) {
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
        return canInsert(EnergySide.NONE, type);
    }

    /**
     * Whether or not the given quantity energyType can be inserted.
     * @param side The side to insert from.
     * @param type The quantity energyType to be inserted.
     */
    boolean canInsert(EnergySide side, @NotNull EnergyType type);

    /**
     * Whether or not the given quantity energyType can be extracted. Implies access from no specific side.
     * @param type The quantity energyType to be extracted.
     */
    default boolean canExtract(@NotNull EnergyType type) {
        if (isSided())
            throw new SideRequiredException("Sideless access to this handler is not permitted!");
        return canExtract(EnergySide.NONE, type);
    }

    /**
     * Whether or not the given quantity energyType can be extracted.
     * @param side The side to extract from.
     * @param type The quantity energyType to be extracted.
     */
    boolean canExtract(EnergySide side, @NotNull EnergyType type);
}
