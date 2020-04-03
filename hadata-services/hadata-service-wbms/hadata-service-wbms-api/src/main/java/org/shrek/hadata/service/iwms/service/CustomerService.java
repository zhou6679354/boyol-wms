package org.shrek.hadata.service.iwms.service;

//import org.shrek.hadata.service.iwms.model.TCarrier;
import org.shrek.hadata.service.iwms.model.TCustomer;
import org.shrek.hadata.service.iwms.model.TItemMaster;
import org.shrek.hadata.service.iwms.model.TVendor;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 11:32
 */
public interface CustomerService {
    /**
     * 创建客户物料信息
     *
     * @param customers
     * @return
     */
    public boolean createCustomers(List<TCustomer> customers);
    public TCustomer getCustomerByWhseAndCode(String whse, String code);
    public TVendor getVendorByWhseAndCode(String code);
    public List<String> getCustomerCodeByWhse(String whse,String code);
    public boolean deleteCustomers(List<TCustomer> customers);
    public boolean createVendor(List<TVendor> vendors);
    public List<String> getVendorCode();
    public boolean createOrUpdateVendor(TVendor vendor);
    public boolean createOrUpdateCustomer(TCustomer customer);
}
