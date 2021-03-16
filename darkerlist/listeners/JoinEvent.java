package darkerlist.listeners;

import darkerlist.Getter;
import darkerlist.functions.Moderation;
import darkerlist.main;
import darkerlist.objects.Config;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        if (Getter.bans.get(e.getUser().getId()) != null){
            Moderation.ban(e.getGuild(), e.getUser(), Getter.bans.get(e.getUser().getId()), true);
        } else if (Getter.reports.get(e.getUser().getId()) != null){
            Config cfg = Getter.guilds.get(e.getGuild().getId());
            if (cfg.getLogChannel() != null || !cfg.getLogChannel().equalsIgnoreCase("off")){
                main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, e.getUser(), "The following user joined recently and got reported.", Getter.reports.get(e.getUser().getId())).build()).queue();
            }
        }
    }

}
