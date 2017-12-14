package carlos.cameraappz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import carlos.cameraappz.R;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    private static final int THUMBNAIL_PICTURE = 1;
    private static final int TAKE_PICTURE = 2;
    int REQUEST_PERMISSION= 100;

    private String photoPath = "";
    private Button btnThumb;
    private Button btnSave;
    private ImageView ivThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnThumb = (Button) findViewById(R.id.btnThumb);
        btnSave = (Button) findViewById(R.id.btnSave);
        ivThumb = (ImageView) findViewById(R.id.ivThumb);

        btnThumb.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent thumbnailIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (thumbnailIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(thumbnailIntent, THUMBNAIL_PICTURE);

        } else if ((Button) view == btnSave) {
            dispatchTakePictureIntent();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == THUMBNAIL_PICTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivThumb.setImageBitmap(imageBitmap);
        }else if(requestCode == TAKE_PICTURE && resultCode == RESULT_OK){
            Toast toast = Toast.makeText(getApplicationContext(), photoPath,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        photoPath = "File saved: " + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // TODO toast exception
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }
    }

}
