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
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk {
    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @Shadow @Final private World world;

    @Inject(method = "loadBlockEntity", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void loadBlockEntity(BlockPos pos, CompoundTag tag, CallbackInfoReturnable<BlockEntity> cir, BlockEntity blockEntity, BlockState blockState, Block block) {
        if(block instanceof ConditionalBlockEntityProvider) {
            if(!((ConditionalBlockEntityProvider) block).hasBlockEntity(blockState) && !((ConditionalBlockEntityProvider) block).hasBlockEntity(pos, (BlockView) this)) {
                blockEntity = null;
            }
        }
    }

    @Inject(method = "createBlockEntity", at = @At("RETURN"), cancellable = true)
    public void createBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(!cbeBlock.hasBlockEntity(this.getBlockState(pos)) && !cbeBlock.hasBlockEntity(pos, (BlockView) this)) {
                cir.setReturnValue(null);
            }
        }
    }

//    @Inject(method = "setBlockState", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//    public void setBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir, int i, int j, int k, ChunkSection chunkSection, boolean bl, BlockState blockState, Block block, BlockEntity blockEntity2) {
//        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
//            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
//            try {
//                if(!cbeBlock.hasBlockEntity(blockState) && !cbeBlock.hasBlockEntity(pos, (BlockView) this)) {
//                    blockEntity2 = null;
//                }
//            } catch (IllegalArgumentException ignored) {
//            }
//        }
//    }

    @Redirect(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)V"))
    public void setBlockState(World world, BlockPos pos, BlockEntity blockEntity, BlockPos pos2, BlockState state, boolean moved) {
        if(state.getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) state.getBlock();
            if(cbeBlock.hasBlockEntity(state) || cbeBlock.hasBlockEntity(pos, (BlockView) this)) {
                this.world.setBlockEntity(pos, blockEntity);
            }
        } else {
            this.world.setBlockEntity(pos, blockEntity);
        }
    }
}
