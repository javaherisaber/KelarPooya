package ir.highteam.ecommercekelar.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by mohammad on 5/17/2016.
 */

public class RtlSupport {

    Context context;

    public RtlSupport(Context context){
        this.context = context;
    }

    public void changeViewDirection(TextView view){
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        if (isRTL()){
            params.gravity = Gravity.RIGHT;
        }
        else {
            params.gravity = Gravity.LEFT;
        }
        view.setLayoutParams(params);
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

}
