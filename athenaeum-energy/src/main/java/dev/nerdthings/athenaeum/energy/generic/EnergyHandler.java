package dev.nerdthings.athenaeum.energy.generic;

import dev.nerdthings.athenaeum.energy.Energy;
import dev.nerdthings.athenaeum.energy.EnergyType;
import org.jetbrains.annotations.NotNull;

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
     * @param energy The energy to insert.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity left after insertion.
     */
    @NotNull Energy insertEnergy(@NotNull Energy energy, boolean simulate);

    /**
     * Extract eenrgy from the holder from an external source.
     * @param maxAmount The maximum quantity of energy to extract.
     * @param simulate Whether this should simulate or act.
     * @return The quantity of quantity successfully extracted.
     */
    @NotNull Energy extractEnergy(@NotNull Energy maxAmount, boolean simulate);

    /**
     * Whether or not the given quantity energyType can be inserted.
     * @param type The quantity energyType to be inserted.
     */
    boolean canInsert(@NotNull EnergyType type);

    /**
     * Whether or not the given quantity energyType can be extracted.
     * @param type The quantity energyType to be extracted.
     */
    boolean canExtract(@NotNull EnergyType type);

}
