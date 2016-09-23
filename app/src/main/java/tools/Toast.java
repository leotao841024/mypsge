package tools;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

public class Toast {
	private static Toast toast;
	private static android.widget.Toast systoast;
	private static int duration;
	private static Handler handler;
	private static HandlerThread thread;
	private static Runnable run = new Runnable() {
		public void run() {
			systoast.cancel();
		}
	};
	interface BuildToast {
		void setText(android.widget.Toast toast); 
		android.widget.Toast create();
	}
	private static Toast makeText(Context ctx, int duration,
			final BuildToast build) {
		if (thread == null) {
			thread = new HandlerThread("toast");
		}
		if (!thread.isAlive()) {
			thread.start();
		}
		handler = new Handler(thread.getLooper());
		Toast.duration = duration;
		if (systoast != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					build.setText(systoast);
				}
			});
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					systoast = build.create();
				}
			});
		}
		if (toast == null) {
			toast = new Toast();
		}
		return toast;
	}

	public static Toast makeText(final Context ctx, final CharSequence msg,
			final int duration) {
		return makeText(ctx, duration, new BuildToast() {
			@Override
			public void setText(android.widget.Toast toast) {
				toast.setText(msg);
			}

			@Override
			public android.widget.Toast create() {
				return android.widget.Toast.makeText(ctx, msg, duration);
			}
		});
	}

	public static Toast makeText(final Context ctx, final int resId,
			final int duration) {
		return makeText(ctx, duration, new BuildToast() {
			@Override
			public void setText(android.widget.Toast toast) {
				toast.setText(resId);
			}

			@Override
			public android.widget.Toast create() {
				return android.widget.Toast.makeText(ctx, resId, duration);
			}
		});
	}
	
	public void show() {
		handler.removeCallbacks(run);
		handler.postDelayed(run, duration);
		while (systoast == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
		systoast.show();
	}
}
