package com.pix.plugin.systems;

import com.pix.plugin.components.PowerLevelComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Set;

public class PowerCommitSystem extends EntityTickingSystem<ChunkStore> {
	private final ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;
	private final Query<ChunkStore> query;

	public PowerCommitSystem(
			ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType
	) {
		this.powerLevelComponentType = powerLevelComponentType;
		this.query = this.powerLevelComponentType;
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
		var power = archetypeChunk.getComponent(index, this.powerLevelComponentType);
		if (power == null) {
			return;
		}

		power.setCurrent(power.getNext());
	}

	@NullableDecl
	@Override
	public SystemGroup<ChunkStore> getGroup() {
		return super.getGroup();
	}

	@NonNullDecl
	@Override
	public Set<Dependency<ChunkStore>> getDependencies() {
		return Set.of(
				new SystemDependency<>(Order.AFTER, PowerWirePropagationSystem.class)
		);
	}
}
