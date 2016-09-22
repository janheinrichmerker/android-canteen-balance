![Icon](http://i.imgur.com/63PHxkq.png)

android-canteen-balance
=======================

[![JitPack](https://jitpack.io/v/com.heinrichreimersoftware/android-canteen-balance.svg)](https://jitpack.io/#com.heinrichreimersoftware/android-canteen-balance)
[![Build Status](https://travis-ci.org/HeinrichReimer/android-canteen-balance.svg?branch=master)](https://travis-ci.org/HeinrichReimer/android-canteen-balance)
[![GNU General Public License v3](https://img.shields.io/github/license/HeinrichReimer/android-canteen-balance.svg)](https://www.gnu.org/licenses/gpl-3.0.html)

Helper library to read out the current balance of canteen cards using NFC.

Supports the following cards:

* [InterCard](http://www.intercard.de/cms/intercard/index.html)
* [Ximedes](https://www.ximedes.com/paytech/) (formerly [MagnaCarta](https://www.ximedes.com/ximedes-press-release/))
(Full list of tested universities at the bottom of the page.)

Dependency
----------

*android-canteen-balance* is available on [**jitpack.io**](https://jitpack.io/#com.heinrichreimersoftware/android-canteen-balance)

**Gradle dependency:**
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```gradle
dependencies {
    compile 'com.heinrichreimersoftware:android-canteen-balance:1.6'
}
```

How-To-Use
-----

*android-canteen-balance* sends broadcasts whenever a card is detected.  
You could either extend `AbstractCardBalanceReceiver` if you want to handle the card balance in the background or `AbstractCardBalanceActivity` if you need the data in an activity.

**Option 1:** Using `AbstractCardBalanceActivity`:
```java
public class CardBalanceActivity extends AbstractCardBalanceActivity{
    @Override
    protected void onReceiveCardBalance(Context context, CardBalance balance) {
        //TODO do something with the balance
    }
}
```

**Option 2:** Using `AbstractCardBalanceReceiver`:
```java
public class CardBalanceReceiver extends AbstractCardBalanceReceiver{
    @Override
    protected void onReceiveCardBalance(Context context, CardBalance balance) {
        //TODO do something with the balance
    }
}
```
Dynamically register an instance of this receiver:
```java
Context.registerReceiver(receiver, new IntentFilter(CardBalance.ACTION_CARD_BALANCE))
```
Or statically publish an implementation in your AndroidManifest.xml:

```xml
<manifest ...>
    <application ...>
        <receiver android:name=".CardBalanceReceiver">
            <intent-filter>
                <action android:name="com.heinrichreimer.canteenbalance.action.CARD_BALANCE"/>
            </intent-filter>
        </receiver>
    </application>
</manifest>
```

Changes
-------

See the [releases section](https://github.com/HeinrichReimer/android-canteen-balance/releases) for detailed changelogs.

Open source libraries
-------

*android-canteen-balance* is mainly based on [MensaGuthaben](https://github.com/jakobwenzel/MensaGuthaben) by [@Jakob Wenzel](https://github.com/jakobwenzel) (GNU General Public License v3).
It also uses some methods of [Apache Commons Lang](https://github.com/apache/commons-lang)'s [ArrayUtils.java](https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/ArrayUtils.java) (Apache License 2.0).

Tested university cards
-------

![Map](http://maps.googleapis.com/maps/api/staticmap?autoscale=false&size=600x400&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C49.89386,+10.88612&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C49.92881,+11.58583&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C52.03541,+8.49634&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C51.44566,+7.2616&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C52.27349,+10.52971&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C53.10675,+8.85203&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C51.80671,+10.34409&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C49.8751,+8.65687&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C49.86643,+8.63843&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C51.02927,+13.72899&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C51.48613,+11.96953&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C52.32123,+9.8152&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C49.41914,+8.67024&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C50.68376,+10.93108&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C50.33604,+7.56956&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C50.93318,+6.94162&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C50.92816,+6.92881&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C51.33857,+12.37846&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C48.78158,+9.17467&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C49.783,+9.97084&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C50.89341,+14.80446)

* [Otto-Friedrich-Universität **Bamberg**](https://www.uni-bamberg.de/)
* [Universität **Bayreuth**](https://www.uni-bayreuth.de/) (only print-/copy-balance visible)
* [Universität **Bielefeld**](https://www.uni-bielefeld.de/) (only newer cards)
* [Ruhr-Universität **Bochum**](http://www.ruhr-uni-bochum.de/) (only newer cards)
* [Technische Universität **Braunschweig**](https://www.tu-braunschweig.de/)
* [Universität **Bremen**](http://www.uni-bremen.de/)
* [Technische Universität **Clausthal**](http://www.tu-clausthal.de/)
* [Technische Universität **Darmstadt**](https://www.tu-darmstadt.de/)
* [Hochschule **Darmstadt**](https://www.h-da.de/)
* [Technische Universität **Dresden**](https://tu-dresden.de/)
* [Martin-Luther-Universität **Halle-Wittenberg**](http://www.uni-halle.de/)
* [Hochschule **Hannover**](http://www.hs-hannover.de/)
* [Ruprecht-Karls-Universität **Heidelberg**](https://www.uni-heidelberg.de/)
* [Technische Universität **Ilmenau**](https://www.tu-ilmenau.de/)
* [Hochschule **Koblenz**](https://www.hs-koblenz.de/)
* [Rheinische Fachhochschule **Köln**](http://www.rfh-koeln.de/)
* [Universität zu **Köln**](http://www.uni-koeln.de/)
* [Universität **Leipzig**](http://www.uni-leipzig.de/)
* [Universität **Stuttgart**](http://www.uni-stuttgart.de/)
* [Julius-Maximilians-Universität **Würzburg**](https://www.uni-wuerzburg.de/)
* [Hochschule **Zittau/Görlitz**](http://www.hszg.de/)

License
-------

```
Copyright (C) 2016 Heinrich Reimer

Authors:
Heinrich Reimer <heinrich@heinrichreimer.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
