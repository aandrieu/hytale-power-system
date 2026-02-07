package com.pix.plugin.components;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PowerWireComponent implements Component<ChunkStore> {
	public static final BuilderCodec<PowerWireComponent> CODEC = BuilderCodec.builder(
			PowerWireComponent.class,
			PowerWireComponent::new
	).build();

	public PowerWireComponent() {
	}

	@NullableDecl
	@Override
	public Component<ChunkStore> clone() {
		return new PowerWireComponent();
	}
}
