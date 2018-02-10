package com.mkytr.locationlisteningapp.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mkytr.locationlisteningapp.Database.Utility;
import com.mkytr.locationlisteningapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by mkyka on 10.02.2018.
 */

public class ExportDatabaseTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    public ExportDatabaseTask(Context getContext){
        this.context = getContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        File dest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File db = context.getDatabasePath(Utility.dbName);

        if(dest.canWrite() && dest.exists()){
            dest = new File(dest, Utility.dbName + ".db");
            try {
                FileChannel source = new FileInputStream(db).getChannel();
                FileChannel destination = new FileOutputStream(dest).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            }catch (FileNotFoundException exception){
                Log.e("EXPORTDB", exception.getLocalizedMessage());
            }catch (IOException exception){
                Log.e("EXPORTDB", exception.getLocalizedMessage());
            }
        }else {
            Log.e("EXPORTDB", "File not exists");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, context.getText(R.string.toast_export_result_success), Toast.LENGTH_LONG).show();
    }
}
