package darkerlist.commands;

import darkerlist.Getter;
import darkerlist.main;
import darkerlist.objects.Ban;
import darkerlist.objects.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ConfigCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().toLowerCase().split("\\s+");
        if (!args[0].startsWith(Getter.PREFIX)){
            return;
        }
        if (e.getMessage().getContentRaw().toLowerCase().startsWith(Getter.PREFIX + "mode")){
            if (args.length == 2 && e.getMember().hasPermission(Permission.ADMINISTRATOR)){
                Config cfg = Getter.guilds.get(e.getGuild().getId());
                if (args[1].equalsIgnoreCase("on")){
                    cfg.setMode("on");
                    try {
                        Getter.saveConfigs();
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The mode was set to \"on\".").build()).queue();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                } else if (args[1].equalsIgnoreCase("off")){
                    cfg.setMode("off");
                    try {
                        Getter.saveConfigs();
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The mode was set to \"off\".").build()).queue();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                } else if (args[1].equalsIgnoreCase("passive")){
                    cfg.setMode("passive");
                    try {
                        Getter.saveConfigs();
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The mode was set to \"passive\".").build()).queue();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                }
            } else if (args.length == 1 && e.getMember().hasPermission(Permission.ADMINISTRATOR)){
                Config cfg = Getter.guilds.get(e.getGuild().getId());
                e.getChannel().sendMessage("My current mode was set to \"" + cfg.getMode() + "\"").queue();
            }
        } else if (e.getMessage().getContentRaw().toLowerCase().startsWith(Getter.PREFIX + "logs") && e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            Config cfg = Getter.guilds.get(e.getGuild().getId());
            if (args.length == 2 && args[1].equalsIgnoreCase("remove")){
                cfg.setLogChannel(null);
                try {
                    Getter.saveConfigs();
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The Log Channel was removed.").build()).queue();
                } catch (IOException x) {
                    main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                    x.printStackTrace();
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("set")){
                if (e.getMessage().getMentionedChannels() != null && !e.getMessage().getMentionedChannels().isEmpty()){
                    List <TextChannel> chs = e.getMessage().getMentionedChannels();
                    TextChannel ch = chs.get(0);
                    if (ch != null){
                        cfg.setLogChannel(ch.getId());
                        try {
                            Getter.saveConfigs();
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The Log Channel was set to " + ch.getAsMention() + ".").build()).queue();
                        } catch (IOException x) {
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }
                    } else {
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "The channel was not found!").build()).queue();
                    }
                } else {
                    try {
                        TextChannel ch = main.jda.getTextChannelById(args[2]);
                        if (ch != null){
                            cfg.setLogChannel(args[2]);
                            try {
                                Getter.saveConfigs();
                                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The Log Channel was set to " + ch.getAsMention() + ".").build()).queue();
                            } catch (IOException x) {
                                main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                                x.printStackTrace();
                            }
                        } else {
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "The channel was not found!").build()).queue();
                        }
                    } catch (Exception x){
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "The channel was not found!").build()).queue();
                    }
                }
            } else if (args.length == 1){
                if (cfg.getLogChannel() == null){
                    e.getChannel().sendMessage("The Log-Channel was not set yet!").queue();
                    return;
                }
                e.getChannel().sendMessage("The current Log Channel is set to " + main.jda.getTextChannelById(cfg.getLogChannel()).getAsMention() + ".").queue();
            }
        } else if (args[0].startsWith(Getter.PREFIX + "whitelist") && e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            Config cfg = Getter.guilds.get(e.getGuild().getId());
            if (args.length == 1) {
                List<String> ids = cfg.getWhiteList();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Whitelist");
                eb.setColor(Color.lightGray);
                eb.setDescription("These users are not affected by the blacklist.");
                if (ids == null || ids.isEmpty()){
                    eb.setDescription("Your whitelist is empty.");
                    e.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
                for (String id : ids){
                    if (Getter.banned.contains(id)){
                        Ban ban = Getter.bans.get(id);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        eb.addField(id, "Mention: " + e.getJDA().retrieveUserById(id).complete().getAsMention() + "\n" +
                                "Ban-Status: Banned\n" +
                                "Reason: " + ban.getReason() + "\n" +
                                "Date: " + format.format(ban.getDate()) + "\n" +
                                "Proof: " + ban.getProof(), false);
                    } else {
                        eb.addField(id, "Mention: " + e.getJDA().retrieveUserById(id).complete().getAsMention() + "\n" +
                                "Ban-Status: Not Banned", false);
                    }
                }
                e.getChannel().sendMessage(eb.build()).queue();
            } else if (args.length == 3){
                if (args[1].equalsIgnoreCase("add")){
                    if (e.getMessage().getMentionedMembers() != null && !e.getMessage().getMentionedMembers().isEmpty()){
                        List<String> whitelist = cfg.getWhiteList();
                        String id = e.getMessage().getMentionedMembers().get(0).getId();
                        whitelist.add(id);
                        if (Getter.bans.get(id) != null){
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setColor(Color.RED);
                            eb.setTitle("Warning!");
                            eb.setDescription("A blacklisted user is whitelisted on the following server.(Join)");
                            eb.addField("**Guild**", "Name: " + e.getGuild().getName() + "\n" +
                                    "ID: " + e.getGuild().getId(), false);
                            eb.addField("**User**", "Name: " + e.getJDA().retrieveUserById(id).complete().getAsTag() + "\n" +
                                    "ID: " + id, false);
                            Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                        } else if (Getter.reports.get(id) != null){
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setColor(Color.RED);
                            eb.setTitle("Warning!");
                            eb.setDescription("A blacklisted user is whitelisted on the following server.(Join)");
                            eb.addField("**Guild**", "Name: " + e.getGuild().getName() + "\n" +
                                    "ID: " + e.getGuild().getId(), false);
                            eb.addField("**User**", "Name: " + e.getJDA().retrieveUserById(id).complete().getAsTag() + "\n" +
                                    "ID: " + id, false);
                            Getter.AUTHOR.openPrivateChannel().complete().sendMessage(eb.build()).queue();
                        }
                        cfg.setWhiteList(whitelist);
                        try {
                            Getter.saveConfigs();
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The user was added to the whitelist.").build()).queue();
                        } catch (IOException x) {
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }
                    } else {
                        List<String> whitelist = cfg.getWhiteList();
                        if (main.jda.retrieveUserById(args[2]) == null){
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This user was not found!").build()).queue();
                            return;
                        }
                        String id = args[2];
                        if (whitelist == null){
                            whitelist = new ArrayList<String>();
                        }
                        whitelist.add(id);
                        cfg.setWhiteList(whitelist);
                        try {
                            Getter.saveConfigs();
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The user was added to the whitelist.").build()).queue();
                        } catch (IOException x) {
                            main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                            x.printStackTrace();
                        }
                    }
                } else if (args[1].equalsIgnoreCase("remove")){
                    if (e.getMessage().getMentionedMembers() != null && !e.getMessage().getMentionedMembers().isEmpty()){
                        List<String> whitelist = cfg.getWhiteList();
                        String id = e.getMessage().getMentionedMembers().get(0).getId();
                        if (whitelist.contains(id)){
                            whitelist.remove(id);
                            cfg.setWhiteList(whitelist);
                            try {
                                Getter.saveConfigs();
                                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The user was removed to the whitelist.").build()).queue();
                            } catch (IOException x) {
                                main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                                x.printStackTrace();
                            }
                        } else {
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This user is not in the whitelist!").build()).queue();
                        }

                    } else {
                        List<String> whitelist = cfg.getWhiteList();
                        if (main.jda.retrieveUserById(args[2]) == null){
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This user was not found!").build()).queue();
                            return;
                        }
                        String id = args[2];
                        if (whitelist.contains(id)){
                            whitelist.remove(id);
                            cfg.setWhiteList(whitelist);
                            try {
                                Getter.saveConfigs();
                                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The user was removed to the whitelist.").build()).queue();
                            } catch (IOException x) {
                                main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                                x.printStackTrace();
                            }
                        } else {
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This user is not in the whitelist!").build()).queue();
                        }
                    }
                }
            }
        } else if (e.getMember().hasPermission(Permission.ADMINISTRATOR) && args[0].equalsIgnoreCase(Getter.PREFIX + "pingrole")){
            Config cfg = Getter.guilds.get(e.getGuild().getId());
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("remove")){
                    cfg.setPingrole(null);
                    try {
                        Getter.saveConfigs();
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The Pingrole was removed.").build()).queue();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                } else if (e.getMessage().getMentionedRoles() != null && !e.getMessage().getMentionedRoles().isEmpty()){
                    String roleid = e.getMessage().getMentionedRoles().get(0).getId();
                    cfg.setPingrole(roleid);
                    try {
                        Getter.saveConfigs();
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The Pingrole has been set to " + e.getJDA().getRoleById(roleid).getAsMention() + ".").build()).queue();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                } else if (e.getJDA().getRoleById(args[1]) != null){
                    cfg.setPingrole(args[1]);
                    try {
                        Getter.saveConfigs();
                        e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "The Pingrole has been set to " + e.getJDA().getRoleById(args[1]).getAsMention() + ".").build()).queue();
                    } catch (IOException x) {
                        main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                        x.printStackTrace();
                    }
                }
            } else if (args.length == 1){
                if (cfg.getPingrole() != null){
                    e.getChannel().sendMessage("The current Pingrole is " + e.getJDA().getRoleById(cfg.getPingrole()).getAsMention() + ".").queue();
                } else {
                    e.getChannel().sendMessage("The Pingrole was not set yet.").queue();
                }
            }

        }
    }

}
