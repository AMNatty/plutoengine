package cz.tefek.io.asl.resource.raid;

import cz.tefek.io.asl.resource.ResourceAddress;

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
