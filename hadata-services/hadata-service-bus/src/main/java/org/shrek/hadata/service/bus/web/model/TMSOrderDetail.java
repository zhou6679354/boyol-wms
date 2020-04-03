package org.shrek.hadata.service.bus.web.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;
import java.util.Date;

@Data

public class TMSOrderDetail extends BasicEntity {


    private int orderDetailId;


    private int orderId;


    private int itemMasterId;


    private String whId;


    private String orderNumber;


    private String lineNumber;


    private String itemNumber;
    private String itemName;


    private double boQty;


    private String boDescription;


    private double boWeight;

    private double qty;


    private double afoPlanQty;


    private String unitPack;


    private double itemWeight;


    private double itemTareWeight;


    private String hazMaterial;


    private String bOLClass;


    private String bOLLine1;


    private String bOLLine2;


    private String bOLLine3;


    private String bOLPlacCode;


    private String bOLPlacDesc;


    private String bOLCode;


    private double qtyShipped;


    private String lineType;


    private String itemDescription;


    private String stackingSeq;


    private String custPart;


    private String lotNumber;


    private String pickingFlow;


    private double unitWeight;


    private double unitVolume;


    private double extendedWeight;


    private double extendedVolume;


    private double overAllocQty;


    private Date dateExpected;


    private String orderUom;


    private String hostWaveId;


    private double tranPlanQty;


    private String useShippableUom;


    private double unitInsuranceAmount;


    private long storedAttributeId;


    private String holdReasonId;


    private String poNumber;


    private String poLineNumber;


    private String poItemNumber;


    private String cancelFlag;


    private String oriLineNumber;

    private String zone;


    private String pickLocation;


    private String huId;


    private long storageType;

    private Date productionDate;

    private Date expirationDate;

    private String measuringNumber;


    private String colorNumber;

    private String stCode;

    private String stName;

}