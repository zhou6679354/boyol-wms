package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "tbl_massive_stock_out_quantity")
public class TblMassiveStockOutQuantity extends BasicEntity {


    @Column(name = "wh_id")
    private String whId;

    @Column(name = "client_code")
    private String clientCode;

    private Integer quantity;

}