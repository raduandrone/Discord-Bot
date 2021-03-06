package vendor.utilities;

import java.util.Set;
import java.util.Stack;

import vendor.abstracts.AbstractBotCommand;
import vendor.abstracts.AbstractCommandRouter;
import vendor.objects.MessageEventDigger;

public class CommandsThreadManager {
	
	private CommandsThreadManager(){}
	
	/**
	 * Method that determines whether a command is running by scanning all the
	 * threads used in the server of the <code>commandID</code> parameter,
	 * looking for the desired <code>command</code> parameter.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param eventDigger
	 *            The server's <code>commandID</code> required to search for
	 *            commands running in said server's text channel.
	 * @return The command found with all of it's attribute in a
	 *         <code>Command</code> object, <code>null</code> if the command
	 *         wasn't found.
	 */
	public static AbstractBotCommand getCommandRunning(String commandName,
			MessageEventDigger eventDigger, AbstractCommandRouter inRouter){
		
		Stack<AbstractCommandRouter> routers = getRunningCommandRouters();
		
		for(AbstractCommandRouter router : routers)
			if(!router.equals(inRouter)
					&& router.getName().equals(
							eventDigger.getCommandKey(commandName)))
				return (AbstractBotCommand)router.getCommand();
		
		return null;
		
	}
	
	public static AbstractBotCommand getLatestRunningCommand(){
		
		Stack<AbstractCommandRouter> routers = getRunningCommandRouters();
		
		if(routers.empty()){
			return null;
		}
		else{
			
			AbstractCommandRouter latestRouter = routers.pop();
			
			return (AbstractBotCommand)latestRouter.getCommand();
			
		}
		
	}
	
	public static AbstractBotCommand getLatestRunningCommand(String guildID){
		
		Stack<AbstractCommandRouter> guildRouters = getRunningCommandRouters(guildID);
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			AbstractCommandRouter latestRouter = guildRouters.pop();
			
			return (AbstractBotCommand)latestRouter.getCommand();
			
		}
		
	}
	
	public static AbstractBotCommand getLatestRunningCommandExcept(
			AbstractBotCommand commandToIgnore){
		
		Stack<AbstractCommandRouter> guildRouters = getRunningCommandRouters();
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			AbstractCommandRouter latestRouter = guildRouters.pop();
			
			if(latestRouter.getCommand().equals(commandToIgnore)){
				if(guildRouters.empty()){
					return null;
				}
				else{
					latestRouter = guildRouters.pop();
				}
			}
			
			return (AbstractBotCommand)latestRouter.getCommand();
			
		}
		
	}
	
	public static AbstractBotCommand getLatestRunningCommandExcept(
			AbstractBotCommand commandToIgnore, String commandID){
		
		Stack<AbstractCommandRouter> guildRouters = getRunningCommandRouters(commandID);
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			AbstractCommandRouter latestRouter = guildRouters.pop();
			
			if(latestRouter.getCommand().equals(commandToIgnore)){
				if(guildRouters.empty()){
					return null;
				}
				else{
					latestRouter = guildRouters.pop();
				}
			}
			
			return (AbstractBotCommand)latestRouter.getCommand();
			
		}
		
	}
	
	/**
	 * Method that quickly tells if a command is running based off its name in
	 * the guild provided in parameters.
	 * <p>
	 * Internally, this uses the method <code>getCommandRunning()</code> and
	 * tests if that returns <code>null</code> or not.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param eventDigger
	 *            The server's <code>commandID</code> required to search for
	 *            commands running in said server.
	 * @return <code>true</code> if the command is running in the specified
	 *         command id, <code>false</code> otherwise.
	 */
	public static boolean isCommandRunning(String commandName,
			MessageEventDigger eventDigger, AbstractCommandRouter router){
		return getCommandRunning(commandName, eventDigger, router) != null;
	}
	
	public static int stopAllCommands(){
		
		int numberOfCommandsStopped = 0;
		
		Stack<AbstractCommandRouter> routers = getRunningCommandRouters();
		
		for(AbstractCommandRouter router : routers)
			if(((AbstractBotCommand)router.getCommand()).stopAction())
				numberOfCommandsStopped++;
		
		return numberOfCommandsStopped;
		
	}
	
	public static boolean hasRunningCommands(){
		return !getRunningCommandRouters().empty();
	}
	
	private static Stack<AbstractCommandRouter> getRunningCommandRouters(){
		Thread[] threads = getThreadsArray();
		
		Stack<AbstractCommandRouter> routers = new Stack<>();
		
		for(Thread thread : threads)
			if(thread instanceof AbstractCommandRouter)
				routers.add((AbstractCommandRouter)thread);
		
		return routers;
	}
	
	private static Stack<AbstractCommandRouter> getRunningCommandRouters(
			String commandID){
		Stack<AbstractCommandRouter> routers = getRunningCommandRouters();
		
		Stack<AbstractCommandRouter> guildRouters = new Stack<>();
		
		for(AbstractCommandRouter router : routers)
			if(router.getName().contains(commandID))
				guildRouters.push(router);
		
		return guildRouters;
	}
	
	private static Thread[] getThreadsArray(){
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		return threadSet.toArray(new Thread[threadSet.size()]);
	}
	
}
