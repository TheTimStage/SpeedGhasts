package org.tts.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HappyGhast.class)
public abstract class HappyGhastEntityMixin {

    @Unique
    private static final Identifier FLIGHT_SPEED_ID = Identifier.fromNamespaceAndPath("speedghasts", "flight_speed");

    @Unique
    private static final Identifier SPEED_MODIFIER_ID = Identifier.fromNamespaceAndPath("speedghasts", "flight_boost");

    @Inject(method = "tick", at = @At("HEAD"))
    private void speedghasts$updateSpeedAttribute(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.level().isClientSide()) {
            return;
        }

        AttributeInstance speedAttr = self.getAttribute(Attributes.FLYING_SPEED);
        if (speedAttr == null) {
            return;
        }

        int level = 0;
        var passenger = self.getControllingPassenger();

        if (passenger instanceof Player) {
            ItemStack harness = self.getItemBySlot(EquipmentSlot.BODY);
            if (!harness.isEmpty()) {
                ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(harness);
                for (var entry : enchants.entrySet()) {
                    if (entry.getKey().is(FLIGHT_SPEED_ID)) {
                        level = entry.getIntValue();
                        break;
                    }
                }
            }
        }


        speedAttr.removeModifier(SPEED_MODIFIER_ID);
        if (level > 0) {
            double targetMultiplier = 1.0 + 1.5 * level;
            double requiredAttributeMultiplier = Math.sqrt(targetMultiplier);
            double modifierValue = requiredAttributeMultiplier - 1.0;

            AttributeModifier modifier = new AttributeModifier(
                    SPEED_MODIFIER_ID,
                    modifierValue,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            );
            speedAttr.addTransientModifier(modifier);
        }
    }
}