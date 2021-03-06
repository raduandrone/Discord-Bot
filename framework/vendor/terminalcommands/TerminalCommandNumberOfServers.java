package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.exceptions.JDANotSetException;
import vendor.modules.Logger;
import vendor.modules.Metrics;

public class TerminalCommandNumberOfServers extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"connections"
		};
	}
	
	@Override
	public void action(){
		
		try{
			
			int numberOfJoinedServers = Metrics.getNumberOfJoinedServers();
			
			Logger.log("The bot has joined " + numberOfJoinedServers
					+ " server(s) so far!");
			
		}
		catch(JDANotSetException e){
			Logger.log("Start the bot to get his details!");
		}
		
	}
	
}
