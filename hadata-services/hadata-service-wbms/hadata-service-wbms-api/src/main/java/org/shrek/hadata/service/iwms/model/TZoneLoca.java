package org.shrek.hadata.service.iwms.model;

import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_zone_loca")
public class TZoneLoca extends BasicEntity {
    @Id
    @Column(name = "location_id")
    private String locationId;

    @Id
    private String zone;

    @Id
    @Column(name = "wh_id")
    private String whId;

    @Column(name = "pick_seq")
    private String pickSeq;

    /**
     * @return location_id
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @param locationId
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * @return zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * @param zone
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

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
     * @return pick_seq
     */
    public String getPickSeq() {
        return pickSeq;
    }

    /**
     * @param pickSeq
     */
    public void setPickSeq(String pickSeq) {
        this.pickSeq = pickSeq;
    }
}