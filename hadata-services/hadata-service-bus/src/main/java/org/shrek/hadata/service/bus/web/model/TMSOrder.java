package org.shrek.hadata.service.bus.web.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;
import java.util.Date;
import java.util.List;

@Data
public class TMSOrder extends BasicEntity {
    private int orderId;
    private String whId;
    private String orderNumber;


    private String storeOrderNumber;


    private int typeId;


    private int customerId;


    private String custPoNumber;


    private String customerName;


    private String customerPhone;


    private String customerFax;


    private String customerEmail;

    private String department;


    private String loadId;


    private int loadSeq;


    private String bolNumber;


    private String proNumber;


    private String masterBolNumber;

    private String carrier;


    private String carrierScac;


    private String freightTerms;

    private String rush;

    private String priority;


    private Date orderDate;


    private Date arriveDate;


    private Date actualArrivalDate;


    private Date datePicked;


    private Date dateExpected;


    private Date promisedDate;

    private double weight;


    private double cubicVolume;

    private int containers;

    private String backorder;


    private String prePaid;


    private double codAmount;


    private double insuranceAmount;


    private double pipAmount;


    private double freightCost;

    private String region;


    private String billToCode;


    private String billToName;


    private String billToAddr1;


    private String billToAddr2;


    private String billToAddr3;


    private String billToCity;


    private String billToState;


    private String billToZip;


    private String billToCountryCode;


    private String billToCountryName;


    private String billToPhone;


    private String shipToCode;


    private String shipToName;


    private String shipToAddr1;


    private String shipToAddr2;


    private String shipToAddr3;


    private String shipToCity;

    private String shipToState;


    private String shipToZip;


    private String shipToCountryCode;


    private String shipToCountryName;


    private String shipToPhone;


    private String deliveryName;


    private String deliveryAddr1;


    private String deliveryAddr2;


    private String deliveryAddr3;


    private String deliveryCity;


    private String deliveryState;


    private String deliveryZip;


    private String deliveryCountryCode;


    private String deliveryCountryName;


    private String deliveryPhone;


    private String billFrghtToCode;


    private String billFrghtToName;


    private String billFrghtToAddr1;


    private String billFrghtToAddr2;


    private String billFrghtToAddr3;


    private String billFrghtToCity;


    private String billFrghtToState;


    private String billFrghtToZip;


    private String billFrghtToCountryCode;


    private String billFrghtToCountryName;


    private String billFrghtToPhone;


    private String returnToCode;


    private String returnToName;


    private String returnToAddr1;


    private String returnToAddr2;


    private String returnToAddr3;


    private String returnToCity;


    private String returnToState;


    private String returnToZip;


    private String returnToCountryCode;


    private String returnToCountryName;


    private String returnToPhone;


    private String rmaNumber;


    private Date rmaExpirationDate;


    private String cartonLabel;


    private String verFlag;


    private int fullPallets;


    private String hazFlag;


    private double orderWgt;

    private String status;

    private String zone;


    private String dropShip;


    private String lockFlag;


    private String partialOrderFlag;


    private Date earliestShipDate;


    private Date latestShipDate;


    private Date actualShipDate;


    private Date earliestDeliveryDate;


    private Date latestDeliveryDate;


    private Date actualDeliveryDate;

    private String route;


    private double baselineRate;


    private double planningRate;


    private int carrierId;


    private int manifestCarrierId;


    private int shipViaId;


    private String displayOrderNumber;


    private String clientCode;


    private String clientName;


    private String shipToResidentialFlag;


    private String carrierMode;


    private String serviceLevel;


    private String shipToAttention;


    private Date earliestApptTime;


    private Date latestApptTime;


    private String paymentTerms;


    private String orderSerialNumber;


    private Date createDate;


    private String carrierNumber;


    private String plateNumber;

    private String interfaceId;
    private String isSendBack;
    private String massiveControl;
    private int tmsSynTimes;
    private int tmsSynStatus;
    private Date tmsSynTime;
    private String tmsSynRemark;
    private String customerCode;
    private List<TMSOrderDetail> orderDetailList;
}