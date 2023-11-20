package me.astero.unifiedstoragemod.data;

import java.util.function.Supplier;


@FunctionalInterface
public interface ObjectData<T> { // using this because if we get from the supplier straight, registry will be frozen.


    Supplier get();
}
