package cz.tefek.pluto.engine.gui.font;

public class CharacterInfo
{
    int leftOffs;
    private int rightOffs;
    int number;

    public CharacterInfo(int number, int leftOffs, int rightOffs)
    {
        this.number = number;
        this.leftOffs = leftOffs;
        this.rightOffs = rightOffs;
    }

    public int getLeftOffset()
    {
        return this.leftOffs;
    }

    public int getNumber()
    {
        return this.number;
    }

    public int getRightOffset()
    {
        return this.rightOffs;
    }
}
