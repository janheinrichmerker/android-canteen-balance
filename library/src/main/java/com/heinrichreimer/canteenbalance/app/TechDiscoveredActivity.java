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

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.heinrichreimer.canteenbalance.cardreader.CardBalance;
import com.heinrichreimer.canteenbalance.cardreader.Readers;
import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireException;

public class TechDiscoveredActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "TechDiscoveredActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            onNewIntent(getIntent());
        }
        else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            try {
                CardBalance balance = Readers.getInstance().readTag(tag);
                if (balance != null) {
                    Log.d(DEBUG_TAG, balance.toString());

                    Intent broadcast = new Intent(CardBalance.ACTION_CARD_BALANCE);
                    broadcast.putExtras(balance.toBundle());
                    sendBroadcast(broadcast);
                }
            } catch (DesfireException ignored) {
                // Card is not supported
            }
        }

        finish();
    }
}
