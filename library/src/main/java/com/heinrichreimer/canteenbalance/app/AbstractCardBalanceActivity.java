/*
 * Copyright (C) 2016 Heinrich Reimer
 *
 * Authors:
 * Heinrich Reimer <heinrich@merker.id>
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

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heinrichreimer.canteenbalance.cardreader.CardBalance;

public abstract class AbstractCardBalanceActivity extends AppCompatActivity {

    private final CardBalanceReceiver receiver = new CardBalanceReceiver(
            new OnReceiveCardBalanceListener() {
                @Override
                public void onReceiveCardBalance(CardBalance balance) {
                    AbstractCardBalanceActivity.this.onReceiveCardBalance(balance);
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                CardBalance balance = CardBalance.fromBundle(extras);
                if (balance != null) {
                    this.onReceiveCardBalance(balance);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(CardBalance.ACTION_CARD_BALANCE));
    }

    @Override
    public void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    protected abstract void onReceiveCardBalance(CardBalance balance);

    private class CardBalanceReceiver extends AbstractCardBalanceReceiver {
        private final OnReceiveCardBalanceListener listener;

        public CardBalanceReceiver(@NonNull OnReceiveCardBalanceListener listener) {
            this.listener = listener;
        }

        @Override
        public void onReceiveCardBalance(Context context, CardBalance balance) {
            listener.onReceiveCardBalance(balance);
        }
    }

    private interface OnReceiveCardBalanceListener {
        void onReceiveCardBalance(CardBalance balance);
    }
}
