package org.shrek.hadata.service.hwms.model;

import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_whse")
public class TWhse extends BasicEntity {
    @Id
    @Column(name = "wh_id")
    private String whId;

    private String code;

    private String name;

    private String addr1;

    private String addr2;

    private String addr3;

    private String city;

    private String state;

    private String zip;

    private String phone;

    private String fax;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "http_address")
    private String httpAddress;

    /**
     * @return wh_id
     */
    public String getWhId() {
        return whId;
    }

    /**
     * @param whId
     */
    public void setWhId(String whId) {
        this.whId = whId;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return addr1
     */
    public String getAddr1() {
        return addr1;
    }

    /**
     * @param addr1
     */
    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    /**
     * @return addr2
     */
    public String getAddr2() {
        return addr2;
    }

    /**
     * @param addr2
     */
    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    /**
     * @return addr3
     */
    public String getAddr3() {
        return addr3;
    }

    /**
     * @param addr3
     */
    public void setAddr3(String addr3) {
        this.addr3 = addr3;
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
     * @return country_name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return http_address
     */
    public String getHttpAddress() {
        return httpAddress;
    }

    /**
     * @param httpAddress
     */
    public void setHttpAddress(String httpAddress) {
        this.httpAddress = httpAddress;
    }
}