package net.matth.ships;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ShipbuildingScreen extends HandledScreen<ShipbuildingScreenHandler> {
    // private static final Identifier TEXTURE =
    // Identifier.ofVanilla("textures/gui/container/dispenser.png");
    // For versions before 1.21:
    private static final Identifier TEXTURE = new Identifier(Ships.MOD_ID, "textures/gui/shipyard.png");

    private int currentlySelectedShip = 0; // 0 = New ship

    public ShipbuildingScreen(ShipbuildingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.titleY -= 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        boolean canScroll = this.handler.getShipCount() > 2;
        context.drawTexture(TEXTURE, x + 66, y + 13, 176 + (canScroll ? 0 : 12), 0, 12, 15);
        for (int i = 0; i < 1; i++) {
            int r = mouseX - (x + 6);
            int s = mouseY - (y + 12 + 19 * i);
            if (currentlySelectedShip == i) {
                context.drawTexture(TEXTURE, x + 6, y + 12 + 19 * i, 0, 185, 58, 19);
            } else if (r >= 0 && s >= 0 && r < 58 && s < 19) {
                context.drawTexture(TEXTURE, x + 6, y + 12 + 19 * i, 0, 204, 58, 19);
            } else {
                context.drawTexture(TEXTURE, x + 6, y + 12 + 19 * i, 0, 166, 58, 19);
            }
            if (i == 0) {
                context.drawText(this.textRenderer, "New ship...", x + 6 + 5, y + 12 + 19 * i + 5, 0xFFFFFF, true);
            }
        }
        if (currentlySelectedShip == 0) {
            // In shipbuilding mode
            context.drawTexture(TEXTURE, x + 84, y + 16, 0, 223, 18, 18);
            context.drawTexture(TEXTURE, x + 84, y + 36, 0, 223, 18, 18);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.build"), button -> this.buildShip())
                .dimensions(this.width / 2 + 20, 96, 32, 14).build());
        // Center the title
        // titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    public void buildShip() {
        ClientPlayNetworking.send(Ships.SPAWN_SHIP_PACKET_ID, PacketByteBufs.empty());
        this.close();
    }

    public void selectShip(int shipIndex) {
        this.currentlySelectedShip = shipIndex;
    }
}
