package dev.nerdthings.athenaeum.energy;

import net.minecraft.util.Identifier;

/**
 * Represents a energyType of energy.
 * Implements no actual methods other than {@link EnergyType#getIdentifier()} because the rest of the implementation detail is up to the mod developer.
 * The developer can choose to expand the energyType if it benefits them, or to not if it doesn't.
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface EnergyType {

    /**
     * Get the EnergyType {@link Identifier}.
     * Used for serialization.
     * @return The identifier.
     */
    Identifier getIdentifier();

    /**
     * Get the {@link Energy} value for 0.
     * This is here in case an energy system decides it doesn't want to start naturally at 0.
     * @return The zero value.
     */
    default Energy zero() {
        return of(0);
    }

    /**
     * Get the {@link Energy} value for a given quantity.
     * @param quantity The quantity of energy.
     * @return The Energy.
     */
    default Energy of(int quantity) {
        return new Energy(this, quantity);
    }

}
