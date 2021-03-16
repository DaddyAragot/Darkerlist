package darkerlist.listeners;

import darkerlist.Getter;
import darkerlist.functions.Moderation;
import darkerlist.objects.Config;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e){
        if (e.getUser() != e.getJDA().getSelfUser()){
            Config cfg = Getter.guilds.get(e.getGuild().getId());
            if (e.getChannel().getId().equalsIgnoreCase(cfg.getLogChannel())){
                if (e.retrieveMessage().complete().getAuthor() == e.getJDA().getSelfUser()){
                    if (e.retrieveMessage().complete().getEmbeds().get(0).getTitle().equalsIgnoreCase("Warning!")){
                        String[] args = e.retrieveMessage().complete().getEmbeds().get(0).getFields().get(0).getValue().split("\n");
                        if (args[1].startsWith("ID:")){
                            String[] id = args[1].split("\\s+");
                            if (e.getReactionEmote().getEmoji().equalsIgnoreCase("\u2705")){
                                Moderation.ban(e.getGuild(), e.getJDA().retrieveUserById(id[1]).complete(), Getter.bans.get(id[1]), false);
                                e.retrieveMessage().complete().delete().queue();
                            } else if (e.getReactionEmote().getEmoji().equalsIgnoreCase("\u274C")){
                                e.retrieveMessage().complete().delete().queue();
                            }
                        }
                    }
                }
            }
        }
    }

}
