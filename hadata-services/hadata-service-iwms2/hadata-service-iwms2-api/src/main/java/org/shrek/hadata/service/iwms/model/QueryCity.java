package org.shrek.hadata.service.iwms.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryCity implements Serializable {
    String cityCode;
    String popCityCode;
}
