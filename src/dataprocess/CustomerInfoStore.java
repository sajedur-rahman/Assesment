/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataprocess;

import bean.CustomerInfo;
import dao.DBConnection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SRTareq
 */
public class CustomerInfoStore {

    private PreparedStatement pstmt = null;
    private final DBConnection dBConnection = new DBConnection();
    private Connection con;

    public static void main(String[] args) {
        CustomerInfoStore store = new CustomerInfoStore();
        List<CustomerInfo> customerInfo = store.readClientInfo();
        store.processCustomerList(customerInfo);
    }

    // Read Data from txt File to Customer Info
    public List<CustomerInfo> readClientInfo() {
        List<String> strings = null;
        try {
            strings = Files.readAllLines(Paths.get("C:\\Users\\sajed\\Desktop\\assesment\\1M-customers.txt"));

        } catch (IOException ex) {
            Logger.getLogger(CustomerInfoStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        long count = 0;
        for (String line : strings) {
            String[] split = line.split(",");
            count++;
            String firts_name = split[0];
            String last_name = split[1];
            String city = split[2];
            String state = split[3];
            int zip_code = zipCodeValidation(split[4]);
            String phone_no = split[5];
            String email = split[6];
            String ip;
            if (split.length > 7) {
                ip = split[7];
            } else {
                ip = "";
            }

            CustomerInfo customerInfo = new CustomerInfo(firts_name, last_name, city, state, zip_code, phone_no, email, ip, count, "");
            customerInfos.add(customerInfo);
            //  System.out.println("..." + customerInfos.size());
        }

        return customerInfos;
    }

    // Process the list of customer from txt file and insert into customer_info table and duplicate_invalid_customer_info
    public void processCustomerList(List<CustomerInfo> customersInfo) {
        List<CustomerInfo> invalidAndDuplicateCustomerList;
        List<CustomerInfo> validCustomerList;

        //Check if the list is empty
        while (!customersInfo.isEmpty()) {
            invalidAndDuplicateCustomerList = new ArrayList<>();
            validCustomerList = new ArrayList<>();
            // Iterate customer data
            Iterator iteratorCustomerList = customersInfo.iterator();
            int counter = 0;
            while (iteratorCustomerList.hasNext()) {
                CustomerInfo nextData = (CustomerInfo) iteratorCustomerList.next();
                if (counter < 1000) {
                    if (!validatePhoneNumber(nextData.getPhone_no())) {
                        nextData.setNotes("Invalid Mobile Number");
                        invalidAndDuplicateCustomerList.add(nextData);
                    } else if (!validateEmail(nextData.getEmail())) {
                        nextData.setNotes("Invalid Email");
                        invalidAndDuplicateCustomerList.add(nextData);
                    } else if (getDuplicateCustomer(nextData)) {
                        nextData.setNotes("Duplicate Customer Info");
                        invalidAndDuplicateCustomerList.add(nextData);
                    } else if (getDuplicateCustomerFromList(nextData, validCustomerList)) {
                        nextData.setNotes("Duplicate Customer Info");
                        invalidAndDuplicateCustomerList.add(nextData);
                    } else {
                        validCustomerList.add(nextData);
                    }
                    counter++;
                } else {
                    break;
                }
            }
            storeInvalidandDuplicateCustomerInfo(invalidAndDuplicateCustomerList);
            storeValidCustomerInfo(validCustomerList);
            // REmove data from list
            customersInfo.subList(0, counter).clear();

        }
    }

    // Store Valid Customer info to customer_info table 
    public void storeValidCustomerInfo(List<CustomerInfo> customerInfo) {
        String insertIntoCustomerInfo = "insert into  customer_info "
                + "(first_name,last_name,city,state,zip_code,phone_no,email,ip,linenumber,remarks) "
                + "values (?,?,?,?,?,?,?,?,?,?)";
        con = dBConnection.getConnection();
        try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(insertIntoCustomerInfo);
            for (CustomerInfo info : customerInfo) {

                pstmt.setString(1, info.getFirts_name());
                pstmt.setString(2, info.getLast_name());
                pstmt.setString(3, info.getCity());
                pstmt.setString(4, info.getState());
                pstmt.setInt(5, info.getZip_code());
                pstmt.setString(6, info.getPhone_no());
                pstmt.setString(7, info.getEmail());
                pstmt.setString(8, info.getIp());
                pstmt.setLong(9, info.getLineNumber());
                pstmt.setString(10, info.getNotes());
                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                pstmt.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CustomerInfoStore.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    // Store Invlid Customer info to duplicate_invalid_customer_info table
    public void storeInvalidandDuplicateCustomerInfo(List<CustomerInfo> customerInfo) {
        String insertIntoDuplicateCustomerInfo = "insert into duplicate_invalid_customer_info "
                + "(first_name,last_name,city,state,zip_code,phone_no,email,ip,linenumber,remarks) "
                + "values (?,?,?,?,?,?,?,?,?,?)";
        con = dBConnection.getConnection();
        try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(insertIntoDuplicateCustomerInfo);
            for (CustomerInfo info : customerInfo) {
                pstmt.setString(1, info.getFirts_name());
                pstmt.setString(2, info.getLast_name());
                pstmt.setString(3, info.getCity());
                pstmt.setString(4, info.getState());
                pstmt.setInt(5, info.getZip_code());
                pstmt.setString(6, info.getPhone_no());
                pstmt.setString(7, info.getEmail());
                pstmt.setString(8, info.getIp());
                pstmt.setLong(9, info.getLineNumber());
                pstmt.setString(10, info.getNotes());
                pstmt.addBatch();
                pstmt.clearParameters();
            }

            pstmt.executeBatch();
            con.commit();
            pstmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                pstmt.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CustomerInfoStore.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    //Check For Duplicate Customers
    public boolean getDuplicateCustomer(CustomerInfo info) {
        boolean isDuplicate = false;
        String duplicateData = "select email from customer_info where email = ? ";

        con = dBConnection.getConnection();
        try {
            pstmt = con.prepareStatement(duplicateData);
            pstmt.setString(1, info.getEmail());
            if (pstmt.executeQuery().next()) {
                isDuplicate = true;
            }
            pstmt.executeQuery();
            pstmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerInfoStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isDuplicate;
    }

    //Email Validation Fucntion
    public boolean validateEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    // Mobile Number Validation
    private static boolean validatePhoneNumber(String phoneNumber) {
        // ^(?:\([2-9]\d{2}\)\ ?|[2-9]\d{2}(?:\-?|\ ?))[2-9]\d{2}[- ]?\d{4}$
        return phoneNumber.matches("^(?:\\([2-9]\\d{2}\\)\\ ?|[2-9]\\d{2}(?:\\-?|\\ ?))[2-9]\\d{2}[- ]?\\d{4}$");
    }

    // Zip Code Validation
    private int zipCodeValidation(String str) {
        int returnValue;
        try {
            returnValue = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            returnValue = 0;
        }
        return returnValue;
    }

    // Get Duplicate Data from Valid List
    private boolean getDuplicateCustomerFromList(CustomerInfo nextData, List<CustomerInfo> validCustomerList) {
        for (CustomerInfo customerInfo : validCustomerList) {
            if (nextData.getEmail().equals(customerInfo.getEmail())) {
                return true;
            }
        }
        return false;
    }

}
