package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An alternative to {@link ConditionalBlockWithEntity}.
 *
 * This did not work as how it should have.
 *
 * @deprecated Please extend {@link ConditionalBlockWithEntity} or
 * override {@link Block#onSyncedBlockEvent(BlockState, World, BlockPos, int, int)}
 * and {@link Block#createScreenHandlerFactory(BlockState, World, BlockPos)} instead.
 * This
 */
@Deprecated
public interface ConditionalScreenHandledBlockEntityProvider extends ConditionalBlockEntityProvider, NamedScreenHandlerFactory {
}
