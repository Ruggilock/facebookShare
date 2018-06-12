package com.abosrtio.sharetest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if(shareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }
        }
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnShareLink = findViewById(R.id.btnShareLink);
        Button btnPhoto = findViewById(R.id.btnPhoto);

//        printkeyHash();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast toast = Toast.makeText(MainActivity.this, "Deal", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onCancel() {
                        Toast toast = Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT);
                        toast.show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        System.out.println(error);
                        Toast toast = Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
                Picasso.with(getBaseContext())
                        .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe-1hEzKJeo-BeiLKnw3EOjz2igjF00PLjH81x9TWkvQSB96k9")
                        .into(target);
            }
        });


    }

    private void printkeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.abosrtio.sharetest", PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("keyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
