package com.pix.plugin.systems;

import com.pix.plugin.components.PowerLevelComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PowerInitSystem extends RefSystem<ChunkStore> {
	private final ComponentType<ChunkStore, PowerLevelComponent> powerLevelComponentType;
	private final Query<ChunkStore> query;

	public PowerInitSystem(
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
	public void onEntityAdded(
			@NonNullDecl Ref<ChunkStore> ref,
			@NonNullDecl AddReason addReason,
			@NonNullDecl Store<ChunkStore> store,
			@NonNullDecl CommandBuffer<ChunkStore> commandBuffer
	) {
		var info = commandBuffer.getComponent(ref, BlockModule.BlockStateInfo.getComponentType());
		if (info == null) {
			return;
		}

		var powerBlock = commandBuffer.getComponent(ref, this.powerLevelComponentType);
		if (powerBlock == null) {
			return;
		}

		int x = ChunkUtil.xFromBlockInColumn(info.getIndex());
		int y = ChunkUtil.yFromBlockInColumn(info.getIndex());
		int z = ChunkUtil.zFromBlockInColumn(info.getIndex());

		var worldChunk = commandBuffer.getComponent(info.getChunkRef(), WorldChunk.getComponentType());
		if (worldChunk == null) {
			return;
		}

		worldChunk.setTicking(x, y, z, true);
	}

	@Override
	public void onEntityRemove(
			@NonNullDecl Ref<ChunkStore> ref,
			@NonNullDecl RemoveReason removeReason,
			@NonNullDecl Store<ChunkStore> store,
			@NonNullDecl CommandBuffer<ChunkStore> commandBuffer
	) {

	}
}
