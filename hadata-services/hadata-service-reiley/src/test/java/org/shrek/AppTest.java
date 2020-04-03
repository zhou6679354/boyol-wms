package org.shrek;

import com.qimencloud.api.DefaultQimenCloudClient;
import com.qimencloud.api.QimenCloudClient;
import com.qimencloud.api.QimenCloudRequest;
import com.qimencloud.api.QimenCloudResponse;
import com.taobao.api.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.reiley.Application;
import org.shrek.hadata.service.reiley.service.erp.WmsToErpB2CScheduled;
import org.shrek.hadata.service.reiley.service.erp.WmsToErpScheduled;
import org.shrek.hadata.service.reiley.service.erp.model.ErpInBoundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Import(Application.class)
public class AppTest {

    String appkey = "25024325";
    String appSecret = "bcf66c17d34c0eecd03fe69bc055446a";
    String format = "xml";

    String url = "http://qimen.api.taobao.com/router/qimen/service";
    String apiName = "taobao.qimen.returnorder.confirm";
    String targetAppkey = "23036663";
    @Autowired
    WmsToErpB2CScheduled wmsToErpB2CScheduled;
    @Autowired
    WmsToErpScheduled wmsToErpScheduled;
    @Test
    public void test() {

        QimenCloudClient client = new DefaultQimenCloudClient(url, appkey, appSecret, format);
        QimenCloudRequest request = new QimenCloudRequest();
        request.setApiMethodName(apiName);
        request.setTargetAppKey(targetAppkey);
        request.addQueryParam("customerId", "reiley");
        request.setTopApiFormat("xml");
        request.setBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "\n" +
                "<request>\n" +
                "  <returnOrder>\n" +
                "    <returnOrderCode>R8091800010</returnOrderCode>\n" +
                "    <warehouseCode>REILEY</warehouseCode>\n" +
                "    <orderType>THRK</orderType>\n" +
                "\t<orderConfirmTime>20180918175016</orderConfirmTime>\n" +
                "    <logisticsCode>OTHER</logisticsCode>\n" +
                "    <expressCode>70370606383329</expressCode>\n" +
                "    <remark/>\n" +
                "    <senderInfo>\n" +
                "      <name>贾进阳</name>\n" +
                "      <zipCode>045000</zipCode>\n" +
                "      <tel/>\n" +
                "      <mobile>15235304306</mobile>\n" +
                "      <province>山西省</province>\n" +
                "      <city>阳泉市</city>\n" +
                "      <area>城区</area>\n" +
                "      <detailAddress>北大街街道巨兴小区3组团3号楼1单元6号家</detailAddress>\n" +
                "    </senderInfo>\n" +
                "  </returnOrder>\n" +
                "  <orderLines>\n" +
                "    <orderLine>\n" +
                "      <orderLineNo>14</orderLineNo>\n" +
                "      <itemCode>12263190</itemCode>\n" +
                "      <planQty>1</planQty>\n" +
                "      <actualQty>1</actualQty>\n" +
                "    </orderLine>\n" +
                "  </orderLines>\n" +
                "</request>");
        QimenCloudResponse response;
        try {
            response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test1() throws ApiException {
        String json="[\"R8102200679366-601\",\n" +
                "\"R8102200679375-601\",\n" +
                "\"R8102200679384-601\",\n" +
                "\"R8102200679393-601\",\n" +
                "\"R8102200679409-601\",\n" +
                "\"R8102200679418-601\",\n" +
                "\"R8102200679427-601\",\n" +
                "\"R8102200679436-601\",\n" +
                "\"R8102200679445-601\",\n" +
                "\"R8102200679454-601\",\n" +
                "\"R8102200679463-601\",\n" +
                "\"R8102200679472-601\",\n" +
                "\"R8102200679481-601\",\n" +
                "\"R8102200679490-601\",\n" +
                "\"R8102200679506-601\",\n" +
                "\"R8102200679515-601\",\n" +
                "\"R8102200679524-601\",\n" +
                "\"R8102200679533-601\",\n" +
                "\"R8102200679542-601\",\n" +
                "\"R8102200679551-601\",\n" +
                "\"R8102200679560-601\",\n" +
                "\"R8102200679579-601\",\n" +
                "\"R8102200679588-601\",\n" +
                "\"R8102200679597-601\",\n" +
                "\"R8102200679603-601\",\n" +
                "\"R8102200679612-601\",\n" +
                "\"R8102200679621-601\",\n" +
                "\"R8102200679630-601\",\n" +
                "\"R8102200679649-601\",\n" +
                "\"R8102200679658-601\",\n" +
                "\"R8102200679667-601\",\n" +
                "\"R8102200679676-601\",\n" +
                "\"R8102200679685-601\",\n" +
                "\"R8102200679694-601\",\n" +
                "\"R8102200679700-601\",\n" +
                "\"R8102200679719-601\",\n" +
                "\"R8102200679728-601\",\n" +
                "\"R8102200679737-601\",\n" +
                "\"R8102200679746-601\",\n" +
                "\"R8102200679755-601\",\n" +
                "\"R8102200679764-601\",\n" +
                "\"R8102200679773-601\",\n" +
                "\"R8102200679782-601\",\n" +
                "\"R8102200679791-601\",\n" +
                "\"R8102200679807-601\",\n" +
                "\"R8102200679816-601\",\n" +
                "\"R8102200679825-601\",\n" +
                "\"R8102200679843-601\",\n" +
                "\"R8102200679852-601\",\n" +
                "\"R8102200679861-601\",\n" +
                "\"R8102200679870-601\",\n" +
                "\"R8102200679889-601\",\n" +
                "\"R8102200679898-601\",\n" +
                "\"R8102200679904-601\",\n" +
                "\"R8102200679913-601\",\n" +
                "\"R8102200679922-601\",\n" +
                "\"R8102200679931-601\",\n" +
                "\"R8102200679940-601\",\n" +
                "\"R8102200679959-601\",\n" +
                "\"R8102200679968-601\",\n" +
                "\"R8102200679977-601\",\n" +
                "\"R8102200679986-601\",\n" +
                "\"R8102200679995-601\",\n" +
                "\"R8102200680007-601\",\n" +
                "\"R8102200680016-601\",\n" +
                "\"R8102200680034-601\",\n" +
                "\"R8102200680043-601\",\n" +
                "\"R8102200680052-601\",\n" +
                "\"R8102200680061-601\",\n" +
                "\"R8102200680070-601\",\n" +
                "\"R8102200680089-601\",\n" +
                "\"R8102200680098-601\",\n" +
                "\"R8102200680104-601\",\n" +
                "\"R8102200680113-601\",\n" +
                "\"R8102200680122-601\",\n" +
                "\"R8102200680131-601\",\n" +
                "\"R8102200680140-601\",\n" +
                "\"R8102200680159-601\",\n" +
                "\"R8102200680168-601\",\n" +
                "\"R8102200680177-601\",\n" +
                "\"R8102200680186-601\",\n" +
                "\"R8102200680195-601\",\n" +
                "\"R8102200680201-601\",\n" +
                "\"R8102200680210-601\",\n" +
                "\"R8102200680229-601\",\n" +
                "\"R8102200680238-601\",\n" +
                "\"R8102200680247-601\",\n" +
                "\"R8102200680256-601\",\n" +
                "\"R8102200680265-601\",\n" +
                "\"R8102200680274-601\",\n" +
                "\"R8102200680283-601\",\n" +
                "\"R8102200680292-601\",\n" +
                "\"R8102200680308-601\",\n" +
                "\"R8102200680317-601\",\n" +
                "\"R8102200680326-601\",\n" +
                "\"R8102200680335-601\",\n" +
                "\"R8102200680344-601\",\n" +
                "\"R8102200680353-601\",\n" +
                "\"R8102200680362-601\",\n" +
                "\"R8102200680371-601\",\n" +
                "\"R8102200680380-601\",\n" +
                "\"R8102200680399-601\",\n" +
                "\"R8102200680405-601\",\n" +
                "\"R8102200680414-601\",\n" +
                "\"R8102200680423-601\",\n" +
                "\"R8102200680432-601\",\n" +
                "\"R8102200680441-601\",\n" +
                "\"R8102200680450-601\",\n" +
                "\"R8102200680469-601\",\n" +
                "\"R8102200680478-601\",\n" +
                "\"R8102200680487-601\",\n" +
                "\"R8102200680496-601\",\n" +
                "\"R8102200680502-601\",\n" +
                "\"R8102200680511-601\",\n" +
                "\"R8102200680520-601\",\n" +
                "\"R8102200680539-601\",\n" +
                "\"R8102200680548-601\",\n" +
                "\"R8102200680557-601\",\n" +
                "\"R8102200680566-601\",\n" +
                "\"R8102200680575-601\",\n" +
                "\"R8102200680584-601\",\n" +
                "\"R8102200680593-601\",\n" +
                "\"R8102200680609-601\",\n" +
                "\"R8102200680618-601\",\n" +
                "\"R8102200680627-601\",\n" +
                "\"R8102200680636-601\",\n" +
                "\"R8102200680645-601\",\n" +
                "\"R8102200680654-601\",\n" +
                "\"R8102200680663-601\",\n" +
                "\"R8102200680672-601\",\n" +
                "\"R8102200680681-601\",\n" +
                "\"R8102200680690-601\",\n" +
                "\"R8102200680706-601\",\n" +
                "\"R8102200680715-601\",\n" +
                "\"R8102200680724-601\",\n" +
                "\"R8102200680733-601\",\n" +
                "\"R8102200680742-601\",\n" +
                "\"R8102200680751-601\",\n" +
                "\"R8102200680760-601\",\n" +
                "\"R8102200680779-601\",\n" +
                "\"R8102200680788-601\",\n" +
                "\"R8102200680797-601\",\n" +
                "\"R8102200680803-601\",\n" +
                "\"R8102200680812-601\",\n" +
                "\"R8102200680821-601\",\n" +
                "\"R8102200680830-601\",\n" +
                "\"R8102200680849-601\",\n" +
                "\"R8102200680858-601\",\n" +
                "\"R8102200680867-601\",\n" +
                "\"R8102200680876-601\",\n" +
                "\"R8102200680885-601\",\n" +
                "\"R8102200680894-601\",\n" +
                "\"R8102200680900-601\",\n" +
                "\"R8102200680919-601\",\n" +
                "\"R8102200680928-601\",\n" +
                "\"R8102200680937-601\",\n" +
                "\"R8102200680946-601\",\n" +
                "\"R8102200680955-601\",\n" +
                "\"R8102200680964-601\",\n" +
                "\"R8102200680973-601\",\n" +
                "\"R8102200680982-601\",\n" +
                "\"R8102200680991-601\",\n" +
                "\"R8102200681006-601\",\n" +
                "\"R8102200681015-601\",\n" +
                "\"R8102200681033-601\",\n" +
                "\"R8102200681042-601\",\n" +
                "\"R8102200681051-601\",\n" +
                "\"R8102200681060-601\",\n" +
                "\"R8102200681079-601\",\n" +
                "\"R8102200681088-601\",\n" +
                "\"R8102200681097-601\",\n" +
                "\"R8102200681103-601\",\n" +
                "\"R8102200681112-601\",\n" +
                "\"R8102200681121-601\",\n" +
                "\"R8102200681130-601\",\n" +
                "\"R8102200681149-601\",\n" +
                "\"R8102200681158-601\",\n" +
                "\"R8102200681167-601\",\n" +
                "\"R8102200681176-601\",\n" +
                "\"R8102200681185-601\",\n" +
                "\"R8102200681194-601\",\n" +
                "\"R8102200681200-601\",\n" +
                "\"R8102200681219-601\",\n" +
                "\"R8102200681228-601\",\n" +
                "\"R8102200681237-601\",\n" +
                "\"R8102200681246-601\",\n" +
                "\"R8102200681255-601\",\n" +
                "\"R8102200681264-601\",\n" +
                "\"R8102200681273-601\",\n" +
                "\"R8102200681282-601\",\n" +
                "\"R8102200681291-601\",\n" +
                "\"R8102200681307-601\",\n" +
                "\"R8102200681316-601\",\n" +
                "\"R8102200681325-601\",\n" +
                "\"R8102200681334-601\",\n" +
                "\"R8102200681343-601\",\n" +
                "\"R8102200681352-601\",\n" +
                "\"R8102200681361-601\",\n" +
                "\"R8102200681370-601\",\n" +
                "\"R8102200681389-601\",\n" +
                "\"R8102200681398-601\",\n" +
                "\"R8102200681404-601\",\n" +
                "\"R8102200681413-601\",\n" +
                "\"R8102200681422-601\",\n" +
                "\"R8102200681431-601\",\n" +
                "\"R8102200681440-601\",\n" +
                "\"R8102200681459-601\",\n" +
                "\"R8102200681468-601\",\n" +
                "\"R8102200681477-601\",\n" +
                "\"R8102200681486-601\",\n" +
                "\"R8102200681495-601\",\n" +
                "\"R8102200681501-601\",\n" +
                "\"R8102200681510-601\",\n" +
                "\"R8102200681529-601\",\n" +
                "\"R8102200681538-601\",\n" +
                "\"R8102200681547-601\",\n" +
                "\"R8102200681556-601\",\n" +
                "\"R8102200681565-601\",\n" +
                "\"R8102200681574-601\",\n" +
                "\"R8102200681583-601\",\n" +
                "\"R8102200681592-601\",\n" +
                "\"R8102200681608-601\",\n" +
                "\"R8102200681617-601\",\n" +
                "\"R8102200681626-601\",\n" +
                "\"R8102200681635-601\",\n" +
                "\"R8102200681644-601\",\n" +
                "\"R8102200681653-601\",\n" +
                "\"R8102200681662-601\",\n" +
                "\"R8102200681671-601\",\n" +
                "\"R8102200681680-601\",\n" +
                "\"R8102200681699-601\",\n" +
                "\"R8102200681705-601\",\n" +
                "\"R8102200681714-601\",\n" +
                "\"R8102200681723-601\",\n" +
                "\"R8102200681732-601\",\n" +
                "\"R8102200681741-601\",\n" +
                "\"R8102200681750-601\",\n" +
                "\"R8102200681769-601\",\n" +
                "\"R8102200681778-601\",\n" +
                "\"R8102200681787-601\",\n" +
                "\"R8102200681796-601\",\n" +
                "\"R8102200681802-601\",\n" +
                "\"R8102200681811-601\",\n" +
                "\"R8102200681820-601\",\n" +
                "\"R8102200681839-601\",\n" +
                "\"R8102200681848-601\",\n" +
                "\"R8102200681857-601\",\n" +
                "\"R8102200681866-601\",\n" +
                "\"R8102200681875-601\",\n" +
                "\"R8102200681884-601\",\n" +
                "\"R8102200681893-601\",\n" +
                "\"R8102200681909-601\",\n" +
                "\"R8102200681918-601\",\n" +
                "\"R8102200681927-601\",\n" +
                "\"R8102200681936-601\",\n" +
                "\"R8102200681945-601\",\n" +
                "\"R8102200681954-601\",\n" +
                "\"R8102200681963-601\",\n" +
                "\"R8102200681972-601\",\n" +
                "\"R8102200681981-601\",\n" +
                "\"R8102200681990-601\",\n" +
                "\"R8102200682005-601\",\n" +
                "\"R8102200682014-601\",\n" +
                "\"R8102200682023-601\",\n" +
                "\"R8102200682032-601\",\n" +
                "\"R8102200682041-601\",\n" +
                "\"R8102200682050-601\",\n" +
                "\"R8102200682069-601\",\n" +
                "\"R8102200682078-601\",\n" +
                "\"R8102200682087-601\",\n" +
                "\"R8102200682096-601\",\n" +
                "\"R8102200682102-601\",\n" +
                "\"R8102200682111-601\",\n" +
                "\"R8102200682120-601\",\n" +
                "\"R8102200682139-601\",\n" +
                "\"R8102200682148-601\",\n" +
                "\"R8102200682157-601\",\n" +
                "\"R8102200682166-601\",\n" +
                "\"R8102200682175-601\",\n" +
                "\"R8102200682184-601\",\n" +
                "\"R8102200682193-601\",\n" +
                "\"R8102200682209-601\",\n" +
                "\"R8102200682218-601\",\n" +
                "\"R8102200682227-601\",\n" +
                "\"R8102200682236-601\",\n" +
                "\"R8102200682245-601\",\n" +
                "\"R8102200682254-601\",\n" +
                "\"R8102200682263-601\",\n" +
                "\"R8102200682272-601\",\n" +
                "\"R8102200682281-601\",\n" +
                "\"R8102200682290-601\",\n" +
                "\"R8102200682306-601\",\n" +
                "\"R8102200682315-601\",\n" +
                "\"R8102200682324-601\",\n" +
                "\"R8102200682333-601\",\n" +
                "\"R8102200682342-601\",\n" +
                "\"R8102200682351-601\",\n" +
                "\"R8102200682360-601\",\n" +
                "\"R8102200682379-601\",\n" +
                "\"R8102200682388-601\",\n" +
                "\"R8102200682397-601\",\n" +
                "\"R8102200682403-601\",\n" +
                "\"R8102200682412-601\",\n" +
                "\"R8102200682421-601\",\n" +
                "\"R8102200682430-601\",\n" +
                "\"R8102200682449-601\",\n" +
                "\"R8102200682458-601\",\n" +
                "\"R8102200682467-601\",\n" +
                "\"R8102200682476-601\",\n" +
                "\"R8102200682485-601\",\n" +
                "\"R8102200682494-601\",\n" +
                "\"R8102200682500-601\",\n" +
                "\"R8102200682519-601\",\n" +
                "\"R8102200682528-601\",\n" +
                "\"R8102200682537-601\",\n" +
                "\"R8102200682546-601\",\n" +
                "\"R8102200682555-601\",\n" +
                "\"R8102200682564-601\",\n" +
                "\"R8102200682573-601\",\n" +
                "\"R8102200682582-601\",\n" +
                "\"R8102200682591-601\",\n" +
                "\"R8102200682607-601\",\n" +
                "\"R8102200682634-601\",\n" +
                "\"R8102200682643-601\",\n" +
                "\"R8102200682652-601\",\n" +
                "\"R8102200682661-601\",\n" +
                "\"R8102200682670-601\",\n" +
                "\"R8102200682689-601\",\n" +
                "\"R8102200682698-601\",\n" +
                "\"R8102200682704-601\",\n" +
                "\"R8102200682713-601\",\n" +
                "\"R8102200682722-601\",\n" +
                "\"R8102200682731-601\",\n" +
                "\"R8102200682740-601\",\n" +
                "\"R8102200682759-601\",\n" +
                "\"R8102200682777-601\",\n" +
                "\"R8102200685400-601\",\n" +
                "\"R8102200685419-601\",\n" +
                "\"R8102200685428-601\",\n" +
                "\"R8102200685437-601\",\n" +
                "\"R8102200685446-601\",\n" +
                "\"R8102200685455-601\",\n" +
                "\"R8102200685464-601\",\n" +
                "\"R8102200685473-601\",\n" +
                "\"R8102200685482-601\",\n" +
                "\"R8102200685491-601\",\n" +
                "\"R8102200685507-601\",\n" +
                "\"R8102200686135-601\",\n" +
                "\"R8102200686205-601\",\n" +
                "\"R8102200686269-601\"]";




        List<String> list = JacksonUtil.nonEmpty().fromJson(json, ArrayList.class, String.class);




        System.out.println(list);
    }
    @Test
    public void test2(){
      String content ="{\n" +
              "    \"billid\":35866722,\n" +
              "    \"billldate\":20181127,\n" +
              "    \"billtypeid\":\"采购入库通知\",\n" +
              "    \"companycode\":\"800\",\n" +
              "    \"erp_no\":\"PO201811011\",\n" +
              "    \"grid\":[\n" +
              "        {\n" +
              "            \"basequantity\":1200,\n" +
              "            \"billdtlid\":35866726,\n" +
              "            \"deliverydate\":20181127,\n" +
              "            \"materialtype\":\"SP\",\n" +
              "            \"quantity\":100,\n" +
              "            \"skucode\":\"11490251\",\n" +
              "            \"storagelocation\":\"8001_80\"\n" +
              "        },\n" +
              "        {\n" +
              "            \"basequantity\":60,\n" +
              "            \"billdtlid\":35866764,\n" +
              "            \"deliverydate\":20181127,\n" +
              "            \"materialtype\":\"YP\",\n" +
              "            \"quantity\":5,\n" +
              "            \"skucode\":\"HSYP11490251\",\n" +
              "            \"storagelocation\":\"8001_80\"\n" +
              "        },\n" +
              "        {\n" +
              "            \"basequantity\":1140,\n" +
              "            \"billdtlid\":35866907,\n" +
              "            \"deliverydate\":20181127,\n" +
              "            \"materialtype\":\"SP\",\n" +
              "            \"quantity\":95,\n" +
              "            \"skucode\":\"11490252\",\n" +
              "            \"storagelocation\":\"8001_80\"\n" +
              "        }\n" +
              "    ],\n" +
              "    \"note\":\"接口测试\",\n" +
              "    \"supplyid\":\"001\"\n" +
              "}";
        ErpInBoundRequest erpInBoundRequest = JacksonUtil.nonEmpty().fromJson(content, ErpInBoundRequest.class);
        System.out.println(erpInBoundRequest);
    }

    @Test
    public void test3(){
        wmsToErpScheduled.feedbackInBoundResponse();
    }
}
