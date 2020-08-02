// Main.java: Main class for config-parser project
public class Main {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String env = null;
        if (args.length > 0) env = args[0];
        ConfigParser config = new ConfigParser("config.txt", env);

        System.out.println(config.get("application.name"));
        System.out.println(config.get("dbname"));
    }
}
