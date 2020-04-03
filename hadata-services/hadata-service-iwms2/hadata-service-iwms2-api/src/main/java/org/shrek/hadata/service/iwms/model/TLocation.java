package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "t_location")
public class TLocation extends BasicEntity {
    @Id
    @Column(name = "location_id")
    private String locationId;

    @Id
    @Column(name = "wh_id")
    private String whId;

    private String description;

    @Column(name = "short_location_id")
    private String shortLocationId;

    private String status;

    private String zone;

    @Column(name = "picking_flow")
    private String pickingFlow;

    @Column(name = "capacity_uom")
    private String capacityUom;

    @Column(name = "capacity_qty")
    private Double capacityQty;

    @Column(name = "stored_qty")
    private Double storedQty;

    private String type;

    @Column(name = "fifo_date")
    private Date fifoDate;

    @Column(name = "cycle_count_class")
    private String cycleCountClass;

    @Column(name = "last_count_date")
    private Date lastCountDate;

    @Column(name = "last_physical_date")
    private Date lastPhysicalDate;

    @Column(name = "user_count")
    private Integer userCount;

    @Column(name = "capacity_volume")
    private Double capacityVolume;

    @Column(name = "time_between_maintenance")
    private Double timeBetweenMaintenance;

    @Column(name = "last_maintained")
    private Date lastMaintained;

    private Double length;

    private Double width;

    private Double height;

    @Column(name = "replenishment_location_id")
    private String replenishmentLocationId;

    @Column(name = "pick_area")
    private String pickArea;

    @Column(name = "allow_bulk_pick")
    private String allowBulkPick;

    @Column(name = "slot_rank")
    private Integer slotRank;

    @Column(name = "slot_status")
    private String slotStatus;

    @Column(name = "item_hu_indicator")
    private String itemHuIndicator;

    private String c1;

    private String c2;

    private String c3;

    @Column(name = "random_cc")
    private Double randomCc;

    @Column(name = "x_coordinate")
    private BigDecimal xCoordinate;

    @Column(name = "y_coordinate")
    private BigDecimal yCoordinate;

    @Column(name = "z_coordinate")
    private BigDecimal zCoordinate;

    @Column(name = "storage_device_id")
    private Integer storageDeviceId;

    public static TLocation newTLocation(String strZone,String id,String flow){
        TLocation tLocation=new TLocation();
        tLocation.setWhId("106");
        tLocation.setLocationId(id);
        tLocation.setDescription(id);
        tLocation.setZone(strZone);
        tLocation.setPickingFlow(flow);
        tLocation.setType("M");
        tLocation.setPickArea("CASE");
        tLocation.setItemHuIndicator("I");
        tLocation.setC2("MSL");
        return tLocation;
    }
}