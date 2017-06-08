import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.ChannelManager;
import net.dv8tion.jda.core.managers.GuildManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Commands.Help;
import Commands.JoinVoiceChannel;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;


/**
 * 
 * @author Stephano
 *
 *<br><br>Cette classe extend <b>ListenerAdapter</b> recoit les commandes de l'utilisateur et appele les classes necessaires
 */
public class MessageListener extends ListenerAdapter{

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message msg = event.getMessage();
		String messageRecu = event.getMessage().getContent();
		TextChannel textChannel = event.getTextChannel();
		if (messageRecu.contains("!")) {
			if (messageRecu.equalsIgnoreCase("!hello")) {
				textChannel.sendMessage("hello " + event.getAuthor().getName()).complete();
			} else if (messageRecu.equalsIgnoreCase("!help")) {
				Help help = new Help(event);
			} else if (messageRecu.equalsIgnoreCase("!connect")) {
				JoinVoiceChannel join = new JoinVoiceChannel(event);

			} else if (messageRecu.contains("!play ")) {
				String playLink = messageRecu.substring(6);
				textChannel.sendMessage(playLink).complete();

			} else if (messageRecu.equalsIgnoreCase("!clear")) {
				List<VoiceChannel> voiceChanel = event.getAuthor().getJDA().getVoiceChannels();
				for (VoiceChannel voiceChannel : voiceChanel) {
					List<Member> members = voiceChannel.getMembers();
					for (Member member : members) {
					}
				}
				// textChannel.deleteMessages((Collection<Message>)
				// event.getTextChannel().getHistory().getRetrievedHistory().subList(0,
				// textChannel.getHistory().size() + 1)).complete();

			}
		}
	}
}