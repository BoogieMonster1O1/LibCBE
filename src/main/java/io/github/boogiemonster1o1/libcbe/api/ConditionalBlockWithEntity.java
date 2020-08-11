package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public abstract class ConditionalBlockWithEntity extends BlockWithEntity implements ConditionalBlockEntityProvider {
    public ConditionalBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public abstract BlockEntity createBlockEntity(BlockView world);

    @Override
    public abstract boolean hasBlockEntity(BlockState state);

    /**
     * Hook to allow loading models for the block.
     * {@link BlockWithEntity#getRenderType sets the {@code BlockRenderType} to {@code INIVISIBLE}}
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
