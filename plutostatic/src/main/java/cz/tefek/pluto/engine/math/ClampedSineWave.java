package cz.tefek.pluto.engine.math;

/**
 * A clamped sine wave generator, for animations, mostly.
 * 
 * @since 0.2
 * @author 493msi
 */
public class ClampedSineWave
{
    /**
     * Gets a clamped value of the abs(sine) function from the given parameters as a
     * {@link double}.
     * 
     * @param animationProgress The animation progress in frames.
     * @param frameOffset       The animation offset in frames.
     * @param animationFrames   The total amount of animation frames.
     * @param bottomClamp       The sine wave clamp, minimum value.
     * @param topClamp          The sine wave clamp, maximum value.
     * 
     * @return The resulting value
     * 
     * @since 0.2
     * @author 493msi
     */
    public static double getAbsolute(double animationProgress, double frameOffset, double animationFrames, double bottomClamp, double topClamp)
    {
        var actualProgress = (animationProgress + frameOffset) / animationFrames;
        return Math.min(Math.max(Math.abs(Math.sin(actualProgress * Math.PI)), bottomClamp), topClamp);
    }

    /**
     * Gets a clamped value of the sine function from the given parameters as a
     * {@link double}.
     * 
     * @param animationProgress The animation progress in frames.
     * @param frameOffset       The animation offset in frames.
     * @param animationFrames   The total amount of animation frames.
     * @param bottomClamp       The sine wave clamp, minimum value.
     * @param topClamp          The sine wave clamp, maximum value.
     * 
     * @return The resulting value
     * 
     * @since 0.2
     * @author 493msi
     */
    public static double get(double animationProgress, double frameOffset, double animationFrames, float bottomClamp, float topClamp)
    {
        var actualProgress = (animationProgress + frameOffset) / animationFrames;
        return Math.min(Math.max(Math.sin(actualProgress * Math.PI), bottomClamp), topClamp);
    }

    /**
     * Gets a clamped value of the sine function from the given parameters as a
     * {@link double}.
     * 
     * @param animationProgress The animation progress in frames.
     * @param animationFrames   The total amount of animation frames.
     * @param bottomClamp       The sine wave clamp, minimum value.
     * @param topClamp          The sine wave clamp, maximum value.
     * 
     * @return The resulting value
     * 
     * @since 0.2
     * @author 493msi
     */
    public static double get(double animationProgress, double animationFrames, float bottomClamp, float topClamp)
    {
        var progress = animationProgress / animationFrames;
        return Math.min(Math.max(Math.sin(progress * Math.PI), bottomClamp), topClamp);
    }
}
