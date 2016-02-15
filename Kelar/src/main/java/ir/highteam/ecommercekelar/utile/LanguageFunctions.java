package ir.highteam.ecommercekelar.utile;

import java.util.Locale;

/**
 * Created by Mahdizit on 02/09/2016.
 */
public class LanguageFunctions {

    public static boolean isRtlLanguage(){
            return (Locale.getDefault().getISO3Language().equals("fas")
                    || Locale.getDefault().getISO3Language().equals("per")
                    || Locale.getDefault().getISO3Language().equals("fa")
                    || Locale.getDefault().getISO3Language().equals("ar")
                    || Locale.getDefault().getISO3Language().equals("ara"));
    }
}
