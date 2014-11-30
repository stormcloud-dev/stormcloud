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
package io.github.stormcloud_dev.stormcloud.command;

import java.lang.reflect.Method;

import static java.lang.String.format;

public class InvalidCommandHandlerException extends Exception {

    private Class<?> executorClass;
    private Method method;

    public InvalidCommandHandlerException(Class<?> executorClass, Method method) {
        this(format("Invalid CommandHandler in %s: %s", executorClass.getCanonicalName(), method.getName()), executorClass, method);
    }

    public InvalidCommandHandlerException(String message, Class<?> executorClass, Method method) {
        super(message);
        this.executorClass = executorClass;
        this.method = method;
    }

    public InvalidCommandHandlerException(String message, Throwable cause, Class<?> executorClass, Method method) {
        super(message, cause);
        this.executorClass = executorClass;
        this.method = method;
    }

    public InvalidCommandHandlerException(Throwable cause, Class<?> executorClass, Method method) {
        super(cause);
        this.executorClass = executorClass;
        this.method = method;
    }

    public InvalidCommandHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> executorClass, Method method) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.executorClass = executorClass;
        this.method = method;
    }

    public Class<?> getExecutorClass() {
        return executorClass;
    }

    public Method getMethod() {
        return method;
    }

}
