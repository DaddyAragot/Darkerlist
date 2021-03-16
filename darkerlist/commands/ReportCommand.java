package darkerlist.commands;

import darkerlist.Getter;
import darkerlist.functions.Global;
import darkerlist.main;
import darkerlist.objects.Report;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReportCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if (e.getMessage().getContentRaw().toLowerCase().startsWith(Getter.PREFIX + "report") && e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            String[] args = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 5);
            if (args.length >= 4){
                if (args[0].equalsIgnoreCase(Getter.PREFIX + "report")){
                    if (e.getJDA().retrieveUserById(args[1]).complete() != null){
                        User u = e.getJDA().retrieveUserById(args[1]).complete();
                        String reason = args[2];
                        String proof = args[3];
                        String additional = "";
                        if (args.length >= 5){
                            additional = args[4];
                        }
                        Date date = new Date();
                        Report report = new Report(reason, proof, date, additional);
                        Getter.reports.put(args[1], report);
                        try {
                            Getter.saveReports();
                            Global.warn(u, report);
                            Getter.AUTHOR.openPrivateChannel().complete().sendMessage(Getter.getReport(u, report).build()).queue();
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The user was successfully reported!").build()).queue();
                        } catch (IOException x) {
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }
                    }
                }
            } else if (e.getAuthor().equals(Getter.AUTHOR) && args.length == 3){
                if (args[0].equalsIgnoreCase(Getter.PREFIX + "report") && args[1].equalsIgnoreCase("get")){
                    if (main.jda.retrieveUserById(args[2]).complete() != null){
                        if (Getter.reports.get(args[2]) != null){
                            Report report = Getter.reports.get(args[2]);
                            e.getChannel().sendMessage(Getter.getReport(main.jda.retrieveUserById(args[2]).complete(), report).build()).queue();
                        }
                    }
                } else if (args[0].equalsIgnoreCase(Getter.PREFIX + "report") && args[1].equalsIgnoreCase("remove")){
                    if (main.jda.retrieveUserById(args[2]).complete() != null){
                        if (Getter.reports.get(args[2]) != null){
                            Getter.reports.remove(args[2]);
                            try {
                                Getter.saveConfigs();
                                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The report was successfully removed!").build()).queue();
                            } catch (IOException x) {
                                main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                                x.printStackTrace();
                            }
                        }
                    }
                }
            } else if (e.getAuthor().equals(Getter.AUTHOR) && args.length == 2){
                if (args[0].equalsIgnoreCase(Getter.PREFIX +"report") && args[1].equalsIgnoreCase("list")){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Reports");
                    eb.setDescription("This is a list of all active reports.");
                    eb.setColor(Color.lightGray);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                    if (Getter.reports.isEmpty()){
                        eb.addField("Nothing!", "All reports have been cleared.", false);
                        e.getChannel().sendMessage(eb.build()).queue();
                        return;
                    }
                    for (String id : Getter.reports.keySet()){
                        try {
                            System.out.println(Getter.reports.keySet());
                            User u = main.jda.retrieveUserById(id).complete();
                            Report report = Getter.reports.get(id);
                            eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                                    "ID: " + u.getId() + "\n" +
                                    "Reason: " + report.getReason() + "\n" +
                                    "Date: " + format.format(report.getDate()) + "\n" +
                                    "Proof: " + report.getProof() + "\n" +
                                    "Additional info: " + report.getAdditional(), false);
                        } catch (Exception x){
                            x.printStackTrace();
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        }
                    }
                    e.getChannel().sendMessage(eb.build()).queue();
                } else if (args[0].equalsIgnoreCase(Getter.PREFIX +"report") && args[1].equalsIgnoreCase("clearlist")) {
                    Getter.reports = new HashMap<String, Report>();
                    try {
                        Getter.saveReports();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The report list was cleared!").build()).queue();
                }
            }
        }
    }

}
