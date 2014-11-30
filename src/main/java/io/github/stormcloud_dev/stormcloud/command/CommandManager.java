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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.stormcloud_dev.stormcloud.util.ReflectionUtils.isSubclassOf;
import static java.lang.String.format;

public class CommandManager {

    private Map<String, Command> commands;

    public CommandManager() {
        commands = new ConcurrentHashMap<>();
    }

    public void addCommand(Object executor) throws InvalidCommandHandlerException, CommandConflictException {
        for (Method method : executor.getClass().getMethods()) {
            if (method.isAnnotationPresent(CommandHandler.class)) {
                if (method.getParameterCount() == 2) {
                    if (isSubclassOf(method.getParameterTypes()[0], String.class) && isSubclassOf(method.getParameterTypes()[1], String[].class)) {
                        CommandHandler commandHandler = method.getAnnotation(CommandHandler.class);
                        String commandName = commandHandler.name();
                        if (!commands.containsKey(commandName) || commands.get(commandName).getPriority().getValue() < commandHandler.priority().getValue()) {
                            commands.put(commandName, new Command(executor, method, commandHandler.priority(), commandHandler.description()));
                        } else if (commands.get(commandName).getPriority() == commandHandler.priority()) {
                            throw new CommandConflictException(commands.get(commandName).getExecutor().getClass(), commands.get(commandName).getCommandHandler(), executor.getClass(), method);
                        }
                        for (String alias : commandHandler.aliases()) {
                            if (!commands.containsKey(alias) || commands.get(alias).getPriority().getValue() < commandHandler.priority().getValue()) {
                                commands.put(alias, new Command(executor, method, commandHandler.priority(), commandHandler.description()));
                            } else if (commands.get(alias).getPriority() == commandHandler.priority()) {
                                throw new CommandConflictException(commands.get(alias).getExecutor().getClass(), commands.get(alias).getCommandHandler(), executor.getClass(), method);
                            }
                        }
                    } else {
                        throw new InvalidCommandHandlerException(format("Invalid CommandHandler in %s: %s: Parameters are not instances of String and String[], respectively", executor.getClass().getCanonicalName(), method.getName()), executor.getClass(), method);
                    }
                } else {
                    throw new InvalidCommandHandlerException(format("Invalid CommandHandler in %s: %s: Parameter count is not equal to 2", executor.getClass().getCanonicalName(), method.getName()), executor.getClass(), method);
                }
            }
        }
    }

    public boolean onCommand(String sender, String command, String[] args) {
        if (commands.containsKey(command)) {
            commands.get(command).onCommand(sender, args);
            return true;
        } else {
            return false;
        }
    }

    public Set<String> getCommands() {
        return commands.keySet();
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

}
