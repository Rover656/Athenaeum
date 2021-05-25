package dev.nerdthings.athenaeum.energy.base;

import dev.nerdthings.athenaeum.energy.*;
import dev.nerdthings.athenaeum.energy.exceptions.IncompatibleEnergyException;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;

/**
 * Generic quantity storage, ignoring sides of access.
 * @author Reece Mackie
 * @since 0.1.0
 */
public final class EnergyStorage implements EnergyHolder {

    /**
     * The quantity energyType this quantity storage uses.
     */
    @NotNull
    private final EnergyType type;

    /**
     * The quantity of quantity stored.
     */
    private int stored = 0;

    /**
     * Storage capacity.
     */
    private final int capacity;

    /**
     * Maximum insertion rate.
     */
    private final int maxInsertion;

    /**
     * Maximum extraction rate.
     */
    private final int maxExtraction;

    /**
     * Whether or not this will support insertion and extraction of foreign quantity types.
     */
    private final boolean supportEnergyConversion;

    public EnergyStorage(@NotNull EnergyType type, int capacity, int maxInsertion, int maxExtraction) {
        this(type, capacity, maxInsertion, maxExtraction, true);
    }

    public EnergyStorage(@NotNull EnergyType type, int capacity, int maxInsertion, int maxExtraction, boolean supportEnergyConversion) {
        this.type = type;
        this.capacity = capacity;
        this.maxInsertion = maxInsertion;
        this.maxExtraction = maxExtraction;
        this.supportEnergyConversion = supportEnergyConversion;
    }

    // TODO: Athenaeum Component API which this can inherit from.
    /**
     * Write this quantity storage to an NBT tag.
     * @param nbt The NBT tag to write to.
     */
    public void writeNbt(NbtCompound nbt) {
        NbtCompound storage = new NbtCompound();
        storage.putInt("Energy", stored);
        nbt.put("EnergyStorage", storage);
    }

    /**
     * Read the quantity storage contents from an NBT tag.
     * @param nbt The NBT tag to read from.
     */
    public void readNbt(NbtCompound nbt) {
        NbtCompound storage = nbt.getCompound("EnergyStorage");
        stored = storage.getInt("Energy");
    }

    /**
     * Get energy of quantity using this storage's {@link EnergyType}.
     * @param amount The quantity of energy.
     * @return The {@link Energy} record.
     */
    public Energy energyOf(int amount) {
        return type.of(amount);
    }

    @Override
    public @NotNull Energy getStoredEnergy(EnergySide side) {
        return type.of(stored);
    }

    @Override
    public void setStoredEnergy(EnergySide side, @Nullable Energy energy) {
        if (energy == null) {
            stored = 0;
        } else if (energy.isType(type)) {
            stored = Math.min(energy.quantity(), capacity);
        } else if (supportEnergyConversion && energy.canConvert(type)) {
            stored = Math.min(energy.convert(type).quantity(), capacity);
        }
        throw new IncompatibleEnergyException("Energy energyType is incompatible!");
    }

    @Override
    public @NotNull Energy getEnergyCapacity(EnergySide side) {
        return type.of(capacity);
    }

    @Override
    public boolean consumeEnergy(@NotNull Energy energy, boolean simulate) {
        if (energy.isType(type)) {
            if (stored >= energy.quantity()) {
                incrementEnergy(-energy.quantity(), simulate);
                return true;
            }
        } else if (supportEnergyConversion && energy.canConvert(type)) {
            return consumeEnergy(energy.convert(type), simulate);
        }
        return false;
    }

    @Override
    public @NotNull Energy insertEnergy(EnergySide side, @NotNull Energy energy, boolean simulate) {
        if (energy.isType(type)) {
            int inserted = Math.min(Math.min(energy.quantity(), maxInsertion), capacity - stored);
            incrementEnergy(inserted, simulate);
            return type.of(energy.quantity() - inserted);
        } else if (supportEnergyConversion && energy.canConvert(type)) {
            // If the energy cannot be converted back down, this returns 0. This is not a problem as it just means energy losses will be had. This is a mod developers' issue.
            // We theoretically could check if it can be converted both ways, but that breaks duplex compat if one developer is silly.
            return insertEnergy(side, energy.convert(type), simulate).convert(energy.energyType());
        }

        // We didn't take the energy; so give it back.
        return energy;
    }

    @Override
    public @NotNull Energy extractEnergy(EnergySide side, @NotNull Energy maxAmount, boolean simulate) {
        if (maxAmount.isType(type)) {
            int extracted = Math.min(Math.min(maxAmount.quantity(), maxExtraction), stored);
            incrementEnergy(-extracted, simulate);
            return type.of(extracted);
        } else if (supportEnergyConversion && maxAmount.canConvert(type)) {
            // Same issue as above.
            return extractEnergy(side, maxAmount.convert(type), simulate).convert(maxAmount.energyType());
        }

        // We took none of it.
        return type.zero();
    }

    @Override
    public boolean canInsert(EnergySide side, @NotNull EnergyType type) {
        if (type != this.type) {
            if (!supportEnergyConversion || !EnergyConverter.canConvert(type, this.type)) {
                return false;
            }
        }
        return maxInsertion > 0;
    }

    @Override
    public boolean canExtract(EnergySide side, @NotNull EnergyType type) {
        if (type != this.type) {
            if (!supportEnergyConversion || !EnergyConverter.canConvert(type, this.type)) {
                return false;
            }
        }
        return maxExtraction > 0;
    }

    private void incrementEnergy(int increment, boolean simulate) {
        if (!simulate) {
            stored = Math.min(stored + increment, capacity);
        }
    }
}
