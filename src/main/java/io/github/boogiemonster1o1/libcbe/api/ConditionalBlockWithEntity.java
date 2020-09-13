package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;

/**
 * Extend this class if you want conditional block entities
 */
public abstract class ConditionalBlockWithEntity extends BlockWithEntity implements ConditionalBlockEntityProvider {
    public ConditionalBlockWithEntity(Settings settings) {
        super(settings);
    }
    /**
     * Hook to allow loading models for the block.
     * {@link BlockWithEntity#getRenderType} sets the {@code BlockRenderType}
     * to {@link BlockRenderType#INVISIBLE}. This prevents any model from
     * being rendered. Some blocks, such as Air and Barriers do set it to
     * {@link BlockRenderType#INVISIBLE} but that has a purpose.
     *
     * Ideally you should set this to {@link BlockRenderType#ENTITYBLOCK_ANIMATED} for
     * block entities that have a custom model, or {@link BlockRenderType#MODEL} for
     * static block entities
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
