package darkerlist.listeners;

import darkerlist.Getter;
import darkerlist.functions.Global;
import darkerlist.functions.Moderation;
import darkerlist.objects.Ban;
import darkerlist.objects.Config;
import darkerlist.objects.Report;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e){
        if (e.getUser() != e.getJDA().getSelfUser()){
            Config cfg = Getter.guilds.get(e.getGuild().getId());
            if (e.getChannel().getId().equalsIgnoreCase(cfg.getLogChannel())){
                if (e.retrieveMessage().complete().getAuthor() == e.getJDA().getSelfUser()){
                    if (e.retrieveMessage().complete().getEmbeds().get(0).getTitle().equalsIgnoreCase("Warning!")){
                        String[] args = e.retrieveMessage().complete().getEmbeds().get(0).getFields().get(0).getValue().split("\n");
                        if (args[1].startsWith("ID:")){
                            String[] id = args[1].split("\\s+");
                            if (e.getReactionEmote().getEmoji().equalsIgnoreCase("\u2705")){
                                Moderation.ban(e.getGuild(), e.getJDA().retrieveUserById(id[1]).complete(), Getter.bans.get(id[1]), false);
                                e.retrieveMessage().complete().delete().queue();
                            } else if (e.getReactionEmote().getEmoji().equalsIgnoreCase("\u274C")){
                                e.retrieveMessage().complete().delete().queue();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e){
        if (e.getUser() != e.getJDA().getSelfUser()){
            if (e.retrieveMessage().complete().getAuthor() == e.getJDA().getSelfUser()){
                if (e.retrieveMessage().complete().getEmbeds().get(0).getTitle().equalsIgnoreCase("Report")){
                    String[] args = e.retrieveMessage().complete().getEmbeds().get(0).getFields().get(0).getValue().split("\n");
                    String[] id = args[1].split("\\s+");
                    if (e.getReactionEmote().getEmoji().equalsIgnoreCase("\u2705")){
                        if (!Getter.banned.contains(id[1])){
                            Report report = Getter.reports.get(id[1]);
                            Ban ban = new Ban();
                            ban.setDate(report.getDate());
                            ban.setReason(report.getReason());
                            ban.setProof(report.getProof());
                            try {
                                Global.ban(e.getJDA().retrieveUserById(id[1]).complete(), ban);
                                Getter.reports.remove(id[1]);
                                e.getChannel().sendMessage(Getter.getEmbed(Getter.SUCCESS, "This user was successfully banned.").build()).queue();
                            } catch (IOException x) {
                                x.printStackTrace();
                            }
                        } else { //already banned
                            e.getChannel().sendMessage(Getter.getEmbed(Getter.ERROR, "This user is already banned").build()).queue();
                        }
                        e.retrieveMessage().complete().delete().queue();
                    } else if (e.getReactionEmote().getEmoji().equalsIgnoreCase("\u274C")){
                        if (Getter.reports.get(id[1]) != null){
                            Getter.reports.remove(id[1]);
                        }
                        e.retrieveMessage().complete().delete().queue();
                    }
                }
            }
        }


    }

}
