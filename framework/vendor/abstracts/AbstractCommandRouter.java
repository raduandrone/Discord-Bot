package vendor.abstracts;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import vendor.exceptions.NoCommandException;
import vendor.interfaces.Command;
import vendor.interfaces.Translatable;
import vendor.interfaces.Utils;
import vendor.objects.*;
import vendor.res.FrameworkResources;

public abstract class AbstractCommandRouter extends Thread implements Utils,
		Translatable, FrameworkResources {
	
	private Dictionary dict;
	private Request request;
	private Buffer buffer;
	private CommandsRepository commandsRepo;
	private MessageEventDigger eventDigger;
	protected Command command;
	
	public AbstractCommandRouter(MessageReceivedEvent event,
			String receivedMessage, Buffer buffer,
			CommandsRepository commandsRepo){
		
		this.buffer = buffer;
		
		eventDigger = new MessageEventDigger(event);
		
		try{
			
			Object bufferedDict = buffer.get(BUFFER_DICTIONARY,
					eventDigger.getGuildId());
			setDictionary((Dictionary)bufferedDict);
			
		}
		catch(NullPointerException e){
			
			setDictionary(new Dictionary());
			
			try{
				buffer.push(getDictionary(), BUFFER_DICTIONARY,
						eventDigger.getGuildId());
			}
			catch(NullPointerException e1){}
			
		}
		
		commandsRepo.setDictionary(getDictionary());
		
		this.commandsRepo = commandsRepo;
		
		this.request = createRequest(receivedMessage, getDictionary());
		
	}
	
	protected abstract Request createRequest(String receivedMessage,
			Dictionary dict);
	
	public Command getCommand(){
		return this.command;
	}
	
	public void setCommand(Command command){
		this.command = command;
	}
	
	public CommandsRepository getCommandsRepo(){
		return this.commandsRepo;
	}
	
	public MessageEventDigger getEventDigger(){
		return this.eventDigger;
	}
	
	public MessageReceivedEvent getEvent(){
		return getEventDigger().getEvent();
	}
	
	public Buffer getBuffer(){
		return this.buffer;
	}
	
	public Request getRequest(){
		return this.request;
	}
	
	@Override
	public Dictionary getDictionary(){
		return this.dict;
	}
	
	@Override
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	/**
	 * Method that validates the message received and return the command to
	 * execute if it is not validated. In the case where the message received
	 * isn't a command (a message that starts with
	 * <i>Ressources.<b>PREFIX</b></i>), a <i>NoCommandException</i> is thrown.
	 * <p>
	 * If the message received is from a Private Channel, the
	 * {@link #commandWhenFromPrivate()} method is called and returns its value.
	 * </p>
	 * <p>
	 * If the message is from a Text Channel but is only the text of the
	 * {@link #getCommandPrefix()}, the value of the
	 * {@link #commandWhenFromServerIsOnlyPrefix()} is returned.
	 * </p>
	 * 
	 * @return The content of {@link #commandIfValidated()} if valid; a command
	 *         to execute otherwise.
	 * @throws NoCommandException
	 *             Generic exception thrown if the message isn't a command.
	 */
	protected Command validateMessage() throws NoCommandException{
		
		MessageReceivedEvent event = getEvent();
		
		if(event.isFromType(ChannelType.PRIVATE)){
			
			return commandWhenFromPrivate();
			
		}
		else if(event.isFromType(ChannelType.TEXT)){
			
			Request request = getRequest();
			String commandPrefix = getCommandPrefix();
			
			if(!request.getCommandNoFormat().matches(commandPrefix + ".*")){
				throw new NoCommandException();
			}
			else{
				
				if(request.getCommandNoFormat().equals(commandPrefix)){
					
					return commandWhenFromServerIsOnlyPrefix();
					
				}
				
			}
			
		}
		
		return commandIfValidated();
		
	}
	
	/**
	 * @return <code>null</code> by default, can be overridden to return another
	 *         value for {@link #validateMessage()}.
	 */
	protected Command commandIfValidated(){
		return null;
	}
	
	public abstract Command commandWhenFromPrivate();
	
	public abstract Command commandWhenFromServerIsOnlyPrefix();
	
	public abstract String getCommandPrefix();
	
}
