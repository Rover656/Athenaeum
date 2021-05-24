package dev.nerdthings.athenaeum.energy;

import net.minecraft.util.math.Direction;

/**
 * Enum for sided actions on {@link EnergyHandler}'s and {@link EnergyHolder}'s.
 * Must use {@link EnergySide#NONE} when interacting with entities or items.
 */
public enum EnergySide {
    DOWN, UP, NORTH, SOUTH, EAST, WEST, NONE;

    public static EnergySide fromDirection(Direction direction) {
        if (direction == null)
            return NONE;
        return values()[direction.ordinal()];
    }
}
