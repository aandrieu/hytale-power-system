package com.pix.plugin.systems;

import com.pix.plugin.PowerPlugin;
import com.pix.plugin.components.PowerLevelComponent;
import com.pix.plugin.components.PowerSourceComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Set;

public class PowerSourceSystem extends EntityTickingSystem<ChunkStore> {
	private final ComponentType<ChunkStore, PowerSourceComponent> powerSourceComponentType;
	private final ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;
	private final Query<ChunkStore> query;

	public PowerSourceSystem(
			ComponentType<ChunkStore, PowerSourceComponent> powerSourceComponentType,
			ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType
	) {
		this.powerSourceComponentType = powerSourceComponentType;
		this.powerLevelComponentType = powerLevelComponentType;
		this.query = Query.and(this.powerSourceComponentType, this.powerLevelComponentType);
	}

	@NullableDecl
	@Override
	public Query<ChunkStore> getQuery() {
		return this.query;
	}

	@Override
	public void tick(
			float dt,
			int index,
			@NonNullDecl ArchetypeChunk<ChunkStore> archetypeChunk,
			@NonNullDecl Store<ChunkStore> store,
			@NonNullDecl CommandBuffer<ChunkStore> commandBuffer
	) {
		var source = archetypeChunk.getComponent(index, this.powerSourceComponentType);
		var power = archetypeChunk.getComponent(index, this.powerLevelComponentType);
		if (source == null || power == null) {
			PowerPlugin.LOGGER.atInfo().log("Source or Power is null");
			return;
		}

		if (source.getActive()) {
			power.setNext(source.getOutputPower());
		} else {
			power.setNext(0);
		}
	}

	@NullableDecl
	@Override
	public SystemGroup<ChunkStore> getGroup() {
		return super.getGroup();
	}

	@NonNullDecl
	@Override
	public Set<Dependency<ChunkStore>> getDependencies() {
		return super.getDependencies();
	}
}
