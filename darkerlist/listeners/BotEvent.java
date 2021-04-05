package darkerlist.listeners;

import darkerlist.main;
import darkerlist.Getter;
import darkerlist.objects.Config;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class BotEvent extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent e){
        for (Guild g : e.getJDA().getGuilds()){
            Config cfg = Getter.guilds.get(g.getId());
            if (cfg == null){
                cfg = new Config("off", null, new ArrayList<String>(), null);
                Getter.guilds.put(g.getId(), cfg);
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        Config cfg = new Config("off", null, new ArrayList<String>(), null);
        Getter.guilds.put(e.getGuild().getId(), cfg);
        e.getJDA().openPrivateChannelById("705786757144379463").complete().sendMessage("Joined Guild: " + e.getGuild().getName()).queue();
        try {
            Getter.saveMaps();
            main.jda.getPresence().setActivity(Activity.watching("for blacklisted users on " + main.jda.getGuilds().size() + " servers."));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            e.getJDA().openPrivateChannelById("705786757144379463").complete().sendMessage("Error: " + ioException.getMessage());
        }
        if (Getter.bannedGuilds.get(e.getGuild().getId()) != null){
            e.getGuild().leave().queue();
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e){
        Getter.guilds.remove(e.getGuild().getId());
        e.getJDA().openPrivateChannelById("705786757144379463").complete().sendMessage("Left Guild: " + e.getGuild().getName()).queue();
        try {
            Getter.saveMaps();
            main.jda.getPresence().setActivity(Activity.watching("for blacklisted users on " + main.jda.getGuilds().size() + " servers."));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            e.getJDA().openPrivateChannelById("705786757144379463").complete().sendMessage("Error: " + ioException.getMessage());
        }
    }
}
