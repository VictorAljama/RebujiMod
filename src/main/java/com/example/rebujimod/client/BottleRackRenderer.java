package com.example.rebujimod.client;

import com.example.rebujimod.BottleRackBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BottleRackRenderer implements BlockEntityRenderer<BottleRackBlockEntity> {

    private final ItemRenderer itemRenderer;
    private static final int COLS = 4;
    private static final int ROWS = 6;
    private static final int SIZE = 24;

    public BottleRackRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(BottleRackBlockEntity tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (tile == null) {
            System.out.println("[BottleRackRenderer] TILE ES NULL!");
            return;
        }

        BlockState state = tile.getBlockState();
        Direction facing = state.getValue(com.example.rebujimod.BottleRack.FACING);

        int bottlesCount = 0;
        for (int i = 0; i < SIZE; i++) {
            if (!tile.getBottle(i).isEmpty()) {
                bottlesCount++;
            }
        }
        System.out.println("[BottleRackRenderer] render() called - Facing: " + facing + ", Botellas: " + bottlesCount + "/" + SIZE);

        // Renderizar cada botella en su posición dentro del botellero
        // El botellero está estructurado como una rejilla 4x6 (columnas x filas)
        for (int slot = 0; slot < SIZE; slot++) {
            ItemStack bottle = tile.getBottle(slot);
            if (bottle.isEmpty()) continue;

            System.out.println("[BottleRackRenderer] Renderizando botella en slot " + slot + ": " + bottle.getItem());

            int row = slot / COLS;
            int col = slot % COLS;

            // Posiciones basadas en la estructura del modelo
            // El modelo tiene dimensiones aproximadas: X=0-16, Y=0-16, Z=10-15
            // Las botellas se distribuyen en estos espacios

            // Distribución en X: 4 columnas espaciadas uniformemente (0-16)
            double x = (col + 0.5) / COLS;  // 0.125, 0.375, 0.625, 0.875

            // Distribución en Y: 6 filas espaciadas uniformemente
            // Pero el botellero tiene estructura variable, ajustar a 2-14 en modelo = 0.125-0.875 normalizado
            double y = 0.125 + (row / (double) ROWS) * 0.75;

            // Distribución en Z: centrado en la profundidad del botellero (Z=10-15 en modelo)
            // Normalizado: (10.5-15) / 16 ≈ 0.656-0.9375
            double z = 0.656 + (row % 2) * 0.1;  // Pequeña variación por profundidad

            // Rotar según dirección del bloque
            double[] rotated = rotatePosition(x, y, z, facing);

            // Transformar a coordenadas de mundo
            poseStack.pushPose();
            poseStack.translate(rotated[0], rotated[1], rotated[2]);

            // Rotar botella según dirección
            float rotationY = getRotationForDirection(facing);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotationY));

            // Aplicar rotación para que las botellas se vean dentro del botellero
            // inclinadas según los soportes del modelo
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(45));
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-30));

            // Escala pequeña para que se vean como botellas dentro del botellero
            float scale = 0.12f;
            poseStack.scale(scale, scale, scale);

            // Renderizar el item en 3D
            itemRenderer.renderGuiItem(poseStack, bottle, 0, 0);

            poseStack.popPose();
        }
    }

    /**
     * Calcula la posición rotada según la dirección del bloque
     * Las coordenadas x, y, z están en rango 0-1 correspondientes al espacio del bloque
     */
    private double[] rotatePosition(double x, double y, double z, Direction facing) {
        // Las posiciones ya están normalizadas (0-1)
        // Solo necesitamos aplicar las rotaciones según la dirección

        switch (facing) {
            case NORTH:
                // Frente hacia norte (hacia el jugador cuando está mirando norte)
                return new double[]{x, y, z};

            case SOUTH:
                // Invertir X y Z
                return new double[]{1.0 - x, y, 1.0 - z};

            case EAST:
                // Rotar 90° (X → Z, Z → -X)
                return new double[]{1.0 - z, y, x};

            case WEST:
                // Rotar -90° (X → -Z, Z → X)
                return new double[]{z, y, 1.0 - x};

            default:
                return new double[]{x, y, z};
        }
    }

    /**
     * Obtiene la rotación en Y según la dirección
     */
    private float getRotationForDirection(Direction facing) {
        return switch (facing) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case EAST -> 90;
            case WEST -> 270;
            default -> 0;
        };
    }
}
