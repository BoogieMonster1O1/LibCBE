package io.github.boogiemonster1o1.libcbe.mixin;

import io.github.boogiemonster1o1.libcbe.api.ConditionalBlockEntityProvider;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

@Mixin(ChunkRegion.class)
public abstract class MixinChunkRegion implements BlockView {
    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @Shadow @Final private ServerWorld world;

    @Inject(method = "getBlockEntity", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void getBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir, Chunk chunk, BlockEntity blockEntity, CompoundTag compoundTag, BlockState blockState, Block block) {
        if(block instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) block;
            if(!cbeBlock.hasBlockEntity(blockState) && !cbeBlock.hasBlockEntity(pos, this.world)) {
                blockEntity = null;
            }
        }
    }

    @Redirect(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)V"))
    public void setBlockState(Chunk chunk, BlockPos pos, BlockEntity blockEntity, BlockPos blockPos, BlockState state, int flags, int maxUpdateDepth) {
        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(cbeBlock.hasBlockEntity(state) || cbeBlock.hasBlockEntity(pos, this.world)) {
                chunk.setBlockEntity(pos, ((BlockEntityProvider)this.getBlockState(pos).getBlock()).createBlockEntity(this));
            }
        } else {
            chunk.setBlockEntity(pos, ((BlockEntityProvider)this.getBlockState(pos).getBlock()).createBlockEntity(this));
        }
    }
}
