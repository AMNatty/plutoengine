/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine.event.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
        private final List<Predicate<T>> consumers;

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
        public void addListener(Predicate<T> callback)
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
        public void removeListener(Predicate<T> callback)
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
            this.consumers.removeIf(c -> !c.test(value));
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
