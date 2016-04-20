# SkyblockShop
**Note:** I do not plan on updating this or supporting it. It was just a fun side project for me to mess around with.

It does not and probably never will hook into other Bukkit/Spigot plugin economies like Essentials Economy.

## Why?
I made this because I was bored. Thats why. I've been playing on a skyblock server with my brother and the map we were using was designed with a really bad villager shop. I didnt want to use a player based economy like Essentials so I just decided to make my own plugin for the whole economy/shop.

## Use
You create shops based on .txt files stored in ./plugins/SkyblockShop/*.txt

These shops are organized by the following
```
item:wool 32 2
name:Orange Wool
category:Sell (page 1 of 3)
slot:15
  action:sell
  value:10
```
This will create the item "Orange Wool" in the shop "Sell (page 1 of 3)" in slot 15 for the price of 10 moneys.

**NOTE:** This is order dependant.

**NOTE:** Shops are dynamically defined by their category. To make a new shop, just name it something else.

**NOTE:** The default shop that gets opened when a player does '!shop' in chat is 'category:Shop'.

That should be enough to get you started. You can check the example shop that I included for things like navegating shops or closing the inventory.
