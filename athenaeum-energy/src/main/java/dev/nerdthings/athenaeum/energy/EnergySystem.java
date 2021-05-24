package dev.nerdthings.athenaeum.energy;

import dev.nerdthings.athenaeum.energy.block.BlockEnergyProvider;
import dev.nerdthings.athenaeum.energy.blockentity.EnergyProvider;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;

// TODO: Maybe take a page out of TechReborn's book with its use of a lookup map instead of using BlockApiLookup?
// https://github.com/TechReborn/Energy/blob/master/src/main/java/team/reborn/energy/Energy.java#L9
// Don't know if that would be wise or not however...

/**
 * Public registration and lookup API for the Quiltech Energy API.
 * @author Reece Mackie
 * @since 0.1.0
 */
public class EnergySystem {

    private static final BlockApiLookup<EnergyHandler, Void> BLOCK_LOOKUP = BlockApiLookup.get(new Identifier("athenaeum-energy", "sided_energy"), EnergyHandler.class, Void.class);

    /**
     *
     * @param blockEntities
     */
    public static void registerSelf(BlockEntityType<?>... blockEntities) {
        BLOCK_LOOKUP.registerSelf(blockEntities);
    }

    /**
     *
     * @param blockEntities
     */
    public static void registerForBlockEntities(BlockEntityType<?>... blockEntities) {
        // Register in the block api lookup
        BLOCK_LOOKUP.registerForBlockEntities((blockEntity, context) -> {
            if (blockEntity instanceof EnergyProvider) {
                return ((EnergyProvider) blockEntity).getEnergyHolder();
            }
            return null;
        }, blockEntities);
    }

    /**
     *
     * @param blocks
     */
    public static void registerForBlocks(Block... blocks) {
        // Register in the block API lookup
        BLOCK_LOOKUP.registerForBlocks((world, pos, state, blockEntity, direction) -> {
            if (state.getBlock() instanceof EnergyProvider) {
                return ((BlockEnergyProvider) state.getBlock()).getEnergyHolder(world, pos, state, blockEntity);
            }
            return null;
        }, blocks);
    }

    private static final WeakHashMap<World, Long2ObjectOpenHashMap<BlockApiCache<EnergyHandler, Void>>> BLOCK_ENERGY_HOLDER_CACHE = new WeakHashMap<>();

    /**
     *
     * @param serverWorld
     * @param pos
     * @return
     */
    public static EnergyHandler of(ServerWorld serverWorld, BlockPos pos) {
        return of(serverWorld, pos, null);
    }

    /**
     *
     * @param serverWorld
     * @param pos
     * @param state
     * @return
     */
    public static EnergyHandler of(ServerWorld serverWorld, BlockPos pos, @Nullable BlockState state) {
        // Populate cache then find
        BLOCK_ENERGY_HOLDER_CACHE.computeIfAbsent(serverWorld, (world) -> new Long2ObjectOpenHashMap<>());
        return BLOCK_ENERGY_HOLDER_CACHE.get(serverWorld).computeIfAbsent(pos.asLong(), (cache) -> BlockApiCache.create(BLOCK_LOOKUP, serverWorld, pos)).find(state, null);
    }

    /**
     * Find an quantity holder for the given {@link BlockPos} and {@link Direction}.
     * @apiNote Use of {@link EnergySystem#of(ServerWorld, BlockPos)} is recommended for frequent use, as it utilises a cache.
     * @param world
     * @param pos
     * @return
     */
    public static EnergyHandler find(World world, BlockPos pos) {
        return find(world, pos, null, null);
    }

    /**
     *
     * @apiNote Use of {@link EnergySystem#of(ServerWorld, BlockPos)} is recommended for frequent use, as it utilises a cache.
     * @param world
     * @param pos
     * @param state
     * @param blockEntity
     * @return
     */
    public static EnergyHandler find(World world, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity) {
        return BLOCK_LOOKUP.find(world, pos, state, blockEntity, null);
    }

}
