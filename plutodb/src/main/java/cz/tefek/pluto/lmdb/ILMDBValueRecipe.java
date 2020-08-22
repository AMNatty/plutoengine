package cz.tefek.pluto.lmdb;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public interface ILMDBValueRecipe
{
    public int sizeOf();

    public void serialize(ByteBuffer output);

    public void deserialize(ByteBuffer input);

    public static int sizeOfUTF8(CharSequence string)
    {
        return Integer.BYTES + MemoryUtil.memLengthUTF8(string, false);
    }

    public static void putUTF8(CharSequence string, ByteBuffer output)
    {
        int strLen = MemoryUtil.memUTF8(string, false, output, output.position() + Integer.BYTES);
        output.putInt(strLen);

        output.position(output.position() + strLen);
    }

    public static String getUTF8(ByteBuffer input)
    {
        var strLen = input.getInt();
        var string = MemoryUtil.memUTF8(input, strLen);

        input.position(input.position() + strLen);

        return string;
    }
}
