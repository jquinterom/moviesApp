package Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.moviesapp.R;

public class GridAdapter extends BaseAdapter {
    private final Context ctx;
    private final String[] names;
    int[] image;
    private ImageLoader imageLoader;
    private final String[] urlImages;

    LayoutInflater inflater;

    public GridAdapter(Context ctx, String[] names, String[] urls) {
        this.ctx = ctx;
        this.names = names;
        this.urlImages = urls;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.grid_item, null);
        }
        TextView txtName = view.findViewById(R.id.txtNameMovie);
        NetworkImageView imageView = view.findViewById(R.id.imageView);
        txtName.setText(names[i]);

            imageLoader = HttpSingleton.getInstance(ctx)
                    .getImageLoader();
            imageLoader.get(urlImages[i], ImageLoader.getImageListener(imageView,
                    R.drawable.loading, android.R.drawable
                            .ic_dialog_alert));
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageUrl(urlImages[i], imageLoader);
                }
            });
        return view;
    }
}
