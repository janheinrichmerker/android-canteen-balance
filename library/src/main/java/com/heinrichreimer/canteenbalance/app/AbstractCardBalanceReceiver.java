/*
 * Copyright (C) 2016 Heinrich Reimer
 *
 * Authors:
 * Heinrich Reimer <heinrich@heinrichreimer.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.heinrichreimer.canteenbalance.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.heinrichreimer.canteenbalance.cardreader.CardBalance;

public abstract class AbstractCardBalanceReceiver extends BroadcastReceiver {
    @Override
    public final void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            CardBalance balance = CardBalance.fromBundle(extras);
            if (balance != null) {
                onReceiveCardBalance(context, balance);
            }
        }
    }

    protected abstract void onReceiveCardBalance(Context context, CardBalance balance);
}
