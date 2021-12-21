package kg.geektech.lvl4lesson1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.EditText;

public class Prefs {
    private EditText editTextName;
    private SharedPreferences preferences;

    public Prefs(Context context) {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void saveBoardState() {
        preferences.edit().putBoolean("isShown", true).apply();
    }

    public boolean isBoardShown() {
        return preferences.getBoolean("isShown", false);
    }

    public void saveNameState(String string) {
        preferences.edit().putString("isNameEntered", string).apply();
    }

    public String isNameEntered()
    {
        return preferences.getString("isNameEntered", "");
    }

    public void saveImage(Uri uri) {
        preferences.edit().putString("saveImage", uri.toString()).apply();
    }

    public String getImage() {
        return preferences.getString("saveImage", "");

    }
}
