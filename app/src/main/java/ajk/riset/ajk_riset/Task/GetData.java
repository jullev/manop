package ajk.riset.ajk_riset.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import ajk.riset.ajk_riset.DB.DBAdapter;

/**
 * Created by AJK-Riset on 8/18/2016.
 */
public class GetData extends AsyncTask<String, Void, String> {
    private ProgressDialog dialog;
    protected Context applicationContext;
    DBAdapter adapter;
    SQLiteDatabase database;

    @Override
    protected void onPreExecute() {
        this.dialog = ProgressDialog.show(applicationContext, "Syncronice	",
                "Please Wait...", true);
        adapter = new DBAdapter(applicationContext);
        database = adapter.getWritableDatabase();
    }
    @Override
    protected String doInBackground(String... params) {
        return null;
    }
}
