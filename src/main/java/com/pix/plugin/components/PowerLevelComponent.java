package com.pix.plugin.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PowerLevelComponent implements Component<ChunkStore> {
    public static final BuilderCodec<PowerLevelComponent> CODEC = BuilderCodec.builder(
        PowerLevelComponent.class,
        PowerLevelComponent::new
    )
        .append(
            new KeyedCodec<>("CurrentPowerLevel", Codec.INTEGER),
            (powerLevelComponent, value) -> powerLevelComponent.current = value,
            powerLevelComponent -> powerLevelComponent.current
        )
        .addValidator(Validators.greaterThanOrEqual(0))
        .add()
        .append(
            new KeyedCodec<>("NextPowerLevel", Codec.INTEGER),
            (powerLevelComponent, value) -> powerLevelComponent.next = value,
            powerLevelComponent -> powerLevelComponent.next
        )
        .addValidator(Validators.greaterThanOrEqual(0))
        .add()
        .build();

    private int current;
    private int next;

    public PowerLevelComponent() {
        this(0);
    }

    public PowerLevelComponent(int current) {
        this(current, 0);
    }

    public PowerLevelComponent(int current, int next) {
        this.current = current;
        this.next = next;
    }

    public PowerLevelComponent(PowerLevelComponent other) {
        this.current = other.current;
        this.next = other.next;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getNext() {
        return this.next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public String getState() {
        int current = this.current;
        if (current == 0) {
            return "NoPower";
        } else if (current <= 5) {
            return "PowerLow";
        } else if (current <= 10) {
            return "PowerMedium";
        } else {
            return "PowerHigh";
        }
    }

    @NullableDecl
    @Override
    public Component<ChunkStore> clone() {
        return new PowerLevelComponent(this);
    }
}
