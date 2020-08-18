package cz.tefek.test.plutodb;

import java.nio.ByteBuffer;

import cz.tefek.plutodb.ILMDBValueRecipe;

public class UserData implements ILMDBValueRecipe
{
    public long money;
    public long keks;
    public String text;
    public int keys;

    @Override
    public void serialize(ByteBuffer output)
    {
        output.putLong(this.money);
        output.putLong(this.keks);
        ILMDBValueRecipe.putUTF8(this.text, output);
        output.putInt(this.keys);
    }

    @Override
    public void deserialize(ByteBuffer input)
    {
        this.money = input.getLong();
        this.keks = input.getLong();
        this.text = ILMDBValueRecipe.getUTF8(input);
        this.keys = input.getInt();
    }

    @Override
    public String toString()
    {
        return "UserData [money=" + this.money + ", keks=" + this.keks + ", text=" + this.text + ", keys=" + this.keys + "]";
    }

    @Override
    public int sizeOf()
    {
        return Long.BYTES + Long.BYTES + ILMDBValueRecipe.sizeOfUTF8(this.text) + Integer.BYTES;

    }
}
