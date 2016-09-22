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

import android.os.Parcel;
import android.os.Parcelable;

import com.heinrichreimer.canteenbalance.cardreader.desfire.util.ArrayUtils;
import com.heinrichreimer.canteenbalance.cardreader.desfire.util.DesfireUtils;

import java.io.ByteArrayInputStream;

public abstract class DesfireFileSettings implements Parcelable {
    private final byte  fileType;
    private final byte  commSetting;
    private final byte[] accessRights;

    /* DesfireFile Types */
    private static final byte STANDARD_DATA_FILE = (byte) 0x00;
    private static final byte BACKUP_DATA_FILE   = (byte) 0x01;
    private static final byte VALUE_FILE         = (byte) 0x02;
    private static final byte LINEAR_RECORD_FILE = (byte) 0x03;
    private static final byte CYCLIC_RECORD_FILE = (byte) 0x04;

    static DesfireFileSettings create(byte[] data) throws DesfireException {
        byte fileType =  data[0];

        ByteArrayInputStream stream = new ByteArrayInputStream(data);

        if (fileType == STANDARD_DATA_FILE || fileType == BACKUP_DATA_FILE)
            return new StandardDesfireFileSettings(stream);
        else if (fileType == LINEAR_RECORD_FILE || fileType == CYCLIC_RECORD_FILE)
            return new RecordDesfireFileSettings(stream);
        else if (fileType == VALUE_FILE)
            return new ValueDesfireFileSettings(stream);
        else
            throw new DesfireException("Unknown file type: " + Integer.toHexString(fileType));
    }

    private DesfireFileSettings (ByteArrayInputStream stream) {
        fileType    = (byte) stream.read();
        commSetting = (byte) stream.read();

        accessRights = new byte[2];
        //noinspection ResultOfMethodCallIgnored
        stream.read(accessRights, 0, accessRights.length);
    }

    private DesfireFileSettings (byte fileType, byte commSetting, byte[] accessRights) {
        this.fileType     = fileType;
        this.commSetting  = commSetting;
        this.accessRights = accessRights;
    }

    public static final Creator<DesfireFileSettings> CREATOR = new Creator<DesfireFileSettings>() {
        public DesfireFileSettings createFromParcel(Parcel source) {
            byte fileType       = source.readByte();
            byte commSetting    = source.readByte();
            byte[] accessRights = new byte[source.readInt()];
            source.readByteArray(accessRights);

            if (fileType == STANDARD_DATA_FILE || fileType == BACKUP_DATA_FILE) {
                int fileSize = source.readInt();
                return new StandardDesfireFileSettings(fileType, commSetting, accessRights, fileSize);
            } else if (fileType == LINEAR_RECORD_FILE || fileType == CYCLIC_RECORD_FILE) {
                int recordSize = source.readInt();
                int maxRecords = source.readInt();
                int curRecords = source.readInt();
                return new RecordDesfireFileSettings(fileType, commSetting, accessRights, recordSize, maxRecords, curRecords);
            } else {
                return new UnsupportedDesfireFileSettings(fileType);
            }
        }

        public DesfireFileSettings[] newArray(int size) {
            return new DesfireFileSettings[size];
        }
    };

    public void writeToParcel (Parcel parcel, int flags) {
        parcel.writeByte(fileType);
        parcel.writeByte(commSetting);
        parcel.writeInt(accessRights.length);
        parcel.writeByteArray(accessRights);
    }

    public int describeContents () {
        return 0;
    }

    private static class StandardDesfireFileSettings extends DesfireFileSettings {
         final int fileSize;

        private StandardDesfireFileSettings (ByteArrayInputStream stream) {
            super(stream);
            byte[] buf = new byte[3];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            fileSize = DesfireUtils.byteArrayToInt(buf);
        }

        StandardDesfireFileSettings (byte fileType, byte commSetting, byte[] accessRights, int fileSize) {
            super(fileType, commSetting, accessRights);
            this.fileSize = fileSize;
        }

        @Override
        public void writeToParcel (Parcel parcel, int flags) {
            super.writeToParcel(parcel, flags);
            parcel.writeInt(fileSize);
        }
    }

    private static class RecordDesfireFileSettings extends DesfireFileSettings {
         final int recordSize;
         final int maxRecords;
         final int curRecords;

         RecordDesfireFileSettings(ByteArrayInputStream stream) {
            super(stream);

            byte[] buf = new byte[3];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            recordSize = DesfireUtils.byteArrayToInt(buf);

            buf = new byte[3];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            maxRecords = DesfireUtils.byteArrayToInt(buf);

            buf = new byte[3];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            curRecords = DesfireUtils.byteArrayToInt(buf);
        }

        RecordDesfireFileSettings (byte fileType, byte commSetting, byte[] accessRights, int recordSize, int maxRecords, int curRecords) {
            super(fileType, commSetting, accessRights);
            this.recordSize = recordSize;
            this.maxRecords = maxRecords;
            this.curRecords = curRecords;
        }

        @Override
        public void writeToParcel (Parcel parcel, int flags) {
            super.writeToParcel(parcel, flags);
            parcel.writeInt(recordSize);
            parcel.writeInt(maxRecords);
            parcel.writeInt(curRecords);
        }
    }




    public static class ValueDesfireFileSettings extends DesfireFileSettings {
         final int lowerLimit;
         final int upperLimit;
        public final int value;
         final byte limitedCreditEnabled;

         ValueDesfireFileSettings(ByteArrayInputStream stream) {
            super(stream);

            byte[] buf = new byte[4];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            lowerLimit = DesfireUtils.byteArrayToInt(buf);

            buf = new byte[4];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            upperLimit = DesfireUtils.byteArrayToInt(buf);

            buf = new byte[4];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            ArrayUtils.reverse(buf);
            value = DesfireUtils.byteArrayToInt(buf);
            

            buf = new byte[1];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buf, 0, buf.length);
            limitedCreditEnabled = buf[0];
            
            //http://www.skyetek.com/docs/m2/desfire.pdf
            //http://neteril.org/files/M075031_desfire.pdf
        }

        @Override
        public void writeToParcel (Parcel parcel, int flags) {
            super.writeToParcel(parcel, flags);
            parcel.writeInt(lowerLimit);
            parcel.writeInt(upperLimit);
            parcel.writeInt(value);
            parcel.writeByte(limitedCreditEnabled);
        }
    }
    private static class UnsupportedDesfireFileSettings extends DesfireFileSettings {
         UnsupportedDesfireFileSettings(byte fileType) {
            super(fileType, Byte.MIN_VALUE, new byte[0]);
        }
    }
}