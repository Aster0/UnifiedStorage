package me.astero.unifiedstoragemod.items.data;

public enum NetworkBlockType {


    CONTROLLER("Storage Controller"),
    STORAGE_CLONE("Storages"),
    STORAGE("Storages");



    private final String blockName;


    public String getBlockName() {
        return this.blockName;
    }

    NetworkBlockType(String blockName) {
        this.blockName = blockName;
    }
}
