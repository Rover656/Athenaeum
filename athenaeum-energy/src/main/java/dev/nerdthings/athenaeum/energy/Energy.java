package dev.nerdthings.athenaeum.energy;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * A record used to bundle energy energyType and quantity together.
 * Prevents messy parameter lists.
 */
public record Energy(EnergyType energyType, int quantity) {
    public Energy {
        // Check that the EnergyType is registered.
        if (!EnergyTypes.exists(energyType.getIdentifier()))
            throw new RuntimeException("EnergyType `" + energyType.getIdentifier() + "` is not registered!");
    }

    /**
     * Add energy together. Must be of the same type.
     * @param other The energy to add.
     * @return The sum of the two energies.
     */
    public @NotNull Energy add(@NotNull Energy other) {
        if (!sameType(other))
            throw new InvalidParameterException("Energy types are not compatible!");
        return energyType.of(quantity + other.quantity);
    }

    public @NotNull Energy add(int amount) {
        return energyType.of(quantity + amount);
    }

    /**
     * Subtract energy from this one. Must be of the same type.
     * @param other The energy to subtract.
     * @return The difference of the two energies.
     */
    public @NotNull Energy sub(@NotNull Energy other) {
        if (!sameType(other))
            throw new InvalidParameterException("Energy types are not compatible!");
        return energyType.of(quantity - other.quantity);
    }

    public @NotNull Energy sub(int amount) {
        return energyType.of(quantity - amount);
    }

    /**
     * Check that the two {@link Energy} objects are of the same energyType.
     * @param other The Energy to check against.
     * @return Whether they are of the same energyType.
     */
    public boolean sameType(Energy other) {
        return energyType == other.energyType;
    }

    /**
     * Test the Energy type.
     * @param type The type to check against.
     * @return Whether this Energy is of the checked type.
     */
    public boolean isType(EnergyType type) {
        return energyType == type;
    }

    /**
     * Determine whether or not this {@link Energy} can be converted to the given {@link EnergyType}.
     * @param newType The type to be converted to.
     * @return Whether or not conversion is possible.
     */
    public boolean canConvert(@NotNull EnergyType newType) {
        return EnergyConverter.canConvert(energyType, newType);
    }

    /**
     * Convert this energy to a new {@link EnergyType}
     * @param newType The energyType to convert to.
     * @return The converted value, or 0 in the new {@link EnergyType} if it cannot be converted.
     */
    public @NotNull Energy convert(@NotNull EnergyType newType) {
        if (isType(newType)) {
            return this;
        } else if (EnergyConverter.canConvert(energyType, newType)) {
            int convertedAmount = EnergyConverter.convert(energyType, newType, quantity);
            if (convertedAmount > 0) {
                return new Energy(newType, convertedAmount);
            }
        }
        return newType.zero();
    }

    /**
     * Write the {@link Energy} to an {@link NbtCompound} for saving.
     * @return The saved NBT.
     */
    public NbtCompound writeNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putInt("Amount", quantity);
        compound.putString("Type", energyType.getIdentifier().toString());
        return compound;
    }

    /**
     * Create an {@link Energy} from a saved {@link NbtCompound}
     * @param compound The compound storing the Energy.
     * @return The Energy.
     */
    public static @Nullable Energy fromNbt(NbtCompound compound) {
        // Check that it has a type.
        if (!compound.contains("Type")) {
            return null;
        }

        // Get the type and check it is registered.
        Identifier energyType = new Identifier(compound.getString("Type"));
        if (!EnergyTypes.exists(energyType)) {
            Logger.getLogger("athenaeum-energy").warning("Failed to load Energy from NBT: Missing EnergyType `" + energyType.toString());
            return null;
        }

        // Get the type
        EnergyType type = EnergyTypes.get(energyType);
        if (type == null) {
            // This should never happen, just here to silence warnings
            return null;
        }

        // If we have an amount, get it. If not, return zero.
        if (compound.contains("Amount")) {
            return type.of(compound.getInt("Amount"));
        }
        return type.zero();
    }
}
