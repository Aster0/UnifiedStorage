package me.astero.mechanicaldrawersmod.registry.data;

import java.util.function.Supplier;


@FunctionalInterface
public interface ObjectData<T> {


    Supplier get();
}
