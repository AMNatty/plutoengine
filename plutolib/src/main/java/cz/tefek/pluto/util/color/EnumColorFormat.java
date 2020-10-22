package cz.tefek.pluto.util.color;

/**
 * TODO
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public enum EnumColorFormat
{
    /**
     * 8-bit RGBA stored in a big-endian 32-bit integer
     */
    CF_INT_RGBA(4),
    /**
     * 8-bit BGRA stored in a big-endian 32-bit integer
     */
    CF_INT_BGRA(4),

    /**
     * 8-bit ARGB stored in a big-endian 32-bit integer
     */
    CF_INT_ARGB(4),
    /**
     * 8-bit ABGR stored in a big-endian 32-bit integer
     */
    CF_INT_ABGR(4),

    /**
     * 8-bit RGB stored in a big-endian 32-bit integer, the highest 8-bits are unused
     */
    CF_INT_RGB(4),
    /**
     * 8-bit RGB stored in a big-endian 32-bit integer, the highest 8-bits are unused
     */
    CF_INT_BGR(4),

    /**
     * 8-bit RGBA, one byte per color component
     */
    CF_4BYTE_RGBA(4),
    /**
     * 8-bit BGRA, one byte per color component
     */
    CF_4BYTE_BGRA(4),

    /**
     * 8-bit ARGB, one byte per color component
     */
    CF_4BYTE_ARGB(4),
    /**
     * 8-bit ABGR, one byte per color component
     */
    CF_4BYTE_ABGR(4),

    /**
     * 8-bit RGB, one byte per color component
     */
    CF_3BYTE_RGB(3),
    /**
     * 8-bit BGR, one byte per color component
     */
    CF_3BYTE_BGR(3);

    private final int size;

    EnumColorFormat(int size)
    {
        this.size = size;
    }

    /**
     * Returns the size in bytes.
     *
     * @return The size of the color format, in bytes
     */
    public int getSize()
    {
        return this.size;
    }
}
