package darkerlist;


import darkerlist.objects.Ban;
import darkerlist.objects.Config;
import darkerlist.objects.Report;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Getter {
    public static final User AUTHOR = main.jda.retrieveUserById("705786757144379463").complete();
    public static HashMap<String, Config> guilds;
    public static List<String> banned;
    public static List<Guild> checked;
    public static HashMap<String, Ban> bans;
    public static HashMap<String, Report> reports;
    public static HashMap<String, String> bannedGuilds;

    public static String PREFIX = "$";

    public static int ERROR = 0;
    public static int SUCCESS = 1;
    public static int UNKNOWN = 2;
    public static int WARN = 3;
    public static int BAN = 4;


    public static EmbedBuilder getEmbed(int i, String desc){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setFooter("Found a bug? Please report it to me: " + AUTHOR.getAsTag(), AUTHOR.getAvatarUrl());
        eb.setDescription(desc);
        switch(i) {
            case 0:
                eb.setTitle("Error");
                eb.setColor(Color.RED);
                return eb;
            case 1:
                eb.setTitle("Success");
                eb.setColor(Color.green);
                return eb;
            case 2:
                eb.setTitle("Unknown");
                eb.setColor(Color.yellow);
                return eb;
            case 3:
                eb.setTitle("Warning!");
                eb.setColor(Color.yellow);
                return eb;
        }

        return null;
    }

    public static EmbedBuilder getEmbed(int i, User u, String desc, Ban ban){
        if (i == 3){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setFooter("Found a bug? Please report it to me: " + AUTHOR.getAsTag(), AUTHOR.getAvatarUrl());
            eb.setColor(Color.YELLOW);
            eb.setTitle("Warning!");
            eb.setDescription(desc);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                    "ID: " + u.getId() + "\n" +
                    "Reason: " + ban.getReason() + "\n" +
                    "Date: " + format.format(ban.getDate()) + "\n" +
                    "Proof: " + ban.getProof() + "\n", false);
            return eb;
        } else if (i == 4){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setFooter("Found a bug? Please report it to me: " + AUTHOR.getAsTag(), AUTHOR.getAvatarUrl());
            eb.setColor(Color.RED);
            eb.setTitle("Ban");
            eb.setDescription(desc);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                    "ID: " + u.getId() + "\n" +
                    "Reason: " + ban.getReason() + "\n" +
                    "Date: " + format.format(ban.getDate()) + "\n" +
                    "Proof: " + ban.getProof() + "\n", false);
            return eb;
        }
        return null;
    }

    public static EmbedBuilder getEmbed(int i, User u, String desc, Report report){
        if (i == 3){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setFooter("Found a bug? Please report it to me: " + AUTHOR.getAsTag(), AUTHOR.getAvatarUrl());
            eb.setColor(Color.YELLOW);
            eb.setTitle("Warning!");
            eb.setDescription(desc);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                    "ID: " + u.getId() + "\n" +
                    "Reason: " + report.getReason() + "\n" +
                    "Date: " + format.format(report.getDate()) + "\n" +
                    "Proof: " + report.getProof() + "\n" +
                    "Additional info: " + report.getAdditional(), false);
            return eb;
        } else if (i == 4){
            EmbedBuilder eb = new EmbedBuilder();

            eb.setColor(Color.RED);
            eb.setTitle("Ban");
            eb.setDescription(desc);
            eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                    "ID: " + u.getId(), false);
            return eb;
        }
        return null;
    }

    public static EmbedBuilder getReport(User u, Report report){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Report");
        eb.setColor(Color.lightGray);
        eb.setDescription("Incoming report, Mutual guilds: " + main.jda.getMutualGuilds(u));
        eb.setFooter("Found a bug? Please report it to me: " + AUTHOR.getAsTag(), AUTHOR.getAvatarUrl());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        eb.addField("**User**", "Name: " + u.getAsTag() + "\n" +
                "ID: " + u.getId() + "\n" +
                "Reason: " + report.getReason() + "\n" +
                "Date: " + format.format(report.getDate()) + "\n" +
                "Proof: " + report.getProof() + "\n" +
                "Additional info: " + report.getAdditional(), false);
        return eb;
    }

    public static void loadMaps() throws IOException, ClassNotFoundException {
        File file = new File("\\DarkerList\\configs.yml");
        FileInputStream fis = new FileInputStream(file);

        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            Getter.guilds = (HashMap<String, Config>) ois.readObject();
            for (Guild g : main.jda.getGuilds()){
                if (!Getter.guilds.containsKey(g.getId())){
                    Config cfg = new Config("off", null, new ArrayList<String>(), null);
                    Getter.guilds.put(g.getId(), cfg);
                }
            }
            ois.close();
            fis.close();
        } catch (EOFException e){
            Getter.guilds = new HashMap<String, Config>();
            for (Guild g : main.jda.getGuilds()){
                if (!Getter.guilds.containsKey(g.getId())){
                    Config cfg = new Config("off", null, new ArrayList<String>(), null);
                    Getter.guilds.put(g.getId(), cfg);
                }
            }
            fis.close();
        }
        file = new File("\\DarkerList\\bans.yml");
        fis = new FileInputStream(file);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            Getter.bans = (HashMap<String, Ban>) ois.readObject();
            ois.close();
            fis.close();
        } catch (EOFException e){
            Getter.bans = new HashMap<String, Ban>();
            fis.close();
        }
        if (bans.isEmpty() || bans == null) {
            Getter.banned = new ArrayList<String>();
        } else {
            Getter.banned = new ArrayList<String>();
            Getter.banned.addAll(Getter.bans.keySet());
        }
        file = new File("\\DarkerList\\reports.yml");
        fis = new FileInputStream(file);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            Getter.reports = (HashMap<String, Report>) ois.readObject();
            ois.close();
            fis.close();
        } catch (EOFException e){
            Getter.reports = new HashMap<String, Report>();
            fis.close();
        }
        file = new File("\\DarkerList\\guildbans.yml");
        fis = new FileInputStream(file);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            Getter.bannedGuilds = (HashMap<String, String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (EOFException e){
            Getter.bannedGuilds = new HashMap<String, String>();
            fis.close();
        }
        Getter.checked = new ArrayList<Guild>();
    }

    public static void saveMaps() throws IOException {
        File file = new File("\\DarkerList\\configs.yml");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(Getter.guilds);

        oos.close();
        fos.close();

        file = new File("\\DarkerList\\bans.yml");
        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(Getter.bans);

        oos.close();
        fos.close();

        file = new File("\\DarkerList\\reports.yml");
        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(Getter.reports);

        oos.close();
        fos.close();

        file = new File("\\DarkerList\\guildbans.yml");
        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(Getter.bannedGuilds);

        oos.close();
        fos.close();

        file = new File("\\DarkerList\\tokens.yml");
        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(main.tokens);

        oos.close();
        fos.close();
    }

    public static void saveConfigs() throws IOException {
        File file = new File("\\DarkerList\\configs.yml");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(Getter.guilds);

        oos.close();
        fos.close();
    }

    public static void saveReports() throws IOException {
        File file = new File("\\DarkerList\\reports.yml");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(Getter.reports);

        oos.close();
        fos.close();
    }

    public static void checkFiles() throws IOException {
        File file1 = new File("\\DarkerList\\");
        File file2 = new File("\\DarkerList\\configs.yml");
        File file3 = new File("\\DarkerList\\bans.yml");
        File file4 = new File("\\DarkerList\\reports.yml");
        File file5 = new File("\\DarkerList\\guildbans.yml");

        if (!file1.exists()){
            if (file1.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory failed");
            }
        }
        if (!file2.exists()){
            file2.createNewFile();
        }
        if (!file3.exists()){
            file3.createNewFile();
        }
        if (!file4.exists()){
            file4.createNewFile();
        }
        if (!file5.exists()){
            file5.createNewFile();
        }
    }

}
