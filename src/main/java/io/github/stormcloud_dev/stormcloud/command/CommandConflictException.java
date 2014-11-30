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

public class CommandConflictException extends Exception {

    private Class<?> originalExecutor;
    private Method originalHandler;
    private Class<?> newExecutor;
    private Method newHandler;

    public CommandConflictException(Class<?> originalExecutor, Method originalHandler, Class<?> newExecutor, Method newHandler) {
        this(format("%s in %s conflicts with %s in %s", newHandler.getName(), newExecutor.getCanonicalName(), originalHandler.getName(), originalExecutor.getCanonicalName()), originalExecutor, originalHandler, newExecutor, newHandler);
    }

    public CommandConflictException(String message, Class<?> originalExecutor, Method originalHandler, Class<?> newExecutor, Method newHandler) {
        super(message);
        this.originalExecutor = originalExecutor;
        this.originalHandler = originalHandler;
        this.newExecutor = newExecutor;
        this.newHandler = newHandler;
    }

    public CommandConflictException(String message, Throwable cause, Class<?> originalExecutor, Method originalHandler, Class<?> newExecutor, Method newHandler) {
        super(message, cause);
        this.originalExecutor = originalExecutor;
        this.originalHandler = originalHandler;
        this.newExecutor = newExecutor;
        this.newHandler = newHandler;
    }

    public CommandConflictException(Throwable cause, Class<?> originalExecutor, Method originalHandler, Class<?> newExecutor, Method newHandler) {
        super(cause);
        this.originalExecutor = originalExecutor;
        this.originalHandler = originalHandler;
        this.newExecutor = newExecutor;
        this.newHandler = newHandler;
    }

    public CommandConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> originalExecutor, Method originalHandler, Class<?> newExecutor, Method newHandler) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.originalExecutor = originalExecutor;
        this.originalHandler = originalHandler;
        this.newExecutor = newExecutor;
        this.newHandler = newHandler;
    }

    public Class<?> getOriginalExecutor() {
        return originalExecutor;
    }

    public Method getOriginalHandler() {
        return originalHandler;
    }

    public Class<?> getNewExecutor() {
        return newExecutor;
    }

    public Method getNewHandler() {
        return newHandler;
    }

}
