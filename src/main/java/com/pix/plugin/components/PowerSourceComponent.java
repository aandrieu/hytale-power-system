package com.pix.plugin.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PowerSourceComponent implements Component<ChunkStore> {
	public static final BuilderCodec<PowerSourceComponent> CODEC = BuilderCodec.builder(
					PowerSourceComponent.class,
					PowerSourceComponent::new
			)
			.append(
					new KeyedCodec<>("OutputPowerLevel", Codec.INTEGER),
					(powerSourceComponent, value) -> powerSourceComponent.outputPower = value,
					powerSourceComponent -> powerSourceComponent.outputPower
			)
			.addValidator(Validators.greaterThanOrEqual(0))
			.add()
			.append(
					new KeyedCodec<>("IsActivePower", Codec.BOOLEAN),
					(powerSourceComponent, value) -> powerSourceComponent.active = value,
					powerSourceComponent -> powerSourceComponent.active
			)
			.add()
			.build();

	private int outputPower;
	private boolean active;

	public PowerSourceComponent() {
		this(0);
	}

	public PowerSourceComponent(int outputPower) {
		this(outputPower, true);
	}

	public PowerSourceComponent(int outputPower, boolean active) {
		this.outputPower = outputPower;
		this.active = active;
	}

	public PowerSourceComponent(PowerSourceComponent other) {
		this.outputPower = other.outputPower;
		this.active = other.active;
	}

	public int getOutputPower() {
		return this.outputPower;
	}

	public void setOutputPower(int outputPower) {
		this.outputPower = outputPower;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@NullableDecl
	@Override
	public Component<ChunkStore> clone() {
		return new PowerSourceComponent(this);
	}
}
