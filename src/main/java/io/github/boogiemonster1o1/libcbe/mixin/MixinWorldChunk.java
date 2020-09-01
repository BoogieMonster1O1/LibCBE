package io.github.boogiemonster1o1.libcbe.mixin;

import io.github.boogiemonster1o1.libcbe.api.ConditionalBlockEntityProvider;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk implements Chunk {
    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @Shadow @Final private World world;

    @Inject(method = "createBlockEntity", at = @At("RETURN"), cancellable = true)
    public void createBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        if(this.getBlockState(pos).getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) this.getBlockState(pos).getBlock();
            if(!cbeBlock.hasBlockEntity(this.getBlockState(pos)) && !cbeBlock.hasBlockEntity(pos, this)) {
                cir.setReturnValue(null);
            }
        }
    }

    @Redirect(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)V"))
    public void setBlockState(World world, BlockPos pos, BlockEntity blockEntity, BlockPos pos2, BlockState state, boolean moved) {
        if(state.getBlock() instanceof ConditionalBlockEntityProvider) {
            ConditionalBlockEntityProvider cbeBlock = (ConditionalBlockEntityProvider) state.getBlock();
            if(cbeBlock.hasBlockEntity(state) || cbeBlock.hasBlockEntity(pos, this)) {
                this.world.setBlockEntity(pos, blockEntity);
            }
        } else {
            this.world.setBlockEntity(pos, blockEntity);
        }
    }
}
