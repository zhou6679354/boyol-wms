package org.shrek.hadata.service.reiley.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * 奇门发货订单
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月23日 09:56
 */
@Data
@JacksonXmlRootElement(localName ="request")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrderRequest {
    private DeliveryOrderBean deliveryOrder;
    @JacksonXmlElementWrapper(localName ="orderLines")
    @JacksonXmlProperty(localName = "orderLine")
    private List<OrderLinesBean> orderLines;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryOrderBean {
        //发货单信息
        private String deliveryOrderCode;
        //原出库单号(ERP分配)
        private String preDeliveryOrderCode;
        //原出库单号(WMS分配)
        private String preDeliveryOrderId;
        //出库单类型
        private String orderType;
        //仓库编码
        private String warehouseCode;
        //订单标记
        private String orderFlag;
        //订单来源平台编码
        private String sourcePlatformCode;
        //订单来源平台名称
        private String sourcePlatformName;
        //发货单创建时间
        private String createTime;
        //前台订单/店铺订单的创建时间/下单时间
        private String placeOrderTime;
        //订单支付时间
        private String payTime;
        //支付平台交易号
        private String payNo;
        //操作员(审核员)编码
        private String operatorCode;
        //操作员(审核员)名称
        private String operatorName;
        //操作(审核)时间
        private String operateTime;
        //店铺名称
        private String shopNick;
        //卖家名称
        private String sellerNick;
        //买家昵称
        private String buyerNick;
        //订单总金额
        private String totalAmount;
        //商品总金额(元)
        private String itemAmount;
        //订单折扣金额(元)
        private String discountAmount;
        //快递费用(元)
        private String freight;
        //应收金额
        private String arAmount;
        //已收金额
        private String gotAmount;
        //COD服务费
        private String serviceFee;
        //物流公司编码
        private String logisticsCode;
        //物流公司名称
        private String logisticsName;
        //运单号
        private String expressCode;
        //快递区域编码
        private String logisticsAreaCode;
        //发货要求列表(类)
        private DeliveryRequirementsBean deliveryRequirements;
        //发件人信息(类)
        private SenderInfoBean senderInfo;
        //收件人信息(类)
        private ReceiverInfoBean receiverInfo;
        //是否紧急(Y/N;默认为N)
        private String isUrgency;
        //是否需要发票(Y/N;默认为N)
        private String invoiceFlag;
        //发票信息(类)
        @JacksonXmlElementWrapper(localName ="invoices")
        @JacksonXmlProperty(localName = "invoice")
        private List<InvoicesBean> invoices;
        //是否需要保险(Y/N;默认为N)
        private String insuranceFlag;
        //保险信息(类)
        private InsuranceBean insurance;
        //买家留言
        private String buyerMessage;
        //卖家留言
        private String sellerMessage;
        //备注
        private String remark;
        //服务编码
        private String serviceCode;
        //	旧版本货主编码
        private String ownerCode;

        /**
         * 发货要求列表
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DeliveryRequirementsBean {
            private String remark;
            //投递时延要求
            private String scheduleType;
            //要求送达日期
            private String scheduleDay;
            //投递时间范围要求
            private String scheduleStartTime;
            //投递时间范围要求
            private String scheduleEndTime;
            //发货服务类型
            private String deliveryType;
        }

        /**
         * 发件人信息
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SenderInfoBean {
            //公司名称
            private String company;
            //姓名
            private String name;
            //邮编
            private String zipCode;
            //固定电话
            private String tel;
            //移动电话
            private String mobile;
            //电子邮箱
            private String email;
            //国家二字码
            private String countryCode;
            //省份
            private String province;
            //城市
            private String city;
            //区域
            private String area;
            //村镇
            private String town;
            //详细地址
            private String detailAddress;
        }

        /**
         * 收件人信息
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ReceiverInfoBean {
            //公司名称
            private String company;
            //姓名
            private String name;
            //邮编
            private String zipCode;
            //固定电话
            private String tel;
            //移动电话
            private String mobile;
            //收件人证件类型(1-身份证、2-军官证、3-护照、4-其他)
            private String idType;
            //收件人证件号码
            private String idNumber;
            //电子邮箱
            private String email;
            //国家二字码
            private String countryCode;
            //省份
            private String province;
            //城市
            private String city;
            //区域
            private String area;
            //村镇
            private String town;
            //详细地址
            private String detailAddress;


        }

        /**
         * 发票信息
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InvoicesBean {
            //发票类型
            private String type;
            //发票抬头
            private String header;
            //发票总金额
            private String amount;
            //发票内容
            private String content;
            //税号
            private String taxNumber;
        }

        /**
         * 保险信息
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InsuranceBean {
            //保险类型
            private String type;
            //保险金额
            private String amount;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderLinesBean {
        //单据行号
        private String orderLineNo;
        //交易平台订单
        private String sourceOrderCode;
        //交易平台子订单编码
        private String subSourceOrderCode;
        //支付平台交易号
        private String payNo;
        //货主编码
        private String ownerCode;
        //商品编码
        private String itemCode;
        //仓储系统商品编码
        private String itemId;
        //库存类型
        private String inventoryType;
        //商品名称
        private String itemName;
        //交易平台商品编码
        private String extCode;
        //应发商品数量
        private String planQty;
        //零售价
        private String retailPrice;
        //实际成交价
        private String actualPrice;
        //单件商品折扣金额
        private String discountAmount;
        //批次编码
        private String batchCode;
        //生产日期
        private String productDate;
        //过期日期
        private String expireDate;
        //生产批号
        private String produceCode;
    }

}

