/*
 * Copyright (C) 2011 Eric Butler
 * Copyright (C) 2016 Heinrich Reimer
 *
 * Authors:
 * Eric Butler <eric@codebutler.com>
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

package com.heinrichreimer.canteenbalance.cardreader.desfire;

public class DesfireException extends Exception {
    DesfireException(String message) {
        super(message);
    }

    DesfireException(Throwable cause) {
        super(cause);
    }
}
