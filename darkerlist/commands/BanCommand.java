package darkerlist.commands;

import darkerlist.Getter;
import darkerlist.main;
import darkerlist.objects.Ban;
import darkerlist.objects.Report;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


public class BanCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().toLowerCase().split("\\s+");
        if (e.getMessage().getContentRaw().toLowerCase().equalsIgnoreCase(Getter.PREFIX + "bans") || args[0].equalsIgnoreCase(Getter.PREFIX + "blacklist")) {
            if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                List<String> banned = Getter.banned;
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Blacklist");
                eb.setDescription("The total amount of blacklisted users is at: **" + banned.size() + "**");
                eb.setColor(Color.lightGray);
                eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                e.getChannel().sendMessage(eb.build()).queue();
            }
        } else if (e.getMessage().getContentRaw().startsWith(Getter.PREFIX + "guildbans")){
            if (e.getMember().hasPermission(Permission.ADMINISTRATOR)){
                EmbedBuilder eb = new EmbedBuilder();
                String guilds = "";
                for (String id : Getter.bannedGuilds.keySet()){
                    guilds = guilds + "\n" + Getter.bannedGuilds.get(id) + ": " + id;
                }
                eb.setColor(Color.lightGray);
                eb.setTitle("Guild Blacklist");
                eb.setDescription("This is a list of guilds that im not allowed to join!");
                eb.addField("Guilds", guilds, false);
                eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                e.getChannel().sendMessage(eb.build()).queue();
            }
        } else if (e.getMessage().getContentRaw().startsWith(Getter.PREFIX + "info") && args.length == 2){
            if (e.getMember().hasPermission(Permission.ADMINISTRATOR)){
                HashMap<String, Ban> bans = Getter.bans;
                HashMap<String, Report> reports = Getter.reports;
                if (e.getJDA().retrieveUserById(args[1]).complete() != null){
                    if (bans.get(args[1]) != null ){
                        Ban ban = bans.get(args[1]);
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                        eb.setColor(Color.RED);
                        eb.setTitle("Info");
                        eb.setDescription("This user is blacklisted. We highly suggest you to do a security check, if you find this user on your server.");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        eb.addField("**User**", "Name: " + e.getJDA().retrieveUserById(args[1]).complete().getAsTag() + "\n" +
                                "ID: " + args[1]+ "\n" +
                                "Ban-Status: Banned \n" +
                                "Reason: " + ban.getReason() + "\n" +
                                "Date: " + format.format(ban.getDate()) + "\n" +
                                "Proof: " + ban.getProof() + "\n", false);
                        e.getChannel().sendMessage(eb.build()).queue();
                    } else if (reports.get(args[1]) != null){
                        Report report = reports.get(args[1]);
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                        eb.setColor(Color.YELLOW);
                        eb.setTitle("Info");
                        eb.setDescription("This user was just reported. Don't panic he'll get banned, if he is guilty.");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        eb.addField("**User**", "Name: " + e.getJDA().retrieveUserById(args[1]).complete().getAsTag() + "\n" +
                                "ID: " + args[1] + "\n" +
                                "Ban-Status: under Investigation \n" +
                                "Reason: " + report.getReason() + "\n" +
                                "Date: " + format.format(report.getDate()) + "\n" +
                                "Proof: " + report.getProof() + "\n" +
                                "Additional info: " + report.getAdditional(), false);
                        e.getChannel().sendMessage(eb.build()).queue();
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                        eb.setColor(Color.green);
                        eb.setTitle("Info");
                        eb.setDescription("Don't worry, this user is clean.");
                        eb.addField("**User**", "Name: " + e.getJDA().retrieveUserById(args[1]).complete().getAsTag() + "\n" +
                                "ID: " + args[1] + "\n" +
                                "Ban-Status: Not banned" + "\n", false);
                        e.getChannel().sendMessage(eb.build()).queue();
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase(Getter.PREFIX + "ban") && args.length == 4){
            if (e.getAuthor().equals(Getter.AUTHOR) &&  e.getJDA().retrieveUserById(args[2]).complete() != null && Getter.bans.get(args[2]) != null){
                User u = e.getJDA().retrieveUserById(args[2]).complete();
                Ban ban = Getter.bans.get(args[2]);
                if (args[1].equalsIgnoreCase("setproof")){
                    ban.setProof(args[3]);
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Proof has been set to " + args[3] + ".").build()).queue();
                } else if (args[1].equalsIgnoreCase("setreason")){
                    ban.setReason(args[3]);
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Reason has been set to " + args[3] + ".").build()).queue();
                }
                try {
                    Getter.saveMaps();
                } catch (IOException x) {
                    main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                    x.printStackTrace();
                }
            }
        }

    }

}
