package com.kerbybit.SkyblockShop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created in com.kerbybit.SkyblockShop by kerbybit on 4/19/2016.
 */
class CommHandler implements Listener {
    /*
     Yeah, I know I didn't do the commands the "right" way. Fun fact, Im lazy :D
     Remember, I made this plugin for a private server that I play on for fun.
     I have no intention to ever update this to use actual commands.
     */


    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event) {
        //get player and chat message arguments
        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");

        //check to make sure its a valid chat message
        if (args.length != 0) {
            if (args[0].toUpperCase().startsWith("!")) {
                //cancel commands from chat
                event.setCancelled(true);

                //switch statement for the different commands
                switch (args[0].toUpperCase()) {
                    case ("!SHOP"):
                        //main shop command
                        if (args.length == 1) {
                            if (ShopHandler.hasDefaultShop()) {
                                //open default shop defined as "category:Shop"
                                ShopHandler.openShop(player);
                            } else {
                                //warn no default shop
                                player.sendMessage(ChatColor.RED + "No shop defined!");
                            }
                        } else {
                            //open specific shop
                            if (!ShopHandler.openShop(player, args[1])) {
                                //if shop doesn't exist
                                player.sendMessage(ChatColor.RED + "That is not a shop!");
                            }
                        }
                        break;
                    case ("!LISTSHOP"):
                        //debug command
                        for (String shop : ShopHandler.getShops()) {
                            //list all defined shops
                            player.sendMessage(shop);
                        }
                        break;
                    default:
                        //bad command
                        player.sendMessage(ChatColor.RED + "Not a command!");
                        break;
                }
            }
        }
    }
}
