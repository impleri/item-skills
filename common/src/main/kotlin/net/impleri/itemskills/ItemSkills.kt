package net.impleri.itemskills

import com.mojang.brigadier.CommandDispatcher
import dev.architectury.event.CompoundEventResult
import dev.architectury.event.EventResult
import dev.architectury.event.events.common.BlockEvent
import dev.architectury.event.events.common.CommandRegistrationEvent
import dev.architectury.event.events.common.EntityEvent
import dev.architectury.event.events.common.InteractionEvent
import dev.architectury.event.events.common.LifecycleEvent
import dev.architectury.event.events.common.PlayerEvent
import dev.architectury.event.events.common.TickEvent
import dev.architectury.utils.value.IntValue
import net.impleri.itemskills.api.ItemRestriction
import net.impleri.playerskills.commands.PlayerSkillsCommands.registerDebug
import net.impleri.playerskills.commands.PlayerSkillsCommands.toggleDebug
import net.impleri.playerskills.utils.PlayerSkillsLogger
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class ItemSkills {
  fun registerEventHandlers() {
    LifecycleEvent.SERVER_STARTING.register(LifecycleEvent.ServerState { onStartup(it) })

    TickEvent.PLAYER_POST.register(TickEvent.Player { onPlayerTick(it) })

    PlayerEvent.PICKUP_ITEM_PRE.register(
      PlayerEvent.PickupItemPredicate { player: Player, _: Entity, stack: ItemStack ->
        beforePlayerPickup(
          player,
          stack,
        )
      },
    )

    InteractionEvent.LEFT_CLICK_BLOCK.register(
      InteractionEvent.LeftClickBlock { player: Player, hand: InteractionHand, pos: BlockPos, _: Direction ->
        beforeUseItemBlock(
          player,
          hand,
          pos,
        )
      },
    )

    InteractionEvent.RIGHT_CLICK_BLOCK.register(
      InteractionEvent.RightClickBlock { player: Player, hand: InteractionHand, pos: BlockPos, _: Direction ->
        beforeUseItemBlock(
          player,
          hand,
          pos,
        )
      },
    )

    InteractionEvent.RIGHT_CLICK_ITEM.register(
      InteractionEvent.RightClickItem { player: Player, hand: InteractionHand ->
        beforeUseItem(
          player,
          hand,
        )
      },
    )

    InteractionEvent.INTERACT_ENTITY.register(
      InteractionEvent.InteractEntity { player: Player, entity: Entity, hand: InteractionHand ->
        beforeInteractEntity(
          player,
          entity,
          hand,
        )
      },
    )

    EntityEvent.LIVING_HURT.register(
      EntityEvent.LivingHurt { entity: LivingEntity, source: DamageSource, amount: Float ->
        beforePlayerAttack(
          entity,
          source,
          amount,
        )
      },
    )

    BlockEvent.BREAK.register(
      BlockEvent.Break { _: Level, pos: BlockPos, state: BlockState, player: ServerPlayer, _: IntValue? ->
        beforeMine(
          pos,
          state,
          player,
        )
      },
    )
  }

  fun registerCommands() {
    CommandRegistrationEvent.EVENT.register(
      CommandRegistrationEvent { dispatcher: CommandDispatcher<CommandSourceStack>, _: CommandBuildContext, _: Commands.CommandSelection ->
        registerDebugCommand(
          dispatcher,
        )
      },
    )
  }

  private fun registerDebugCommand(
    dispatcher: CommandDispatcher<CommandSourceStack>,
  ) {
    registerDebug(
      dispatcher,
      "itemskills",
      toggleDebug("Item Skills", Supplier { toggleDebug() }),
    )
  }

  private var savedServer: MinecraftServer? = null

  internal val server: MinecraftServer by lazy {
    savedServer ?: throw RuntimeException("Unable to access the server before it is available")
  }

  private fun onStartup(minecraftServer: MinecraftServer) {
    savedServer = minecraftServer
  }

  private fun onPlayerTick(player: Player) {
    if (player.getLevel().isClientSide) {
      return
    }
    val inventory = player.inventory

    // Move unwearable items from armor and offhand into normal inventory
    ItemRestriction.filterFromList(player, inventory.armor)
    ItemRestriction.filterFromList(player, inventory.offhand)

    // Get unholdable items from inventory
    val itemsToRemove = ItemRestriction.getItemsToRemove(player, inventory.items)

    // Drop the unholdable items from the normal inventory
    if (itemsToRemove.isNotEmpty()) {
      LOGGER.debug("${player.name} is holding ${itemsToRemove.size} item(s) that should be dropped")
      itemsToRemove.forEach(ItemRestriction.dropFromInventory(player))
    }
  }

  private fun beforePlayerPickup(player: Player, stack: ItemStack): EventResult {
    val item = ItemRestriction.getValue(stack)
    if (ItemRestriction.canHold(player, item, null)) {
      return EventResult.pass()
    }
    LOGGER.debug("${player.name} is about to pickup ${ItemRestriction.getName(item)}")
    return EventResult.interruptFalse()
  }

  private fun beforePlayerAttack(entity: LivingEntity, source: DamageSource, amount: Float): EventResult {
    val attacker = source.entity
    if (attacker is Player) {
      val weapon: Item = ItemRestriction.getValue(attacker.mainHandItem)
      if (!ItemRestriction.canAttackWith(attacker, weapon, null)) {
        LOGGER.debug(
          "${attacker.name} was about to attack ${entity.name} using ${ItemRestriction.getName(weapon)} for $amount damage",
        )
        return EventResult.interruptFalse()
      }
    }
    return EventResult.pass()
  }

  private fun beforeMine(
    pos: BlockPos,
    state: BlockState,
    player: ServerPlayer,
  ): EventResult {
    val tool = ItemRestriction.getValue(player.mainHandItem)
    if (ItemRestriction.canUse(player, tool, pos)) {
      return EventResult.pass()
    }
    LOGGER.debug(
      "${player.name} was about to mine ${state.block.name} using ${ItemRestriction.getName(tool)}",
    )
    return EventResult.interruptFalse()
  }

  private fun beforeInteractEntity(player: Player, entity: Entity, hand: InteractionHand): EventResult {
    val tool = ItemRestriction.getItemUsed(player, hand)
    if (ItemRestriction.canUse(player, tool, null)) {
      return EventResult.pass()
    }
    LOGGER.debug(
      "${player.name} was about to interact with entity ${entity.name} using ${ItemRestriction.getName(tool)}",
    )
    return EventResult.interruptFalse()
  }

  private fun beforeUseItem(player: Player, hand: InteractionHand): CompoundEventResult<ItemStack> {
    val tool = ItemRestriction.getItemUsed(player, hand)
    if (ItemRestriction.canUse(player, tool, null)) {
      return CompoundEventResult.pass()
    }
    LOGGER.debug("${player.name} is about to use ${ItemRestriction.getName(tool)}")
    return CompoundEventResult.interruptFalse(null)
  }

  private fun beforeUseItemBlock(player: Player, hand: InteractionHand, pos: BlockPos): EventResult {
    val tool = ItemRestriction.getItemUsed(player, hand)
    if (ItemRestriction.isDefaultItem(tool) || ItemRestriction.canUse(player, tool, pos)) {
      return EventResult.pass()
    }

    val blockName = player.level.getBlockState(pos).block.name
    val itemName = ItemRestriction.getName(tool)
    LOGGER.debug("${player.name} is about to interact with block $blockName using $itemName")

    return EventResult.interruptFalse()
  }

  companion object {
    const val MOD_ID = "itemskills"

    val LOGGER: PlayerSkillsLogger = PlayerSkillsLogger.create(MOD_ID, "ITEMS")

    private val INSTANCE = ItemSkills()

    internal val server: Lazy<MinecraftServer> = lazy { INSTANCE.server }

    fun init() {
      LOGGER.info("Loaded Item Skills")
      INSTANCE.registerEventHandlers()
      INSTANCE.registerCommands()
    }

    fun toggleDebug(): Boolean {
      return LOGGER.toggleDebug()
    }
  }
}
