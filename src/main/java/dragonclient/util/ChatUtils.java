package dragonclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils
{
	public static void component(String message)
  {
      Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(message));
  }
  public static void message(String message)
  {
      component("§l§c[§eDragonX§c]§f " + message);
  }
    
	public static void warning(String message)
	{
		message("§c[§6§lWARNING§c]§f " + message);
	}
	
	public static void error(String message)
	{
		message("§c[§4§lERROR§c]§f " + message);
	}
	
	public static void success(String message)
	{
		message("§a[§2§lSUCCESS§a]§f " + message);
	}
	
	public static void failure(String message)
	{
		message("§c[§4§lFAILURE§c]§f " + message);
	}
    
}
