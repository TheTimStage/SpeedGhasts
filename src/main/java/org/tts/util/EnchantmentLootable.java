package org.tts.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.tts.SpeedGhasts;

public class EnchantmentLootable {

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;
            if (!key.equals(BuiltInLootTables.RUINED_PORTAL)) return;

            HolderLookup.RegistryLookup<Enchantment> enchantmentRegistry = registries.lookupOrThrow(Registries.ENCHANTMENT);
            Holder.Reference<Enchantment> myEnchantEntry = enchantmentRegistry.getOrThrow(SpeedGhasts.ENCHANTMENT_KEY);

            EnchantRandomlyFunction.Builder myEnchantFunction = EnchantRandomlyFunction.randomEnchantment().withEnchantment(myEnchantEntry);

            LootItemCondition.Builder chance = LootItemRandomChanceCondition.randomChance(0.50f);
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0f))
                    .when(chance)
                    .add(LootItem.lootTableItem(Items.BOOK).apply(myEnchantFunction));

            tableBuilder.withPool(poolBuilder);
        });
    }
}
