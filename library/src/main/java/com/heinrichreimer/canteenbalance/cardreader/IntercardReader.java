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
import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireFileSettings;
import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireProtocol;
import com.heinrichreimer.canteenbalance.cardreader.desfire.util.DesfireUtils;

import java.math.BigDecimal;

class IntercardReader implements ICardReader {

    private static final BigDecimal THOUSAND = new BigDecimal(1000);

    @Override
	public CardBalance readCard(DesfireProtocol card) throws DesfireException {

		final int appId = 0x5F8415;
		final int fileId = 1;
		// Selecting app and file
		DesfireFileSettings settings = DesfireUtils.selectAppFile(card, appId, fileId);

		if (settings instanceof DesfireFileSettings.ValueDesfireFileSettings) {
			// Found value file

            // Last transaction in tenths of Euro cents
			int lastTransactionTenthsOfCents = ((DesfireFileSettings.ValueDesfireFileSettings) settings).value;

            // Last transaction in Euro
            BigDecimal lastTransaction = new BigDecimal(lastTransactionTenthsOfCents)
                    .divide(THOUSAND, 4, BigDecimal.ROUND_HALF_UP);

			// Reading value
			try {
				// Balance in tenths of Euro cents
				int balanceTenthsOfCents = card.readValue(fileId);

				// Balance in Euro
				BigDecimal balance = new BigDecimal(balanceTenthsOfCents)
						.divide(THOUSAND, 4, BigDecimal.ROUND_HALF_UP);


				return new CardBalance(balance, lastTransaction);
			} catch (Exception e) {
				return null;
			}

		}
		else {
			// File is not a value file, tag is incompatible
			return null;
		}
	}
}
