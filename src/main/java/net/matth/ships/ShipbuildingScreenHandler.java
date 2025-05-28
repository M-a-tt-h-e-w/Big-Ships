package net.matth.ships;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ShipbuildingScreenHandler extends ScreenHandler {
    protected final ScreenHandlerContext context;
    private final Inventory inventory;
    private final PlayerEntity player;

    // This constructor gets called on the client when the server wants it to open
    // the screenHandler,
    // The client will call the other constructor with an empty Inventory and the
    // screenHandler will automatically
    // sync this empty inventory with the inventory on the server.
    public ShipbuildingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    // This constructor gets called from the BlockEntity on the server without
    // calling the other constructor first, the server knows the inventory of the
    // container
    // and can therefore directly provide it as an argument. This inventory will
    // then be synced to the client.
    public ShipbuildingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Ships.SHIPBUILDING_SCREEN_HANDLER, syncId);
        this.context = context;
        this.inventory = new SimpleInventory(2);
        this.player = playerInventory.player;

        // This will place the slot in the correct locations for a 3x3 Grid. The slots
        // exist on both server and client!
        // This will not render the background of the slots however, this is the Screens
        // job
        int m;
        int l;
        this.addSlot(new Slot(inventory, 0, 85, 17) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ItemTags.PLANKS);
            }
        });
        this.addSlot(new Slot(inventory, 1, 85, 37) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ItemTags.PLANKS);
            }
        });
        // Our inventory
        // for (m = 0; m < 3; ++m) {
        // for (l = 0; l < 3; ++l) {
        // this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
        // }
        // }
        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    public void buildShip() {
        Slot inputSlot1 = this.getSlot(0),
                inputSlot2 = this.getSlot(1);
        ItemStack stack1 = inputSlot1.getStack(),
                stack2 = inputSlot2.getStack();
        if (stack1.isIn(ItemTags.PLANKS) && stack1.getCount() >= 64 && stack2.isIn(ItemTags.PLANKS)
                && stack2.getCount() >= 64) {
            inputSlot1.takeStack(64);
            inputSlot1.markDirty();
            inputSlot2.takeStack(64);
            inputSlot2.markDirty();

            World world = this.player.getWorld();
            // BlockPos spawnPos = findNearestWater(this.player);
            // if (spawnPos != null) {
            // ShipEntity ship = new ShipEntity(MyEntities.SHIP, world);
            // ship.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY() + 1,
            // spawnPos.getZ() + 0.5, 0, 0);
            // world.spawnEntity(ship);
            // }
            ShipEntity ship = new ShipEntity(Ships.SHIP,
                    world);
            ship.refreshPositionAndAngles(this.player.getX(), this.player.getY(), this.player.getZ(), this.player.getYaw(), 0);
            world.spawnEntity(ship);

            this.player.sendMessage(Text.of("Ship spawned!"), true);
        } else {
            this.player.sendMessage(Text.of("Not enough planks (2 stacks required)"), true);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    public int getShipCount() {
        return 0;
    }

    public String getShipName(int shipIndex) {
        return "Schooner";
    }
}
