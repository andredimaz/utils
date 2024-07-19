import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {
    public ActionBar() {
    }

    public static void send(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte)2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
