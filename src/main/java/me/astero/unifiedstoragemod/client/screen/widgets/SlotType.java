package me.astero.unifiedstoragemod.client.screen.widgets;

public enum SlotType {
    UPGRADE(80, 2),
    NETWORK(80, 2),
    FILTER(80, 2),
    VISUAL_BLOCK(103, 3);


    private int iconHeight, iconWidth;

    SlotType(int iconHeight, int iconWidth) {

        this.iconHeight = iconHeight;
        this.iconWidth = iconWidth;

    }

    public int getIconHeight() {
        return iconHeight;
    }

    public int getIconWidth() {
        return iconWidth;
    }
}
