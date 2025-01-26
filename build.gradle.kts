plugins {
    java
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

// See https://purpurmc.org/docs/purpur/ for the latest version
val purpurVersion = "1.21.4-R0.1-SNAPSHOT"

// See https://hangar.papermc.io/pianoman911/MapEngine/versions for the latest version
val mapEngineVersion = "1.8.7"

// See https://hangar.papermc.io/pianoman911/MapEngine-MediaExtension/versions for the latest version
val mapEngineMediaExtVersion = "1.1.3"

// See https://kotlinlang.org/docs/home.html for the latest version
val kotlinVersion = "2.1.0"

// See https://central.sonatype.com/artifact/net.luckperms/api/versions for the latest version
val luckPermsVersion = "5.4"

// See https://repo.dmulloy2.net/repository/public/com/comphenix/protocol/ProtocolLib/maven-metadata.xml for the latest version
val protocolLibVersion = "5.3.0"

// See https://plasmovoice.com/docs/api/ for the latest version
val plasmoVoiceVersion = "2.1.3-SNAPSHOT"

// See https://jitpack.io/#LeonMangler/SuperVanish for the latest version
val premiumVanishVersion = "6.2.19"

// See https://maven.citizensnpcs.co/#/repo/net/citizensnpcs/citizens-main for the latest version
val citizensVersion = "2.0.37-SNAPSHOT"

// Change this to your package name
group = "net.hauc3.hauc3raft"
version = "1.2"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "purpurmc"
        url = uri("https://repo.purpurmc.org/snapshots")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "tjcserver"
        url = uri("https://repo.thejocraft.net/releases/")
    }
    maven {
        name = "dmulloy2"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "plasmoverse-releases"
        url = uri("https://repo.plasmoverse.com/releases")
    }
    maven {
        name = "plasmoverse-snapshots"
        url = uri("https://repo.plasmoverse.com/snapshots")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "citizens-repo"
        url = uri("https://maven.citizensnpcs.co/repo")
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:$purpurVersion")
    compileOnly("de.pianoman911:mapengine-api:$mapEngineVersion")
    compileOnly("de.pianoman911:mapengine-mediaext:$mapEngineMediaExtVersion")
    compileOnly("net.luckperms:api:$luckPermsVersion")
    compileOnly("com.comphenix.protocol:ProtocolLib:$protocolLibVersion")
    compileOnly("su.plo.voice.api:server:$plasmoVoiceVersion")
    compileOnly("com.github.LeonMangler:SuperVanish:$premiumVanishVersion")
    compileOnly("net.citizensnpcs:citizens-main:$citizensVersion") {
        exclude(group = "*", module = "*")
    }
    shadow("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.undefinedcreations.runServer:com.undefinedcreations.runServer.gradle.plugin:0.1.6")
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.runServer {
    minecraftVersion("1.21.4")
}