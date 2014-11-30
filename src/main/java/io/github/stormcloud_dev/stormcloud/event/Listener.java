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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Listener {

    private Object listener;
    private Method eventHandler;

    public Listener(Object listener, Method eventHandler) {
        this.listener = listener;
        this.eventHandler = eventHandler;
    }

    public void onEvent(Event event) {
        try {
            eventHandler.invoke(listener, event);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

}
