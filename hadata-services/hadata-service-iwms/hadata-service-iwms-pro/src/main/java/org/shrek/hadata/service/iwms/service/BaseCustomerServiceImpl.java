package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.iwms.mapper.TCustomerMapper;
import org.shrek.hadata.service.iwms.mapper.TVendorMapper;
import org.shrek.hadata.service.iwms.model.TCarrier;
import org.shrek.hadata.service.iwms.model.TCustomer;
import org.shrek.hadata.service.iwms.model.TVendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 11:33
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseCustomerServiceImpl implements CustomerService {

    @Autowired
    TCustomerMapper tCustomerMapper;
    @Autowired
    TVendorMapper tVendorMapper;
    @Override
    @Transactional
    public boolean createCustomers(List<TCustomer> customers) {
        boolean result = true;
        try {
            for (int i = 0; i < customers.size(); i++) {
                TCustomer exampleCriteria = new TCustomer();
                exampleCriteria.setCustomerCode(customers.get(i).getCustomerCode());
                exampleCriteria.setWhId(customers.get(i).getWhId());
                exampleCriteria.setCustomerCountryCode(customers.get(i).getCustomerCountryCode());
                int count = tCustomerMapper.selectCount(exampleCriteria);
                if (count == 0) {
                    tCustomerMapper.insertSelective(customers.get(i));
                }
            }
        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
        } finally {
            return result;
        }
    }
    @Override
    @Transactional
    public boolean createOrUpdateCustomer(TCustomer customer) {
        boolean result = true;
        try {
                TCustomer exampleCriteria = new TCustomer();
                exampleCriteria.setCustomerCode(customer.getCustomerCode());
                exampleCriteria.setWhId(customer.getWhId());
                exampleCriteria.setCustomerCountryCode(customer.getCustomerCountryCode());
                TCustomer customerCount = tCustomerMapper.selectOne(exampleCriteria);
                if (customerCount !=null) {
                    customer.setCustomerId(customerCount.getCustomerId());
                    tCustomerMapper.updateByPrimaryKeySelective(customer);
                }else{
                    tCustomerMapper.insertSelective(customer);
                }

        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
        } finally {
            return result;
        }
    }
    @Override
    @Transactional
    public boolean deleteCustomers(List<TCustomer> customers) {
        boolean result = true;
        try {
            for (int i = 0; i < customers.size(); i++) {
                tCustomerMapper.delete(customers.get(i));
            }
        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
        } finally {
            return result;
        }
    }

    @Override
    @Transactional
    public boolean createOrUpdateVendor(TVendor vendor) {
        boolean result = true;
        try {
                TVendor exampleCriteria = new TVendor();
                exampleCriteria.setVendorCode(vendor.getVendorCode());
                exampleCriteria.setVendorName(vendor.getVendorName());
                TVendor vendorCount = tVendorMapper.selectOne(exampleCriteria);
                if (vendorCount !=null) {
                    vendor.setVendorId(vendorCount.getVendorId());
                    tVendorMapper.updateByPrimaryKeySelective(vendor);
                }else{
                    tVendorMapper.insertSelective(vendor);
                }

        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
        } finally {
            return result;
        }
    }
    @Override
    @Transactional
    public boolean createVendor(List<TVendor> vendors) {
        boolean result = true;
        try {
            for (int i = 0; i < vendors.size(); i++) {
                TVendor exampleCriteria = new TVendor();
                exampleCriteria.setVendorCode(vendors.get(i).getVendorCode());
                exampleCriteria.setVendorName(vendors.get(i).getVendorName());
                int count = tVendorMapper.selectCount(exampleCriteria);
                if (count == 0) {
                    tVendorMapper.insertSelective(vendors.get(i));
                }
            }
        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
        } finally {
            return result;
        }
    }
    @Override
    public TCustomer getCustomerByWhseAndCode(String whse,String code) {
        TCustomer tCustomer = new TCustomer();
        tCustomer.setWhId(whse);
        tCustomer.setCustomerCode(code);
        return tCustomerMapper.selectOne(tCustomer);
    }

    @Override
    public TVendor getVendorByWhseAndCode(String code) {
        TVendor vendor = new TVendor();
        vendor.setVendorCode(code);
        return tVendorMapper.selectOne(vendor);
    }

    @Override
    public List<String> getCustomerCodeByWhse(String whse,String code) {
        TCustomer tCustomer = new TCustomer();
        tCustomer.setWhId(whse);
        tCustomer.setCustomerCountryCode(code);
        return tCustomerMapper.getCustomerCodeByWhse(tCustomer);
    }
    @Override
    public List<String> getVendorCode() {
        return tVendorMapper.getVendorCode();
    }
}
