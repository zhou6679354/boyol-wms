package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "t_order")
public class TOrder extends BasicEntity {

    @Id
    @Column(name = "order_id", updatable = false)
    private Integer orderId;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "store_order_number")
    private String storeOrderNumber;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "cust_po_number")
    private String custPoNumber;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_fax")
    private String customerFax;

    @Column(name = "customer_email")
    private String customerEmail;

    private String department;

    @Column(name = "load_id")
    private String loadId;

    @Column(name = "load_seq")
    private Integer loadSeq;

    @Column(name = "bol_number")
    private String bolNumber;

    @Column(name = "pro_number")
    private String proNumber;

    @Column(name = "master_bol_number")
    private String masterBolNumber;

    private String carrier;

    @Column(name = "carrier_scac")
    private String carrierScac;

    @Column(name = "freight_terms")
    private String freightTerms;

    private String rush;

    private String priority;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "arrive_date")
    private Date arriveDate;

    @Column(name = "actual_arrival_date")
    private Date actualArrivalDate;

    @Column(name = "date_picked")
    private Date datePicked;

    @Column(name = "date_expected")
    private Date dateExpected;

    @Column(name = "promised_date")
    private Date promisedDate;

    private Double weight;

    @Column(name = "cubic_volume")
    private Double cubicVolume;

    private Integer containers;

    private String backorder;

    @Column(name = "pre_paid")
    private String prePaid;

    @Column(name = "cod_amount")
    private Double codAmount;

    @Column(name = "insurance_amount")
    private Double insuranceAmount;

    @Column(name = "pip_amount")
    private Double pipAmount;

    @Column(name = "freight_cost")
    private Double freightCost;

    private String region;

    @Column(name = "bill_to_code")
    private String billToCode;

    @Column(name = "bill_to_name")
    private String billToName;

    @Column(name = "bill_to_addr1")
    private String billToAddr1;

    @Column(name = "bill_to_addr2")
    private String billToAddr2;

    @Column(name = "bill_to_addr3")
    private String billToAddr3;

    @Column(name = "bill_to_city")
    private String billToCity;

    @Column(name = "bill_to_state")
    private String billToState;

    @Column(name = "bill_to_zip")
    private String billToZip;

    @Column(name = "bill_to_country_code")
    private String billToCountryCode;

    @Column(name = "bill_to_country_name")
    private String billToCountryName;

    @Column(name = "bill_to_phone")
    private String billToPhone;

    @Column(name = "ship_to_code")
    private String shipToCode;

    @Column(name = "ship_to_name")
    private String shipToName;

    @Column(name = "ship_to_addr1")
    private String shipToAddr1;

    @Column(name = "ship_to_addr2")
    private String shipToAddr2;

    @Column(name = "ship_to_addr3")
    private String shipToAddr3;

    @Column(name = "ship_to_city")
    private String shipToCity;

    @Column(name = "ship_to_state")
    private String shipToState;

    @Column(name = "ship_to_zip")
    private String shipToZip;

    @Column(name = "ship_to_country_code")
    private String shipToCountryCode;

    @Column(name = "ship_to_country_name")
    private String shipToCountryName;

    @Column(name = "ship_to_phone")
    private String shipToPhone;

    @Column(name = "delivery_name")
    private String deliveryName;

    @Column(name = "delivery_addr1")
    private String deliveryAddr1;

    @Column(name = "delivery_addr2")
    private String deliveryAddr2;

    @Column(name = "delivery_addr3")
    private String deliveryAddr3;

    @Column(name = "delivery_city")
    private String deliveryCity;

    @Column(name = "delivery_state")
    private String deliveryState;

    @Column(name = "delivery_zip")
    private String deliveryZip;

    @Column(name = "delivery_country_code")
    private String deliveryCountryCode;

    @Column(name = "delivery_country_name")
    private String deliveryCountryName;

    @Column(name = "delivery_phone")
    private String deliveryPhone;

    @Column(name = "bill_frght_to_code")
    private String billFrghtToCode;

    @Column(name = "bill_frght_to_name")
    private String billFrghtToName;

    @Column(name = "bill_frght_to_addr1")
    private String billFrghtToAddr1;

    @Column(name = "bill_frght_to_addr2")
    private String billFrghtToAddr2;

    @Column(name = "bill_frght_to_addr3")
    private String billFrghtToAddr3;

    @Column(name = "bill_frght_to_city")
    private String billFrghtToCity;

    @Column(name = "bill_frght_to_state")
    private String billFrghtToState;

    @Column(name = "bill_frght_to_zip")
    private String billFrghtToZip;

    @Column(name = "bill_frght_to_country_code")
    private String billFrghtToCountryCode;

    @Column(name = "bill_frght_to_country_name")
    private String billFrghtToCountryName;

    @Column(name = "bill_frght_to_phone")
    private String billFrghtToPhone;

    @Column(name = "return_to_code")
    private String returnToCode;

    @Column(name = "return_to_name")
    private String returnToName;

    @Column(name = "return_to_addr1")
    private String returnToAddr1;

    @Column(name = "return_to_addr2")
    private String returnToAddr2;

    @Column(name = "return_to_addr3")
    private String returnToAddr3;

    @Column(name = "return_to_city")
    private String returnToCity;

    @Column(name = "return_to_state")
    private String returnToState;

    @Column(name = "return_to_zip")
    private String returnToZip;

    @Column(name = "return_to_country_code")
    private String returnToCountryCode;

    @Column(name = "return_to_country_name")
    private String returnToCountryName;

    @Column(name = "return_to_phone")
    private String returnToPhone;

    @Column(name = "rma_number")
    private String rmaNumber;

    @Column(name = "rma_expiration_date")
    private Date rmaExpirationDate;

    @Column(name = "carton_label")
    private String cartonLabel;

    @Column(name = "ver_flag")
    private String verFlag;

    @Column(name = "full_pallets")
    private Integer fullPallets;

    @Column(name = "haz_flag")
    private String hazFlag;

    @Column(name = "order_wgt")
    private Double orderWgt;

    private String status;

    private String zone;

    @Column(name = "drop_ship")
    private String dropShip;

    @Column(name = "lock_flag")
    private String lockFlag;

    @Column(name = "partial_order_flag")
    private String partialOrderFlag;

    @Column(name = "earliest_ship_date")
    private Date earliestShipDate;

    @Column(name = "latest_ship_date")
    private Date latestShipDate;

    @Column(name = "actual_ship_date")
    private Date actualShipDate;

    @Column(name = "earliest_delivery_date")
    private Date earliestDeliveryDate;

    @Column(name = "latest_delivery_date")
    private Date latestDeliveryDate;

    @Column(name = "actual_delivery_date")
    private Date actualDeliveryDate;

    private String route;

    @Column(name = "baseline_rate")
    private Double baselineRate;

    @Column(name = "planning_rate")
    private Double planningRate;

    @Column(name = "carrier_id")
    private Integer carrierId;

    @Column(name = "manifest_carrier_id")
    private Integer manifestCarrierId;

    @Column(name = "ship_via_id")
    private Integer shipViaId;

    @Column(name = "display_order_number")
    private String displayOrderNumber;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "ship_to_residential_flag")
    private String shipToResidentialFlag;

    @Column(name = "carrier_mode")
    private String carrierMode;

    @Column(name = "service_level")
    private String serviceLevel;

    @Column(name = "ship_to_attention")
    private String shipToAttention;

    @Column(name = "earliest_appt_time")
    private Date earliestApptTime;

    @Column(name = "latest_appt_time")
    private Date latestApptTime;

    @Column(name = "payment_terms")
    private String paymentTerms;

    @Column(name = "order_serial_number")
    private String orderSerialNumber;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "carrier_number")
    private String carrierNumber;

    @Column(name = "plate_number")
    private String plateNumber;

    @Column(name = "interface_id")
    private String interfaceId;

    @Column(name = "is_send_back")
    private String isSendBack;

    @Column(name = "interface_platform")
    private String interfacePlatform;

    @Transient
    private List<TOrderDetail> orderDetailList;
    @Transient
    private List<TOrderConfirm> orderConfirmList;
    @Transient
    private List<TOrderPackage> orderPackageList;



}