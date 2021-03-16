package darkerlist.commands;

import darkerlist.Getter;
import darkerlist.main;
import darkerlist.objects.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class HelpCommand extends ListenerAdapter {
    /*
        report
        globalunban
        globalban
        whitelist
        servercheck
        mode (Passive ban -> report in logs, off, active)
        set Log channel
        check
     */

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith(Getter.PREFIX)){
            return;
        }
        if (e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            if (e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "help")){
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("\u2753 Help");
                eb.setColor(Color.darkGray);
                eb.setDescription("Note: You have to be an administrator to use these commands!");
                eb.addField("**Report**", "Usage: $report <UserID> <Reason> <Link of proof> <additional Information> \n" +
                        "\n Information: Please upload your screenshots on https://prnt.sc. Use ONE word as the reason (Example: \"Dm_Advert\"). You CAN add important information after the link. \n" +
                        "\n Example: $report 705786757144379463 Dm_Advert https://prnt.sc/ab1234 He's a moderator on ####'s Server, here is some more proof: https://prnt.sc/cd5678\n", false);
                eb.addField("**Whitelist**", "Usage: $whitelist <add/remove> <UserID> \n" +
                        "\n Information: Please do not use this command often. The bot is not supposed to stay in a server with banned people. Please report those servers as BotAbuse. \n" +
                        "\n Example: \n" +
                        "$whitelist add 705786757144379463 \n" +
                        "$whitelist remove 705786757144379463\n", false);
                eb.addField("**Mode**", "Usage: $mode <on/off/passive> \n" +
                        "\n Information: \n" +
                        "on -> Bans all blacklisted Users. \n" +
                        "off -> No Warnings, No Bans. \n" +
                        "passive -> Logs all recently joined blacklisted Users in the dedicated Log-Channel. \n" +
                        "\nExample: \n" +
                        "$mode on\n" +
                        "$mode off\n" +
                        "$mode passive\n", false);
                eb.addField("**Logs**", "Usage: $logs <set/remove> <ChannelID>\n" +
                        "\nInformation: The Log-Channel is important and will log every player that gets banned. If there is no dedicated Logs-Channel, the passive mode will work as if it was turned off.\n" +
                        "\nExample: \n" +
                        "$logs set 766031388645261342 \n" +
                        "$logs remove", false);
                eb.addField("**Pingrole**", "Usage: $pingrole <roleMention/roleID/remove>\n" +
                        "\nInformations: The Pingrole is getting pinged, whenever the developer announces or bans users. Removing the Pingrole will stop pinging the role. \n" +
                        "\nExample: \n" +
                        "$pingrole 811210038479028285\n" +
                        "$pingrole @rolename\n" +
                        "$pingrole remove\n", false);
                eb.addField("**Info**" , "Usage: $info <UserID>\n" +
                        "\nInformation: This will check the user for any existing reports/bans. It'll provide you with evidence, if there is any.\n" +
                        "\nExample: $info 705786757144379463", false);
                eb.addField("**Bans**", "Usage: \n " +
                        "$bans\n" +
                        "$blacklist\n" +
                        "$guildbans\n" +
                        "\nInformation: The bans/blacklist command will return you the amount of blacklisted users. The Guildbans command will return you the names and ID's of the banned guilds.\n" +
                        "\nExample: \n" +
                        "$blacklist\n" +
                        "$bans\n" +
                        "$guildbans", false);
                eb.addField("**Security Check**", "Usage: $securitycheck\n" +
                        "\nInformation: This Command will check your server for blacklisted users. \n" +
                        "\nExample: $securitycheck \n", false);
                eb.addField("**About Me**", "Author: " + Getter.AUTHOR.getAsTag() + "\n" +
                        "\nSource Code: https://github.com/DaddyAragot/Darkerlist\n" +
                        "\nDonations: \n" +
                        "\n -Paypal: mtardio925@gmail.com\n" +
                        "\n -Bitcoin: bc1qjxudukw76y7p2t26lvwaee5x4wtwm2mx4w0rdw", false);
                eb.setFooter("Found a bug? Please report it to me: " + Getter.AUTHOR.getAsTag(), Getter.AUTHOR.getAvatarUrl());
                e.getChannel().sendMessage(eb.build()).queue();
            } else if (e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "ping")) {
                e.getChannel().sendMessage("Current ping: " + e.getJDA().getGatewayPing() + "ms").queue();
            } else if (e.getMessage().getContentRaw().equalsIgnoreCase(Getter.PREFIX + "loadmaps") && e.getAuthor().equals(Getter.AUTHOR)){
                for (Guild g : main.jda.getGuilds()){
                    if (!Getter.guilds.containsKey(g.getId())){
                        Config cfg = new Config("off", null, new ArrayList<String>(), null);
                        Getter.guilds.put(g.getId(), cfg);
                    }
                }
                try {
                    Getter.saveMaps();
                    e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "Loaded and saved all Maps successfully").build()).queue();
                } catch (IOException x) {
                    main.jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error in Guild " + e.getGuild().getName() + ":"  + x.getMessage()).queue();
                    x.printStackTrace();
                }
            }
        }
    }
}
