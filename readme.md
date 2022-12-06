# Item Skills

A library mod that exposes KubeJS methods to restrict items to the player based on skills. Built around 
[Player Skills](https://github.com/impleri/player-skills). This interacts with both JEI and REI. Note that this does not
alter interactions with placed items (e.g. a placed bed will still be interactable by a player who can't craft one). Those
interactions will be handled by the yet-to-be-released `block-skills`;

## KubeJS API

### Register

We use the `ItemSkillEvents.register` event to register skill-based items. Registration requires a test (`if` or `unless`)
in a callback function which uses a player skills condition object (`can` and `cannot` methods). If the player ***matches*** 
the criteria, the following restrictions are applied. This can cascade with other restrictions, so any restrictions which
disallow an action will trump any which do allow it. We also expose these methods to indicate what restrictions are in
place for when a player meets that condition.


#### Allow Restriction Methods

 - `nothing`: shorthand to apply all "allow" restrictions
 - `craftable`: the item is craftable and recipes for crafting it are visible in REI/JEI. This will automatically set `holdable` since it's required
 - `visible`: recipes using the item are visible in REI/JEI. This will automatically set `holdable` since it's required
 - `holdable`: the item can be picked up and held, i.e. the player can hold the item in their inventory
 - `identifiable`: the item can be identified, i.e. the player can see details in a tooltip about the item
 - `harmful`: the item can be used as a weapon (if applicable). This will automatically set `holdable` since it's required
 - `wearable`: the item can be equipped (if applicable). This will automatically set `holdable` since it's required
 - `usable`: the item can be used (if applicable), e.g. if a water bucket can place water or if a diamond pickaxe can mine obsidian. This will automatically set `holdable` since it's required

#### Deny Restriction Methods

 - `everything`: shorthand to apply the below "deny" abilities
 - `uncraftable`: the item is not craftable
 - `hidden`: recipes using the item are hidden in REI/JEI
 - `unholdable`: the item cannot be picked up
 - `unidentifiable`: the item cannot be identified
 - `harmless`: the item cannot be used as a weapon (if applicable)
 - `unwearable`: the item cannot be equipped (if applicable)
 - `unusable`: the item cannot be used (if applicable)

```js
  // Bed item cannot be used at all unless player is at stage 2 (or later)
  ItemSkillEvents.register('minecraft:bed', restrict => {
    restrict.everything()
      .if(player => player.cannot('skills:stage', 2));
  });
 
 // Bread can be picked up and used in other recipes if player is at stage 1 or below but it cannot be eaten or identified
 ItemSkillEvents.register('minecraft:bread', restrict => {
   restrict.everything()
     .holdable()
     .visible()
     .unless(player => player.can('skills:stage', 2));
 });
 
 // The following does not result in the same effects as above: everything will still be denied to the player
 ItemSkillEvents.register('minecraft:bread', restrict => {
   restrict.everything().holdable().unless(player => player.can('skills:stage', 2));
   restrict.everything().visible().unless(player => player.cannot('skills:stage', 2));
 });
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.

## TODOS

- [] Hook into REI to hide recipes related to the item (visible, craftable)
- [] Hook into JEI to hide recipes related to the item (visible, craftable)
- [] `Empty Stack` error thrown by Rhino when too many events
- [] Implement restrictions based on tags
- [] Implement restrictions based on mod IDs
- [] Implement replacement items
