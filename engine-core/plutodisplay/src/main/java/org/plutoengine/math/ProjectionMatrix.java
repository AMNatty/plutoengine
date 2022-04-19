package org.plutoengine.math;

import org.joml.Matrix4f;

public class ProjectionMatrix
{
    /**
     * Create a 2D orthogonal projection Matrix4f based on the width and height.
     * 
     * @param width  The ortho width
     * @param height The ortho height
     * 
     * @return the matrix
     * 
     * @author 493msi
     * @since 0.1
     */
    public static Matrix4f createOrtho2D(int width, int height)
    {
        var orthoMatrix = new Matrix4f();
        orthoMatrix.setOrtho2D(0, width, height, 0);

        return orthoMatrix;
    }

    /**
     * Create a centered 2D orthogonal projection Matrix3x2f based on the width and
     * height.
     * 
     * @param width  The ortho width
     * @param height The ortho height
     * 
     * @return the matrix
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix4f createOrtho2DCentered(int width, int height)
    {
        var orthoMatrix = new Matrix4f();
        orthoMatrix.setOrtho2D(width / 2.0f, width / 2.0f, height / 2.0f, height / 2.0f);

        return orthoMatrix;
    }

    /**
     * Create a perspective frustum based on the parameters.
     * 
     * @param aspectRatio The aspect ratio of the frustum
     * @param fov         The fov of the frustum
     * @param zNear       The distance of the zNear clipping plane
     * @param zFar        The distance of the zFar clipping plane
     * 
     * @return the perspective matrix
     * 
     * @author 493msi
     * @since 0.1
     */
    public static Matrix4f createPerspective(float aspectRatio, float fov, float zNear, float zFar)
    {
        var perspective = new Matrix4f();
        perspective.setPerspective(fov, aspectRatio, zNear, zFar);

        return perspective;
    }
}
