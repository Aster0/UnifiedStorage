package me.astero.unifiedstoragemod.utils;

import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;


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

    public static List<Component> breakComponentLine(MutableComponent component) {


        List<Component> components = new ArrayList<>();
        String[] componentString = component.getString().split("\n");

        for(String str : componentString) {


            components.add(Component.literal(str));
        }


        return components;


    }

    public static List<Component> breakComponentLine(MutableComponent component, int ...amount) {


        List<Component> components = new ArrayList<>();
        String[] componentString = component.getString().split("\n");

        int index = 0;

        for(String str : componentString) {


            components.add(Component.literal(str.replace("%amount%", String.valueOf(amount[index]))));

            if(str.contains("%amount%")) {
                index++;
            }
        }


        return components;


    }
}
