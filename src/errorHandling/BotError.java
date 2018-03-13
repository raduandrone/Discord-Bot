package errorHandling;

import utilities.Command;

public class BotError extends AbstractBotError {
	
	public BotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner, Object[] replacements){
		super(commandInError, errorMessage, isErrorOneLiner, replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	public BotError(Command commandInError, String errorMessage,
			Object[] replacements){
		super(commandInError, errorMessage, replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner, Object[] replacements){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner,
				replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji, Object[] replacements){
		super(commandInError, errorMessage, errorEmoji, replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	public BotError(Command commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	public BotError(String errorMessage, boolean isErrorOneLiner,
			Object[] replacements){
		super(errorMessage, isErrorOneLiner, replacements);
	}
	
	public BotError(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	public BotError(String errorMessage, Object[] replacements){
		super(errorMessage, replacements);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner, Object[] replacements){
		super(errorMessage, errorEmoji, isErrorOneLiner, replacements);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			Object[] replacements){
		super(errorMessage, errorEmoji, replacements);
	}
	
	public BotError(String errorMessage, String errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	public BotError(String errorMessage){
		super(errorMessage);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoMessage(messageToSend, isErrorOneLiner);
	}
	
}
