package dragonclient.anticheat.data.tracker.impl;

import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.data.tracker.Tracker;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;


public class AimTracker extends Tracker {

    private float yaw, pitch, lastYaw, lastPitch, yawDiff, pitchDiff;

    private boolean rotating;

    public AimTracker(PlayerData data) {
        super(data);

        this.yaw = data.getPlayer().rotationYaw;
        this.pitch = data.getPlayer().rotationPitch;

        this.rotating = false;
    }

    @Override
    public void handle(Packet<?> packet) {
        if(packet instanceof SPacketEntity.S16PacketEntityLook) {
            SPacketEntity.S16PacketEntityLook packetEntity = (SPacketEntity.S16PacketEntityLook) packet;

            if(packetEntity.getEntityId() == getData().getPlayer().getEntityId()) {

                this.lastYaw = yaw;
                this.lastPitch = pitch;

                this.yaw = packetEntity.getYaw();
                this.pitch = packetEntity.getPitch();

                this.yawDiff = Math.max(yaw, lastYaw) -  Math.min(yaw, lastYaw);
                this.pitchDiff = Math.max(pitch, lastPitch) -  Math.min(pitch, lastPitch);

                this.rotating = yaw != lastYaw || pitch != lastPitch;
            }
        }
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public float getYawDiff() {
        return yawDiff;
    }

    public float getPitchDiff() {
        return pitchDiff;
    }

    public boolean isRotating() {
        return rotating;
    }
}
