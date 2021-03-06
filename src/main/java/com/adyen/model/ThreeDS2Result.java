/*
 *                       ######
 *                       ######
 * ############    ####( ######  #####. ######  ############   ############
 * #############  #####( ######  #####. ######  #############  #############
 *        ######  #####( ######  #####. ######  #####  ######  #####  ######
 * ###### ######  #####( ######  #####. ######  #####  #####   #####  ######
 * ###### ######  #####( ######  #####. ######  #####          #####  ######
 * #############  #############  #############  #############  #####  ######
 *  ############   ############  #############   ############  #####  ######
 *                                      ######
 *                               #############
 *                               ############
 *
 * Adyen Java API Library
 *
 * Copyright (c) 2019 Adyen B.V.
 * This file is open source and available under the MIT license.
 * See the LICENSE file for more info.
 */
package com.adyen.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import static com.adyen.constants.TextConstants.LINE_BREAK;
import static com.adyen.util.Util.toIndentedString;

/**
 * ThreeDS2Result
 */
public class ThreeDS2Result {
    @SerializedName("authenticationValue")
    private String authenticationValue = null;

    @SerializedName("cavvAlgorithm")
    private String cavvAlgorithm = null;

    @SerializedName("dsTransID")
    private String dsTransID = null;

    @SerializedName("eci")
    private String eci = null;

    @SerializedName("messageVersion")
    private String messageVersion = null;

    @SerializedName("threeDSServerTransID")
    private String threeDSServerTransID = null;

    @SerializedName("timestamp")
    private String timestamp = null;

    @SerializedName("transStatus")
    private String transStatus = null;

    @SerializedName("transStatusReason")
    private String transStatusReason = null;

    @SerializedName("whiteListStatus")
    private String whiteListStatus = null;

    public ThreeDS2Result authenticationValue(String authenticationValue) {
        this.authenticationValue = authenticationValue;
        return this;
    }

    /**
     * The &#x60;authenticationValue&#x60; value as defined in the 3D Secure 2 specification.
     * @return authenticationValue
     **/
    public String getAuthenticationValue() {
        return authenticationValue;
    }

    public void setAuthenticationValue(String authenticationValue) {
        this.authenticationValue = authenticationValue;
    }

    public ThreeDS2Result cavvAlgorithm(String cavvAlgorithm) {
        this.cavvAlgorithm = cavvAlgorithm;
        return this;
    }

    /**
     * The algorithm used by the ACS to calculate the authentication value, only for CartesBancaires integrations.
     * @return cavvAlgorithm
     **/
    public String getCavvAlgorithm() {
        return cavvAlgorithm;
    }

    public void setCavvAlgorithm(String cavvAlgorithm) {
        this.cavvAlgorithm = cavvAlgorithm;
    }

    public ThreeDS2Result dsTransID(String dsTransID) {
        this.dsTransID = dsTransID;
        return this;
    }

    /**
     * The &#x60;dsTransID&#x60; value as defined in the 3D Secure 2 specification.
     * @return dsTransID
     **/
    public String getDsTransID() {
        return dsTransID;
    }

    public void setDsTransID(String dsTransID) {
        this.dsTransID = dsTransID;
    }

    public ThreeDS2Result eci(String eci) {
        this.eci = eci;
        return this;
    }

    /**
     * The &#x60;eci&#x60; value as defined in the 3D Secure 2 specification.
     * @return eci
     **/
    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public ThreeDS2Result messageVersion(String messageVersion) {
        this.messageVersion = messageVersion;
        return this;
    }

    /**
     * The &#x60;messageVersion&#x60; value as defined in the 3D Secure 2 specification.
     * @return messageVersion
     **/
    public String getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(String messageVersion) {
        this.messageVersion = messageVersion;
    }

    public ThreeDS2Result threeDSServerTransID(String threeDSServerTransID) {
        this.threeDSServerTransID = threeDSServerTransID;
        return this;
    }

