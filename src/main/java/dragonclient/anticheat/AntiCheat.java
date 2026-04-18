package dragonclient.anticheat;

import net.lax1dude.eaglercraft.EaglercraftUUID;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dragonclient.anticheat.data.PlayerData;

public class AntiCheat {

    public static final AntiCheat INSTANCE = new AntiCheat();

    private final Map<EaglercraftUUID, PlayerData> players = new ConcurrentHashMap<>();

    public void handlePlayers() {
        players.values().stream().filter(this::hasEntity).forEach(PlayerData::updateTicks);
        players.values().stream().filter(d -> !hasEntity(d)).forEach(d -> players.remove(d.getPlayer().getUniqueID()));
    }

    private boolean hasEntity(PlayerData data) {
        return Minecraft.getMinecraft().world.playerEntities.contains(data.getPlayer());
    }

    public Map<EaglercraftUUID, PlayerData> getPlayers() {
        return players;
    }
}
