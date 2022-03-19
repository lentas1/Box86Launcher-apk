// DO NOT EDIT THIS FILE - it is automatically generated, ALL YOUR CHANGES WILL BE OVERWRITTEN, edit the file under $JAVA_SRC_PATH dir
/*
Simple DirectMedia Layer
Java source code (C) 2009-2014 Sergii Pylypenko

This software is provided 'as-is', without any express or implied
warranty.  In no event will the authors be held liable for any damages
arising from the use of this software.

Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software. If you use this software
   in a product, an acknowledgment in the product documentation would be
   appreciated but is not required. 
2. Altered source versions must be plainly marked as such, and must not be
   misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
*/

package x.org.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.allplatform.box86.R;

public class DummyService extends Service
{
	public DummyService()
	{
		super();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent != null && Intent.ACTION_DELETE.equals(intent.getAction()))
		{
			Log.v("SDL", "User dismissed notification, killing myself");
			stopSelfResult(5);
			stopSelfResult(0);
			System.exit(0);
		}
		Log.v("SDL", "Starting dummy service - displaying notification");
		Notification.Builder builder;
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
		{
			String channelId = "app_running_ntf";
			NotificationChannel channel = new NotificationChannel(channelId,
										getString(R.string.notification_app_is_running, getString(getApplicationInfo().labelRes)),
										NotificationManager.IMPORTANCE_LOW);
			NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mgr.createNotificationChannel(channel);
			builder = new Notification.Builder(this, channelId);
		}
		else
		{
			builder = new Notification.Builder(this);
		}
		Notification ntf = builder
			.setSmallIcon(R.drawable.icon)
			.setTicker(getString(getApplicationInfo().labelRes))
			.setOngoing(true)
			.build();
		PendingIntent killIntent = PendingIntent.getService(this, 5, new Intent(Intent.ACTION_DELETE, null, this, DummyService.class), PendingIntent.FLAG_CANCEL_CURRENT);
		PendingIntent showIntent = PendingIntent.getActivity(this, 0, new Intent("", null, this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		ntf.deleteIntent = killIntent;
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
		view.setCharSequence(R.id.notificationText, "setText", getString(R.string.notification_app_is_running, getString(getApplicationInfo().labelRes)));
		view.setOnClickPendingIntent(R.id.notificationText, showIntent);
		view.setOnClickPendingIntent(R.id.notificationIcon, showIntent);
		view.setOnClickPendingIntent(R.id.notificationView, showIntent);
		view.setOnClickPendingIntent(R.id.notificationStop, killIntent);
		ntf.contentView = view;
		startForeground(1, ntf);
		return Service.START_NOT_STICKY;
	}
	@Override
	public void onDestroy()
	{
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
