package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_wave_master")
public class TWaveMaster extends BasicEntity {
    @Id
    @Column(name = "wave_id")
    private String waveId;
    @Column(name = "status")
    private String status;
    @Column(name = "sequence")
    private int sequence;
    @Column(name = "wh_id")
    private String whId;
    @Column(name = "staging_location")
    private String stagingLocation;
    @Column(name = "door_location")
    private String doorLocation;
    @Column(name = "earliest_ship_date")
    private Date earliestShipDate;
    @Column(name = "latest_ship_date")
    private Date latestShipDate;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "released_date")
    private Date releasedDate;
    @Column(name = "cancle_flag")
    private String cancleFlag;
}
