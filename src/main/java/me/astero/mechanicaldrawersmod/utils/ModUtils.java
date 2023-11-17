package me.astero.mechanicaldrawersmod.utils;

import me.astero.mechanicaldrawersmod.registry.items.data.CustomBlockPosData;

public class ModUtils {

    public static final String MODID = "mechanicaldrawers";


    public static CustomBlockPosData convertStringToBlockData(String[] value) {
        try {
            int x = Integer.parseInt(value[0].substring(2));
            int y = Integer.parseInt(value[1].substring(2));
            int z = Integer.parseInt(value[2].substring(2));


            return new CustomBlockPosData(x, y, z);
        }
        catch(NumberFormatException e) {
            throw new NumberFormatException("Check the inputted pos if it's numbers.");
        }
    }
}
