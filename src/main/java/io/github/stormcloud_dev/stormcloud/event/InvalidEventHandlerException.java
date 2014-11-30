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

import java.lang.reflect.Method;

import static java.lang.String.format;

public class InvalidEventHandlerException extends Exception {

    private Class<?> listenerClass;
    private Method method;

    public InvalidEventHandlerException(Class<?> listenerClass, Method method) {
        this(format("Invalid EventHandler in %s: %s", listenerClass.getCanonicalName(), method.getName()), listenerClass, method);
    }

    public InvalidEventHandlerException(String message, Class<?> listenerClass, Method method) {
        super(message);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public InvalidEventHandlerException(String message, Throwable cause, Class<?> listenerClass, Method method) {
        super(message, cause);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public InvalidEventHandlerException(Throwable cause, Class<?> listenerClass, Method method) {
        super(cause);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public InvalidEventHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> listenerClass, Method method) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public Class<?> getListenerClass() {
        return listenerClass;
    }

    public Method getMethod() {
        return method;
    }

}
