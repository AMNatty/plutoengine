package org.plutoengine.audio.al;

import org.lwjgl.openal.AL11;

import java.util.Arrays;

public enum EnumDistanceModel implements IOpenALEnum
{
    NONE(AL11.AL_NONE),
    INVERSE_DISTANCE(AL11.AL_INVERSE_DISTANCE),
    INVERSE_DISTANCE_CLAMPED(AL11.AL_INVERSE_DISTANCE_CLAMPED),
    LINEAR_DISTANCE(AL11.AL_LINEAR_DISTANCE),
    LINEAR_DISTANCE_CLAMPED(AL11.AL_LINEAR_DISTANCE_CLAMPED),
    EXPONENT_DISTANCE(AL11.AL_EXPONENT_DISTANCE),
    EXPONENT_DISTANCE_CLAMPED(AL11.AL_EXPONENT_DISTANCE_CLAMPED);

    private final int alID;

    EnumDistanceModel(int alID)
    {
        this.alID = alID;
    }

    public static EnumDistanceModel getByID(int id)
    {
        return Arrays.stream(EnumDistanceModel.values()).filter(model -> model.getALID() == id).findAny().orElse(null);
    }

    @Override
    public int getALID()
    {
        return this.alID;
    }
}
