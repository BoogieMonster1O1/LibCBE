package io.github.boogiemonster1o1.libcbe.test;

import io.github.boogiemonster1o1.libcbe.api.ConditionalBlockEntityProvider;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class LibCBETest implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {

    }

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(DOOR_BLOCK_ENTITY, DoorBlockEntityRenderer::new);
    }

    public static final Door DOOR;
    public static final BlockEntityType<DoorBlockEntity> DOOR_BLOCK_ENTITY;

    private static class Door extends DoorBlock implements ConditionalBlockEntityProvider {
        public Door(Settings settings) {
            super(settings);
        }

        @Override
        public boolean hasBlockEntity(BlockState state) {
            return state.get(HALF) == DoubleBlockHalf.LOWER;
        }

        @Override
        public BlockEntity createBlockEntity(BlockView world) {
            return new DoorBlockEntity();
        }
    }

    private static class DoorBlockEntity extends BlockEntity {
        public DoorBlockEntity() {
            super(DOOR_BLOCK_ENTITY);
        }
    }

    @Environment(EnvType.CLIENT)
    private static class DoorBlockEntityRenderer extends BlockEntityRenderer<DoorBlockEntity> {
        public DoorBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public void render(DoorBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            matrices.push();
            double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            matrices.translate(0.5, 1.25 + offset, 0.5);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.POTATO), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
            matrices.pop();
        }
    }

    static {
        DOOR = Registry.register(Registry.BLOCK, new Identifier("libcbetest", "door"), new Door(FabricBlockSettings.copyOf(Blocks.OAK_DOOR)));
        DOOR_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("libcbetest", "doorbe"), BlockEntityType.Builder.create(DoorBlockEntity::new,DOOR).build(null));
    }
}
