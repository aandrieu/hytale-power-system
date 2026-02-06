package com.pix.plugin;

import com.pix.plugin.components.PowerLevelComponent;
import com.pix.plugin.components.PowerSourceComponent;
import com.pix.plugin.systems.PowerCommitSystem;
import com.pix.plugin.systems.PowerInitSystem;
import com.pix.plugin.systems.PowerPropagationSystem;
import com.pix.plugin.systems.PowerSourceSystem;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PowerPlugin extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private ComponentType<ChunkStore, PowerSourceComponent> powerSourceComponentType;
    private ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;

    public PowerPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        // Test command: to be removed
        this.getCommandRegistry().registerCommand(new HelloCommand("hello", "Hello"));

        this.powerSourceComponentType = this.getChunkStoreRegistry().registerComponent(
            PowerSourceComponent.class,
            "PowerSourceComponent",
            PowerSourceComponent.CODEC
        );
        this.powerLevelComponentType = this.getChunkStoreRegistry().registerComponent(
            PowerLevelComponent.class,
            "PowerLevelComponent",
            PowerLevelComponent.CODEC
        );
    }

    @Override
    protected void start() {
        super.start();

        this.getChunkStoreRegistry().registerSystem(
            new PowerInitSystem(this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerSourceSystem(this.powerSourceComponentType, this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerPropagationSystem(this.powerLevelComponentType)
        );
        this.getChunkStoreRegistry().registerSystem(
            new PowerCommitSystem(this.powerLevelComponentType)
        );
    }
}
