import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ActionBarUtil {

    private static final Map<Player, BukkitTask> PENDING_MESSAGES = new HashMap<>();

    public static void sendActionBarMessage(@NotNull Player bukkitPlayer, @NotNull String message) {
        sendRawActionBarMessage(bukkitPlayer, "{\"text\": \"" + message + "\"}");
    }


    public static void sendRawActionBarMessage(@NotNull Player bukkitPlayer, @NotNull String rawMessage) {
        CraftPlayer player = (CraftPlayer) bukkitPlayer;
        IChatBaseComponent chatBaseComponent = ChatSerializer.a(rawMessage);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
        player.getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }


    public static void sendActionBarMessage(@NotNull final Player bukkitPlayer, @NotNull final String message,
                                            @NotNull final int duration, @NotNull Plugin plugin) {
        cancelPendingMessages(bukkitPlayer);
        final BukkitTask messageTask = new BukkitRunnable() {
            private int count = 0;
            @Override
            public void run() {
                if (count >= (duration - 3)) {
                    this.cancel();
                }
                sendActionBarMessage(bukkitPlayer, message);
                count++;
            }
        }.runTaskTimer(plugin, 0L, 20L);
        PENDING_MESSAGES.put(bukkitPlayer, messageTask);
    }

    private static void cancelPendingMessages(@NotNull Player bukkitPlayer) {
        if (PENDING_MESSAGES.containsKey(bukkitPlayer)) {
            PENDING_MESSAGES.get(bukkitPlayer).cancel();
        }
    }
}
