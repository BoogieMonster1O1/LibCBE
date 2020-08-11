package io.github.boogiemonster1o1.libcbe.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public abstract class ConditionalBlockWithEntity extends Block implements ConditionalBlockEntityProvider {
    public ConditionalBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public abstract BlockEntity createBlockEntity(BlockView world);

    @Override
    public abstract boolean hasBlockEntity(BlockState state);
}
