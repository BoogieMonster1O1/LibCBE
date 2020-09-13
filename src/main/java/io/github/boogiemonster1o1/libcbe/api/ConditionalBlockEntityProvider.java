package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * This is the root interface for conditional block entities. Every check
 * happens through this interface. The others only provide mere hooks, such as
 * {@link ConditionalScreenHandledBlockEntityProvider} and {@link ConditionalBlockWithEntity}.
 *
 * The creation of the block entity will fail if either one of these
 * methods return {@code false}, or will succeed only if both of these
 * methods return {@code true}.
 *
 * It is recommended that you implement/extend one of the other classes,
 * {@link ConditionalScreenHandledBlockEntityProvider} or {@link ConditionalBlockWithEntity}
 * when making a block entity that is linked to a {@link ScreenHandler} as it provides
 * some necessary hooks.
 */
public interface ConditionalBlockEntityProvider extends BlockEntityProvider {
    boolean hasBlockEntity(BlockState state);

    default boolean hasBlockEntity(BlockPos pos, BlockView world) {
        return this.hasBlockEntity(world.getBlockState(pos));
    }
}
