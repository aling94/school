/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emailvalidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alvin Ling
 */
public class EmailAddress
{

    private String address;
    private String local;
    private String domain;

    public EmailAddress()
    {
        address = "";
        local = "";
        domain = "";
    }

    public EmailAddress(String a)
    {
        address = a;
        if (address.matches(".+@.+"))
        {
            local = a.split("@")[0];
            domain = a.split("@")[1];
        } else
        {
            local = "";
            domain = "";
        }
    }

    public String toString()
    {
        return address;
    }

    public boolean isName()
    {
        //validates local part of email address [section before @]      
        boolean illCharsDot = local.matches("[a-zA-Z]\\w*\\.?\\w*[a-zA-Z0-9]");
        Matcher m = Pattern.compile("[_\\.]{2,}").matcher(local);
        boolean doubUnde = !m.find();
        return illCharsDot && doubUnde && local.length() <= 64;
    }

    public static boolean isName(String name)
    {
        //validates local part of email address [section before @]
        boolean illCharsDot = name.matches("[a-zA-Z]\\w*\\.?\\w*[a-zA-Z0-9]");
        Matcher m = Pattern.compile("[_\\.]{2,}").matcher(name);
        boolean doubUnde = !m.find();
        return illCharsDot && doubUnde && name.length() <= 64;
    }

    public boolean isDomain()
    {
        boolean validLabel = domain.matches("[a-zA-Z0-9][a-zA-Z0-9\\-]{0,62}"
                + "(\\.[a-zA-Z0-9\\-]{1,63})*\\.[a-zA-Z]+");
        Matcher m = Pattern.compile("[\\.\\-]{2,}").matcher(domain);
        boolean doubHyph = !m.find();
        return validLabel && doubHyph;
    }

    public static boolean isDomain(String domain)
    {
        boolean validLabel = domain.matches("[a-zA-Z0-9][a-zA-Z0-9\\-]{0,62}"
                + "(\\.[a-zA-Z0-9\\-]{1,63})*\\.[a-zA-Z]+");
        Matcher m = Pattern.compile("[\\.\\-]{2,}").matcher(domain);
        boolean doubHyph = !m.find();
        return validLabel && doubHyph;
    }

    public boolean validate()
    {
        return isName() && isDomain() && address.length() <= 255;
    }
}
