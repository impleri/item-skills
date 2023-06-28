val modId: String = property("archives_base_name").toString()
val kotlinVersion: String = property("kotlin_version").toString()
val minecraftVersion: String = property("minecraft_version").toString()
val fabricLoaderVersion: String = property("fabric_loader_version").toString()
val architecturyVersion: String = property("architectury_version").toString()
val kubejsVersion: String = property("kubejs_version").toString()
val craftTweakerVersion: String = property("crafttweaker_version").toString()
val playerskillsVersion: String = property("playerskills_version").toString()
val reiVersion: String = property("rei_version").toString()
val jeiVersion: String = property("jei_version").toString()

dependencies {
  implementation(kotlin("stdlib", kotlinVersion))
  implementation(kotlin("reflect", kotlinVersion))

  modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
  modApi("dev.architectury:architectury:$architecturyVersion")

  compileOnly("dev.latvian.mods:kubejs:$kubejsVersion")
  compileOnly("com.blamejared.crafttweaker:CraftTweaker-common-$minecraftVersion:$craftTweakerVersion")

  modImplementation("net.impleri:playerskills:$minecraftVersion-$playerskillsVersion")

  modCompileOnly("me.shedaniel:RoughlyEnoughItems-api:$reiVersion")
  modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin:$reiVersion")

  compileOnly("mezz.jei:jei-$minecraftVersion-common-api:$jeiVersion")
}
