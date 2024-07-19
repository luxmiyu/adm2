package dev.luxmiyu.adm2.portal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private final Map<UUID, TeleportData> dataMap;
    private final int MAX_COOLDOWN = 20;

    public TeleportManager() {
        dataMap = new HashMap<>();
    }

    public void setData(UUID uuid, int cooldown, boolean teleported) {
        dataMap.put(uuid, new TeleportData(cooldown, teleported));
    }

    public TeleportData getData(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, k -> new TeleportData(MAX_COOLDOWN, false));
    }

    public void tick(UUID uuid) {
        TeleportData data = getData(uuid);
        if (!data.teleported) {
            data.cooldown = Math.max(0, data.cooldown - 1);
        }
    }

    public void resetCooldown(UUID uuid) {
        getData(uuid).cooldown = MAX_COOLDOWN;
    }

    public boolean shouldTeleport(UUID uuid) {
        TeleportData data = getData(uuid);
        return data.cooldown <= 0 && !data.teleported;
    }

    public void onPlayerTeleport(UUID uuid) {
        setData(uuid, MAX_COOLDOWN, true);
    }

    public boolean hasTeleported(UUID uuid) {
        return getData(uuid).teleported;
    }

    public void resetTeleported(UUID uuid) {
        getData(uuid).teleported = false;
    }
}
