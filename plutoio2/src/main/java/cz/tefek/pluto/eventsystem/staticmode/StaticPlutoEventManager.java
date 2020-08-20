package cz.tefek.pluto.eventsystem.staticmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;
import cz.tefek.pluto.eventsystem.EventData;

/**
 * A universal event manager. Register an event {@link Annotation} of your
 * choice (must be annotated with {@link StaticPlutoEvent &commat;Event}), then
 * annotate some public static method and you are done! Now you can trigger the
 * callbacks with ease. Multiple per-method events are possible! <i>Note that
 * event callbacks require a data-passing parameter
 * <tt>(extends {@link EventData})</tt>. The method will not be invoked
 * otherwise!</i>
 *
 * @author 493msi
 *
 */
public class StaticPlutoEventManager
{
    private static Map<Class<? extends Annotation>, List<Method>> eventRegistry = new HashMap<Class<? extends Annotation>, List<Method>>();
    private static List<Method> orphans = new ArrayList<Method>();

    public static void registerEventHandler(Class<?> clazz)
    {
        Method[] methods = clazz.getMethods();

        int methodsFound = 0;

        for (Method method : methods)
        {
            // The callback method must be static and public!
            if (!(Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())))
            {
                continue;
            }

            for (Annotation annotation : method.getDeclaredAnnotations())
            {
                if (annotation.annotationType().getAnnotation(StaticPlutoEvent.class) != null)
                {
                    methodsFound++;

                    var parentAnnotation = eventRegistry.get(annotation.annotationType());

                    // Find a parent annotation.
                    if (parentAnnotation != null)
                    {
                        parentAnnotation.add(method);
                    }
                    // No parent annotation and fix for methods with 2+ event
                    // annotations.
                    else if (!orphans.contains(method))
                    {
                        orphans.add(method);
                    }
                }
            }
        }

        Logger.log(SmartSeverity.EVENT_PLUS, "Event handler " + clazz.getCanonicalName() + " scan found " + methodsFound + " method callback(s).");
    }

    public static void registerEvent(Class<? extends Annotation> annotation)
    {
        // @Event is necessary.
        if (annotation.getAnnotation(StaticPlutoEvent.class) != null)
        {
            if (eventRegistry.containsKey(annotation))
            {
                Logger.log(SmartSeverity.EVENT_ERROR, "Annotation " + annotation.getCanonicalName() + " is already registered!");
                return;
            }
            else
            {
                eventRegistry.put(annotation, new ArrayList<Method>());

                Logger.log(SmartSeverity.EVENT_PLUS, "Event " + annotation.getCanonicalName() + " successfully registered!");

                short retroactivelyFound = 0;

                // Let's check all existing event Methods for this event.
                for (Entry<Class<? extends Annotation>, List<Method>> entry : eventRegistry.entrySet())
                {
                    // Checking the Method list for this event would make no
                    // sense.
                    if (annotation.equals(entry.getKey()))
                    {
                        continue;
                    }

                    for (Method method : entry.getValue())
                    {
                        // Just in case.
                        if (method.isAnnotationPresent(annotation))
                        {
                            eventRegistry.get(annotation).add(method);
                            retroactivelyFound++;
                        }
                    }
                }

                Logger.log(SmartSeverity.EVENT_PLUS, "Retroactive method checking found " + retroactivelyFound + " item(s).");

                // Let's check the Method orphanage for some potential
                // candidates.

                short orphansFound = 0;

                int orphansBefore = orphans.size();

                List<Method> foundParents = new ArrayList<Method>();

                for (Method method : orphans)
                {
                    if (method.isAnnotationPresent(annotation))
                    {
                        foundParents.add(method);

                        // No duplicates.
                        if (!eventRegistry.get(annotation).contains(method))
                        {
                            eventRegistry.get(annotation).add(method);
                            orphansFound++;
                        }
                    }
                }

                orphans.removeAll(foundParents);

                Logger.log(SmartSeverity.EVENT_PLUS, orphansFound + " orphan method(s) was/were bound and " + (orphansBefore - orphans.size()) + " removed from the storage!");
            }
        }
        else
        {
            Logger.log(SmartSeverity.EVENT_ERROR, "Annotation " + annotation.getCanonicalName() + " is not annotated with @Event, can't register it.");
        }
    }

    public static void fireEvent(Class<? extends Annotation> event, EventData data)
    {
        if (event.getAnnotation(StaticPlutoEvent.class) != null)
        {
            List<Method> methodList = eventRegistry.get(event);

            if (methodList != null)
            {
                for (Method m : methodList)
                {
                    // If a method contains more than one parameter, the most
                    // viable one will be chosen. Also, methods with no
                    // parameters are not valid.

                    Class<?>[] params = m.getParameterTypes();

                    Class<?> mostSuitableParam = null;
                    EventData[] paramOut = new EventData[params.length];

                    if (params.length == 0)
                    {
                        Logger.log(SmartSeverity.EVENT_WARNING, "Method " + m.toGenericString() + " has no parameters, will not be invoked by event!");
                    }

                    for (int i = 0; i < params.length; i++)
                    {
                        Class<?> parameter = params[i];

                        if (!EventData.class.isAssignableFrom(parameter))
                        {
                            Logger.log(SmartSeverity.EVENT_ERROR, "Method " + m.toGenericString() + " contains invalid parameters. Only EventData instances are permitted.");
                            mostSuitableParam = null;
                            break;
                        }

                        if (mostSuitableParam == null && parameter.isInstance(data))
                        {
                            mostSuitableParam = parameter;
                            paramOut[i] = data;
                        }

                        if (parameter.isInstance(data) && mostSuitableParam.isAssignableFrom(parameter))
                        {
                            mostSuitableParam = parameter;
                            paramOut = new EventData[params.length];
                            paramOut[i] = data;
                        }
                    }

                    if (mostSuitableParam != null)
                    {
                        try
                        {
                            m.invoke(null, (Object[]) paramOut);
                        }
                        catch (Exception e)
                        {
                            Logger.logException(e);
                        }
                    }
                }
            }
            else
            {
                Logger.log(SmartSeverity.EVENT_ERROR, "There is no event like " + event.getCanonicalName() + " registered.");
            }
        }
        else
        {
            Logger.log(SmartSeverity.EVENT_ERROR, event.getCanonicalName() + " is not an event!");
        }
    }

    public static void unregisterAll()
    {
        eventRegistry.clear();
        orphans.clear();
    }
}
