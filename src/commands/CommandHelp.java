package commands;

import errorHandling.BotError;
import utilities.BotCommand;
import vendor.exceptions.CommandNotFoundException;
import vendor.modules.Logger;
import vendor.objects.ParametersHelp;

/**
 * Classe qui envois un message a l'utilisateur qui demande de l'aide avec une
 * liste complète des commandes.
 * 
 * @author Stephano
 */

public class CommandHelp extends BotCommand {
	
	@Override
	public void action(){
		
		String content = getContent();
		
		if(content == null){
			
			boolean isSimple = hasParameter("s", "simple");
			
			String fullHelpString = getRouter().getCommandsRepo()
					.getFullHelpString("Available commands :", isSimple);
			
			if(hasParameter("p", "private")){
				sendPrivateMessage(fullHelpString);
				sendInfoMessage(lang("HelpSentMessage"));
			}
			else{
				sendMessage(fullHelpString);
			}
			
		}
		else{
			
			try{
				
				BotCommand commandToExplain = (BotCommand)getRouter()
						.getCommandsRepo().getContainer().findCommand(content);
				
				commandToExplain.putStateFromCommand(this);
				
				StringBuilder builder = new StringBuilder();
				
				String helpString = commandToExplain.getHelp(
						"*Available parameters :*", "Usage : "
								+ commandToExplain.getUsage() + ".", null);
				
				builder.append(helpString);
				
				if(hasParameter("p", "private")){
					sendPrivateMessage(builder.toString());
					sendInfoMessage(lang("HelpSentMessage"));
				}
				else{
					sendMessage(builder.toString());
				}
				
			}
			catch(CommandNotFoundException e){
				new BotError(this, "The command " + buildVCommand(content)
						+ "does not exist!");
			}
			catch(Exception e){
				Logger.log(e);
			}
			
		}
		
	}
	
	@Override
	public Object getCalls(){
		return HELP;
	}
	
	@Override
	public String getCommandDescription(){
		return "Well you just used this command.So ;)";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(
					"Send the requested help string to your private channel.",
					"p", "private")
		};
	}
	
}
