package com.kerbybit.SkyblockShop;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

/**
 * Created in com.kerbybit.SkyblockShop by kerbybit on 4/19/2016.
 */
public class Main extends JavaPlugin {

    //global money variable
    static int money;


    //startup
    @Override
    public void onEnable() {
        registerEvents(this, new CommHandler(), new ShopHandler());

        //load .meta file (money)
        FileHandler.loadMeta();

        //load shop txts and generate shop inventories
        ShopHandler.shopList.addAll(FileHandler.loadShop());
        ShopHandler.createShopInvs();

        //run scheduler every 40 ticks to send action bar data for money display
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                //build the message packet to send
                String message = ChatColor.GREEN + "$" + money;
                IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
                PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);

                //iterate through players and send it
                ArrayList<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                for (Player player : players) {
                    CraftPlayer craftPlayer = (CraftPlayer) player;
                    craftPlayer.getHandle().playerConnection.sendPacket(ppoc);
                }
            }
        }, 0L, 40L);
    }


    //check and update money
    static Boolean updateMoney(int change) {
        //make sure that money is above or equal to 0
        if (money + change >= 0) {
            money = money + change;
            //save .meta file
            FileHandler.saveMeta();
            return true;
        } else {
            return false;
        }
    }

    //Method for registering events
    private static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
