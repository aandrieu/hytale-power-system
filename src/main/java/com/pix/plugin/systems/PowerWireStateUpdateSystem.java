package com.pix.plugin.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.pix.plugin.components.PowerLevelComponent;
import com.pix.plugin.components.PowerWireComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Set;

public class PowerWireStateUpdateSystem extends EntityTickingSystem<ChunkStore> {
	private final ComponentType<ChunkStore, PowerWireComponent> powerWireComponentType;
	private final ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;
	private final Query<ChunkStore> query;

	public PowerWireStateUpdateSystem(
			ComponentType<ChunkStore, PowerWireComponent> powerWireComponentType,
			ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType
	) {
		this.powerWireComponentType = powerWireComponentType;
		this.powerLevelComponentType = powerLevelComponentType;
		this.query = Query.and(
				this.powerWireComponentType,
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

		var ref = archetypeChunk.getReferenceTo(index);
		var info = store.getComponent(ref, BlockModule.BlockStateInfo.getComponentType());
		if (info == null) {
			return;
		}

		var chunkRef = info.getChunkRef();
		if (!chunkRef.isValid()) {
			return;
		}

		var worldChunk = store.getComponent(chunkRef, WorldChunk.getComponentType());
		if (worldChunk == null) {
			return;
		}

		int x = ChunkUtil.xFromBlockInColumn(info.getIndex());
		int y = ChunkUtil.yFromBlockInColumn(info.getIndex());
		int z = ChunkUtil.zFromBlockInColumn(info.getIndex());

		int blockId = worldChunk.getBlock(x, y, z);
		BlockType blockType = BlockType.getAssetMap().getAsset(blockId);

		if (blockType == null) {
			return;
		}

		// Inspired from BlockSetStateCommand. Is it okay?
		worldChunk.setBlockInteractionState(x, y, z, blockType, power.getState(), true);
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
				new SystemDependency<>(Order.AFTER, PowerCommitSystem.class)
		);
	}
}
