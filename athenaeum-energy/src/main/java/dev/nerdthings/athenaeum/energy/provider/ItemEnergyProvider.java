package dev.nerdthings.athenaeum.energy.provider;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @param <T> The type of handler attached. This helps cut down casts.
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface ItemEnergyProvider<T extends EnergyHandler> {
    @Nullable T getEnergyHandler(ItemStack itemStack);
}
