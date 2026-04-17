package dragonclient.anticheat.data.tracker;

import dragonclient.anticheat.data.PlayerData;
import net.minecraft.network.Packet;

public abstract class Tracker {
    private final PlayerData data;

    public Tracker(PlayerData data) {
        this.data = data;
    }

    public abstract void handle(Packet<?> packet);

    public PlayerData getData() {
        return data;
    }
}