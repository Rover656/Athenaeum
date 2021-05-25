package dev.nerdthings.athenaeum.energy.base;

import dev.nerdthings.athenaeum.energy.*;
import dev.nerdthings.athenaeum.energy.exceptions.IncompatibleEnergyException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Energy storage that is attached to an {@link ItemStack}.
 * Uses item tags to store the amount of energy.
 */
public class ItemEnergyStorage implements EnergyHolder {
    private final EnergyType energyType;
    private final int capacity;
    private final int maxInsertion;
    private final int maxExtraction;
    private final ItemStack itemStack;

    public ItemEnergyStorage(@NotNull EnergyType energyType, int capacity, int maxInsertion, int maxExtraction, ItemStack itemStack) {
        this.energyType = energyType;
        this.capacity = capacity;
        this.maxInsertion = maxInsertion;
        this.maxExtraction = maxExtraction;
        this.itemStack = itemStack;
    }

    /**
     * Get energy of quantity using this storage's {@link EnergyType}.
     * @param amount The quantity of energy.
     * @return The {@link Energy} record.
     */
    public Energy energyOf(int amount) {
        return energyType.of(amount);
    }

    @Override
    public boolean consumeEnergy(@NotNull Energy energy, boolean simulate) {
        if (energy.isType(energyType)) {
            int stored = getQuantity();
            if (stored >= energy.quantity()) {
                if (!simulate) {
                    setQuantity(stored - energy.quantity());
                }
                return true;
            }
        } else if (energy.canConvert(energyType)) {
            return consumeEnergy(energy.convert(energyType), simulate);
        }
        return false;
    }

    @Override
    public @NotNull Energy insertEnergy(EnergySide side, @NotNull Energy energy, boolean simulate) {
        if (energy.isType(energyType)) {
            int stored = getQuantity();
            int inserted = Math.min(Math.min(energy.quantity(), maxInsertion), capacity - stored);
            if (!simulate) {
                setQuantity(stored + inserted);
            }
            return energyType.of(energy.quantity() - inserted);
        } else if (energy.canConvert(energyType)) {
            return insertEnergy(side, energy.convert(energyType), simulate).convert(energy.energyType());
        }

        return energy;
    }

    @Override
    public @NotNull Energy extractEnergy(EnergySide side, @NotNull Energy maxAmount, boolean simulate) {
        if (maxAmount.isType(energyType)) {
            int stored = getQuantity();
            int extracted = Math.min(Math.min(maxAmount.quantity(), maxExtraction), stored);
            if (!simulate) {
                setQuantity(stored - extracted);
            }
            return energyType.of(extracted);
        } else if (maxAmount.canConvert(energyType)) {
            return extractEnergy(side, maxAmount.convert(energyType), simulate).convert(maxAmount.energyType());
        }

        return energyType.zero();
    }

    @Override
    public boolean canInsert(EnergySide side, @NotNull EnergyType type) {
        return maxInsertion > 0 && (type == energyType || EnergyConverter.canConvert(type, energyType));
    }

    @Override
    public boolean canExtract(EnergySide side, @NotNull EnergyType type) {
        return maxExtraction > 0 && (type == energyType || EnergyConverter.canConvert(type, energyType));
    }

    @Override
    public @NotNull Energy getStoredEnergy(EnergySide side) {
        return energyType.of(getQuantity());
    }

    @Override
    public void setStoredEnergy(EnergySide side, @Nullable Energy energy) {
        if (energy == null) {
            setQuantity(0);
        } else if (energy.isType(energyType)) {
            setQuantity(energy.quantity());
        } else if (energy.canConvert(energyType)) {
            setStoredEnergy(side, energy.convert(energyType));
        }
        throw new IncompatibleEnergyException("Energy energyType is incompatible!");
    }

    @Override
    public @NotNull Energy getEnergyCapacity(EnergySide side) {
        return energyType.of(capacity);
    }

    private int getQuantity() {
        NbtCompound tag = itemStack.getTag();
        if (tag == null) return 0;
        if (!tag.contains("Energy")) return 0;
        return tag.getInt("Energy");
    }

    private void setQuantity(int quantity) {
        NbtCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NbtCompound();
        }

        if (quantity == 0) {
            tag.remove("Energy");
        } else {
            tag.putInt("Energy", Math.max(0, Math.min(quantity, capacity)));
        }

        itemStack.setTag(tag);
    }
}
