package io.github.boogiemonster1o1.libcbe.mixin;

import io.github.boogiemonster1o1.libcbe.api.ConditionalBlockEntityProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

@Mixin(ChunkRegion.class)
public abstract class MixinChunkRegion implements BlockView {
    @Redirect(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)V"))
    public void setBlockState(Chunk chunk, BlockPos pos, BlockEntity blockEntity, BlockPos blockPos, BlockState state, int flags, int maxUpdateDepth) {
        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(cbeBlock.hasBlockEntity(state) || cbeBlock.hasBlockEntity(pos, this)) {
                chunk.setBlockEntity(pos, ((BlockEntityProvider) this.getBlockState(pos).getBlock()).createBlockEntity(this));
            }
        } else {
            chunk.setBlockEntity(pos, ((BlockEntityProvider) this.getBlockState(pos).getBlock()).createBlockEntity(this));
        }
    }
}
