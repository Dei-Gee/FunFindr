package com.example.funfindr.utilites;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

// This class handles retrieving assets from the assets folder
public class AssetsRetriever {
    private AssetManager assetManager;
    Context context;
    InputStream is;

    public AssetsRetriever(Context ctx)
    {
        this.context = ctx;
    }

    /*
        This method retrieves image assets
     */
    public Bitmap getBitmapFromAsset(String filename)
    {
        assetManager = context.getAssets();

        is = null;
        try{
            is = assetManager.open(filename);
        }
        catch(IOException e)
        {
            Log.d("error => ", e.getMessage());
        }

        Bitmap bitmapImage = BitmapFactory.decodeStream(is);
        return bitmapImage;
    }
}
