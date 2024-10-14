/*
 * Copyright (C) 2014 Jakob Wenzel
 * Copyright (C) 2016 Heinrich Reimer
 *
 * Authors:
 * Jakob Wenzel <jakobwenzel92@gmail.com>
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

package com.heinrichreimer.canteenbalance.cardreader;

import android.os.Bundle;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CardBalance {
    private static final String KEY_BALANCE =
            "com.heinrichreimer.canteenbalance.cardreader.CardBalance.BALANCE";
    private static final String KEY_LAST_TRANSACTION =
            "com.heinrichreimer.canteenbalance.cardreader.CardBalance.LAST_TRANSACTION";

    private static final DecimalFormat GERMAN_NUMBER_FORMAT =
            (DecimalFormat) NumberFormat.getInstance(Locale.GERMAN);

    static {
        GERMAN_NUMBER_FORMAT.setMinimumFractionDigits(2);
        GERMAN_NUMBER_FORMAT.setMaximumFractionDigits(2);
    }

    private static String CURRENCY = "€";

    public static final String ACTION_CARD_BALANCE =
            "com.heinrichreimer.canteenbalance.action.CARD_BALANCE";

    /**
     * Current balance on card in Euros or any other currency.
     */
    private BigDecimal balance;
    /**
     * Last transaction in Euros or any other currency, null if not supported by card.
     */
    private BigDecimal lastTransaction;

    public CardBalance() {
    }

    CardBalance(BigDecimal balance, BigDecimal lastTransaction) {
        this.balance = balance;
        this.lastTransaction = lastTransaction;
    }

    @Nullable
    public static CardBalance fromBundle(Bundle in) {
        Serializable balance = in.getSerializable(KEY_BALANCE);
        Serializable lastTransaction = in.getSerializable(KEY_LAST_TRANSACTION);
        if (!(balance instanceof BigDecimal && lastTransaction instanceof BigDecimal)) {
            return null;
        }
        return new CardBalance((BigDecimal) balance, (BigDecimal) lastTransaction);
    }

    public static void setCurrency(String currency) {
        CardBalance.CURRENCY = currency;
    }

    public static String getCurrency() {
        return CardBalance.CURRENCY;
    }

    public String getBalance() {
        return GERMAN_NUMBER_FORMAT.format(balance);
    }

    public String getLastTransaction() {
        return lastTransaction == null ? "" : GERMAN_NUMBER_FORMAT.format(lastTransaction);
    }

    public boolean isLastTransactionSupported() {
        return lastTransaction != null;
    }

    @Override
    public String toString() {
        return "Card balance: " + getBalance() +
                "€ (Last transaction: " + getLastTransaction() + "€)";
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BALANCE, balance);
        bundle.putSerializable(KEY_LAST_TRANSACTION, lastTransaction);
        return bundle;
    }
}
