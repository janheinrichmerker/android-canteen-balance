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

package com.heinrichreimer.canteenbalance.cardreader.desfire;

import android.nfc.tech.IsoDep;

import com.heinrichreimer.canteenbalance.cardreader.desfire.util.ArrayUtils;
import com.heinrichreimer.canteenbalance.cardreader.desfire.util.DesfireUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DesfireProtocol {
    /* Commands */
    private static final byte GET_ADDITIONAL_FRAME = (byte) 0xAF;
    private static final byte SELECT_APPLICATION = (byte) 0x5A;
    private static final byte READ_DATA = (byte) 0xBD;
    private static final byte READ_VALUE = (byte) 0x6C;
    private static final byte GET_FILE_SETTINGS = (byte) 0xF5;

    /* Status codes */
    private static final byte OPERATION_OK = (byte) 0x00;
    private static final byte PERMISSION_DENIED = (byte) 0x9D;
    private static final byte ADDITIONAL_FRAME = (byte) 0xAF;

    private final IsoDep mTagTech;

    public DesfireProtocol(IsoDep tagTech) {
        mTagTech = tagTech;
    }

    public void selectApp(int appId) throws DesfireException {
        byte[] appIdBuff = new byte[3];
        appIdBuff[0] = (byte) ((appId & 0xFF0000) >> 16);
        appIdBuff[1] = (byte) ((appId & 0xFF00) >> 8);
        appIdBuff[2] = (byte) (appId & 0xFF);

        sendRequest(SELECT_APPLICATION, appIdBuff);
    }

    public DesfireFileSettings getFileSettings(int fileNo) throws DesfireException {
        byte[] data;
        data = sendRequest(GET_FILE_SETTINGS, new byte[]{(byte) fileNo});
        return DesfireFileSettings.create(data);
    }

    public byte[] readFile(int fileNo) throws DesfireException {
        return sendRequest(READ_DATA, new byte[]{
                (byte) fileNo,
                (byte) 0x0, (byte) 0x0, (byte) 0x0,
                (byte) 0x0, (byte) 0x0, (byte) 0x0
        });
    }

    public int readValue(int fileNum) throws DesfireException {
        byte[] buf = sendRequest(READ_VALUE, new byte[]{
                (byte) fileNum
        });
        ArrayUtils.reverse(buf);
        return DesfireUtils.byteArrayToInt(buf);
    }


    private byte[] sendRequest(byte command, byte[] parameters) throws DesfireException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] recvBuffer;
        try {
            recvBuffer = mTagTech.transceive(wrapMessage(command, parameters));
        } catch (IOException e) {
            throw new DesfireException(e);
        }

        while (true) {
            if (recvBuffer[recvBuffer.length - 2] != (byte) 0x91)
                throw new DesfireException("Invalid response");

            output.write(recvBuffer, 0, recvBuffer.length - 2);

            byte status = recvBuffer[recvBuffer.length - 1];
            if (status == OPERATION_OK) {
                break;
            }
            else if (status == ADDITIONAL_FRAME) {
                try {
                    recvBuffer = mTagTech.transceive(wrapMessage(GET_ADDITIONAL_FRAME, null));
                } catch (IOException e) {
                    throw new DesfireException(e);
                }
            }
            else if (status == PERMISSION_DENIED) {
                throw new DesfireException("Permission denied");
            }
            else {
                throw new DesfireException("Unknown status code: " + Integer.toHexString(status & 0xFF));
            }
        }

        return output.toByteArray();
    }

    private byte[] wrapMessage(byte command, byte[] parameters) throws DesfireException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        stream.write((byte) 0x90);
        stream.write(command);
        stream.write((byte) 0x00);
        stream.write((byte) 0x00);
        if (parameters != null) {
            stream.write((byte) parameters.length);
            try {
                stream.write(parameters);
            } catch (IOException e) {
                throw new DesfireException(e);
            }
        }
        stream.write((byte) 0x00);

        return stream.toByteArray();
    }

}