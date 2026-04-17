package dragonclient.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dragonclient.Dragon;
import dragonclient.command.commands.HelpCommand;
import dragonclient.event.Listener;
import dragonclient.event.Events.ChatEvent;
import dragonclient.util.ChatUtils;


public class CommandManager implements Listener {
    public List<Command> commands = new ArrayList<Command>();
    public String prefix = ".";

    public CommandManager() {
        Dragon.eventManager.registerListener(this, ChatEvent.class);
        setup();
    }

    public void setup() {
        commands.add(new HelpCommand());
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void onChatEvent(ChatEvent event) {
        String message = event.message;

        if(!message.startsWith(prefix))
            return;

        event.cancelEvent();

        message = message.substring(prefix.length());

        boolean foundCommand = false;

        if(message.split(" ").length > 0) {
            String commandName = message.split(" ")[0];

            for(Command c : commands) {
                if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
                    c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                    foundCommand = true;
                    break;
                }
            }
        }
        if(!foundCommand) {
            if(message.split(" ").length > 0) {
                String commandName = message.split(" ")[0];
                ChatUtils.error(commandName + " is not a valid command.");
            } else {
                ChatUtils.error("command not found!");
            }
        }
    }
    
}
