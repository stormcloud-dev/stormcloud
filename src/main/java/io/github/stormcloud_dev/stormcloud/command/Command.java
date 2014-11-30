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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Command {

    private Object executor;
    private Method commandHandler;
    private CommandHandlerPriority priority;
    private String description;

    public Command(Object executor, Method commandHandler, CommandHandlerPriority priority, String description) {
        this.executor = executor;
        this.commandHandler = commandHandler;
        this.priority = priority;
        this.description = description;
    }

    public void onCommand(String sender, String[] args) {
        try {
            commandHandler.invoke(executor, sender, args);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public Object getExecutor() {
        return executor;
    }

    public Method getCommandHandler() {
        return commandHandler;
    }

    public CommandHandlerPriority getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

}
