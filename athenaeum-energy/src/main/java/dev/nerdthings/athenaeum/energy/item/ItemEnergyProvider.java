package dev.nerdthings.athenaeum.energy.item;

import dev.nerdthings.athenaeum.energy.EnergyHandler;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Reece Mackie
 * @since 0.1.0
 */
public interface ItemEnergyProvider {
    EnergyHandler getEnergyHandler(ItemStack itemStack);
}
