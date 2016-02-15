package ir.highteam.ecommercekelar.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ir.highteam.ecommercekelar.R;

/**
 * Created by mohammad on 5/6/2016.
 */
public class PurchaseIconCompat {

    Context context;

    public PurchaseIconCompat(Context context){
        this.context = context;
    }

    public Drawable buildPurchaseCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_purchase_count, null);
        TextView textView = (TextView) view.findViewById(R.id.count);
        if (count == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(String.valueOf(count));
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
