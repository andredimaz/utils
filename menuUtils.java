package github.andredimaz.plugin.menus;

import github.andredimaz.plugin.Main;
import github.andredimaz.plugin.utils.colorUtils;
import github.andredimaz.plugin.utils.materialUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Set;
import java.util.stream.Collectors;

public class MenuSword {

    private final Main plugin;

    public MenuSword(Main plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        ConfigurationSection menuConfig = plugin.getMainConfig().getConfigurationSection("menu");
        if (menuConfig == null) {
            player.sendMessage(ChatColor.RED + "Erro: Configuração do menu não encontrada.");
            return;
        }

        int slots = menuConfig.getInt("slots");
        String title = colorUtils.colorize(menuConfig.getString("titulo", "Menu da Espada"));
        Inventory menu = Bukkit.createInventory(null, slots, title);

        Set<String> itemKeys = menuConfig.getKeys(false);
        for (String itemKey : itemKeys) {
            ConfigurationSection itemConfig = menuConfig.getConfigurationSection(itemKey);
            if (itemConfig != null) {
                ItemStack item = createItemFromConfig(itemConfig, player);
                int slot = itemConfig.getInt("slot", 0);
                if (slot >= 0 && slot < slots) {
                    menu.setItem(slot, item);
                } else {
                    plugin.getLogger().warning("Slot inválido para item " + itemKey + ": " + slot);
                }
            }
        }

        player.openInventory(menu);
    }

    public static ItemStack createItemFromConfig(ConfigurationSection itemConfig, Player player) {
        ItemStack item;
        String materialString = itemConfig.getString("material");

        if (materialString.equals("{player}")) {
            item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwner(player.getName());


            item.setItemMeta(skullMeta);

        } else {
            item = materialUtils.parseMaterial(materialString);
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(colorUtils.colorize(itemConfig.getString("nome")));
        if (meta.getDisplayName().isEmpty()) {
            meta.setDisplayName("");
        }
        meta.setLore(itemConfig.getStringList("lore").stream().map(colorUtils::colorize).collect(Collectors.toList()));
        item.setItemMeta(meta);

        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // Esconder atributos
            item.setItemMeta(meta);
        }

        return item;
    }
}
