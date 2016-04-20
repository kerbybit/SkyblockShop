package com.kerbybit.SkyblockShop;

import java.io.*;
import java.util.ArrayList;

/**
 * Created in com.kerbybit.SkyblockShop by kerbybutt on 4/19/2016.
 */
class FileHandler {

    //load all .txt files in ./plugins/SkyblockShop/ as shops
    static ArrayList<String> loadShop() {
        //check directory, create if missing
        File dir = new File("./plugins/SkyblockShop/");
        if (!dir.exists()) {if (!dir.mkdir()) { System.out.println("Failed to create SkyblockShop directory!");}}

        //create fullShop to return all retrieved files
        ArrayList<String> fullShop = new ArrayList<>();

        //iterate through all files in directory and add them to fullShop
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt")) {
                    fullShop.addAll(loadShop(file));
                }
            }
        }

        return fullShop;
    }

    //load single shop file
    private static ArrayList<String> loadShop(File file) {
        System.out.println("Loading shop " + file.getName() + "...");

        ArrayList<String> temp = new ArrayList<>();

        try {
            //load file into a list of strings
            String lineRead;
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("./plugins/SkyblockShop/" + file.getName()), "UTF-8"));
            while ((lineRead = bufferedReader.readLine()) != null) {
                lines.add(lineRead);
            }
            bufferedReader.close();

            //set what number the list is on for counting
            int itemNum = -1;

            //parse list of strings
            for (String line : lines) {
                //split line into args
                String[] args = line.trim().split(":");

                //check validity
                if (args.length == 2) {
                    //switch for line
                    switch(args[0].toUpperCase()) {
                        case("ITEM"):
                            //move to next item and start adding it
                            itemNum++;
                            temp.add(args[1]);
                            break;
                        case("NAME"):
                        case("CATEGORY"):
                        case("SLOT"):
                        case("ACTION"):
                        case("VALUE"):
                            //add to current item
                            temp.set(itemNum, temp.get(itemNum) + "->" + args[1]);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong while reading the file!");
        }

        return temp;
    }

    static void saveMeta() {
        //save the meta file for storing money

        //check directory, create if missing
        File dir = new File("./plugins/SkyblockShop/");
        if (!dir.exists()) {if (!dir.mkdir()) { System.out.println("Failed to create SkyblockShop directory!");}}

        //save 'money' to .meta file
        try {
            PrintWriter writer = new PrintWriter("./plugins/SkyblockShop/.meta", "UTF-8");
            writer.println(Main.money);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong while creating the .meta file!");
        }
    }

    static void loadMeta() {
        System.out.println("Loading meta...");

        //check directory, create if missing
        File dir = new File("./plugins/SkyblockShop/");
        if (!dir.exists()) {if (!dir.mkdir()) { System.out.println("Failed to create SkyblockShop directory!");}}

        //check file, create if missing
        File file = new File("./plugins/SkyblockShop/.meta");
        if (!file.isFile()) {saveMeta();}

        //load the .meta file
        try {
            String lineRead;
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("./plugins/SkyblockShop/.meta"), "UTF-8"));
            while ((lineRead = bufferedReader.readLine()) != null) {
                lines.add(lineRead);
            }
            bufferedReader.close();

            //set money from the first line of file
            Main.money = Integer.parseInt(lines.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            Main.money = 0;
            System.out.println("Something went wrong while loading the .meta file!");
        }
    }
}
