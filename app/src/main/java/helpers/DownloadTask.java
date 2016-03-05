package helpers;

import android.os.AsyncTask;

import java.io.File;

import interfaces.FileDownloadedHandle;

/**
 * Created by smallfly on 16-3-5.
 *
 */
public class DownloadTask extends AsyncTask<Void, Void, File> {

    private String address;
    private String directory_to_save;
    private String filename;
    private int timeout_ms;

    private FileDownloadedHandle fileDownloadedHandle;

    public DownloadTask(String url, String directory_to_save, String filename, FileDownloadedHandle fileDownloadedHandle, int timeout_ms){
        this.address = url;
        this.directory_to_save = directory_to_save;
        this.filename = filename;
        this.fileDownloadedHandle = fileDownloadedHandle;
        this.timeout_ms = timeout_ms;
    }

    @Override
    protected File doInBackground(Void... params) {
        return URLDownloader.download(address, directory_to_save, filename, timeout_ms);
    }

    @Override
    protected void onPostExecute(File file) {
        fileDownloadedHandle.handle_downloaded_file(file);
    }
}
