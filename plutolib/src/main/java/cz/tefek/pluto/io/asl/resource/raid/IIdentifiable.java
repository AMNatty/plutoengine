package cz.tefek.pluto.io.asl.resource.raid;

import cz.tefek.pluto.io.asl.resource.ResourceAddress;

public interface IIdentifiable
{
    ResourceAddress getID();

    default String getStringID()
    {
        return this.getID().toString();
    }

    int getRAID();

    void onRegister(int raid);
}
