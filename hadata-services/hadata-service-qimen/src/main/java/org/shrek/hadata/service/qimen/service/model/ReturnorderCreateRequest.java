package org.shrek.hadata.service.qimen.service.model;

import com.qimen.api.QimenRequest;
import com.taobao.api.ApiRuleException;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年12月21日 11:07
 */
@Data
public class ReturnorderCreateRequest extends QimenRequest<ReturnorderCreateResponse> {
    private Map extendProps;
    private List<ReturnorderCreateRequest.Item> itemList;
    private List<ReturnorderCreateRequest.OrderLine> orderLines;
    private ReturnorderCreateRequest.ReturnOrder returnOrder;
    @Override
    public String getApiMethodName() {
        return "taobao.qimen.returnorder.create";
    }

    @Override
    public Class<ReturnorderCreateResponse> getResponseClass() {
        return ReturnorderCreateResponse.class;
    }

    public void check() throws ApiRuleException {
    }

    @Data
    public static class OrderLine {
        @ApiField("actualPrice")
        private String actualPrice;
        @ApiField("actualQty")
        private String actualQty;
        @ApiField("amount")
        private String amount;
        @ApiField("batchCode")
        private String batchCode;
        @ApiListField("batchs")
        @ApiField("batch")
        private List<com.qimen.api.request.ReturnorderCreateRequest.Batch> batchs;
        @ApiField("color")
        private String color;
        @ApiField("deliveryOrderId")
        private String deliveryOrderId;
        @ApiField("discount")
        private String discount;
        @ApiField("discountAmount")
        private String discountAmount;
        @ApiField("discountPrice")
        private String discountPrice;
        @ApiField("exceptionQty")
        private String exceptionQty;
        @ApiField("expireDate")
        private String expireDate;
        @ApiField("extCode")
        private String extCode;
        @ApiField("inventoryType")
        private String inventoryType;
        @ApiField("itemCode")
        private String itemCode;
        @ApiField("itemId")
        private String itemId;
        @ApiField("itemName")
        private String itemName;
        @ApiField("locationCode")
        private String locationCode;
        @ApiField("moveInLocation")
        private String moveInLocation;
        @ApiField("moveOutLocation")
        private String moveOutLocation;
        @ApiField("orderLineNo")
        private String orderLineNo;
        @ApiField("orderSourceCode")
        private String orderSourceCode;
        @ApiField("outBizCode")
        private String outBizCode;
        @ApiField("ownerCode")
        private String ownerCode;
        @ApiField("payNo")
        private String payNo;
        @ApiField("planQty")
        private Long planQty;
        @ApiField("produceCode")
        private String produceCode;
        @ApiField("productCode")
        private String productCode;
        @ApiField("productDate")
        private String productDate;
        @ApiField("purchasePrice")
        private String purchasePrice;
        @ApiField("qrCode")
        private String qrCode;
        @ApiField("quantity")
        private String quantity;
        @ApiField("referencePrice")
        private String referencePrice;
        @ApiField("remark")
        private String remark;
        @ApiField("retailPrice")
        private String retailPrice;
        @ApiField("settlementAmount")
        private String settlementAmount;
        @ApiField("size")
        private String size;
        @ApiField("skuProperty")
        private String skuProperty;
        @ApiField("snList")
        private com.qimen.api.request.ReturnorderCreateRequest.SnList snList;
        @ApiField("sourceOrderCode")
        private String sourceOrderCode;
        @ApiField("standardAmount")
        private String standardAmount;
        @ApiField("standardPrice")
        private String standardPrice;
        @ApiField("status")
        private String status;
        @ApiField("stockInQty")
        private String stockInQty;
        @ApiField("stockOutQty")
        private String stockOutQty;
        @ApiField("subDeliveryOrderId")
        private String subDeliveryOrderId;
        @ApiField("subSourceCode")
        private String subSourceCode;
        @ApiField("subSourceOrderCode")
        private String subSourceOrderCode;
        @ApiField("taobaoItemCode")
        private String taobaoItemCode;
        @ApiField("warehouseCode")
        private String warehouseCode;
        }
    @Data
    public static class SnList {
        @ApiListField("sn")
        @ApiField("string")
        private List<String> sn;

        public SnList() {
        }

        public List<String> getSn() {
            return this.sn;
        }

