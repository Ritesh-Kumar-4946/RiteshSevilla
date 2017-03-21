package com.ritesh.sevilla;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by Mickael on 10/10/2016.
 */

public class CameraPickerManager extends PickerManager {

    /*https://github.com/Tofira/ImagePickerWithCrop?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=4760*/
    /*https://github.com/Tofira/ImagePickerWithCrop?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=4760*/
    /*https://github.com/Tofira/ImagePickerWithCrop?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=4760*/

    public CameraPickerManager(Activity activity) {
        super(activity);
    }

    protected void sendToExternalApp()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mProcessingPhotoUri =  getImageFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mProcessingPhotoUri);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }
}
