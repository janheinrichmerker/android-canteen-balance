/*
 * Copyright (C) 2011 Eric Butler
 * Copyright (C) 2016 Heinrich Reimer
 *
 * Authors:
 * Eric Butler <eric@codebutler.com>
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

package com.heinrichreimer.canteenbalance.cardreader.desfire.util;

import androidx.annotation.Nullable;

import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireException;
import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireFileSettings;
import com.heinrichreimer.canteenbalance.cardreader.desfire.DesfireProtocol;

public class DesfireUtils {

    private DesfireUtils() {
    }

    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    private static int byteArrayToInt(byte[] b, int offset) {
        return byteArrayToInt(b, offset, b.length);
    }

    private static int byteArrayToInt(byte[] b, int offset, int length) {
        return (int) byteArrayToLong(b, offset, length);
    }

    private static long byteArrayToLong(byte[] b, int offset, int length) {
        if (b.length < length)
            throw new IllegalArgumentException("length must be less than or equal to b.length");

        long value = 0;
        for (int i = 0; i < length; i++) {
            int shift = (length - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    @Nullable
    public static DesfireFileSettings selectAppFile(DesfireProtocol tag, int appID, int fileID) {
        try {
            tag.selectApp(appID);
        } catch (DesfireException e) {
            return null;
        }
        try {
            return tag.getFileSettings(fileID);
        } catch (DesfireException e) {
            return null;
        }
    }
}
