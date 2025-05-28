package net.matth.ships;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ShipEntity extends BoatEntity {

    public ShipEntity(EntityType<? extends ShipEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Item asItem() {
        return Items.AIR;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity
                && ((PlayerEntity) source.getAttacker()).getAbilities().creativeMode) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected int getMaxPassengers() {
        return 1;
    }

    @Override
    public double getMountedHeightOffset() {
        return 6.75D;
    }

    @Override
    protected float getPassengerHorizontalOffset() {
        return -8.0F;
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return new Box(
                this.getX() - 15D,
                this.getY() - 1.5D,
                this.getZ() - 15D,
                this.getX() + 15D,
                this.getY() + 20D,
                this.getZ() + 15D);
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    @Override
    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        // if (this.hasPassenger(passenger)) {
        //     double d = this.getY() + this.getMountedHeightOffset() +
        //             passenger.getHeightOffset(),
        //             offset = this.getPassengerHorizontalOffset(),
        //             yaw = Math.toRadians(this.getBodyYaw());
        //     positionUpdater.accept(passenger, this.getX() + offset * -Math.sin(yaw), d,
        //             this.getZ() + offset * Math.cos(yaw));
        // }
        super.updatePassengerPosition(passenger, positionUpdater);
    }
}
