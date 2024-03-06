package com.project_five.serverconnection.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project_five.serverconnection.MainActivity;
import com.project_five.serverconnection.R;
import com.project_five.serverconnection.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class CustomListViewArrayAdapter<T> extends ArrayAdapter<Country> {
    Context context;
    List<Country> countries;
    int resource;
    LruCache<Integer, Bitmap> photos_cache;
    public CustomListViewArrayAdapter(@NonNull Context context, int resource, List<Country> countries) {
        super(context, resource, countries);
        this.context = context;
        this.countries = countries;
        this.resource = resource;
        /*
            byte / 1024 -> kilo byte
            maxMemoryUsedAtMoment : the maximum amount of memory that is currently usable
            we use 1/16 of this memory
         */
        final int maxMemoryUsedAtMoment = (int) (Runtime.getRuntime().maxMemory()/1024);
        photos_cache = new LruCache<Integer, Bitmap>(maxMemoryUsedAtMoment/16);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource, null);
        Country country = countries.get(position);
        
        TextView text = (TextView) view.findViewById(R.id.lv_item_tv);
        text.setText(country.getName()+":"+country.getCode()+":"+country.getRank());
        Bitmap cached_bitmap = photos_cache.get(country.getId());
        if (cached_bitmap != null){
            /*
                if use this :

                photoElements.country.setBitmap(photoElements.bitmap);

                in onPostExecute then put this :

                if (country.getBitmap() != null){
                    ImageView photo = (ImageView) view.findViewById(R.id.lv_item_iv);
                    photo.setImageBitmap(country.getBitmap());
                }

                here.
             */
            ImageView photo = (ImageView) view.findViewById(R.id.lv_item_iv);
            photo.setImageBitmap(cached_bitmap);
        } else {
            PhotoElements elements = new PhotoElements();
            elements.country = country;
            elements.view = view;
            PhotoLoadingManager manager = new PhotoLoadingManager();
            manager.execute(elements);
        }
        return view;
    }
    /*
        each photo must be downloaded and displayed when it appears on the screen
        : the view that is inside the getView method is the same view that is currently on the screen
        , we only download and display the related to this view until other views appear on the screen.
     */
    class PhotoElements{
        /*
            this class is like a structure of each photo ...
            Country country ->  this element which is related to each photo is located in getView methode (each photos information such as name and ... is in Country class)
            View view -> this element which is related to each photo is located in getView methode (each photo must be display in a view)
            Bitmap bitmap -> we download bitmap of each photo in photoLoadingManager class
         */
        Country country;
        View view;
        Bitmap bitmap;
    }
    //download bitmap task
    class PhotoLoadingManager extends AsyncTask<PhotoElements, String, PhotoElements>{

        @Override
        protected PhotoElements doInBackground(PhotoElements... photoElements) {
            PhotoElements elements = photoElements[0];
            try {
                URL url = new URL(MainActivity.photos_url + elements.country.getName().replaceAll(" ", "_").toLowerCase() + ".jpg");
                InputStream inputStream = (InputStream) url.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                elements.bitmap = bitmap;
                inputStream.close();
                return elements;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PhotoElements photoElements) {
            ImageView iv = photoElements.view.findViewById(R.id.lv_item_iv);
            iv.setImageBitmap(photoElements.bitmap);
            /*
                instead of saving all the images and occupying the phones memory
                like this :

                photoElements.country.setBitmap(photoElements.bitmap);

                --> we save the last seen images in a cache with a limited volume and
                automatically delete the older ones in the cache.
                this help to application not crash when the memory is full
             */
            photos_cache.put(photoElements.country.getId(), photoElements.bitmap);
            super.onPostExecute(photoElements);
        }
    }
}
