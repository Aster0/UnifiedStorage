package me.astero.unifiedstoragemod.client.screen.widgets;

public enum SlotType {
    UPGRADE(216, 3, 9),
    NETWORK(80, 2, 10),
    FILTER(80, 2, 10),
    VISUAL_BLOCK(103, 3, 10);


    private int iconHeight, iconWidth, offsetX;

    SlotType(int iconHeight, int iconWidth, int offsetX) {

        this.iconHeight = iconHeight;
        this.iconWidth = iconWidth;
        this.offsetX = offsetX;

    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public int getIconWidth() {
        return iconWidth;
    }
}
