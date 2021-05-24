package dev.nerdthings.athenaeum.energy;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles conversion between quantity types.
 * Mod's that add quantity types must add *explicit* support for each other.
 * @author Reece Mackie
 * @since 0.1.0
 */
public class EnergyConverter {
    private static final Object2FloatMap<Pair<EnergyType, EnergyType>> MULTIPLIER_MAP = new Object2FloatOpenHashMap<>();

    private static final Map<EnergyType, EnergyConversionHandler> CUSTOM_CONVERSION_HANDLERS = new HashMap<>();

    /**
     *
     * @param from
     * @param to
     * @param energy
     * @return
     */
    public static int convert(@NotNull EnergyType from, @NotNull EnergyType to, int energy) {
        // If they're the same, somebody is doing something stupid.
        if (from == to) return energy;

        // TODO: Cache conversion results? Because right now we check the map up to twice each time, this can be reduced to one...

        // Look for a custom conversion handler
        if (CUSTOM_CONVERSION_HANDLERS.containsKey(from)) {
            EnergyConversionHandler handler = CUSTOM_CONVERSION_HANDLERS.get(from);
            if (!handler.canConvert(from, to))
                return 0;
            return CUSTOM_CONVERSION_HANDLERS.get(from).convert(from, to, energy);
        }

        if (CUSTOM_CONVERSION_HANDLERS.containsKey(to)) {
            EnergyConversionHandler handler = CUSTOM_CONVERSION_HANDLERS.get(to);
            if (!handler.canConvert(from, to))
                return 0;
            return CUSTOM_CONVERSION_HANDLERS.get(from).convert(from, to, energy);
        }

        // Check the first orientation.
        Pair<EnergyType, EnergyType> pairA = Pair.of(from, to);
        if (MULTIPLIER_MAP.containsKey(pairA)) {
            float multiplier = MULTIPLIER_MAP.getFloat(pairA);
            if (multiplier <= 0.0f) {
                return 0;
            }

            // TODO: Here is where we cache the multiplier
            return (int) (energy * multiplier);
        }

        // Also check the other way around, you never know...
        Pair<EnergyType, EnergyType> pairB = Pair.of(to, from);
        if (MULTIPLIER_MAP.containsKey(pairB)) {
            float multiplier = MULTIPLIER_MAP.getFloat(pairB);
            if (multiplier <= 0.0f) {
                return 0;
            }

            // TODO: Here is where we cache the multiplier
            return (int) (energy * (1 / multiplier));
        }

        // No mapping, no convert.
        return 0;
    }

    /**
     * Check if conversion can be done.
     * @param from
     * @param to
     * @return
     */
    public static boolean canConvert(@NotNull EnergyType from, @NotNull EnergyType to) {
        // If they're the same, somebody is doing something stupid.
        if (from == to) return true;

        // Check our custom handlers.
        if (CUSTOM_CONVERSION_HANDLERS.containsKey(from)) {
            if (CUSTOM_CONVERSION_HANDLERS.get(from).canConvert(from, to))
                return true;
        }
        if (CUSTOM_CONVERSION_HANDLERS.containsKey(to)) {
            if (CUSTOM_CONVERSION_HANDLERS.get(to).canConvert(from, to))
                return true;
        }

        // Check if we have multipliers
        Pair<EnergyType, EnergyType> pairA = Pair.of(from, to);
        if (MULTIPLIER_MAP.containsKey(pairA)) {
            return MULTIPLIER_MAP.getFloat(pairA) > 0.0f;
        }
        Pair<EnergyType, EnergyType> pairB = Pair.of(to, from);
        if (MULTIPLIER_MAP.containsKey(pairB)) {
            return MULTIPLIER_MAP.getFloat(pairB) > 0.0f;
        }

        // We couldn't attempt conversion, don't ever attempt to.
        return false;
    }

    /**
     * Register a geometric conversion between two quantity types (bi-directional unless otherwise overridden).
     * @param from
     * @param to
     * @param multiplier
     */
    public static void registerConversion(@NotNull EnergyType from, @NotNull EnergyType to, float multiplier) {
        Pair<EnergyType, EnergyType> pair = Pair.of(from, to);
        if (MULTIPLIER_MAP.containsKey(pair)) {
            // TODO: Some way of overriding other mods' options here. Maybe even mod config to define overruling multipliers for modpack devs.
            throw new InvalidParameterException("This pair of quantity types already have a conversion registered.");
        }
        MULTIPLIER_MAP.put(Pair.of(from, to), multiplier);
    }

    /**
     * Register a conversion handler. This allows you to run more advanced logic using the {@link EnergyType}'s. Can use custom field's etc. too.
     * @param from
     * @param handler This handler will take the from and to {@link EnergyType}'s and will return the quantity of quantity after conversion. You should consider making it handle inverse conversions as well for compatibility.
     */
    public static void registerConversionHandler(@NotNull EnergyType from, EnergyConversionHandler handler) {
        CUSTOM_CONVERSION_HANDLERS.put(from, handler);
    }

    public interface EnergyConversionHandler {
        boolean canConvert(EnergyType type, EnergyType to);
        int convert(EnergyType from, EnergyType to, int energy);
    }

}
