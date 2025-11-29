package org.tts.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.tts.SpeedGhasts;

import java.util.Map;

public class EnchantmentLootable {

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) {
                return;
            }

            if (!key.equals(LootTables.RUINED_PORTAL_CHEST)) {
                return;
            }

            RegistryWrapper.Impl<Enchantment> enchantmentRegistry =
                    registries.getOrThrow(RegistryKeys.ENCHANTMENT);

            RegistryEntry<Enchantment> myEnchantEntry =
                    enchantmentRegistry.getOrThrow(SpeedGhasts.ENCHANTMENT_KEY);

            EnchantRandomlyLootFunction.Builder myEnchantFunction =
                    EnchantRandomlyLootFunction.builder(registries)
                            .option(myEnchantEntry)
                            .allowIncompatible();

            LootCondition.Builder chance = RandomChanceLootCondition.builder(0.50f);
            LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0f))
                    .conditionally(chance)
                    .with(
                            ItemEntry.builder(Items.ENCHANTED_BOOK)
                                    .apply(myEnchantFunction)
                    );

            tableBuilder.pool(poolBuilder);
        });
    }
}
