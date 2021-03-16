package darkerlist.commands;

import darkerlist.Getter;
import darkerlist.functions.Global;
import darkerlist.main;
import darkerlist.objects.Ban;
import darkerlist.objects.Config;
import darkerlist.objects.Report;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GlobalCommand extends ListenerAdapter {

    /*
    Global ban
    Global warn
    Global announce
     */

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getMessage().getContentRaw().startsWith(Getter.PREFIX + "global") && e.getAuthor().equals(Getter.AUTHOR)) {
            String[] args = e.getMessage().getContentRaw().toLowerCase().split("\\s+");
            if (args.length == 2 && args[1].equalsIgnoreCase("guilds")){
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.lightGray);
                eb.setDescription("This is a list of all guilds I'm in!");
                List<Guild> guilds = main.jda.getGuilds();
                HashMap<String, Config> guild = Getter.guilds;
                String names = "";
                for (Guild g : guilds){
                    names = names + "\n" + g.getName() + ": " + g.getId();
                }
                String keyset = "";
                for (String id : guild.keySet()){
                    keyset = keyset + "\n" + main.jda.getGuildById(id).getName() + ": " + id;
                }
                eb.setTitle("Guild List");
                eb.addField("Actual guilds",names, false);
                eb.addField("Saved HashMap", keyset, false);
                Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                e.getMessage().addReaction("\u2705").queue();

            } else if (args.length == 3){
                if (args[1].equalsIgnoreCase("ban")){
                    if (e.getJDA().retrieveUserById(args[2]).complete() != null){
                        Report report = Getter.reports.get(args[2]);
                        if (report == null){
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This report was not found!").build()).queue();
                            return;
                        }
                        Ban ban = new Ban();
                        ban.setDate(report.getDate());
                        ban.setProof(report.getProof());
                        ban.setReason(report.getReason());
                        Getter.reports.remove(args[2]);
                        try {
                            Global.ban(e.getJDA().retrieveUserById(args[2]).complete(), ban);
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "This user was banned!").build()).queue();
                        } catch (Exception x){
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }

                    }
                } else if (args[1].equalsIgnoreCase("unban")){
                    if (e.getJDA().retrieveUserById(args[2]).complete() != null){
                        try {
                            Getter.saveMaps();
                            Global.unban(main.jda.retrieveUserById(args[2]).complete());
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "This user was unbanned!").build()).queue();
                        } catch (Exception x){
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }

                    }
                } else if (args[1].equalsIgnoreCase("guildban")){
                    if (e.getJDA().getGuildById(args[2]) != null){
                        if (Getter.bannedGuilds.get(args[2]) == null){
                            Getter.bannedGuilds.put(args[2], e.getJDA().getGuildById(args[2]).getName());
                            e.getJDA().getGuildById(args[2]).leave().queue();
                            try {
                                Getter.saveMaps();
                                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The bot left and banned the guild.").build()).queue();
                            } catch (IOException x) {
                                main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                                x.printStackTrace();
                            }
                        } else {
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This Guild is already banned!").build()).queue();
                        }
                    }
                } else if (args[1].equalsIgnoreCase("guildunban")){
                    if (Getter.bannedGuilds.get(args[2]) != null){
                        Getter.bannedGuilds.remove(args[2]);
                        try {
                            Getter.saveMaps();
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The guild has been unbanned").build()).queue();
                        } catch (IOException x) {
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }
                    } else {
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This guild is not banned").build()).queue();
                    }
                }
            } else if (args.length == 4) {
                if (args[1].equalsIgnoreCase("massadd")) {
                    Path path = Paths.get("//DarkerList//massadd.yml");
                    try {
                        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                        for (String id : lines){
                            System.out.println(id);
                            Ban ban = new Ban();
                            ban.setReason(args[2]);
                            ban.setProof(args[3]);
                            ban.setDate(new Date());
                            Global.ban(e.getJDA().retrieveUserById(id).complete(), ban);
                        }
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Added all ID's to the blacklist. \n" +
                                "Reason: " + args[2] + "\n" +
                                "Proof: " + args[3]).build()).queue();
                    } catch (Exception x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                }
            } else if (args.length >= 3){
                if (args[1].equalsIgnoreCase("warn")){
                    String[] messageArray = e.getMessage().getContentRaw().split("\\s+", 3);
                    Global.warn(messageArray[2]);
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "All guilds have been warned!").build()).queue();
                } else if (args[1].equalsIgnoreCase("announce")){
                    String[] messageArray = e.getMessage().getContentRaw().split("\\s+", 3);
                    Global.announce(messageArray[2]);
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Announcement has been sent!").build()).queue();
                } else if (args[1].equalsIgnoreCase("ban")){
                    String[] cmd = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 6);
                    Ban ban = new Ban();
                    ban.setDate(new Date());
                    ban.setProof(args[4]);
                    ban.setReason(args[3]);
                    try {
                        Global.ban(e.getJDA().retrieveUserById(args[2]).complete(), ban);
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "This user was banned!").build()).queue();
                    } catch (Exception x){
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }

                }
            }
        }
    }
}
