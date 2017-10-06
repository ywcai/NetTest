package ywcai.ls.mobileutil.global.util.statics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zmy_11 on 2017/10/1.
 */

public class InputValidate {
    public static boolean  isIpAddr(String input)
    {
        String valid= "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern p=  Pattern.compile(valid);
        Matcher m=p.matcher(input);
        return m.matches();
    }
}
