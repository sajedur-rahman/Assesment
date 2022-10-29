/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import bean.RegistrationBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SRTareq
 */
public class DAO {

    PreparedStatement pstmt = null;
    private Statement stmt = null;
    DBConnection dBConnection = new DBConnection();
    private Connection con;
    ResultSet rs;

    public String registration(RegistrationBean reg) {
        String insertQuery = "insert into  registration (name,empid,Department,Designation,password,isactive) values (?,?,?,?,?,?)";
        con = dBConnection.getConnection();
        try {
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setString(1, reg.getName());
            pstmt.setString(2, reg.getEmpid());
            pstmt.setString(3, reg.getDept());
            pstmt.setString(4, reg.getDesig());
            pstmt.setString(5, reg.getPaass());
            pstmt.setBoolean(6, reg.isIsactive());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("Exception from Dao:" + ex);

        }
        return insertQuery;
    }
    
     public List<RegistrationBean> selectallUser(){
         List<RegistrationBean> obj = new ArrayList<RegistrationBean>();
         String query ="SELECT * FROM registration";
         con=dBConnection.getConnection();
         
        try {           
            rs=pstmt.executeQuery(query);
            while(rs.next()){
             RegistrationBean bean = new RegistrationBean();
             bean.setName(rs.getString("name"));
             bean.setEmpid(rs.getString("empid"));
             bean.setDept(rs.getString("Department"));
             bean.setDesig(rs.getString("Designation"));
             bean.setIsactive(rs.getBoolean("isactive"));
             obj.add(bean);
            }
        } catch (SQLException ex) {
           
        }         
     return obj;
     }
     
     public void updateTable(List<RegistrationBean> obj){
         String qry = "UPDATE registration SET "
                 + " name=?, Department=?, "
                 + "Designation=?, isactive=? WHERE empid = ?";
         con = dBConnection.getConnection();
         for(RegistrationBean reg : obj){
             try {
                 pstmt = con.prepareStatement(qry);
                 pstmt.setString(1, reg.getName());
                 pstmt.setString(2, reg.getDept());
                 pstmt.setString(3, reg.getDesig());
                 pstmt.setBoolean(4, reg.isIsactive());
                 pstmt.setString(5, reg.getEmpid());
                 
                 pstmt.executeUpdate();
                 
             } catch (SQLException ex) {
                 Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
             }
             
         }
     }
}
