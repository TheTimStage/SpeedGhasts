package org.tts;

import net.fabricmc.api.ModInitializer;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tts.util.EnchantmentLootable;

public class SpeedGhasts implements ModInitializer {
	public static final String MOD_ID = "speedghasts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final RegistryKey<Enchantment> ENCHANTMENT_KEY =
			RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("speedghasts", "flight_speed"));

	@Override
	public void onInitialize() {
		EnchantmentLootable.register();
	}
}