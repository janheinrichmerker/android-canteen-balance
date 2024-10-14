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

import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireException;
import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireProtocol;

import java.math.BigDecimal;

class MagnaCartaReader implements ICardReader {

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    @Override
    public CardBalance readCard(DesfireProtocol card) {
        final int appId = 0xF080F3;
        final int fileId = 2;

        //We don't want to use getFileSettings as they are doing some weird stuff with the fileType
        try {
            card.selectApp(appId);

            //For some reason we can't use getFileList either, because the card answers with an
            //authentication error

            byte[] data = card.readFile(fileId);

            int low = ((int) data[7]) & 0xFF;
            int hi = ((int) data[6]) & 0xFF;

            // Balance in cents
            int cents = hi << 8 | low;

            // Balance in Euro
            BigDecimal balance = new BigDecimal(cents)
                    .divide(HUNDRED, 3, BigDecimal.ROUND_HALF_UP);

            return new CardBalance(balance, null);

        } catch (DesfireException e) {
            return null;
        }
    }
}
