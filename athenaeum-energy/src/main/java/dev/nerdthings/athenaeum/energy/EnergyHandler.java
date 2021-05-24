package dev.nerdthings.athenaeum.energy;

import org.jetbrains.annotations.NotNull;

/**
 * A handler of Energy input and output.
 * Not necessarily an object that *stores* energy, but it handles it in some way.
 */
public interface EnergyHandler {

    /**
     * Attempt to consume quantity from the handler.
     * @param energy The energy to consume.
     * @param simulate Whether or not this is a simulated action.
     * @return Whether or not the quantity was consumed.
     */
    boolean consumeEnergy(@NotNull Energy energy, boolean simulate);

    /**
     * Insert quantity into the holder from an external source.
     * @param side The side to insert from.
     * @param energy The energy to insert.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity left after insertion.
     */
    @NotNull Energy insertEnergy(EnergySide side, @NotNull Energy energy, boolean simulate);

    /**
     * Extract eenrgy from the holder from an external source.
     * @param side The side to extract from.
     * @param maxAmount The maximum quantity of energy to extract.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity successfully extracted.
     */
    @NotNull Energy extractEnergy(EnergySide side, @NotNull Energy maxAmount, boolean simulate);

    /**
     * Whether or not the given quantity energyType can be inserted.
     * @param side The side to insert from.
     * @param type The quantity energyType to be inserted.
     */
    boolean canInsert(EnergySide side, @NotNull EnergyType type);

    /**
     * Whether or not the given quantity energyType can be extracted.
     * @param side The side to extract from.
     * @param type The quantity energyType to be extracted.
     */
    boolean canExtract(EnergySide side, @NotNull EnergyType type);
}
