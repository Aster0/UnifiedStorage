package me.astero.unifiedstoragemod.items.data;

public enum NetworkBlockType {


    CONTROLLER("Storage Controller"),
    STORAGE("Storages");



    private final String blockName;


    public String getBlockName() {
        return this.blockName;
    }

    NetworkBlockType(String blockName) {
        this.blockName = blockName;
    }
}
