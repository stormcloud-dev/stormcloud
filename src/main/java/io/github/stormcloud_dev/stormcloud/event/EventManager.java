/*
 *   Copyright 2014 StormCloud Development Group
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.stormcloud_dev.stormcloud.event;

import io.github.stormcloud_dev.stormcloud.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

import static io.github.stormcloud_dev.stormcloud.event.EventHandlerPriority.*;
import static java.lang.String.format;

public class EventManager {

    private Map<Class<? extends Event>, Map<EventHandlerPriority, List<Listener>>> listeners;

    public EventManager() {
        listeners = new HashMap<>();
    }

    public void addListener(Object listener) throws InvalidEventHandlerException {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                if (method.getParameterCount() == 1) {
                    if (ReflectionUtils.isSubclassOf(method.getParameterTypes()[0], Event.class)) {
                        EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                        Class<? extends Event> event = method.getParameterTypes()[0].asSubclass(Event.class);
                        if (listeners.get(event) == null) {
                            listeners.put(event, new EnumMap<>(EventHandlerPriority.class));
                        }
                        EventHandlerPriority priority = eventHandler.priority();
                        if (listeners.get(event).get(priority) == null) {
                            listeners.get(event).put(priority, new ArrayList<>());
                        }
                        listeners.get(event).get(priority).add(new Listener(listener, method));
                    } else {
                        throw new InvalidEventHandlerException(format("Invalid EventHandler in %s: %s: Parameter is not a subclass of Event", listener.getClass().getCanonicalName(), method.getName()), listener.getClass(), method);
                    }
                } else {
                    throw new InvalidEventHandlerException(format("Invalid EventHandler in %s: %s: Parameter count is not equal to 1", listener.getClass().getCanonicalName(), method.getName()), listener.getClass(), method);
                }
            }
        }
    }

    public void onEvent(Event event) {
        Class<? extends Event> eventType = event.getClass();
        if (listeners.get(eventType) == null) return;
        //listeners.get(eventType).get(VERY_LOW).stream().forEach(listener -> listener.onEvent(event));
        //listeners.get(eventType).get(LOW).stream().forEach(listener -> listener.onEvent(event));
        listeners.get(eventType).get(NORMAL).stream().forEach(listener -> listener.onEvent(event));
        //listeners.get(eventType).get(HIGH).stream().forEach(listener -> listener.onEvent(event));
        //listeners.get(eventType).get(VERY_HIGH).stream().forEach(listener -> listener.onEvent(event));
    }

}
