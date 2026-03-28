package org.tts;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tts.util.EnchantmentLootable;

public class SpeedGhasts implements ModInitializer {
	public static final String MOD_ID = "speedghasts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ResourceKey<Enchantment> ENCHANTMENT_KEY =
			ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("speedghasts", "flight_speed"));

	@Override
	public void onInitialize() {
		EnchantmentLootable.register();
	}
}