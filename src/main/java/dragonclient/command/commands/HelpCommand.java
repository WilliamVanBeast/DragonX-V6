package dragonclient.command.commands;

import dragonclient.Dragon;
import dragonclient.command.Command;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("Help", "Shows all commands", "Help", "help");
    }

	@Override
	public void onCommand(String[] args, String command) {
		if (args.length == 0) {
			Dragon.moduleManager.addChatMessage("---DragonX Commands---");
            Dragon.moduleManager.addChatMessage(".help - shows this");
            Dragon.moduleManager.addChatMessage("----------------------");
			return;
		}
	}
}
