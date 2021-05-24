package dev.nerdthings.athenaeum.energy;

import dev.nerdthings.athenaeum.energy.compat.fasttransferlib.FTLEnergyType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link EnergyType} registry.
 * Allows mod developers to add quantity support without having to reference the mod in code.
 * For example in Quiltech, you can get the "Basic" quantity energyType by using:
 * <code>
 *     EnergyType quiltechBasic = EnergyTypes.get(new Identifier("quiltech", "basic_energy"));
 *     // Remember to check for null in case Quiltech isn't installed however!
 * </code>
 *
 * @author Reece Mackie
 * @since 0.1.0
 */
public class EnergyTypes {

    private static final Map<Identifier, EnergyType> ENERGY_TYPE_MAP = new HashMap<>();

    public static final FTLEnergyType FTL = register(new FTLEnergyType());

    public static <T extends EnergyType> T register(T type) {
        ENERGY_TYPE_MAP.put(type.getIdentifier(), type);
        return type;
    }

    /**
     * Get an quantity energyType by its identifier.
     * @param identifier The identifier to get by.
     * @return The quantity energyType.
     */
    public static EnergyType get(Identifier identifier) {
        if (ENERGY_TYPE_MAP.containsKey(identifier)) {
            return ENERGY_TYPE_MAP.get(identifier);
        }
        return null;
    }

    /**
     * Determine if an energy energyType exists by its's {@link Identifier}.
     * @param identifier The energy energyType to test for.
     * @return Whether it exists.
     */
    public static boolean exists(Identifier identifier) {
        return ENERGY_TYPE_MAP.containsKey(identifier);
    }
}
