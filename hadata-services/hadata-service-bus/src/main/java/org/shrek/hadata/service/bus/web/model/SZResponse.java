package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

@Data
public class SZResponse {
    private String  flag;
    private String  response;
    private String msg;
    public static SZResponse success(String msg,String response) {
        SZResponse r = new SZResponse();
        r.setFlag("T");
        r.setResponse(response);
        r.setMsg(msg);
        return r;
    }

    public static SZResponse fail(String msg,String response) {
        SZResponse r = new SZResponse();
        r.setFlag("F");
        r.setResponse(response);
        r.setMsg(msg);
        return r;
    }
}
