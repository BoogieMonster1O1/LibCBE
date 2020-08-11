package io.github.boogiemonster1o1.libcbe.mixin;

import io.github.boogiemonster1o1.libcbe.api.ConditionalBlockEntityProvider;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk {
    @Shadow @Final private World world;

    @Shadow public abstract void addBlockEntity(BlockEntity blockEntity);

    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @Shadow private volatile boolean shouldSave;

    @Inject(method = "loadBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;"), cancellable = true)
    public void loadBlockEntity(BlockPos pos, CompoundTag tag, CallbackInfoReturnable<BlockEntity> cir) {
        if(this.getBlockState(pos) instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(!cbeBlock.hasBlockEntity(this.getBlockState(pos)) && !cbeBlock.hasBlockEntity(pos, this.world)) {
                BlockEntity dummy = BlockEntity.createFromTag(this.getBlockState(pos), tag);
                if(dummy != null) {
                    dummy.setLocation(this.world, pos);
                    this.addBlockEntity(dummy);
                }
                cir.setReturnValue(dummy);
            }
        }
    }

    @Inject(method = "createBlockEntity", at = @At("RETURN"), cancellable = true)
    public void createBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(cbeBlock.hasBlockEntity(this.getBlockState(pos)) || cbeBlock.hasBlockEntity(pos, this.world)) {
                cir.setReturnValue(cbeBlock.createBlockEntity(this.world));
            }
            else {
                cir.setReturnValue(null);
            }
        }
    }

    @Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void setBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir, int i, int j, int k, ChunkSection chunkSection, boolean bl, BlockState blockState, Block block, BlockEntity blockEntity2) {
        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(cbeBlock.hasBlockEntity(this.getBlockState(pos)) || cbeBlock.hasBlockEntity(pos, this.world)) {
                blockEntity2 = cbeBlock.createBlockEntity(this.world);
                this.world.setBlockEntity(pos, blockEntity2);
            }
            else {
                this.shouldSave = true;
                cir.setReturnValue(blockState);
            }
        }
    }
}
