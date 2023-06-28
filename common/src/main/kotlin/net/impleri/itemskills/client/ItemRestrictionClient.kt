package net.impleri.itemskills.client

import net.impleri.itemskills.api.ItemRestriction
import net.impleri.itemskills.restrictions.Restriction
import net.impleri.playerskills.client.RestrictionsClient
import net.impleri.playerskills.restrictions.Registry
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe

class ItemRestrictionClient private constructor(
  registry: Registry<Restriction>,
  serverApi: ItemRestriction,
) : RestrictionsClient<Item, Restriction, ItemRestriction>(registry, serverApi) {
  private fun pluckTarget(list: List<Restriction>): List<Item> {
    return list.map { it.target }
  }

  val hidden: List<Item>
    get() = pluckTarget(getFiltered { !it.producible && !it.consumable })

  val unproducible: List<Item>
    get() = pluckTarget(getFiltered { !it.producible })

  val unconsumable: List<Item>
    get() = pluckTarget(getFiltered { !it.consumable })

  internal fun isProducible(item: Item): Boolean {
    return serverApi.isProducible(player, item, null)
  }

  internal fun isProducible(recipe: Recipe<*>): Boolean {
    return serverApi.isProducible(player, recipe, null)
  }

  internal fun isConsumable(item: Item): Boolean {
    return serverApi.isConsumable(player, item, null)
  }

  internal fun isHoldable(item: Item): Boolean {
    return serverApi.isHoldable(player, item, null)
  }

  internal fun isIdentifiable(item: Item): Boolean {
    return serverApi.isIdentifiable(player, item, null)
  }

  internal fun isHarmful(item: Item): Boolean {
    return serverApi.isHarmful(player, item, null)
  }

  internal fun isWearable(item: Item): Boolean {
    return serverApi.isWearable(player, item, null)
  }

  internal fun isUsable(item: Item): Boolean {
    return serverApi.isUsable(player, item, null)
  }

  companion object {
    val INSTANCE = ItemRestrictionClient(ItemRestriction.RestrictionRegistry, ItemRestriction.INSTANCE)

    val hidden: List<Item>
      get() = INSTANCE.hidden

    val unproducible: List<Item>
      get() = INSTANCE.unproducible

    val unconsumable: List<Item>
      get() = INSTANCE.unconsumable

    fun canCraft(item: Item): Boolean {
      return INSTANCE.isProducible(item)
    }

    fun canCraft(recipe: Recipe<*>): Boolean {
      return INSTANCE.isProducible(recipe)
    }

    fun canCraftWith(item: Item): Boolean {
      return INSTANCE.isConsumable(item)
    }

    fun canHold(item: Item): Boolean {
      return INSTANCE.isHoldable(item)
    }

    fun canIdentify(item: Item): Boolean {
      return INSTANCE.isIdentifiable(item)
    }

    fun canAttackWith(item: Item): Boolean {
      return INSTANCE.isHarmful(item)
    }

    fun canEquip(item: Item): Boolean {
      return INSTANCE.isWearable(item)
    }

    fun canUse(item: Item): Boolean {
      return INSTANCE.isUsable(item)
    }
  }
}
