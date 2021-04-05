package darkerlist.functions;

import darkerlist.Getter;
import darkerlist.main;
import darkerlist.objects.Ban;
import darkerlist.objects.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class Moderation {

    public static void ban(Guild g, User u, Ban ban, Boolean check){
        Config cfg = Getter.guilds.get(g.getId());
        if (check){
            if (cfg.getMode().equalsIgnoreCase("on")){
                if (cfg.getWhiteList().contains(u.getId())){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.RED);
                    eb.setTitle("Warning!");
                    eb.setDescription("A blacklisted user is whitelisted on the following server.(Join)");
                    eb.addField("**Guild**", "Name: " + g.getName() + "\n" +
                            "ID: " + g.getId(), false);
                    eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                            "ID: " + u.getId(), false);
                    Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                    main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, u, "A user on your server is blacklisted!", ban).build()).queue(message -> {
                        message.addReaction("\u2705").queue();
                        message.addReaction("\u274C").queue();
                    });
                } else {
                    try {
                        u.openPrivateChannel().complete().sendMessage(Getter.getEmbed(Getter.BAN, u, "You were banned from the server " + g.getName() + ". Dm " + Getter.AUTHOR.getAsTag() + " to appeal for the ban.", ban).build()).queue();
                        g.ban(g.getMember(u), 7, ban.getReason()).queue();
                        main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.BAN, u, "A blacklisted user was banned from your server!", ban).build()).queue();
                    } catch (Exception e) {
                        main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.UNKNOWN, "An error occurred during banning this user. Please make sure that the bots highest role is higher than the users highest role.").build()).queue();
                    }

                }
            } else if (cfg.getMode().equalsIgnoreCase("passive")){
                if (cfg.getWhiteList().contains(u)){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.RED);
                    eb.setTitle("Warning!");
                    eb.setDescription("A blacklisted user is whitelisted on the following server.(Join)");
                    eb.addField("**Guild**", "Name: " + g.getName() + "\n" +
                            "ID: " + g.getId(), false);
                    eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                            "ID: " + u.getId(), false);
                    Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                    main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, u, "A user on your server is blacklisted!", ban).build()).queue(message -> {
                        message.addReaction("\u2705").queue();
                        message.addReaction("\u274C").queue();
                    });
                } else {
                    main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, u, "A user on your server is blacklisted!", ban).build()).queue(message -> {
                        message.addReaction("\u2705").queue();
                        message.addReaction("\u274C").queue();
                    });
                }
             }
        } else {
            try {
                u.openPrivateChannel().complete().sendMessage(Getter.getEmbed(Getter.BAN, u, "You were banned from the server " + g.getName() + ". Dm " + Getter.AUTHOR.getAsTag() + " to appeal for the ban.", ban).build()).queue();
                g.ban(g.getMember(u), 7, ban.getReason()).queue();
                main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.BAN, u, "A blacklisted user was banned from your server!", ban).build()).queue();
            } catch (Exception e) {
                main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.UNKNOWN, "An error occurred during banning this user. Please make sure that the bots highest role is higher than the users highest role.").build()).queue();
            }
        }
    }

}
