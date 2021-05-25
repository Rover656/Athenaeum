package dev.nerdthings.athenaeum.energy;

import dev.nerdthings.athenaeum.energy.generic.EnergyHandler;
import dev.nerdthings.athenaeum.energy.generic.item.ItemEnergyProvider;
import dev.nerdthings.athenaeum.energy.sided.SidedEnergyHandler;
import dev.nerdthings.athenaeum.energy.sided.SidedEnergyHolder;
import dev.nerdthings.athenaeum.energy.compat.fasttransferlib.FTLCompat;
import dev.nerdthings.athenaeum.energy.sided.block.BlockEnergyProvider;
import dev.nerdthings.athenaeum.energy.sided.blockentity.EnergyProvider;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;

/**
 * Public registration and lookup API for the Quiltech Energy API.
 * @author Reece Mackie
 * @since 0.1.0
 */
@SuppressWarnings("unused") // This is an API, dummy!
public class EnergySystem {

    private static final BlockApiLookup<SidedEnergyHandler, Void> BLOCK_LOOKUP = BlockApiLookup.get(new Identifier("athenaeum-energy", "sided_energy"), SidedEnergyHandler.class, Void.class);

//    private static final ItemApiLookup<EnergyHandler, Void> ITEM_LOOKUP = ItemApiLookup.get(new Identifier("athenaeum-energy", "item_energy"), EnergyHandler.class, Void.class);

    // TODO: Attach energy to entities...

    static {
        // Register default handlers
        BLOCK_LOOKUP.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity != null) {
                if (blockEntity instanceof EnergyProvider provider) {
                    return provider.getEnergyHandler();
                } else if (blockEntity instanceof SidedEnergyHolder holder) {
                    return holder;
                }
            } else if (state.getBlock() instanceof BlockEnergyProvider provider) {
                return provider.getEnergyHandler(world, pos, state);
            }
            return null;
        });

//        ITEM_LOOKUP.registerFallback((itemStack, context) -> {
//            if (itemStack.getItem() instanceof ItemEnergyProvider provider) {
//                return provider.getEnergyHolder(itemStack);
//            }
//            return null;
//        });

        // FTL Compat
        if (FabricLoader.getInstance().isModLoaded("fasttransferlib")) {
            FTLCompat.registerFallbacks();
        }
    }

    // TODO: Custom energy handler registration.

    /**
     * Register a custom provider for a list of {@link BlockEntityType}'s.
     * @param provider The custom provider.
     * @param blockEntityTypes The block entity types to register for.
     */
    public static void registerForBlockEntities(BlockApiLookup.BlockEntityApiProvider<SidedEnergyHandler, Void> provider, BlockEntityType<?>... blockEntityTypes) {
        BLOCK_LOOKUP.registerForBlockEntities(provider, blockEntityTypes);

        // FTL Compat
        if (FabricLoader.getInstance().isModLoaded("fasttransferlib")) {
            FTLCompat.registerForBlockEntities(provider, blockEntityTypes);
        }
    }

    /**
     * Register a custom provider for a list of {@link Block}'s.
     * @param provider The custom provider.
     * @param blocks The block entity types to register for.
     */
    public static void registerForBlocks(BlockApiLookup.BlockApiProvider<SidedEnergyHandler, Void> provider, Block... blocks) {
        BLOCK_LOOKUP.registerForBlocks(provider, blocks);

        // FTL Compat
        if (FabricLoader.getInstance().isModLoaded("fasttransferlib")) {
            FTLCompat.registerForBlocks(provider, blocks);
        }
    }

    // TODO: Is this caching correct?
    private static final WeakHashMap<World, Long2ObjectOpenHashMap<BlockApiCache<SidedEnergyHandler, Void>>> BLOCK_ENERGY_HOLDER_CACHE = new WeakHashMap<>();

    /**
     * Get the {@link SidedEnergyHandler} of a {@link Block}.
     * @param serverWorld The world.
     * @param pos The block position.
     * @return The energy handler.
     */
    public static @Nullable SidedEnergyHandler of(@NotNull ServerWorld serverWorld, BlockPos pos) {
        return of(serverWorld, pos, null);
    }

    /**
     * Get the {@link SidedEnergyHandler} of a {@link Block}.
     * @param serverWorld The world.
     * @param pos The block position.
     * @param state The block state.
     * @return The energy handler.
     */
    public static @Nullable SidedEnergyHandler of(@NotNull ServerWorld serverWorld, BlockPos pos, @Nullable BlockState state) {
        // Populate cache then find
        BLOCK_ENERGY_HOLDER_CACHE.computeIfAbsent(serverWorld, (world) -> new Long2ObjectOpenHashMap<>());
        return BLOCK_ENERGY_HOLDER_CACHE.get(serverWorld).computeIfAbsent(pos.asLong(), (cache) -> BlockApiCache.create(BLOCK_LOOKUP, serverWorld, pos)).find(state, null);
    }

    /**
     * Get the {@link SidedEnergyHandler} of an {@link ItemStack}
     * @param itemStack The item stack.
     * @return The energy handler.
     */
    public static @Nullable EnergyHandler of(@NotNull ItemStack itemStack) {
//        return ITEM_LOOKUP.find(itemStack, null);

        // TEMP. This is bascially what the lookup does anyways. Might not even use it. Depends if it makes it into quilt or not.
        if (itemStack.getItem() instanceof ItemEnergyProvider provider) {
            return provider.getEnergyHandler(itemStack);
        }
        return null;
    }

}
