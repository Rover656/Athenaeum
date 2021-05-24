package dev.nerdthings.athenaeum.energy.base;

import dev.nerdthings.athenaeum.energy.*;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;

/**
 * Example of a {@link EnergyType}-flexible energy storage.
 * Takes a reference energyType and capacity for compatibility purposes.
 *
 * Warning: this is a reference implementation and is by no means ready to be considered best practice!
 */
public final class DynamicEnergyStorage implements EnergyHolder {
    /**
     * The energy stored.
     */
    private Energy stored;

    /**
     * The reference energy type.
     * This is used for defining the base capacity and max rates.
     */
    private final EnergyType referenceType;
    private final Energy capacity;
    private final Energy maxInsertion;
    private final Energy maxExtraction;

    public DynamicEnergyStorage(EnergyType referenceType, int capacity, int maxInsertion, int maxExtraction) {
        this.referenceType = referenceType;
        this.capacity = referenceType.of(capacity);
        this.maxInsertion = referenceType.of(maxInsertion);
        this.maxExtraction = referenceType.of(maxExtraction);
    }

    /**
     * Write this quantity storage to an NBT tag.
     * @param nbt The NBT tag to write to.
     */
    public void writeNbt(NbtCompound nbt) {
        NbtCompound storage = new NbtCompound();
        if (stored != null)
            storage.put("Energy", stored.writeNbt());
        nbt.put("EnergyStorage", storage);
    }

    /**
     * Read the quantity storage contents from an NBT tag.
     * @param nbt The NBT tag to read from.
     */
    public void readNbt(NbtCompound nbt) {
        NbtCompound storage = nbt.getCompound("EnergyStorage");
        if (storage.contains("Energy"))
            stored = Energy.fromNbt(storage.getCompound("Energy"));
        else stored = null;
    }

    @Override
    public boolean consumeEnergy(@NotNull Energy energy, boolean simulate) {
        if (energy.sameType(stored)) {
            if (stored.quantity() >= energy.quantity()) {
                if (!simulate) {
                    setStoredEnergy(EnergySide.NONE, stored.sub(energy));
                }
                return true;
            }
        } else if (energy.canConvert(stored.energyType())) {
            return consumeEnergy(energy.convert(stored.energyType()), simulate);
        }
        return false;
    }

    @Override
    public @NotNull Energy insertEnergy(EnergySide side, @NotNull Energy energy, boolean simulate) {
        if (energy.sameType(stored)) {
            // We use EnergyHolder#getEnergyCapacity so that it handles conversion for us :)
            int inserted = Math.min(Math.min(energy.quantity(), getMaxInsertion()), getEnergyCapacity(EnergySide.NONE).quantity() - stored.quantity());
            if (!simulate) {
                setStoredEnergy(side, stored.add(inserted));
            }
            return referenceType.of(energy.quantity() - inserted);
        } else if (energy.canConvert(stored.energyType())) {
            return insertEnergy(side, energy.convert(stored.energyType()), simulate).convert(energy.energyType());
        }
        return energy;
    }

    @Override
    public @NotNull Energy extractEnergy(EnergySide side, @NotNull Energy maxAmount, boolean simulate) {
        if (maxAmount.sameType(stored)) {
            int extracted = Math.min(Math.min(maxAmount.quantity(), getMaxExtraction()), stored.quantity());
            if (!simulate) {
                setStoredEnergy(side, stored.sub(extracted));
            }
            return referenceType.of(extracted);

        } else if (maxAmount.canConvert(stored.energyType())) {
            return extractEnergy(side, maxAmount.convert(stored.energyType()), simulate).convert(maxAmount.energyType());
        }
        return maxAmount.energyType().zero();
    }

    @Override
    public boolean canInsert(EnergySide side, @NotNull EnergyType type) {
        if (EnergyConverter.canConvert(referenceType, type)) {
            return getMaxInsertion() > 0;
        }
        return false;
    }

    @Override
    public boolean canExtract(EnergySide side, @NotNull EnergyType type) {
        if (EnergyConverter.canConvert(referenceType, type)) {
            return getMaxExtraction() > 0;
        }
        return false;
    }

    @Override
    public @NotNull Energy getStoredEnergy(EnergySide side) {
        if (stored != null)
            return stored;
        return referenceType.of(0);
    }

    @Override
    public void setStoredEnergy(EnergySide side, @Nullable Energy energy) {
        if (energy == null) {
            stored = null;
        } else if (energy.canConvert(referenceType)) {
            stored = energy;
        } else {
            throw new InvalidParameterException("Energy energyType must be convertible to the reference energyType!");
        }
    }

    @Override
    public @NotNull Energy getEnergyCapacity(EnergySide side) {
        if (stored != null) {
            return capacity.convert(stored.energyType());
        }
        return capacity;
    }

    private int getMaxInsertion() {
        // Convert to the correct type so we have the correct quantity.
        return maxInsertion.convert(stored.energyType()).quantity();
    }

    private int getMaxExtraction() {
        // Convert to the correct type so we have the correct quantity.
        return maxExtraction.convert(stored.energyType()).quantity();
    }
}
