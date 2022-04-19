package org.plutoengine.gameobject;

public interface IGameObject<H>
{
    H objectKey();

    void onRegister(int objectID);

    int objectID();
}
