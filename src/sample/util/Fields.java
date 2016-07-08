package sample.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fields {
    private static Pattern pattern;
    private static Matcher matcher;

    private Fields(){}

    public static boolean verifyLogin(String login){
        pattern = Pattern.compile("^[a-z0-9_-]{3,16}$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(login);

        return matcher.matches();
    }

    public static boolean verifyMail(String mail){
        pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(mail);

        return matcher.matches();
    }
}
