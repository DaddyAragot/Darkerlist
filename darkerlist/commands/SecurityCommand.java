package darkerlist.commands;

import darkerlist.Getter;
import darkerlist.functions.Moderation;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class SecurityCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if (e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "securitycheck") && e.getMember().hasPermission(Permission.ADMINISTRATOR) && !Getter.checked.contains(e.getGuild())){
            e.getChannel().sendMessage("Security Check started! Please wait a bit.").queue();
            e.getGuild().loadMembers().onSuccess(members -> {
               try {
                   for (Member m : members){
                       if (Getter.banned.contains(m.getId())){
                           Moderation.ban(e.getGuild(), m.getUser(), Getter.bans.get(m.getId()), true);
                       }
                   }
                   Getter.checked.add(e.getGuild());
                   e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Your server has been successfully checked.").build()).queue();
               } catch (Exception x){
                   x.printStackTrace();
               }
            });
        } else if (e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "securitycheck") && e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "You can only use this command once every bot reboot. On important occasions you can message my developer and ask for a priority check!").build()).queue();
        } else if (e.getAuthor().equals(Getter.AUTHOR) && e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "prioritycheck")){
            e.getChannel().sendMessage("Priority Check started! Please wait a bit.").queue();
            e.getGuild().loadMembers().onSuccess(members -> {
                for (Member m : members){
                    if (Getter.banned.contains(m.getId())){
                        Moderation.ban(e.getGuild(), m.getUser(), Getter.bans.get(m.getId()), true);
                    }
                }
                Getter.checked.add(e.getGuild());
                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Your server has been successfully checked.").build()).queue();
            });
        } else if (e.getAuthor().equals(Getter.AUTHOR) && e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "securityreset")){
            Getter.checked = new ArrayList<Guild>();
            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The securitychecks have been reset.").build()).queue();
        }
    }
}
