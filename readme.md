# Item Skills

A library mod to control how players interact with items using skill-based restrictions created in KubeJS scripts. This
is very similar to Item Stages.

[![CurseForge](https://cf.way2muchnoise.eu/short_715904.svg)](https://www.curseforge.com/minecraft/mc-mods/item-skills)
[![Modrinth](https://img.shields.io/modrinth/dt/item-skills?color=bcdeb7&label=%20&logo=modrinth&logoColor=096765&style=plastic)](https://modrinth.com/mod/item-skills)
[![MIT license](https://img.shields.io/github/license/impleri/item-skills?color=bcdeb7&label=Source&logo=github&style=flat)](https://github.com/impleri/item-skills)
[![Discord](https://img.shields.io/discord/1093178610950623233?color=096765&label=Community&logo=discord&logoColor=bcdeb7&style=plastic)](https://discord.com/invite/avxJgbaUmG)
[![1.19.2](https://img.shields.io/maven-metadata/v?label=1.19.2&color=096765&metadataUrl=https%3A%2F%2Fmaven.impleri.org%2Fminecraft%2Fnet%2Fimpleri%2Fitem-skills-1.19.2%2Fmaven-metadata.xml&style=flat)](https://github.com/impleri/item-skills#developers)
[![1.18.2](https://img.shields.io/maven-metadata/v?label=1.18.2&color=096765&metadataUrl=https%3A%2F%2Fmaven.impleri.org%2Fminecraft%2Fnet%2Fimpleri%2Fitem-skills-1.18.2%2Fmaven-metadata.xml&style=flat)](https://github.com/impleri/item-skills#developers)

### xSkills Mods

[Player Skills](https://github.com/impleri/player-skills)
| [Block Skills](https://github.com/impleri/block-skills)
| [Dimension Skills](https://github.com/impleri/dimension-skills)
| [Fluid Skills](https://github.com/impleri/fluid-skills)
| [Item Skills](https://github.com/impleri/item-skills)
| [Mob Skills](https://github.com/impleri/mob-skills)

## Concepts

This mod leans extensively on Player Skills by creating and consuming the Skill-based Restrictions. Out of the box, this
mod can restrict whether an item can be equipped, held in inventory, crafted, visible in JEI or REI, and whether it can
be identified in a tooltip. Note that this does not alter interactions with placed items (e.g. a placed bed will still
be interactable by a player who can't craft one). However, Block Skills provides that complementary functionality.

When using this with JEI or REI, please be sure to disable the vanilla recipe book as the restricted items can appear
there despite being uncraftable as this mod does not remove any actual recipe. Instead of removing recipes, it restricts
the crafting result when a player tries to craft an item and hides the recipes in JEI/REI.

## KubeJS API

### Register

We use the `ItemSkillEvents.register` ***server*** event to register item restrictions. Registration should have a
condition (`if` or `unless`). If the player ***matches*** the criteria, the following restrictions are applied. This can
cascade with other restrictions, so any restrictions which disallow an action will trump any which do allow it. We also
expose these methods to indicate what restrictions are in place for when a player meets that condition. By default, no
restrictions are set, so be sure to set actual
restrictions. [See Player Skills documentation for the shared API](https://github.com/impleri/player-skills#kubejs-restrictions-api).

#### Allow Restriction Methods

- `nothing()` - shorthand to apply all "allow" restrictions
- `producible()` - the item is craftable and recipes for crafting it are visible in REI/JEI. This will automatically
  set `holdable` since it's required
- `consumable()` - recipes using the item are visible in REI/JEI. This will automatically set `holdable` since it's
  required
- `holdable()` - the item can be picked up and held, i.e. the player can hold the item in their inventory
- `identifiable()` - the item can be identified, i.e. the player can see details in a tooltip about the item
- `harmful()` - the item can be used as a weapon (if applicable). This will automatically set `holdable` since it's
  required
- `wearable()` - the item can be equipped (if applicable). This will automatically set `holdable` since it's required
- `usable()` - the item can be used (if applicable), e.g. if a water bucket can place water or if a diamond pickaxe can
  mine obsidian. This will automatically set `holdable` since it's required

#### Deny Restriction Methods

- `everything()` - shorthand to apply the below "deny" abilities
- `unproducible()` - the item is not craftable
- `unconsumable()` - recipes using the item are hidden in REI/JEI
- `unholdable()` - the item cannot be picked up
- `unidentifiable()` - the item cannot be identified
- `harmless()` - the item cannot be used as a weapon (if applicable)
- `unwearable()` - the item cannot be equipped (if applicable)
- `unusable()` - the item cannot be used (if applicable)

#### Examples

```js
ItemSkillEvents.register(event => {
  // Vanilla items cannot be used at all unless player is at stage 2 (or later)
  event.restrict('minecraft:*', restrict => {
    restrict.everything()
      .if(player => player.cannot('skills:stage', 2));
  });

  // Vanilla items cannot be crafted at all unless player is at stage 2 (or later)
  event.restrict('@minecraft', restrict => {
    restrict.unproducible()
      .if(player => player.cannot('skills:stage', 2));
  });

  // Any item tagged as wool cannot be used
  event.restrict('#minecraft:wool', restrict => {
    restrict.everything()
      .if(player => player.cannot('skills:stage', 2));
  });

  // Bed item cannot be used/placed at all unless player is at stage 2 (or later)
  event.restrict('minecraft:bed', restrict => {
    restrict.usable()
      .if(player => player.cannot('skills:stage', 2));
  });

  // Bread can be picked up and used in other recipes if player is at stage 1 or below but it cannot be eaten or identified
  event.restrict('minecraft:bread', restrict => {
    restrict.everything()
      .holdable()
      .consumable()
      .unless(player => player.can('skills:stage', 2));
  });

  // The following two restrictions does not result in the same effects as above: everything will still be denied to the player
  event.restrict('minecraft:bread', restrict => {
    restrict.everything().holdable().unless(player => player.can('skills:stage', 2));
  });

  event.restrict('minecraft:bread', restrict => {
    restrict.everything().visible().unless(player => player.cannot('skills:stage', 2));
  });
});
```

### Caveats

JEI integration does not remove recipes related to the `unconsumable` flag. It does hide the recipes from right-clicking
on the ingredient. However, it does not remove the recipe itself -- only `unproducible` does that. That is, a crafty
player could view a recipe that produces the item, then right click on the produced item to see what recipes with which
it can be consumed.

## Developers

Add the following to your `build.gradle`. I depend
on [Architectury API](https://github.com/architectury/architectury-api), [KubeJS](https://github.com/KubeJS-Mods/KubeJS),
and [PlayerSkills](https://github.com/impleri/player-skills), so you'll need those as well.

```groovy
dependencies {
    // Common should always be included 
    modImplementation "net.impleri:item-skills-${minecraft_version}:${itemskills_version}"
    // Plus forge
    modApi "net.impleri:item-skills-${minecraft_version}-forge:${itemskills_version}"
    // Or fabric
    modApi "net.impleri:item-skills-${minecraft_version}-fabric:${itemskills_version}"
}
repositories {
    maven {
        url = "https://maven.impleri.org/minecraft"
        name = "Impleri Mods"
        content {
            includeGroup "net.impleri"
        }
    }
}
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.
