package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An alternative to {@link ConditionalBlockWithEntity}. This basically overrides
 * the {@link AbstractBlock#onSyncedBlockEvent} and {@link AbstractBlock#createScreenHandlerFactory}.
 */
public interface ConditionalScreenHandledBlockEntityProvider extends ConditionalBlockEntityProvider, NamedScreenHandlerFactory {
    default boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!this.hasBlockEntity(state) || !this.hasBlockEntity(pos, world)) {
            blockEntity = null;
        }
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    default NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!this.hasBlockEntity(state) || !this.hasBlockEntity(pos, world)) {
            blockEntity = null;
        }
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
    }
}
