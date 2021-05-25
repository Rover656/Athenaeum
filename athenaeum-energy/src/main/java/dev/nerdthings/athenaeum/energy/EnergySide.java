package dev.nerdthings.athenaeum.energy;

import dev.nerdthings.athenaeum.energy.sided.SidedEnergyHandler;
import dev.nerdthings.athenaeum.energy.sided.SidedEnergyHolder;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * Enum for sided actions on {@link SidedEnergyHandler}'s and {@link SidedEnergyHolder}'s.
 * Must use {@link EnergySide#NONE} when interacting with entities or items.
 */
public enum EnergySide {
    DOWN, UP, NORTH, SOUTH, EAST, WEST, NONE;

    public static EnergySide fromDirection(@Nullable Direction direction) {
        if (direction == null)
            return NONE;
        return values()[direction.ordinal()];
    }
}
