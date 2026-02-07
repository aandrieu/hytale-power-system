package com.pix.plugin;

import com.pix.plugin.components.PowerLevelComponent;
import com.pix.plugin.components.PowerSourceComponent;
import com.pix.plugin.components.PowerWireComponent;
import com.pix.plugin.systems.*;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PowerPlugin extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;
    private ComponentType<ChunkStore, PowerSourceComponent> powerSourceComponentType;
    private ComponentType<ChunkStore, PowerWireComponent> powerWireComponentType;

    public PowerPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        PowerPlugin.LOGGER.atInfo().log("Setup PowerPlugin");

        this.powerLevelComponentType = this.getChunkStoreRegistry().registerComponent(
            PowerLevelComponent.class,
            "PowerLevelComponent",
            PowerLevelComponent.CODEC
        );
        this.powerSourceComponentType = this.getChunkStoreRegistry().registerComponent(
            PowerSourceComponent.class,
            "PowerSourceComponent",
            PowerSourceComponent.CODEC
        );
        this.powerWireComponentType = this.getChunkStoreRegistry().registerComponent(
            PowerWireComponent.class,
            "PowerWireComponent",
            PowerWireComponent.CODEC
        );

        this.getChunkStoreRegistry().registerSystem(
            new PowerInitSystem(this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerSourceSystem(this.powerSourceComponentType, this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerWirePropagationSystem(this.powerWireComponentType, this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerCommitSystem(this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerWireStateUpdateSystem(this.powerWireComponentType, this.powerLevelComponentType)
        );

        // Test command: to be removed
        this.getCommandRegistry().registerCommand(new HelloCommand("hello1", "Hello"));
    }
}
