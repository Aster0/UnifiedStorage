package me.astero.unifiedstoragemod.utils;

import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;

public class ModUtils {

    public static final String MODID = "unifiedstorage";


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

    public static String serializeBlockPosNbt(String value) {

        String eValue = value.substring(value.indexOf("x"), value.length() - 1);
        // Edited value to e.g., "x=2, y=-60, z=36"


        return eValue;

    }
}
