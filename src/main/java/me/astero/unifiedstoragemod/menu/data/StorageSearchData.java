package me.astero.unifiedstoragemod.menu.data;

import me.astero.unifiedstoragemod.data.ItemIdentifier;

import java.util.ArrayList;
import java.util.List;

public class StorageSearchData {

    private boolean isSearching = false;

    private List<ItemIdentifier> searchedStorageList = new ArrayList<>();

    public List<ItemIdentifier> getSearchedStorageList() {
        return searchedStorageList;
    }

    public void setSearching(boolean searching) {
        isSearching = searching;
    }

    public boolean isSearching() {
        return isSearching;
    }
}
