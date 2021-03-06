/**
 * @author Andy Gup
 *
 * Copyright 2016 Esri
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.​
 */
package com.esri.cordova.geolocation.utils;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Threadsafe class for converting location data into JSON
 */
public final class JSONHelper {

    public static final String SATELLITE_PROVIDER = "satellite";
    public static final String CELLINFO_PROVIDER = "cell_info";
    public static final String CELLLOCATION_PROVIDER = "cell_location";
    private static final String CDMA = "cdma";
    private static final String WCDMA = "wcdma";
    private static final String GSM = "gsm";
    private static final String LTE = "lte";
    private static final String TAG = "GeolocationPlugin";

    /**
     * Converts location data into a JSON form that can be consumed within a JavaScript application
     * @param provider Indicates if this location is coming from gps or network provider
     * @param location The android Location
     * @param cached Indicates if the value was pulled from the device cache or not
     * @return Location data. Note: this library returns 0 rather than null to avoid nullPointExceptions
     */
    public static String locationJSON(String provider, Location location, boolean cached) {

        final JSONObject json = new JSONObject();

        if(location != null){
            try {

                json.put("provider", provider);
                json.put("latitude", location.getLatitude());
                json.put("longitude", location.getLongitude());
                json.put("altitude", location.getAltitude());
                json.put("accuracy", location.getAccuracy());
                json.put("bearing", location.getBearing());
                json.put("speed", location.getSpeed());
                json.put("timestamp", location.getTime());
                json.put("cached", cached);
            }
            catch (JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }

        return json.toString();
    }

    /**
     * Converts location data into a JSON form that can be consumed within a JavaScript application
     * @param provider Indicates if this location is coming from gps or network provider
     * @param location The android Location
     * @param cached Indicates if the value was pulled from the device cache or not
     * @param buffer Boolean indicates whether or not buffering is activated
     * @param bufferLat The buffer's geometric latitudinal center.
     * @param bufferedLon The buffer's geometric longitudinal center.
     * @param bufferedAccuracy The buffer's average accuracy.
     * @param bufferSize The number of elements within the buffer
     * @return Location data. Note: this library returns 0 rather than null to avoid nullPointExceptions
     */
    public static String locationJSON(
            String provider,
            Location location,
            boolean cached,
            boolean buffer,
            double bufferLat,
            double bufferedLon,
            float bufferedAccuracy,
            int bufferSize) {

        final JSONObject json = new JSONObject();

        if(location != null){
            try {

                json.put("provider", provider);
                json.put("latitude", location.getLatitude());
                json.put("longitude", location.getLongitude());
                json.put("altitude", location.getAltitude());
                json.put("accuracy", location.getAccuracy());
                json.put("bearing", location.getBearing());
                json.put("speed", location.getSpeed());
                json.put("timestamp", location.getTime());
                json.put("cached", cached);
                json.put("buffer", buffer);
                json.put("bufferSize", bufferSize);
                json.put("bufferedLatitude", bufferLat);
                json.put("bufferedLongitude", bufferedLon);
                json.put("bufferedAccuracy", bufferedAccuracy);
            }
            catch (JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }

        return json.toString();
    }

    /**
     * Converts CellInfoCdma into JSON
     * @param cellInfo
     * @return JSON
     */
    public static String cellInfoCDMAJSON(CellInfoCdma cellInfo){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        if(cellInfo != null){
            try {
                json.put("provider", CELLINFO_PROVIDER);
                json.put("type", CDMA);
                json.put("timestamp", calendar.getTimeInMillis());

                CellIdentityCdma identityCdma = cellInfo.getCellIdentity();

                json.put("latitude", CdmaCellLocation.convertQuartSecToDecDegrees(identityCdma.getLatitude()));
                json.put("longitude", CdmaCellLocation.convertQuartSecToDecDegrees(identityCdma.getLongitude()));
                json.put("basestationId", identityCdma.getBasestationId());
                json.put("networkId", identityCdma.getNetworkId());
                json.put("systemId", identityCdma.getSystemId());
            }
            catch(JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }
        return json.toString();
    }

    /**
     * Converts CellInfoWcdma into JSON
     * Some devices may not work correctly:
     * - Reference 1: https://code.google.com/p/android/issues/detail?id=191492
     * - Reference 2: http://stackoverflow.com/questions/17815062/cellidentitygsm-on-android
     * @param cellInfo
     * @return JSON
     */
    public static String cellInfoWCDMAJSON(CellInfoWcdma cellInfo){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        if(cellInfo != null){
            try {
                json.put("provider", CELLINFO_PROVIDER);
                json.put("type", WCDMA);
                json.put("timestamp", calendar.getTimeInMillis());

                CellIdentityWcdma identityWcdma = cellInfo.getCellIdentity();

                json.put("cid", identityWcdma.getCid());
                json.put("lac", identityWcdma.getLac());
                json.put("mcc", identityWcdma.getMcc());
                json.put("mnc", identityWcdma.getMnc());
                json.put("psc", identityWcdma.getPsc());
            }
            catch(JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }
        return json.toString();
    }

    /**
     * Converts CellInfoGsm into JSON
     * @param cellInfo
     * @return JSON
     */
    public static String cellInfoGSMJSON(CellInfoGsm cellInfo){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        if(cellInfo != null){
            try {
                json.put("provider", CELLINFO_PROVIDER);
                json.put("type", GSM);
                json.put("timestamp", calendar.getTimeInMillis());

                CellIdentityGsm identityGsm = cellInfo.getCellIdentity();

                json.put("cid", identityGsm.getCid());
                json.put("lac", identityGsm.getLac());
                json.put("mcc", identityGsm.getMcc());
                json.put("mnc", identityGsm.getMnc());
            }
            catch(JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }
        return json.toString();
    }

    /**
     * Converts CellInfoLte into JSON
     * @param cellInfo
     * @return JSON
     */
    public static String cellInfoLTEJSON(CellInfoLte cellInfo){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        if(cellInfo != null){
            try {
                json.put("provider", CELLINFO_PROVIDER);
                json.put("type", LTE);
                json.put("timestamp", calendar.getTimeInMillis());

                final CellIdentityLte identityLte = cellInfo.getCellIdentity();

                json.put("ci", identityLte.getCi());
                json.put("mcc", identityLte.getMcc());
                json.put("mnc", identityLte.getMnc());
                json.put("pci", identityLte.getPci());
                json.put("tac", identityLte.getTac());
            }
            catch(JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }
        return json.toString();
    }

    /**
     * Parses data from PhoneStateListener.LISTEN_CELL_LOCATION.onCellLocationChanged
     * http://developer.android.com/reference/android/telephony/cdma/CdmaCellLocation.html
     * @param location
     * @return JSON
     */
    public static String cdmaCellLocationJSON(CdmaCellLocation location){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        if(location != null){
            try {
                json.put("provider", CELLLOCATION_PROVIDER);
                json.put("type", CDMA);
                json.put("timestamp", calendar.getTimeInMillis());
                json.put("baseStationId", location.getBaseStationId()); // -1 if unknown
                json.put("networkId", location.getNetworkId()); // -1 if unknown
                json.put("systemId", location.getSystemId()); // -1 if unknown
                json.put("baseStationLatitude", CdmaCellLocation.convertQuartSecToDecDegrees(location.getBaseStationLatitude()));
                json.put("baseStationLongitude", CdmaCellLocation.convertQuartSecToDecDegrees(location.getBaseStationLongitude()));
            }
            catch(JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }

        return json.toString();
    }

    /**
     * Parses data from PhoneStateListener.LISTEN_CELL_LOCATION.onCellLocationChanged
     * http://developer.android.com/reference/android/telephony/cdma/CdmaCellLocation.html
     * @param location
     * @return JSON
     */
    public static String gsmCellLocationJSON(GsmCellLocation location){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        if(location != null){
            try {
                json.put("provider", CELLLOCATION_PROVIDER);
                json.put("type", GSM);
                json.put("timestamp", calendar.getTimeInMillis());
                json.put("cid", location.getCid());
                json.put("lac", location.getLac());
                json.put("psc", location.getPsc());
            }
            catch(JSONException exc) {
                Log.d(TAG, exc.getMessage());
            }
        }

        return json.toString();
    }

    /**
     * Converts GpsStatus into JSON.
     * @param gpsStatus Send a GpsStatus whenever the GPS fires
     * @return JSON representation of the satellite data
     */
    public static String satelliteDataJSON(GpsStatus gpsStatus){

        final Calendar calendar = Calendar.getInstance();
        final JSONObject json = new JSONObject();

        try {
            json.put("provider", SATELLITE_PROVIDER);
            json.put("timestamp", calendar.getTimeInMillis());

            if(gpsStatus.getSatellites() != null) {
                int count = 0;
                final int timeToFirstFix = gpsStatus.getTimeToFirstFix();

                for(GpsSatellite sat: gpsStatus.getSatellites() ){
                    final JSONObject satelliteInfo = new JSONObject();

                    satelliteInfo.put("PRN", sat.getPrn());
                    satelliteInfo.put("timeToFirstFix", timeToFirstFix);
                    satelliteInfo.put("usedInFix", sat.usedInFix());
                    satelliteInfo.put("azimuth", sat.getAzimuth());
                    satelliteInfo.put("elevation", sat.getElevation());
                    satelliteInfo.put("hasEphemeris", sat.hasEphemeris());
                    satelliteInfo.put("hasAlmanac", sat.hasAlmanac());
                    satelliteInfo.put("SNR", sat.getSnr());

                    json.put(Integer.toString(count), satelliteInfo);

                    count++;
                }
            }
        }
        catch (JSONException exc){
            Log.d(TAG, exc.getMessage());
        }

        return json.toString();
    }

    /**
     * Helper method for reporting errors coming off a location provider
     * @param provider Indicates if this error is coming from gps or network provider
     * @param error The actual error being thrown by the provider
     * @return Error string
     */
    public static String errorJSON(String provider, String error) {

        final JSONObject json = new JSONObject();

        try {
            json.put("provider", provider);
            json.put("error", error);
        }
        catch (JSONException exc) {
            Log.d(TAG, exc.getMessage());
        }

        return json.toString();
    }
}
