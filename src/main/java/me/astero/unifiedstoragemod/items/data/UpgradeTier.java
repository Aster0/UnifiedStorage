package me.astero.unifiedstoragemod.items.data;

public enum UpgradeTier {

    MAX(255);



    private int amount;
    UpgradeTier(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
