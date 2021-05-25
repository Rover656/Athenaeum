package dev.nerdthings.athenaeum.energy.generic.base;

import dev.nerdthings.athenaeum.energy.Energy;
import dev.nerdthings.athenaeum.energy.EnergyConverter;
import dev.nerdthings.athenaeum.energy.EnergyType;
import dev.nerdthings.athenaeum.energy.generic.EnergyHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEnergyStorage implements EnergyHolder {
    private final EnergyType energyType;
    private final int capacity;
    private final int maxInsertion;
    private final int maxExtraction;
    private final ItemStack itemStack;

    // TODO: Can insert, can extract dealio

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
    public @NotNull Energy insertEnergy(@NotNull Energy energy, boolean simulate) {
        if (energy.isType(energyType)) {
            int stored = getQuantity();
            int inserted = Math.min(Math.min(energy.quantity(), maxInsertion), capacity - stored);
            if (!simulate) {
                setQuantity(stored + inserted);
            }
            return energyType.of(energy.quantity() - inserted);
        } else if (energy.canConvert(energyType)) {
            return insertEnergy(energy.convert(energyType), simulate).convert(energy.energyType());
        }

        return energy;
    }

    @Override
    public @NotNull Energy extractEnergy(@NotNull Energy maxAmount, boolean simulate) {
        if (maxAmount.isType(energyType)) {
            int stored = getQuantity();
            int extracted = Math.min(Math.min(maxAmount.quantity(), maxExtraction), stored);
            if (!simulate) {
                setQuantity(stored - extracted);
            }
            return energyType.of(extracted);
        } else if (maxAmount.canConvert(energyType)) {
            return extractEnergy(maxAmount.convert(energyType), simulate).convert(maxAmount.energyType());
        }

        return energyType.zero();
    }

    @Override
    public boolean canInsert(@NotNull EnergyType type) {
        return maxInsertion > 0 && (type == energyType || EnergyConverter.canConvert(type, energyType));
    }

    @Override
    public boolean canExtract(@NotNull EnergyType type) {
        return maxExtraction > 0 && (type == energyType || EnergyConverter.canConvert(type, energyType));
    }

    @Override
    public @NotNull Energy getStoredEnergy() {
        return energyType.of(getQuantity());
    }

    @Override
    public void setStoredEnergy(@Nullable Energy energy) {
        if (energy == null) {
            setQuantity(0);
        } else if (energy.isType(energyType)) {
            setQuantity(energy.quantity());
        } else if (energy.canConvert(energyType)) {
            setStoredEnergy(energy.convert(energyType));
        }
    }

    @Override
    public @NotNull Energy getEnergyCapacity() {
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
