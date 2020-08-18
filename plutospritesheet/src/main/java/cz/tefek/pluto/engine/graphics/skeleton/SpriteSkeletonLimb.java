package cz.tefek.pluto.engine.graphics.skeleton;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.graphics.RectangleRenderer2D;
import cz.tefek.pluto.engine.graphics.Renderer2D;
import cz.tefek.pluto.engine.graphics.sprite.PartialTextureSprite;

public class SpriteSkeletonLimb
{
    protected Vector2f pivotPoint; // The origin point of this limb's transformation

    protected float rotation;

    protected float width;
    protected float height;

    protected Matrix3x2f transformation;

    private PartialTextureSprite sprite;

    protected static final Vector2f DEFAULT_SCALE = new Vector2f(1);

    // The reason I don't use an IdentityHashMap here is because I need to retain the insertion order perfectly,
    // so the skeleton nicely draws back to front and not in a seemingly random order.

    private List<Pair<SpriteSkeletonLimb, Vector2f>> backChildren; // Children <keys> connected to this limb from behind at the <value> position
    private List<Pair<SpriteSkeletonLimb, Vector2f>> frontChildren; // Children <keys> connected to this limb from front at <value> position

    public static final int MAX_DEPTH = 8;

    public SpriteSkeletonLimb(float pivotX, float pivotY)
    {
        this.backChildren = new ArrayList<>();
        this.frontChildren = new ArrayList<>();

        this.transformation = new Matrix3x2f();

        this.pivotPoint = new Vector2f(pivotX, pivotY);
    }

    public List<Pair<SpriteSkeletonLimb, Vector2f>> getBackChildren()
    {
        return this.backChildren;
    }

    public List<Pair<SpriteSkeletonLimb, Vector2f>> getFrontChildren()
    {
        return this.frontChildren;
    }

    public void addChildFront(SpriteSkeletonLimb limb, float mountX, float mountY)
    {
        this.frontChildren.add(Pair.of(limb, new Vector2f(mountX, mountY)));
    }

    public void addChildBack(SpriteSkeletonLimb limb, float mountX, float mountY)
    {
        this.backChildren.add(Pair.of(limb, new Vector2f(mountX, mountY)));
    }

    public Vector2f getPivotPoint()
    {
        return this.pivotPoint;
    }

    public void setSprite(PartialTextureSprite sprite)
    {
        this.sprite = sprite;
    }

    public PartialTextureSprite getSprite()
    {
        return this.sprite;
    }

    protected void renderInternal(RectangleRenderer2D renderer, Vector2fc position, Vector2fc scale)
    {
        renderer.identity().translate(position.x(), position.y()).rotate(this.rotation).translate(-this.pivotPoint.x * scale.x(), -this.pivotPoint.y * scale.y()).scale(this.width * scale.x(), this.height * scale.y()).sprite(this.sprite).flush();
    }

    protected void renderChildren(RectangleRenderer2D renderer, Vector2fc position, Vector2fc scale)
    {
        this.backChildren.forEach(entry ->
        {
            var limb = entry.getLeft();
            var mountingPoint = entry.getRight();
            var rescaledMountingPoint = mountingPoint.mul(scale, new Vector2f());
            var transformedMountingPoint = this.transformation.transformPosition(rescaledMountingPoint, new Vector2f());
            var transformedPos = position.add(transformedMountingPoint, new Vector2f());

            limb.renderChildren(renderer, transformedPos, scale);
        });

        this.renderInternal(renderer, position, scale);

        this.frontChildren.forEach(entry ->
        {
            var limb = entry.getLeft();
            var mountingPoint = entry.getRight();
            var rescaledMountingPoint = mountingPoint.mul(scale, new Vector2f());
            var transformedMountingPoint = this.transformation.transformPosition(rescaledMountingPoint, new Vector2f());
            var transformedPos = position.add(transformedMountingPoint, new Vector2f());

            limb.renderChildren(renderer, transformedPos, scale);
        });
    }

    public void render(Vector2fc position, Vector2fc scale)
    {
        if (scale.x() * scale.y() < 0)
        {
            GL33.glCullFace(GL33.GL_FRONT);
            this.renderChildren(RectangleRenderer2D.draw(Renderer2D.centeredQuad), position, scale);
            GL33.glCullFace(GL33.GL_BACK);
        }
        else
        {
            this.renderChildren(RectangleRenderer2D.draw(Renderer2D.centeredQuad), position, scale);
        }
    }

    public void render(Vector2fc position)
    {
        this.renderChildren(RectangleRenderer2D.draw(Renderer2D.centeredQuad), position, DEFAULT_SCALE);
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return this.height;
    }

    public float getWidth()
    {
        return this.width;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
        this.updateTransformation();
    }

    public void addRotation(float rotation)
    {
        this.rotation += rotation;
        this.updateTransformation();
    }

    public void setRotationAdjusted(float rotation)
    {
        var rotationDiff = rotation - this.rotation;
        this.backChildren.forEach(entry -> entry.getKey().addRotationAdjusted(rotationDiff));
        this.frontChildren.forEach(entry -> entry.getKey().addRotationAdjusted(rotationDiff));
        this.setRotation(rotation);
    }

    public void addRotationAdjusted(float rotation)
    {
        this.backChildren.forEach(entry -> entry.getKey().addRotationAdjusted(rotation));
        this.frontChildren.forEach(entry -> entry.getKey().addRotationAdjusted(rotation));
        this.addRotation(rotation);
    }

    public float getRotation()
    {
        return this.rotation;
    }

    protected void updateTransformation()
    {
        this.transformation.identity();
        this.transformation.rotate(this.rotation);
    }
}
