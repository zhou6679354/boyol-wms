package org.shrek.hadata.service.iwms.model;

import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_carrier")
public class TCarrier extends BasicEntity {
    @Id
    @Column(name = "carrier_id")
    private Integer carrierId;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "scac_code")
    private String scacCode;

    @Column(name = "transport_mode")
    private String transportMode;

    @Column(name = "carrier_group_id")
    private Integer carrierGroupId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "cdl_verify")
    private String cdlVerify;

    @Column(name = "time_allowed_early")
    private Integer timeAllowedEarly;

    @Column(name = "time_allowed_late")
    private Integer timeAllowedLate;

    private String disposition;

    @Column(name = "default_trailer_type_id")
    private Integer defaultTrailerTypeId;

    @Column(name = "default_priority")
    private String defaultPriority;

    @Column(name = "dock_schedule_method")
    private String dockScheduleMethod;

    private String notes;

    private Date effective;

    private String status;

    @Column(name = "freight_fwd_flag")
    private String freightFwdFlag;

    private String address1;

    private String address2;

    private String address3;

    private String address4;

    private String city;

    private String county;

    private String state;

    private String zip;

    private String country;

    @Column(name = "country_code")
    private String countryCode;

    private String phone;

    private String extension;

    private String fax;

    private String email;

    private String website;

    @Column(name = "manifest_carrier_flag")
    private String manifestCarrierFlag;

    @Column(name = "flagship_carrier_code")
    private String flagshipCarrierCode;

    /**
     * @return carrier_id
     */
    public Integer getCarrierId() {
        return carrierId;
    }

    /**
     * @param carrierId
     */
    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    /**
     * @return carrier_code
     */
    public String getCarrierCode() {
        return carrierCode;
    }

    /**
     * @param carrierCode
     */
    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    /**
     * @return carrier_name
     */
    public String getCarrierName() {
        return carrierName;
    }

    /**
     * @param carrierName
     */
    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    /**
     * @return scac_code
     */
    public String getScacCode() {
        return scacCode;
    }

    /**
     * @param scacCode
     */
    public void setScacCode(String scacCode) {
        this.scacCode = scacCode;
    }

    /**
     * @return transport_mode
     */
    public String getTransportMode() {
        return transportMode;
    }

    /**
     * @param transportMode
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return carrier_group_id
     */
    public Integer getCarrierGroupId() {
        return carrierGroupId;
    }

    /**
     * @param carrierGroupId
     */
    public void setCarrierGroupId(Integer carrierGroupId) {
        this.carrierGroupId = carrierGroupId;
    }

    /**
     * @return contact_name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return cdl_verify
     */
    public String getCdlVerify() {
        return cdlVerify;
    }

    /**
     * @param cdlVerify
     */
    public void setCdlVerify(String cdlVerify) {
        this.cdlVerify = cdlVerify;
    }

    /**
     * @return time_allowed_early
     */
    public Integer getTimeAllowedEarly() {
        return timeAllowedEarly;
    }

    /**
     * @param timeAllowedEarly
     */
    public void setTimeAllowedEarly(Integer timeAllowedEarly) {
        this.timeAllowedEarly = timeAllowedEarly;
    }

    /**
     * @return time_allowed_late
     */
    public Integer getTimeAllowedLate() {
        return timeAllowedLate;
    }

    /**
     * @param timeAllowedLate
     */
    public void setTimeAllowedLate(Integer timeAllowedLate) {
        this.timeAllowedLate = timeAllowedLate;
    }

    /**
     * @return disposition
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * @param disposition
     */
    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    /**
     * @return default_trailer_type_id
     */
    public Integer getDefaultTrailerTypeId() {
        return defaultTrailerTypeId;
    }

    /**
     * @param defaultTrailerTypeId
     */
    public void setDefaultTrailerTypeId(Integer defaultTrailerTypeId) {
        this.defaultTrailerTypeId = defaultTrailerTypeId;
    }

    /**
     * @return default_priority
     */
    public String getDefaultPriority() {
        return defaultPriority;
    }

    /**
     * @param defaultPriority
     */
    public void setDefaultPriority(String defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    /**
     * @return dock_schedule_method
     */
    public String getDockScheduleMethod() {
        return dockScheduleMethod;
    }

    /**
     * @param dockScheduleMethod
     */
    public void setDockScheduleMethod(String dockScheduleMethod) {
        this.dockScheduleMethod = dockScheduleMethod;
    }

    /**
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return effective
     */
    public Date getEffective() {
        return effective;
    }

    /**
     * @param effective
     */
    public void setEffective(Date effective) {
        this.effective = effective;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return freight_fwd_flag
     */
    public String getFreightFwdFlag() {
        return freightFwdFlag;
    }

    /**
     * @param freightFwdFlag
     */
    public void setFreightFwdFlag(String freightFwdFlag) {
        this.freightFwdFlag = freightFwdFlag;
    }

    /**
     * @return address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return address3
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * @param address3
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * @return address4
     */
    public String getAddress4() {
        return address4;
    }

    /**
     * @param address4
     */
    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return county
     */
    public String getCounty() {
        return county;
    }

    /**
     * @param county
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return country_code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @param website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * @return manifest_carrier_flag
     */
    public String getManifestCarrierFlag() {
        return manifestCarrierFlag;
    }

    /**
     * @param manifestCarrierFlag
     */
    public void setManifestCarrierFlag(String manifestCarrierFlag) {
        this.manifestCarrierFlag = manifestCarrierFlag;
    }

    /**
     * @return flagship_carrier_code
     */
    public String getFlagshipCarrierCode() {
        return flagshipCarrierCode;
    }

    /**
     * @param flagshipCarrierCode
     */
    public void setFlagshipCarrierCode(String flagshipCarrierCode) {
        this.flagshipCarrierCode = flagshipCarrierCode;
    }
}