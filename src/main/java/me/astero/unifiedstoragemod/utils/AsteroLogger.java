package me.astero.unifiedstoragemod.utils;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class AsteroLogger {


    private static final Logger CONSOLE = LogUtils.getLogger();


    public static void info(String message) {

        CONSOLE.info(message);

    }

    public static void warning(String message) {

        CONSOLE.warn(message);

    }

    public static void error(String message) {

        CONSOLE.error(message);

    }
}
