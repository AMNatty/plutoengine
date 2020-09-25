package cz.tefek.pluto.event.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A simple functional interface based event factory for objects basically
 * implementing the Observer design pattern.
 * 
 * @since 0.2
 * 
 * @author 493msi
 */
public class LambdaEventFactory
{
    /**
     * A class representing a list of observing consumers.
     * 
     * @param <T> the type of the event data
     * 
     * @since 0.2
     * 
     * @author 493msi
     */
    public static class LambdaEvent<T>
    {
        private final List<Consumer<T>> consumers;

        private LambdaEvent()
        {
            this.consumers = new ArrayList<>();
        }

        /**
         * Adds a new listener to observe this object.
         * 
         * @param callback a functional interface representing a callback
         * function
         * 
         * @since 0.2
         * 
         * @author 493msi
         */
        public void addListener(Consumer<T> callback)
        {
            this.consumers.add(callback);
        }

        /**
         * Removes a calback from the list of observers.
         * 
         * @param callback A functional interface representing a callback
         * function
         * 
         * @since 0.2
         * 
         * @author 493msi
         */
        public void removeListener(Consumer<T> callback)
        {
            this.consumers.remove(callback);
        }

        /**
         * Notifies all observers by invoking their callbacks with the specified
         * value.
         * 
         * @param value the data to distribute to all observers
         * 
         * @since 0.2
         * 
         * @author 493msi
         */
        public void fire(T value)
        {
            this.consumers.forEach(c -> c.accept(value));
        }
    }

    /**
     * 
     * @return A new observable {@link LambdaEvent} object
     * 
     * @param <T> the data type this event object will work with
     * 
     * @since 0.2
     * 
     * @author 493msi
     */
    public static <T> LambdaEvent<T> createEvent()
    {
        return new LambdaEvent<>();
    }
}
