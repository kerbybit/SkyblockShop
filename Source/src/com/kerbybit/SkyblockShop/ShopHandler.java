package com.kerbybit.SkyblockShop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created in com.kerbybit.SkyblockShop by kerbybit on 4/19/2016.
 */
class ShopHandler implements Listener {
    private static HashMap<String, ArrayList<String>> shopInvPre = new HashMap<>();
    private static HashMap<String, Inventory> shopInv = new HashMap<>();

    static ArrayList<String> shopList = new ArrayList<>();

    static void createShopInvs() {
        //parse 'shopList' into a hashmap for sorting into unique categories
        for (String shop : shopList) {
            //split string into args
            String[] args = shop.split("->");

            //check args length to make sure its valid
            if (args.length == 6) {

                //add to the shopInvPre hashmap
                if (!shopInvPre.containsKey(args[2])) {
                    //if shopInvPre doesn't contain shop category
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(shop);
                    shopInvPre.put(args[2], temp);
                } else {
                    //if shopInvPre already contains shop category
                    ArrayList<String> temp = new ArrayList<>(shopInvPre.get(args[2]));
                    temp.add(shop);
                    shopInvPre.put(args[2], temp);
                }
            }
        }

        //parse 'shotInvPre' to set up all of the inventories with items
        for (Map.Entry<String, ArrayList<String>> shop : shopInvPre.entrySet()) {
            //get key and value from shopInvPre
            String shopName = shop.getKey();
            ArrayList<String> shopItems = shop.getValue();

            //create the inventory
            shopInv.put(shopName, Bukkit.getServer().createInventory(null, 54, shopName));

            //create all of the items for that inventory
            for (String item : shopItems) {
                //split string into args
                String[] args = item.split("->");

                //check args length to make sure it valid
                if (args.length == 6) {
                    //Split the item (eg: grass 10)
                    String[] itemString = args[0].split(" ");

                    //set defaulted values
                    int itemNum = 1;
                    byte itemByte = (byte)0;
                    Material m = null;

                    //check for material
                    if (itemString.length >= 1) {
                        m = Material.getMaterial(itemString[0].toUpperCase());

                        //check for amount
                        if (itemString.length >= 2) {
                            itemNum = Integer.parseInt(itemString[1]);

                            //check for block data
                            if (itemString.length >= 3) {
                                itemByte = (byte)Integer.parseInt(itemString[2]);
                            }
                        }
                    }

                    //default material if nulled
                    if (m==null) {m = Material.STONE_BUTTON;}

                    //create item stack and meta
                    ItemStack itemStack = new ItemStack(m, itemNum, itemByte);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.GOLD + args[1]);

                    //create lore for item stack
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.BLACK + args[4] + "->" + args[5]);
                    if (args[4].equalsIgnoreCase("SELL")) {
                        lore.add(ChatColor.BLACK + "" + itemNum);
                        lore.add(ChatColor.GREEN + "+$" + args[5] + " for " +itemNum);
                    }
                    if (args[4].equalsIgnoreCase("BUY")) {
                        lore.add(ChatColor.BLACK + "" + itemNum);
                        lore.add(ChatColor.RED + "-$" + args[5] + " for " + itemNum);
                    }

                    //set lore
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);

                    //get temp shopInv to add to
                    Inventory temp = shopInv.get(shopName);

                    //check if more than one slot
                    if (args[3].contains(",")) {
                        //if more than one slot, split and add item multiple times
                        String[] slots = args[3].split(",");
                        for (String slot : slots) {temp.setItem(Integer.parseInt(slot.trim()), itemStack);}
                    } else {
                        //if only in one slot, add once
                        temp.setItem(Integer.parseInt(args[3]), itemStack);
                    }

                    //add temp back to shopInv
                    shopInv.put(shopName, temp);
                }
            }
        }
    }

    //open default shop method
    static void openShop(Player player) {
        player.openInventory(shopInv.get("Shop"));
    }

    //open specified shop method
    static boolean openShop(Player player, String shop) {
        if (shopInv.containsKey(shop)) {
            player.openInventory(shopInv.get(shop));
            return true;
        } else {
            return false;
        }
    }

    //check for default shop
    static Boolean hasDefaultShop() {
        return shopInv.containsKey("Shop");
    }

    //get all shops
    static ArrayList<String> getShops() {
        ArrayList<String> temp = new ArrayList<>();
        for (String shop : shopInv.keySet()) {
            temp.add(shop);
        }
        return temp;
    }




    /*
    Inventory handler
    Reads item lore to apply actions
     */
    @EventHandler
    @SuppressWarnings( "deprecation" )
    public static void onInventoryClick(InventoryClickEvent event) {
        //get the player that clicked in an inventory
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());

        //check to make sure the inventory is one of the shops
        if (shopInv.containsKey(event.getInventory().getName())) {
            //make sure item clicked is not null along with its lore (avoids console spam)
            if (event.getCurrentItem()!=null && event.getCurrentItem().getItemMeta()!=null && event.getCurrentItem().getItemMeta().getLore()!=null) {
                //get the lore and item picked up
                List<String> lore = event.getCurrentItem().getItemMeta().getLore();
                ItemStack itemStack = event.getCurrentItem();

                //cancel the pick up event (to avoid getting free stuff from the shop)
                event.setCancelled(true);

                //get the action arguments from the lore (remove the black pretext)
                String[] action = lore.get(0).replace(ChatColor.BLACK.toString(),"").split("->");

                //check length to make sure lore is valid
                if (action.length == 2) {

                    //switch for the different actions
                    switch (action[0].toUpperCase()) {

                        //go to another inventory
                        case "GOTO":
                            player.closeInventory();
                            openShop(player, action[1]);
                            break;

                        //close the inventory
                        case "CLOSE":
                            player.closeInventory();
                            break;

                        //sell an item from the players inventory
                        case "SELL":
                            //check that player has item
                            if (player.getInventory().contains(itemStack.getType(), Integer.parseInt(lore.get(1).replace(ChatColor.BLACK.toString(), "")))) {
                                //remove items from player inventory and give money
                                Main.updateMoney(Integer.parseInt(action[1]));
                                player.getInventory().removeItem(new ItemStack(itemStack.getType(), Integer.parseInt(lore.get(1).replace(ChatColor.BLACK.toString(), "")), itemStack.getData().getData()));
                            } else {
                                //close the shop and warn player in chat
                                player.closeInventory();
                                player.sendMessage(ChatColor.RED + "You don't have that!");
                            }
                            break;

                        //buy an item from the shop inventory
                        case "BUY":
                            //check that there is enough money
                            if (Main.updateMoney(0 - Integer.parseInt(action[1]))) {
                                //create leftover in case of full inventory
                                HashMap<Integer, ItemStack> leftOver = new HashMap<>();

                                //place any left over items into 'leftOver' (addItem returns number of unsuccessful items)
                                leftOver.putAll(player.getInventory().addItem(new ItemStack(itemStack.getType(), Integer.parseInt(lore.get(1).replace(ChatColor.BLACK.toString(), "")), itemStack.getData().getData())));
                                //check if there is leftover
                                if (!leftOver.isEmpty()) {
                                    //drop items by player
                                    Location loc = player.getLocation();
                                    player.getWorld().dropItem(loc, new ItemStack(leftOver.get(0).getType(), leftOver.get(0).getAmount(), itemStack.getData().getData()));
                                }
                            } else {
                                //close the shop and warn player in chat
                                player.closeInventory();
                                player.sendMessage(ChatColor.RED + "Not enough money!");
                            }
                            break;

                        //default case to do nothing
                        default:
                            break;
                    }
                }
            }
        }
    }
}
