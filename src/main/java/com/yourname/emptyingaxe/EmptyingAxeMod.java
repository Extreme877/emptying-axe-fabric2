package com.yourname.emptyingaxe;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EmptyingAxeMod implements ModInitializer {
    public static final String MODID = "emptying_axe";
    public static final Item EMPTYING_AXE = Registry.register(
            Registries.ITEM,
            new Identifier(MODID, "emptying_axe"),
            new EmptyingAxeItem(ToolMaterials.IRON, 6.0f, -3.1f, new Item.Settings().maxCount(1))
    );

    @Override
    public void onInitialize() {
        // Nothing extra needed
    }
}
