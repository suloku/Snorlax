/*
 * Copyright (c) 2016. Pedro Diaz <igoticecream@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alucas.snorlax.module.feature.gym;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.alucas.snorlax.R;
import com.alucas.snorlax.module.NotificationId;
import com.alucas.snorlax.module.context.pokemongo.PokemonGo;
import com.alucas.snorlax.module.context.snorlax.Snorlax;
import com.alucas.snorlax.module.util.Resource;

@Singleton
final class EjectNotification {

	private final Context mContext;
	private final Resources mResources;
	private final NotificationManager mNotificationManager;

	@Inject
	EjectNotification(@Snorlax Context context, @Snorlax Resources resources, @PokemonGo NotificationManager notificationManager) {
		mContext = context;
		mResources = resources;
		mNotificationManager = notificationManager;
	}

	void show(final int pokemonNumber, final String pokemonName, final String gymName) {
		new Handler(Looper.getMainLooper()).post(() -> {
			Notification notification = createNotification(pokemonNumber, pokemonName, gymName);
			hideIcon(notification);

			mNotificationManager.notify(NotificationId.getUniqueID(), notification);
		});
	}

	private Notification createNotification(final int pokemonNumber, final String pokemonName, final String gymName) {
		return new NotificationCompat.Builder(mContext)
			.setSmallIcon(R.drawable.ic_eject)
			.setLargeIcon(Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(
					mResources,
					Resource.getPokemonResourceId(mContext, mResources, pokemonNumber)
				),
				Resource.getLargeIconWidth(mResources),
				Resource.getLargeIconHeight(mResources),
				false
			))
			.setContentTitle(mContext.getString(R.string.notification_gym_eject_title, pokemonName))
			.setContentText(mContext.getString(R.string.notification_gym_eject_content, gymName))
			.setColor(ContextCompat.getColor(mContext, R.color.red_700))
			.setAutoCancel(true)
			.setVibrate(new long[]{0, 1000})
			.setPriority(Notification.PRIORITY_MAX)
			.setCategory(NotificationCompat.CATEGORY_ALARM)
			.build();
	}

	@SuppressWarnings("deprecation")
	private void hideIcon(Notification notification) {
		int iconId = mResources.getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
		if (iconId != 0) {
			if (notification.contentView != null) {
				notification.contentView.setViewVisibility(iconId, View.INVISIBLE);
			}
			if (notification.bigContentView != null) {
				notification.bigContentView.setViewVisibility(iconId, View.INVISIBLE);
			}
		}
	}
}
