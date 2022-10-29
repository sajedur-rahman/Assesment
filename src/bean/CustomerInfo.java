/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bean;

/**
 *
 * @author SRTareq
 */
public class CustomerInfo {
    private String firts_name;
    private String last_name;
    private String city;
    private String state;
    private int zip_code;
    private String phone_no;
    private String email;
    private String ip;
    private long  lineNumber;
    private String notes;

   
    public CustomerInfo(String firts_name, String last_name, String city, String state, int zip_code, String phone_no, String email, String ip,long lineNumber,String notes) {
        this.firts_name = firts_name;
        this.last_name = last_name;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.phone_no = phone_no;
        this.email = email;
        this.ip = ip;
        this.notes = notes;
        this.lineNumber = lineNumber;
    }

    public String getFirts_name() {
        return firts_name;
    }

    public void setFirts_name(String firts_name) {
        this.firts_name = firts_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }
    
     public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
