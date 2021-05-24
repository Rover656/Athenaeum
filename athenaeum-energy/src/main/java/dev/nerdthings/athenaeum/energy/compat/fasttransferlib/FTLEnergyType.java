package dev.nerdthings.athenaeum.energy.compat.fasttransferlib;

import dev.nerdthings.athenaeum.energy.EnergyType;
import net.minecraft.util.Identifier;

public class FTLEnergyType implements EnergyType {
    @Override
    public Identifier getIdentifier() {
        return new Identifier("fasttransferlib", "energy");
    }
}
