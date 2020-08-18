package cz.tefek.pluto.engine.math;

import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2fc;
import org.joml.Vector3fc;

public class TransformationMatrix
{
    /**
     * <p>
     * Creates a transformation matrix from the given translation, rotation and
     * scale.
     * </p>
     * <p>
     * The rotation is specified as rotating around the Z axis (roll), then rotating
     * around the X axis (pitch) and finally rotating around the Y axis (yaw).
     * </p>
     * 
     * @param translation The translation vector
     * @param rotation    The rotation angles
     * @param scale       The scale vector
     * 
     * @author 493msi
     * @since 0.1
     */
    public static Matrix4f create(Vector3fc translation, Vector3fc yxzRotation, Vector3fc scale)
    {
        var transformation = new Matrix4f();
        transformation.translate(translation);
        transformation.rotateAffineYXZ(yxzRotation.x(), yxzRotation.y(), yxzRotation.z());
        transformation.scale(scale);

        return transformation;
    }

    /**
     * <p>
     * Creates a transformation matrix from the given translation, rotation and
     * scale.
     * </p>
     * 
     * @param x      The translation on the X axis
     * @param y      The translation on the Y axis
     * @param z      The translation on the Z axis
     * @param pitch  The pitch rotation
     * @param yaw    The yaw rotation
     * @param roll   The roll rotation
     * @param scaleX The scale on the X axis
     * @param scaleY The scale on the Y axis
     * @param scaleY The scale on the Z axis
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix4f create(float x, float y, float z, float pitch, float yaw, float roll, float scaleX, float scaleY, float scaleZ)
    {
        var transformation = new Matrix4f();
        transformation.translate(x, y, z);
        transformation.rotateAffineYXZ(pitch, yaw, roll);
        transformation.scale(scaleX, scaleY, scaleZ);

        return transformation;
    }

    /**
     * <p>
     * Creates a 2D transformation matrix from the given translation, rotation and
     * scale.
     * </p>
     * 
     * @param translation The translation vector
     * @param rotation    The rotation angle
     * @param scale       The scale vector
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix3x2f create2D(Vector2fc translation, float rotation, Vector2fc scale)
    {
        var transformation = new Matrix3x2f();
        transformation.translate(translation.x(), translation.y());
        transformation.rotate(rotation);
        transformation.scale(scale);

        return transformation;
    }

    /**
     * <p>
     * Creates a 2D transformation matrix from the given translation, rotation and
     * scale.
     * </p>
     * 
     * @param x        The translation on the X axis
     * @param y        The translation on the Y axis
     * @param rotation The rotation angle
     * @param scaleX   The scale on the X axis
     * @param scaleY   The scale on the Y axis
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix3x2f create2D(float x, float y, float rotation, float scaleX, float scaleY)
    {
        var transformation = new Matrix3x2f();
        transformation.translate(x, y);
        transformation.rotate(rotation);
        transformation.scale(scaleX, scaleY);

        return transformation;
    }
}
