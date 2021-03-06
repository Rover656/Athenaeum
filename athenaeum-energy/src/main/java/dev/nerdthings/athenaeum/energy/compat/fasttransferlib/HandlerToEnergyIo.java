package dev.nerdthings.athenaeum.energy.compat.fasttransferlib;

import dev.nerdthings.athenaeum.energy.*;
import dev.technici4n.fasttransferlib.api.Simulation;
import dev.technici4n.fasttransferlib.api.energy.EnergyIo;

public class HandlerToEnergyIo implements EnergyIo {
    private final EnergyHandler handler;
    private final EnergySide side;

    public HandlerToEnergyIo(EnergyHandler handler, EnergySide side) {
        this.handler = handler;
        this.side = side;
    }

    @Override
    public double getEnergy() {
        if (handler instanceof EnergyHolder)
            return ((EnergyHolder) handler).getStoredEnergy(side).convert(EnergyTypes.FTL).quantity();
        return 0;
    }

    @Override
    public double getEnergyCapacity() {
        if (handler instanceof EnergyHolder)
            return ((EnergyHolder) handler).getEnergyCapacity(side).convert(EnergyTypes.FTL).quantity();
        return 0;
    }

    @Override
    public boolean supportsInsertion() {
        return handler.canInsert(side, EnergyTypes.FTL);
    }

    @Override
    public double insert(double amount, Simulation simulation) {
        return handler.insertEnergy(side, EnergyTypes.FTL.of((int) amount), simulation.isSimulating()).convert(EnergyTypes.FTL).quantity();
    }

    @Override
    public boolean supportsExtraction() {
        return handler.canExtract(side, EnergyTypes.FTL);
    }

    @Override
    public double extract(double maxAmount, Simulation simulation) {
        return handler.extractEnergy(side, EnergyTypes.FTL.of((int) maxAmount), simulation.isSimulating()).convert(EnergyTypes.FTL).quantity();
    }
}
