import me.shedaniel.unifiedpublishing.UnifiedPublishingExtension

val modId: String = property("archives_base_name").toString()
val minecraftVersion: String = property("minecraft_version").toString()
val kotlinVersion: String = property("kotlin_version").toString()
val kotlinFabricVersion: String = property("kotlin_fabric_version").toString()
val architecturyVersion: String = property("architectury_version").toString()
val fabricLoaderVersion: String = property("fabric_loader_version").toString()
val fabricApiVersion: String = property("fabric_api_version").toString()
val kubejsVersion: String = property("kubejs_version").toString()
val craftTweakerVersion: String = property("crafttweaker_version").toString()
val playerskillsVersion: String = property("playerskills_version").toString()
val reiVersion: String = property("rei_version").toString()
val jeiVersion: String = property("jei_version").toString()
val trinketsVersion: String = property("trinkets_version").toString()

configure<UnifiedPublishingExtension> {
  project {
    relations {
      depends {
        curseforge.set("fabric-api")
        modrinth.set("fabric-api")
      }

      optional {
        curseforge.set("trinkets")
        modrinth.set("trinkets")
      }
    }
  }
}

repositories {
  maven {
    name = "TerraformersMC"
    url = uri("https://maven.terraformersmc.com/")
  }
  maven {
    name = "Ladysnake Libs"
    url = uri("https://ladysnake.jfrog.io/artifactory/mods")
  }
}

dependencies {
  modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
  modApi("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

  modImplementation("net.fabricmc:fabric-language-kotlin:$kotlinFabricVersion+kotlin.$kotlinVersion")

  modApi("dev.architectury:architectury-fabric:$architecturyVersion")

  modApi("dev.latvian.mods:kubejs-fabric:$kubejsVersion")
  modImplementation("com.blamejared.crafttweaker:CraftTweaker-fabric-$minecraftVersion:$craftTweakerVersion")

  modImplementation("net.impleri:playerskills:$minecraftVersion-fabric-$playerskillsVersion")

  modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:$reiVersion")
  modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:$reiVersion")
  modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:$reiVersion")

  modCompileOnly("mezz.jei:jei-$minecraftVersion-common-api:$jeiVersion")
  modCompileOnly("mezz.jei:jei-$minecraftVersion-fabric-api:$jeiVersion")
  modRuntimeOnly("mezz.jei:jei-$minecraftVersion-fabric:$jeiVersion")

  // TODO: Temorarily add this dependency until we can update to 1.20 which has the fix to bundle it correctly for trinkets
  modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:5.0.0-beta.1")
  modApi("dev.emi:trinkets:$trinketsVersion")
}
