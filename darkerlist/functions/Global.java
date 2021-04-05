package darkerlist.functions;

import darkerlist.Getter;
import darkerlist.main;
import darkerlist.objects.Ban;
import darkerlist.objects.Config;
import darkerlist.objects.Report;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class Global {

    public static void ban(User u, Ban ban) throws IOException {
        HashMap<String, Config> guilds = Getter.guilds;
        Getter.banned.add(u.getId());
        Getter.bans.put(u.getId(), ban);
        Getter.saveMaps();
        for (String guild : guilds.keySet()){
            Guild g = main.jda.getGuildById(guild);
            if (g.isMember(u)){
                Config cfg = guilds.get(guild);
                if (cfg.getMode().equalsIgnoreCase("on")){
                    if (guilds.get(g.getId()).getWhiteList().contains(u.getId())){
                        if (cfg.getLogChannel() == null){
                            continue;
                        }
                        if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                            continue;
                        }
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.RED);
                        eb.setTitle("Warning!");
                        eb.setDescription("A recently blacklisted user is whitelisted on the following server.");
                        eb.addField("**Guild**", "Name: " + g.getName() + "\n" +
                                "ID: " + g.getId(), false);
                        eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                                "ID: " + u.getId(), false);
                        Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                    } else {
                        u.openPrivateChannel().complete().sendMessage(Getter.getEmbed(Getter.BAN, u, "You were banned from the server " + g.getName() + ". Dm " + Getter.AUTHOR.getAsTag() + " to appeal for the ban.", ban).build()).queue();
                        g.ban(g.getMember(u), 7, ban.getReason()).queue();
                        if (cfg.getLogChannel() == null){
                            continue;
                        }
                        if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                            continue;
                        }
                        if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) != null) {
                            main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(main.jda.getRoleById(cfg.getPingrole()).getAsMention()).embed(Getter.getEmbed(Getter.BAN, u, "A recently blacklisted user was banned from your server!", ban).build()).queue();
                            continue;
                        } else if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) == null){
                            cfg.setPingrole(null);
                        }
                        main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.BAN, u, "A recently blacklisted user was banned from your server!", ban).build()).queue(); }
                } else if (cfg.getMode().equalsIgnoreCase("passive")){
                    if (cfg.getLogChannel() == null){
                        continue;
                    }
                    if (guilds.get(g.getId()).getWhiteList().contains(u)){
                        if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                            continue;
                        }
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.RED);
                        eb.setTitle("Warning!");
                        eb.setDescription("A recently blacklisted user is whitelisted on the following server.");
                        eb.addField("**Guild**", "Name: " + g.getName() + "\n" +
                                "ID: " + g.getId(), false);
                        eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                                "ID: " + u.getId(), false);
                        Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                        if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) != null){
                            main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(main.jda.getRoleById(cfg.getPingrole()).getAsMention()).embed(Getter.getEmbed(Getter.WARN, u, "A user on your server was blacklisted!", ban).build()).queue(message -> {
                                message.addReaction("\u2705").queue();
                                message.addReaction("\u274C").queue();
                            });
                            continue;
                        } else if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) == null){
                            cfg.setPingrole(null);
                        }
                        main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, u, "A user on your server was blacklisted!", ban).build()).queue(message -> {
                            message.addReaction("\u2705").queue();
                            message.addReaction("\u274C").queue();
                        });
                    } else {
                        if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) != null){
                            main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(main.jda.getRoleById(cfg.getPingrole()).getAsMention()).embed(Getter.getEmbed(Getter.WARN, u, "A user on your server was blacklisted!", ban).build()).queue(message -> {
                                message.addReaction("\u2705").queue();
                                message.addReaction("\u274C").queue();
                            });
                            continue;
                        } else if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) == null){
                            cfg.setPingrole(null);
                        }
                        main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, u, "A user on your server was blacklisted!", ban).build()).queue(message -> {
                            message.addReaction("\u2705").queue();
                            message.addReaction("\u274C").queue();
                        });
                    }
                }
            }
        }
    }

    public static void unban(User u) throws IOException {
        HashMap<String, Config> guilds = Getter.guilds;
        Getter.banned.remove(u.getId());
        Getter.bans.remove(u.getId());
        Getter.saveMaps();
        for (String guild : guilds.keySet()){
            if (main.jda.getGuildById(guild) != null){
                Config cfg = Getter.guilds.get(guild);
                Guild g = main.jda.getGuildById(guild);
                try {
                    g.unban(u).queue();
                    if (cfg.getLogChannel() == null){
                        continue;
                    }
                    if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                        continue;
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Unban");
                    eb.setColor(Color.green);
                    eb.addField("User","Name: " + u.getAsTag() + "\n" +
                            "ID: " + u.getId(),false);
                    eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                    main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(eb.build()).queue();
                } catch (Exception e){
                }
            }
        }
    }

    public static void warn(String warning){
        HashMap<String, Config> guilds = Getter.guilds;
        for (String guild : guilds.keySet()){
            Config cfg = guilds.get(guild);
            if (cfg.getLogChannel() != null && !cfg.getMode().equalsIgnoreCase("off")){
                if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                    continue;
                }
                main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, warning).build()).queue();
            }
        }
    }

    public static void warn(User u, Report report){
        HashMap<String, Config> guilds = Getter.guilds;
        for (String guild : guilds.keySet()){
            Config cfg = guilds.get(guild);
            if (cfg.getLogChannel() != null && !cfg.getMode().equalsIgnoreCase("off")){
                if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                    continue;
                }
                if (main.jda.getGuildById(guild).isMember(u)){
                    main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(Getter.getEmbed(Getter.WARN, u, "The following user was reported.", report).build()).queue();
                }
            }
        }
    }

    public static void announce(String announcement){
        HashMap<String, Config> guilds = Getter.guilds;
        for (String guild : guilds.keySet()){
            Config cfg = guilds.get(guild);

            if (cfg.getLogChannel() != null && !cfg.getMode().equalsIgnoreCase("off")){
                if (main.jda.getTextChannelById(cfg.getLogChannel()) == null) {
                    continue;
                }
                System.out.println(main.jda.getGuildById(guild).getName());
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Announcement");
                eb.setColor(Color.lightGray);
                eb.setDescription("This is a public announcement by the coder. Please take note that these announcements are important.");
                eb.addField("Information", announcement, false);
                eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) != null) {
                    main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(main.jda.getRoleById(cfg.getPingrole()).getAsMention()).embed(eb.build()).queue();
                    continue;
                } else if (cfg.getPingrole() != null && main.jda.getRoleById(cfg.getPingrole()) == null){
                    cfg.setPingrole(null);
                }
                main.jda.getTextChannelById(cfg.getLogChannel()).sendMessage(eb.build()).queue();
            }
        }

    }
}
