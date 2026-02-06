package com.pix.plugin.systems;

import com.pix.plugin.components.PowerLevelComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Set;

public class PowerPropagationSystem extends EntityTickingSystem<ChunkStore> {
	private final ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;
	private final Query<ChunkStore> query;

	public PowerPropagationSystem(
			ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType
	) {
		this.powerLevelComponentType = powerLevelComponentType;
		this.query = Query.and(
				BlockModule.BlockStateInfo.getComponentType(),
				this.powerLevelComponentType
		);
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

		var info = archetypeChunk.getComponent(index, BlockModule.BlockStateInfo.getComponentType());
		if (info == null) {
			return;
		}

		var chunkRef = info.getChunkRef();
		var worldChunk = store.getComponent(chunkRef, WorldChunk.getComponentType());
		if (worldChunk == null) {
			return;
		}

		int x = ChunkUtil.xFromBlockInColumn(info.getIndex());
		int y = ChunkUtil.yFromBlockInColumn(info.getIndex());
		int z = ChunkUtil.zFromBlockInColumn(info.getIndex());

		Vector3i[] directions = {
				new Vector3i(0, 0, -1),
				new Vector3i(0, 0, 1),
				new Vector3i(-1, 0, 0),
				new Vector3i(1, 0, 0)
		};

		int maxPower = 0;
		for (Vector3i direction : directions) {
			var currentX = x + direction.x;
			var currentZ = z + direction.z;

			var neighborBlockRef = worldChunk.getBlockComponentEntity(currentX, y, currentZ);
			if (neighborBlockRef == null) {
				continue;
			}

			var neighborPower = store.getComponent(neighborBlockRef, this.powerLevelComponentType);
			if (neighborPower == null) {
				continue;
			}

			int neighborPropagationPower = Math.max(neighborPower.getCurrent() - 1, 0);
			maxPower = Math.max(maxPower, neighborPropagationPower);
		}

		power.setNext(maxPower);
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
				new SystemDependency<>(Order.AFTER, PowerSourceSystem.class)
		);
	}
}
