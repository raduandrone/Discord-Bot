package vendor.objects;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.modules.Logger;

public class TerminalCommandsLinker extends CommandsLinker {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer("vendor.terminalcommands"){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new AbstractTerminalCommand(){
					@Override
					public String[] getCalls(){
						return null;
					}
					
					@Override
					public void action(){
						Logger.log("No command with the name \"" + commandName
								+ "\"!", Logger.LogType.WARNING, false);
					}
				};
				
			}
			
		};
		
	}
	
}
