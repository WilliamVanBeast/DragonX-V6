package dragonclient.anticheat.data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.check.CheckStorage;
import dragonclient.anticheat.data.tracker.impl.AimTracker;
import dragonclient.anticheat.data.tracker.impl.MovementTracker;

public class PlayerData {

    private final EntityPlayer player;

    private final MovementTracker movementTracker;
    private final AimTracker aimTracker;

    private final List<Check> checks;

    private int ticksExisted;

    private long lastTeleport;

    long serverPosX;

    long serverPosY;

    private long serverPosZ;

    private int ticksSinceLastTeleport,
            ticksSinceLastVelocity;

    private double x, y, z,
            lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ,
            lastDeltaX, lastDeltaY, lastDeltaZ;

    private double groundX, groundY, groundZ,
            lastGroundX, lastGroundY, lastGroundZ;

    private double yaw, pitch;

    private boolean onGround, lastOnGround;
    private boolean isRoughlyGround, lastIsRoughlyGround;

    public PlayerData(EntityPlayer player) {
        this.player = player;

        this.serverPosX = player.serverPosX;
        this.serverPosY = player.serverPosY;
        this.serverPosZ = player.serverPosZ;

        this.movementTracker = new MovementTracker(this);
        this.aimTracker = new AimTracker(this);
        this.checks = CheckStorage.getInstance().loadChecks(this);
    }

    public void handle(Packet<?> packet) {
        if(packet instanceof SPacketEntityTeleport) {
            lastTeleport = System.currentTimeMillis();
        }

                if (packet instanceof SPacketEntity.S15PacketEntityRelMove
                || packet instanceof SPacketEntity.S17PacketEntityLookMove) {
            final SPacketEntity wrapper = ((SPacketEntity) packet);

            if (wrapper.entityId == this.player.getEntityId()) {
                this.serverPosX += wrapper.posX;
                this.serverPosY += wrapper.posY;
                this.serverPosZ += wrapper.posZ;

                this.lastX = this.x;
                this.lastY = this.y;
                this.lastZ = this.z;

                this.x = (double) this.serverPosX / 32.0D;
                this.y = (double) this.serverPosY / 32.0D;
                this.z = (double) this.serverPosZ / 32.0D;

                if (packet instanceof SPacketEntity.S17PacketEntityLookMove) {
                    this.yaw = wrapper.yaw;
                    this.pitch = wrapper.pitch;
                }

                this.lastDeltaX = deltaX;
                this.lastDeltaY = deltaY;
                this.lastDeltaZ = deltaZ;
                this.deltaY = this.y - this.lastY;
                this.deltaZ = this.z - this.lastZ;
                this.deltaX = this.x - this.lastX;

                this.lastOnGround = this.onGround;
                this.onGround = !(getBlock(this.x - 0.5, this.y - 0.43, this.z - 0.5) instanceof BlockAir) ||
                        !(getBlock(this.x + 0.5, this.y - 0.43, this.z - 0.5) instanceof BlockAir) ||
                        !(getBlock(this.x + 0.5, this.y - 0.43, this.z + 0.5) instanceof BlockAir) ||
                        !(getBlock(this.x - 0.5, this.y - 0.43, this.z + 0.5) instanceof BlockAir);

                this.lastIsRoughlyGround = this.isRoughlyGround;
                this.isRoughlyGround = !(getBlock(this.x - 0.5, this.y - 0.99, this.z - 0.5) instanceof BlockAir) ||
                        !(getBlock(this.x + 0.5, this.y - 0.99, this.z - 0.5) instanceof BlockAir) ||
                        !(getBlock(this.x + 0.5, this.y - 0.99, this.z + 0.5) instanceof BlockAir) ||
                        !(getBlock(this.x - 0.5, this.y - 0.99, this.z + 0.5) instanceof BlockAir);

                if (this.onGround) {
                    this.lastGroundX = this.groundX;
                    this.lastGroundY = this.groundY;
                    this.lastGroundZ = this.groundZ;
                    this.groundX = this.x;
                    this.groundY = this.y;
                    this.groundZ = this.z;
                }
            }
        } else if (packet instanceof SPacketEntityTeleport) {
            final SPacketEntityTeleport wrapper = ((SPacketEntityTeleport) packet);

            if (wrapper.getEntityId() == this.player.getEntityId()) {
                this.serverPosX = (long) wrapper.getX();
                this.serverPosY = (long) wrapper.getY();
                this.serverPosZ = (long) wrapper.getZ();

                this.lastX = this.x;
                this.lastY = this.y;
                this.lastZ = this.z;

                this.x = (double) this.serverPosX / 32.0D;
                this.y = (double) this.serverPosY / 32.0D;
                this.z = (double) this.serverPosZ / 32.0D;

                this.ticksSinceLastTeleport = 0;
            }
        } else if (packet instanceof SPacketEntityVelocity) {
            this.ticksSinceLastVelocity = 0;
        }

        movementTracker.handle(packet);
        aimTracker.handle(packet);
        checks.forEach(c -> c.handle(packet));
    }

    public void updateTicks() {
        ticksExisted++;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public MovementTracker getMovementTracker() {
        return movementTracker;
    }

    public AimTracker getAimTracker() {
        return aimTracker;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public int getTicksExisted() {
        return ticksExisted;
    }

    public long getLastTeleport() {
        return lastTeleport;
    }
    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
    public void incrementTick() {
        this.ticksSinceLastVelocity++;
        this.ticksSinceLastTeleport++;
    }

        public long getServerPosX() {
        return serverPosX;
    }

    public long getServerPosY() {
        return serverPosY;
    }

    public long getServerPosZ() {
        return serverPosZ;
    }

    public int getTicksSinceLastTeleport() {
        return ticksSinceLastTeleport;
    }

    public int getTicksSinceLastVelocity() {
        return ticksSinceLastVelocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getLastX() {
        return lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public double getLastZ() {
        return lastZ;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getLastDeltaX() {
        return lastDeltaX;
    }

    public double getLastDeltaY() {
        return lastDeltaY;
    }

    public double getLastDeltaZ() {
        return lastDeltaZ;
    }

    public double getGroundX() {
        return groundX;
    }

    public double getGroundY() {
        return groundY;
    }

    public double getGroundZ() {
        return groundZ;
    }

    public double getLastGroundX() {
        return lastGroundX;
    }

    public double getLastGroundY() {
        return lastGroundY;
    }

    public double getLastGroundZ() {
        return lastGroundZ;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isLastOnGround() {
        return lastOnGround;
    }

    public boolean isRoughlyGround() {
        return isRoughlyGround;
    }

    public boolean isLastIsRoughlyGround() {
        return lastIsRoughlyGround;
    }
}