        public void setSn(List<String> sn) {
            this.sn = sn;
        }
    }
    @Data
    public static class ReturnOrder {
        private Map extendProps;
        @ApiField("actionType")
        private String actionType;
        @ApiField("buyerNick")
        private String buyerNick;
        @ApiField("earliestArrivalTime")
        private String earliestArrivalTime;
        @ApiField("expressCode")
        private String expressCode;
        @ApiField("logisticsCode")
        private String logisticsCode;
        @ApiField("logisticsName")
        private String logisticsName;
        @ApiField("orderConfirmTime")
        private String orderConfirmTime;
        @ApiField("orderFlag")
        private String orderFlag;
        @ApiField("orderType")
        private String orderType;
        @ApiField("outBizCode")
        private String outBizCode;
        @ApiField("ownerCode")
        private String ownerCode;
        @ApiField("planArrivalTime")
        private String planArrivalTime;
        @ApiField("preDeliveryOrderCode")
        private String preDeliveryOrderCode;
        @ApiField("preDeliveryOrderId")
        private String preDeliveryOrderId;
        @ApiField("refOrderCode")
        private String refOrderCode;
        @ApiField("remark")
        private String remark;
        @ApiField("returnOrderCode")
        private String returnOrderCode;
        @ApiField("returnOrderId")
        private String returnOrderId;
        @ApiField("returnOrderStatus")
        private String returnOrderStatus;
        @ApiField("returnReason")
        private String returnReason;
        @ApiField("senderInfo")
        private com.qimen.api.request.ReturnorderCreateRequest.SenderInfo senderInfo;
        @ApiField("sourceOrderCode")
        private String sourceOrderCode;
        @ApiField("supplierCode")
        private String supplierCode;
        @ApiField("supplierName")
        private String supplierName;
        @ApiField("warehouseCode")
        private String warehouseCode;
        }
    @Data
    public static class SenderInfo {
        @ApiField("area")
        private String area;
        @ApiField("birthDate")
        private String birthDate;
        @ApiField("carNo")
        private String carNo;
        @ApiField("career")
        private String career;
        @ApiField("city")
        private String city;
        @ApiField("company")
        private String company;
        @ApiField("countryCode")
        private String countryCode;
        @ApiField("countryCodeCiq")
        private String countryCodeCiq;
        @ApiField("countryCodeCus")
        private String countryCodeCus;
        @ApiField("detailAddress")
        private String detailAddress;
        @ApiField("email")
        private String email;
        @ApiField("fax")
        private String fax;
        @ApiField("gender")
        private String gender;
        @ApiField("id")
        private String id;
        @ApiField("idNumber")
        private String idNumber;
        @ApiField("idType")
        private String idType;
        @ApiField("mobile")
        private String mobile;
        @ApiField("name")
        private String name;
        @ApiField("nick")
        private String nick;
        @ApiField("province")
        private String province;
        @ApiField("remark")
        private String remark;
        @ApiField("tel")
        private String tel;
        @ApiField("town")
        private String town;
        @ApiField("zipCode")
        private String zipCode;
    }
    @Data
    public static class Item {
        @ApiField("actualAmount")
        private String actualAmount;
        @ApiField("actualQty")
        private String actualQty;
        @ApiField("adventLifecycle")
        private String adventLifecycle;
        @ApiField("amount")
        private String amount;
        @ApiField("approvalNumber")
        private String approvalNumber;
        @ApiField("barCode")
        private String barCode;
        @ApiField("batchCode")
        private String batchCode;
        @ApiField("batchRemark")
        private String batchRemark;
        @ApiListField("batchs")
        @ApiField("batch")
        private List<com.qimen.api.request.ReturnorderCreateRequest.Batch> batchs;
        @ApiField("brandCode")
        private String brandCode;
        @ApiField("brandName")
        private String brandName;
        @ApiField("categoryId")
        private String categoryId;
        @ApiField("categoryName")
        private String categoryName;
        @ApiField("changeTime")
        private String changeTime;
        @ApiField("channelCode")
        private String channelCode;
        @ApiField("color")
        private String color;
        @ApiField("costPrice")
        private String costPrice;
        @ApiField("defectiveQty")
        private String defectiveQty;
        @ApiField("diffQuantity")
        private String diffQuantity;
        @ApiField("discount")
        private String discount;
        @ApiField("discountPrice")
        private String discountPrice;
        @ApiField("englishName")
        private String englishName;
        @ApiField("exCode")
        private String exCode;
        @ApiField("expireDate")
        private String expireDate;
        @ApiField("extCode")
        private String extCode;
        @ApiField("goodsCode")
        private String goodsCode;
        @ApiField("grossWeight")
        private String grossWeight;
        @ApiField("height")
        private String height;
        @ApiField("inventoryType")
        private String inventoryType;
        @ApiField("isAreaSale")
        private String isAreaSale;
        @ApiField("isBatchMgmt")
        private String isBatchMgmt;
        @ApiField("isFragile")
        private String isFragile;
        @ApiField("isHazardous")
        private String isHazardous;
        @ApiField("isSNMgmt")
        private String isSNMgmt;
        @ApiField("isShelfLifeMgmt")
        private String isShelfLifeMgmt;
        @ApiField("isSku")
        private String isSku;
        @ApiField("itemCode")
        private String itemCode;
        @ApiField("itemId")
        private String itemId;
        @ApiField("itemName")
        private String itemName;
        @ApiField("itemType")
        private String itemType;
        @ApiField("lackQty")
        private String lackQty;
        @ApiField("latestUpdateTime")
        private String latestUpdateTime;
        @ApiField("length")
        private String length;
        @ApiField("lockQuantity")
        private String lockQuantity;
        @ApiField("lockupLifecycle")
        private String lockupLifecycle;
        @ApiField("netWeight")
        private String netWeight;
        @ApiField("normalQty")
        private String normalQty;
        @ApiField("orderCode")
        private String orderCode;
        @ApiField("orderLineNo")
        private String orderLineNo;
        @ApiField("orderType")
        private String orderType;
        @ApiField("originAddress")
        private String originAddress;
        @ApiField("originCode")
        private String originCode;
        @ApiField("outBizCode")
        private String outBizCode;
        @ApiField("ownerCode")
        private String ownerCode;
        @ApiField("packCode")
        private String packCode;
        @ApiField("packageMaterial")
        private String packageMaterial;
        @ApiField("paperQty")
        private String paperQty;
        @ApiField("pcs")
        private String pcs;
        @ApiField("planQty")
        private String planQty;
        @ApiField("price")
        private String price;
        @ApiField("priceAdjustment")
        private com.qimen.api.request.ReturnorderCreateRequest.PriceAdjustment priceAdjustment;
        @ApiField("pricingCategory")
        private String pricingCategory;
        @ApiField("produceCode")
        private String produceCode;
        @ApiField("productCode")
        private String productCode;
        @ApiField("productDate")
        private String productDate;
        @ApiField("purchasePrice")
        private String purchasePrice;
        @ApiField("quantity")
        private String quantity;
        @ApiField("reason")
        private String reason;
        @ApiField("receiveQty")
        private String receiveQty;
        @ApiField("referencePrice")
        private String referencePrice;
        @ApiField("rejectLifecycle")
        private String rejectLifecycle;
        @ApiField("remark")
        private String remark;
        @ApiField("retailPrice")
        private String retailPrice;
        @ApiField("safetyStock")
        private String safetyStock;
        @ApiField("seasonCode")
        private String seasonCode;
        @ApiField("seasonName")
        private String seasonName;
        @ApiField("shelfLife")
        private String shelfLife;
        @ApiField("shortName")
        private String shortName;
        @ApiField("size")
        private String size;
        @ApiField("skuProperty")
        private String skuProperty;
        @ApiField("sn")
        private String sn;
        @ApiField("snCode")
        private String snCode;
        @ApiField("sourceOrderCode")
        private String sourceOrderCode;
        @ApiField("standardPrice")
        private String standardPrice;
        @ApiField("stockStatus")
        private String stockStatus;
        @ApiField("stockUnit")
        private String stockUnit;
        @ApiField("subSourceOrderCode")
        private String subSourceOrderCode;
        @ApiField("supplierCode")
        private String supplierCode;
        @ApiField("supplierName")
        private String supplierName;
        @ApiField("tagPrice")
        private String tagPrice;
        @ApiField("tareWeight")
        private String tareWeight;
        @ApiField("tempRequirement")
        private String tempRequirement;
        @ApiField("title")
        private String title;
        @ApiField("unit")
        private String unit;
        @ApiField("volume")
        private String volume;
        @ApiField("warehouseCode")
        private String warehouseCode;
        @ApiField("width")
        private String width;
    }
    @Data
    public static class PriceAdjustment {
        @ApiField("discount")
        private String discount;
        @ApiField("endDate")
        private String endDate;
        @ApiField("remark")
        private String remark;
        @ApiField("standardPrice")
        private String standardPrice;
        @ApiField("startDate")
        private String startDate;
        @ApiField("type")
        private String type;
    }
}
