package net.hauc3.hauc3raft.traits

import net.citizensnpcs.api.trait.Trait
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class HauC3InvisibleTrait : Trait("hauc3invisible") {
    override fun onSpawn() {
        super.onSpawn()
        val entity = npc.entity as LivingEntity
        entity.addPotionEffect(
            PotionEffect(
                PotionEffectType.INVISIBILITY,
                PotionEffect.INFINITE_DURATION,
                0,
                false,
                false
            )
        )
    }

    override fun onRemove() {
        super.onRemove()
        val entity = npc.entity as LivingEntity
        entity.removePotionEffect(PotionEffectType.INVISIBILITY)
    }
}
