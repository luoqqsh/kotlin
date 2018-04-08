package comm.test.kotlin.tools;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, String info) {
		if (!TextUtils.isEmpty(info)) {
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showLONG(Context context, String info) {
		if (!TextUtils.isEmpty(info)) {
			Toast.makeText(context, info, Toast.LENGTH_LONG).show();
		}
	}

	public static void show(Context context, String info, int gravity) {
		if (!TextUtils.isEmpty(info)) {
			Toast toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
			toast.setGravity(gravity, 0, (int) Tools.getScreenDensity(context) + 20);
			toast.show();
		}
	}

}
