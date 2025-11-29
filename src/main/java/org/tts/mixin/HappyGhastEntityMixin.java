package org.tts.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HappyGhastEntity.class)
public abstract class HappyGhastEntityMixin {
    private static final Identifier FLIGHT_SPEED_ID = Identifier.of("speedghasts", "flight_speed");

    @ModifyVariable(
            method = "travel",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Vec3d speedghasts$modifyTravelInput(Vec3d movementInput) {
        HappyGhastEntity self = (HappyGhastEntity) (Object) this;
        var controllingPlayer = self.getControllingPassenger();
        if (!(controllingPlayer instanceof net.minecraft.entity.player.PlayerEntity)) {
            return movementInput;
        }

        ItemStack harness = self.getEquippedStack(EquipmentSlot.BODY);
        if (harness.isEmpty()) {
            return movementInput;
        }

        ItemEnchantmentsComponent enchants = EnchantmentHelper.getEnchantments(harness);
        if (enchants.isEmpty()) {
            return movementInput;
        }

        int level = 0;

        for (var entry : enchants.getEnchantmentEntries()) {
            RegistryEntry<Enchantment> enchantEntry = entry.getKey();

            if (enchantEntry.matchesId(FLIGHT_SPEED_ID)) {
                level = entry.getIntValue();
                break;
            }
        }

        if (level <= 0) return movementInput;



        double multiplier = 1.0 + 1.5 * level;
        return movementInput.multiply(multiplier);
    }
}