    /**
     * The &#x60;threeDSServerTransID&#x60; value as defined in the 3D Secure 2 specification.
     * @return threeDSServerTransID
     **/
    public String getThreeDSServerTransID() {
        return threeDSServerTransID;
    }

    public void setThreeDSServerTransID(String threeDSServerTransID) {
        this.threeDSServerTransID = threeDSServerTransID;
    }

    public ThreeDS2Result timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * The &#x60;timestamp&#x60; value of the 3D Secure 2 authentication.
     * @return timestamp
     **/
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ThreeDS2Result transStatus(String transStatus) {
        this.transStatus = transStatus;
        return this;
    }

    /**
     * The &#x60;transStatus&#x60; value as defined in the 3D Secure 2 specification.
     * @return transStatus
     **/
    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public ThreeDS2Result transStatusReason(String transStatusReason) {
        this.transStatusReason = transStatusReason;
        return this;
    }

    /**
     * The &#x60;transStatusReason&#x60; value as defined in the 3D Secure 2 specification.
     * @return transStatusReason
     **/
    public String getTransStatusReason() {
        return transStatusReason;
    }

    public void setTransStatusReason(String transStatusReason) {
        this.transStatusReason = transStatusReason;
    }

    public ThreeDS2Result whiteListStatus(String whiteListStatus) {
        this.whiteListStatus = whiteListStatus;
        return this;
    }

    /**
     * The &#x60;whiteListStatus&#x60; value as defined in the 3D Secure 2 specification.
     * @return whiteListStatus
     **/
    public String getWhiteListStatus() {
        return whiteListStatus;
    }

    public void setWhiteListStatus(String whiteListStatus) {
        this.whiteListStatus = whiteListStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ThreeDS2Result threeDS2Result = (ThreeDS2Result) o;
        return Objects.equals(this.authenticationValue, threeDS2Result.authenticationValue) &&
                Objects.equals(this.cavvAlgorithm, threeDS2Result.cavvAlgorithm) &&
                Objects.equals(this.dsTransID, threeDS2Result.dsTransID) &&
                Objects.equals(this.eci, threeDS2Result.eci) &&
                Objects.equals(this.messageVersion, threeDS2Result.messageVersion) &&
                Objects.equals(this.threeDSServerTransID, threeDS2Result.threeDSServerTransID) &&
                Objects.equals(this.timestamp, threeDS2Result.timestamp) &&
                Objects.equals(this.transStatus, threeDS2Result.transStatus) &&
                Objects.equals(this.transStatusReason, threeDS2Result.transStatusReason) &&
                Objects.equals(this.whiteListStatus, threeDS2Result.whiteListStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticationValue, cavvAlgorithm, dsTransID, eci, messageVersion, threeDSServerTransID, timestamp, transStatus, transStatusReason, whiteListStatus);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ThreeDS2Result {").append(LINE_BREAK);

        sb.append("    authenticationValue: ").append(toIndentedString(authenticationValue)).append(LINE_BREAK);
        sb.append("    cavvAlgorithm: ").append(toIndentedString(cavvAlgorithm)).append(LINE_BREAK);
        sb.append("    dsTransID: ").append(toIndentedString(dsTransID)).append(LINE_BREAK);
        sb.append("    eci: ").append(toIndentedString(eci)).append(LINE_BREAK);
        sb.append("    messageVersion: ").append(toIndentedString(messageVersion)).append(LINE_BREAK);
        sb.append("    threeDSServerTransID: ").append(toIndentedString(threeDSServerTransID)).append(LINE_BREAK);
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append(LINE_BREAK);
        sb.append("    transStatus: ").append(toIndentedString(transStatus)).append(LINE_BREAK);
        sb.append("    transStatusReason: ").append(toIndentedString(transStatusReason)).append(LINE_BREAK);
        sb.append("    whiteListStatus: ").append(toIndentedString(whiteListStatus)).append(LINE_BREAK);
        sb.append("}");
        return sb.toString();
    }



}
