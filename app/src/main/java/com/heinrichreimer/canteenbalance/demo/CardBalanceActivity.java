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

package com.heinrichreimer.canteenbalance.demo;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.heinrichreimer.canteenbalance.app.AbstractCardBalanceActivity;
import com.heinrichreimer.canteenbalance.cardreader.CardBalance;
import com.heinrichreimer.canteenbalance.demo.databinding.ActivityCardBalanceBinding;

public class CardBalanceActivity extends AbstractCardBalanceActivity {

    private ActivityCardBalanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_card_balance);
    }

    @Override
    public void onReceiveCardBalance(CardBalance balance) {
        binding.setCardBalance(balance);
    }
}
