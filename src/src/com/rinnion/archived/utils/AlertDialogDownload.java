package com.rinnion.archived.utils;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by alekseev on 27.01.2016.
 */
public class AlertDialogDownload  {
    private Context context;
    private AlertDialog.Builder ad;
    private AlertDialogDownloadInterface alertDialogDownloadInterface;






    public AlertDialogDownload(Context context,String title,String message,String positiveButton,String negativeButton)
    {


        this.context=context;

        ad= new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(alertDialogDownloadInterface!=null)
                alertDialogDownloadInterface.OnPositiveButton();
            }
        });
        ad.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(alertDialogDownloadInterface!=null)
                alertDialogDownloadInterface.OnNegativeButton();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(alertDialogDownloadInterface!=null)
                alertDialogDownloadInterface.OnCancel();
            }
        });
    }

    public void SetListener(AlertDialogDownloadInterface alertDialogDownloadInterface)
    {
        this.alertDialogDownloadInterface=alertDialogDownloadInterface;
    }



    public void Show()
    {
        ad.show();
    }



}
