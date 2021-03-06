package darkerlist;

import darkerlist.commands.*;
import darkerlist.listeners.BotEvent;
import darkerlist.listeners.JoinEvent;
import darkerlist.listeners.ReactionEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Scanner;

public class main{

    /*
       Donator Role/Premium version
     */

    public static HashMap<String, String> tokens;
    public static JDA jda;
    public static void checkTokens() throws IOException {
        File file = new File("//DarkerList//");
        if (file.mkdir()){
            System.out.println("Directory created");
        }
        file = new File("//DarkerList//tokens.yml");
        if (!file.exists()){
            file.createNewFile();
        }
    }
    public static void loadTokens() throws IOException {
        File file = new File("//DarkerList//tokens.yml");
        FileInputStream fis = new FileInputStream(file);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            tokens = (HashMap<String, String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (EOFException | ClassNotFoundException e){
            tokens = new HashMap<String, String>();
            fis.close();
        }
    }
    public static void saveTokens() throws IOException {
        File file = new File("//DarkerList//tokens.yml");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(main.tokens);
        oos.close();
        fos.close();
    }
    public static void main (String [] args){

        try {
            checkTokens();
            loadTokens();
            if (tokens.get("dev") == null  || tokens.get("bot") == null) {
                System.out.println("Please enter the bot token: ");
                Scanner sc = new Scanner(System.in);
                String input = sc.nextLine();
                tokens.put("bot", input);
                System.out.println("Please enter the DEV bot token: ");
                String input2 = sc.nextLine();
                tokens.put("dev", input2);
                sc.close();
                saveTokens();
            }
            main.jda = JDABuilder.createDefault(tokens.get("bot"), EnumSet.allOf(GatewayIntent.class)).setMemberCachePolicy(MemberCachePolicy.ALL).build();
            jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            jda.addEventListener(new HelpCommand());
            jda.addEventListener(new BotEvent());
            jda.addEventListener(new SecurityCommand());
            jda.addEventListener(new ConfigCommand());
            jda.addEventListener(new ReportCommand());
            jda.addEventListener(new GlobalCommand());
            jda.addEventListener(new ReactionEvent());
            jda.addEventListener(new BanCommand());
            jda.addEventListener(new JoinEvent());
            Getter.checkFiles();
            Getter.loadMaps();
            Getter.saveConfigs();
            jda.getPresence().setActivity(Activity.watching("for blacklisted users on " + Getter.guilds.size() + " Servers"));

        } catch (Exception e) {
            e.printStackTrace();
            jda.openPrivateChannelById("705786757144379463").complete().sendMessage("Error: " + e.getMessage()).queue();
        }
    }
}
