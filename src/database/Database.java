/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Database 
{
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    public Database() throws SQLException
    {
        try{
             
            //MAKE SURE YOU KEEP THE mysql_connector.jar file in java/lib folder
            //ALSO SET THE CLASSPATH
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/amt22_inventory","root","123456");
            System.out.println("connetion success.....");
             
           }
        catch (ClassNotFoundException e) 
        {
            System.out.println("exception : "+e);
        }
    }
        //ip:username,password
        //return boolean
    public Boolean checkAdminLogin(String uname,String pwd)
    {
        try {
            pst=con.prepareStatement("select * from admin where username=? and password=?");           
            pst.setString(1, uname); //this replaces the 1st  "?" in the query for username
            pst.setString(2, pwd);    //this replaces the 2st  "?" in the query for password
            //executes the prepared statement
            rs=pst.executeQuery();
            
            if(rs.next())
            {
                //TRUE iff the query founds any corresponding data
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return false;
        }
    }
    
      public Boolean checkCurrentPwdExist(String pwd)
    {
        try {
            pst=con.prepareStatement("select * from admin where password=?");           
            pst.setString(1, pwd);
            rs=pst.executeQuery();
            if(rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return false;
        }
    }
      
     
    
    public Boolean changePassword(String pwd1)
    {
         try {
            pst=con.prepareStatement("update admin set password='"+pwd1+"'");           
            //executes the prepared statement
            int n=pst.executeUpdate();           
            if(n==1)
            {
                //TRUE iff the query founds any corresponding data
                return true;
            }
            else
            {
                return false;
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while val"+e);
            return false;
        }
    }
    
    public int addProduct(String name,String price,String color,String size,String category,String img,String cod)
    {
     
        try {
//            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
//            LocalDateTime now = LocalDateTime.now();  
//            String dt=dtf.format(now);  
            
            pst=con.prepareStatement("insert into products ( `name`, `price`, `color`, `size`, `category`, `image`, `code`) values(?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setString(2, price);
            pst.setString(3, color);
            pst.setString(4, size);
            pst.setString(5, category);
            pst.setString(6, img);
            pst.setString(7, cod);
            
            int n=pst.executeUpdate();
            ResultSet keys = pst.getGeneratedKeys(); 
            if(n==1)
            {
                keys.next(); 
                int key = keys.getInt(1);
                return key;
            }
            else
            {
                return 0;
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return 0;
        }
    }
    
    public String getProductImage(String code){
        try {
            pst=con.prepareStatement("select * from products where code=?");           
            pst.setString(1, code);
            rs=pst.executeQuery();
            if(rs.next())
            {
                return rs.getString("image");
            }
            else
            {
                return "";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return "";
        }
    }
    
      public Boolean checkProductNameExist(String name)
    {
        try {
            pst=con.prepareStatement("select * from products where code=?");           
            pst.setString(1, name);
            rs=pst.executeQuery();
            if(rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return false;
        }
    }
      
        public ResultSet getCustomerById(int id)
    {
        try {
            pst=con.prepareStatement("select * from customers where id=?");           
            pst.setInt(1, id);
            rs=pst.executeQuery();
            return rs;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
      
        public int checkInvnoExist(int inv)
    {
        try {
            pst=con.prepareStatement("select * from invoice where invno=?");           
            pst.setInt(1, inv);
            rs=pst.executeQuery();
            if(rs.next())
            {
                return rs.getInt("id");
            }
            else
            {
                return 0;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return 0;
        }
    }
    
      public ResultSet searchProducts(String key)
    {
        try {
            pst=con.prepareStatement("select * from products where name LIKE '%"+key+"%' or code LIKE '%"+key+"%'");         
            rs=pst.executeQuery();
            return rs;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
      
        public ResultSet searchInvoices(String key)
    {
        try {
            pst=con.prepareStatement("select * from invoice where invno LIKE '%"+key+"%'");         
            rs=pst.executeQuery();
            return rs;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
        
        public ResultSet InvoiceProducts(int id)
    {
        try {
            pst=con.prepareStatement("select * from invoice_products where invoice_id="+id);         
            rs=pst.executeQuery();
            return rs;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
        
            public ResultSet getInvoiceById(int id)
    {
        try {
            pst=con.prepareStatement("select * from invoice where invno="+id);         
            rs=pst.executeQuery();
            return rs;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
        
           public ResultSet getProductById(int id)
    {
        try {
            pst=con.prepareStatement("select * from products where id="+id);         
            rs=pst.executeQuery();
            return rs;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
        
           public Double InvoiceProductsTotPrice(int id)
    {
        try {
            double totprice=0;
            pst=con.prepareStatement("select * from invoice_products where invoice_id="+id);         
            rs=pst.executeQuery();
            while(rs.next()){
                int pid=rs.getInt("product_id");
                System.out.println("pid "+pid);
                Double qty=rs.getDouble("qty");
                
                PreparedStatement pstt=con.prepareStatement("select * from products where id="+pid);         
                ResultSet prs=pstt.executeQuery();
                if(prs.next()){
                    Double price=prs.getDouble("price");
                    totprice=totprice+(price*qty);
                    System.out.println("tot "+totprice);
                }
            }
            return totprice;
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return 0.0;
        }
    }
           
    public Double checkProdSalesstock(int pid){
        try {
            Double totstock=0.0;
            pst=con.prepareStatement("select * from invoice_products where product_id="+pid);         
            rs=pst.executeQuery();
            while(rs.next()){
                totstock=totstock+rs.getDouble("qty");
            }
            return totstock;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return 0.0;
        }
    }
    
    public Double checkProdPurchasestock(int pid){
        try {
            Double totstock=0.0;
            pst=con.prepareStatement("select * from purchase_item where product_id="+pid);         
            rs=pst.executeQuery();
            while(rs.next()){
                totstock=totstock+rs.getDouble("qty");
            }
            return totstock;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return 0.0;
        }
    }
      
      public ResultSet searchPurchase(String key)
    {
        try {
            pst=con.prepareStatement("select * from purchase where pnumber LIKE '%"+key+"%'");         
            rs=pst.executeQuery();
            return rs;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return null;
        }
    }
    
    public String dateformat(String date,String fromformat,String toformat)
    {
        try {
            DateFormat uformatter = new SimpleDateFormat(fromformat);
//            System.out.println(date);
            java.util.Date uctdate = (java.util.Date) uformatter.parse(date);
//            System.out.println(fromformat);
            SimpleDateFormat unewFormat = new SimpleDateFormat(toformat);
            return unewFormat.format(uctdate);
        } catch (ParseException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int addCustomer(String nm, String em, String conn, String add) {
            try {
                pst=con.prepareStatement("insert into customers ( `name`, `email`, `contact`, `address`) values(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, nm);
                pst.setString(2, em);
                pst.setString(3, conn);
                pst.setString(4, add);

                int n=pst.executeUpdate();
                ResultSet keys = pst.getGeneratedKeys(); 
                if(n==1)
                {
                    keys.next(); 
                    int key = keys.getInt(1);
                    return key;
                }
                else
                {
                    return 0;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("error while validating"+e);
                return 0;
            }
        }
          public ResultSet searchCustomer(String key)
        {
            try {
                pst=con.prepareStatement("select * from customers where name LIKE '%"+key+"%' and or LIKE '%"+key+"%' or contact LIKE '%"+key+"%'");         
                rs=pst.executeQuery();
                return rs;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("error while validating"+e);
                return null;
            }
        }

    public int addPurchase(String nm, String add, String conn, String pno, String d, String m, String y, String p1, String p2,String p3, String q1, String q2, String q3) {
        try {
                pst=con.prepareStatement("insert into purchase ( `name`, `address`, `contact`, `pnumber`, `date`) values(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, nm);
                pst.setString(2, add);
                pst.setString(3, conn);
                pst.setString(4, pno);
                pst.setString(5, y+"-"+m+"-"+d);

                int n=pst.executeUpdate();
                ResultSet keys = pst.getGeneratedKeys(); 
                if(n==1)
                {
                    keys.next(); 
                    int key = keys.getInt(1);
                    System.out.println(key);
                    String [] pro1=p1.split("-");
                    int p1id=Integer.parseInt(pro1[0]);
                    PreparedStatement pst1=con.prepareStatement("insert into purchase_item ( `product_name`,`product_id`, `qty`,`purchase_id`) values('"+p1+"',"+p1id+","+Float.parseFloat(q1)+","+key+")");
                    pst1.executeUpdate();
                    String [] pro2=p2.split("-");
                    int p2id=Integer.parseInt(pro2[0]);
                    PreparedStatement pst2=con.prepareStatement("insert into purchase_item ( `product_name`,`product_id`, `qty`,`purchase_id`) values('"+p2+"',"+p2id+","+Float.parseFloat(q2)+","+key+")");
                    pst2.executeUpdate();
                    String [] pro3=p3.split("-");
                    int p3id=Integer.parseInt(pro3[0]);
                    PreparedStatement pst3=con.prepareStatement("insert into purchase_item ( `product_name`,`product_id`, `qty`,`purchase_id`) values('"+p3+"',"+p3id+","+Float.parseFloat(q2)+","+key+")");
                    pst3.executeUpdate();
                    return key;
                }
                else
                {
                    return 0;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("error while validating"+e);
                return 0;
            }
    }

    public int addInvoice(String cust, String d, String m, String y,String ino) {
        String[] custm=cust.split("-");
        int res=this.checkInvnoExist(Integer.parseInt(ino));
        if(res>0){
            PreparedStatement pst1;
            try {
                pst1 = con.prepareStatement("update invoice set `customer_id`='"+custm[0]+"',`date`='"+y+"-"+m+"-"+d+"' where invno="+Integer.parseInt(ino));
                pst1.executeUpdate();
                return res;
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
        }else{
            try {
                    pst=con.prepareStatement("insert into invoice ( `customer_id`, `date`, `invno`) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
                    pst.setString(1, custm[0]);
                    pst.setString(2, y+"-"+m+"-"+d);
                    pst.setInt(3,Integer.parseInt(ino));

                    int n=pst.executeUpdate();
                    ResultSet keys = pst.getGeneratedKeys(); 
                    if(n==1)
                    {
                        keys.next(); 
                        int key = keys.getInt(1);
                        return key;
                    }
                    else
                    {
                        return 0;
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("error while validating"+e);
                    return 0;
                }
        }
    }

    public int addInvoiceProduct(int invid, String p, String q) {
        String[] proddata=p.split(":");
        try {
                    pst=con.prepareStatement("insert into invoice_products ( `invoice_id`, `product_id`, `qty`) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
                    pst.setInt(1, invid);
                    pst.setInt(2, Integer.parseInt(proddata[0]));
                    pst.setDouble(3,Double.parseDouble(q));

                    int n=pst.executeUpdate();
                    ResultSet keys = pst.getGeneratedKeys(); 
                    if(n==1)
                    {
                        keys.next(); 
                        int key = keys.getInt(1);
                        return key;
                    }
                    else
                    {
                        return 0;
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("error while validating"+e);
                    return 0;
                }
    }
}

