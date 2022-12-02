# Item Skills

A library mod that exposes KubeJS methods to make items uncraftable and unable to be picked up by the player. Built around 
[Player Skills](https://github.com/impleri/player-skills). This interacts with both JEI and REI. Note that this does not
alter interactions with placed items (e.g. a placed bed will still be interactable by a player who can't craft one). Those
interactions will be handled by the yet-to-be-released `block-skills`;

## KubeJS API

### Register

We use the `ItemSkillEvents.register` event to register skill-based items. Registration requires a test (`if`) in a 
callback function which uses a player skills condition object (`can` and `cannot` methods). If the player ***matches*** the
criteria, the following restrictions are applied. This can cascade with other restrictions, so any restrictions which
disallow an action will trump any which do allow it. We also expose these methods  to indicate what restrictions are in
place for when a player meets that condition:

 - `nothing`: shorthand to allow all of the following abilities
 - `everything`: shorthand to apply all of the following abilities
 - `uncraftable`: whether the item is craftable and recipes for crafting it are visible in REI/JEI
 - `hidden`: whether recipes using the item are visible in REI/JEI
 - `unholdable`: whether the item can be picked up, i.e. can the player hold the item in their inventory
 - `unidentifiable`: whether the item can be identified, i".e. can the player see details in a tooltip about the item
 - `harmless`: whether the item can be used as a weapon if applicable, e.g. if a diamond sword can do more damage than punching
 - `unwearable`: whether the item can be equipped if applicable, e.g. if a diamond helmet could be worn
 - `unusable`: whether the item can be used if applicable, e.g. if a water bucket can place water or if a diamond pickaxe can mine obsidian

```js
  // Bed item cannot be used at all unless player is at stage 2 (or later)
  ItemSkillEvents.register('minecraft:bed', restrict => {
    restrict.if(player => player.cannot('skills:stage', 2));
  });
 
 // Bread can be picked up and used in other recipes if player is at stage 1 or below but it cannot be eaten or identified
 ItemSkillEvents.register('minecraft:bread', restrict => {
   restrict.if(player => player.cannot('skills:stage', 2))
     .holdable()
     .visible();
 });
 
 // The following does not result in the same effects as above
 ItemSkillEvents.register('minecraft:bread', restrict => {
   restrict.if(player => player.cannot('skills:stage', 2)).holdable();
   restrict.if(player => player.cannot('skills:stage', 2)).visible();
 });
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.

## TODOS

- [] Mixin for return value of `RecipeManager.getRecipeFor` to prevent recognizing the crafting recipe as valid
- [] Hook into REI to hide recipes related to the item
- [] Hook into JEI to hide recipes related to the item
- [] Implement Architectury `PlayerEvent.PickupItemPredicate` interface to prevent picking up items
- [] Mixin to `net.minecraft.world.item.Item::apendHoverText` (fabric) or use `ItemTooltipEvent` (forge) to modify the tooltip
- [] Implement Architectury `EntityEvent.LivingHurt` interface to prevent doing damage with denied items
- [] Implement Architectury `Block.Break` and `InteractionEvent.InteractEntity` interface to prevent using tools
- [] Mixin to `net.minecraft.world.entity.player.Player::setItemSlot`
- [] Implement Architectury `InteractionEvent.RightClickItem` interface to prevent using an item
- [] Implement restrictions based on tags
- [] Implement restrictions based on mod IDs
