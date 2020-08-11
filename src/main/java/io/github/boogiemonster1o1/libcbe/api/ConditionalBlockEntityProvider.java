package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * Implement this interface if you want conditional block entities
 */
public interface ConditionalBlockEntityProvider extends BlockEntityProvider {
    boolean hasBlockEntity(BlockState state);

    default boolean hasBlockEntity(BlockPos pos, BlockView world) {
        return this.hasBlockEntity(world.getBlockState(pos));
    }
}
