package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calcbill")

public class CalcBill extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		PrintWriter out = resp.getWriter();
		HttpSession session = req.getSession();
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		
		
		
		String consumNum = req.getParameter("comNum");
		String lastRead = req.getParameter("lmeterread");
		String curRead = req.getParameter("cmeterread");
		
		if(consumNum.startsWith("0") || consumNum.length() != 6) {
			out.println("Consumer number cannot start with 0 or less or greater than 6 digits");
		}
		else {
			int consNum = Integer.parseInt(consumNum);
			double lastRead1 = Double.parseDouble(lastRead);
			double curRead1 = Double.parseDouble(curRead);
			if(lastRead1 < 0 || curRead1 < 0 || curRead1 < lastRead1 ) {
				out.println("Last Month and Current month reading cannot be negative or current month cannot be less than last month reading");
			}
			else {
				double consumed = curRead1 - lastRead1;
				double netAmount = (consumed * 1.15) + 100;
				String s = String.format("%.1f", netAmount);
				String g = String.format("%.1f", consumed);
				out.println("<h1> Welcome " + username + "</h1><br>");
				out.println("<h2> Electricity for Consumer Number " + consNum + " is </h2><br><br>");
				out.println("<h3> Units consumed:: " + g + "</h3>");
				out.println("<h3> Net Amount:: RS " + s + "</h3>");
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					try(Connection c = DriverManager.getConnection("jdbc:oracle:thin:@oracle.cv9u7djpxtb4.us-east-1.rds.amazonaws.com:1521:orcl", "admin", "oracle123");
							Statement statement = c.createStatement();){
							c.setAutoCommit(false);
							statement.execute("CREATE TABLE Consumers(consumer_num NUMBER(6) PRIMARY KEY,  consumer_name VARCHAR2(20) NOT NULL,  address VARCHAR2(30) ); ");
							//statement.execute("CREATE TABLE BillDetails(bill_num NUMBER(6) PRIMARY KEY,  consumer_num NUMBER(6) REFERENCES Consumers(consumer_num), cur_reading NUMBER(5,2),   unitConsumed NUMBER(5,2),   netAmount NUMBER(5,2),   bill_date DATE DEFAULT SYSDATE); ");
							//statement.execute("CREATE SEQUENCE seq_bill_num START WITH 100;");
							out.println("Tables created");
//							int rows = statement.executeUpdate("INSERT INTO Consumers VALUES(100001,'Sumeet','Shivaji Nagar, Pune');");
//							int rows1 = statement.executeUpdate("INSERT INTO Consumers VALUES(100002,'Meenal','M G Colony Panvel, Mumbai');");
//							int rows2 = statement.executeUpdate("INSERT INTO Consumers VALUES(100003,'Neeraj','Whitefield, Bangalore'); ");	  
//							int rows3 = statement.executeUpdate("INSERT INTO Consumers VALUES(100004,'Arul','Karapakkam, Chennai');");
//							int rows4 = statement.executeUpdate("insert into BillDetails values(seq_bill_num.nextval, '" + consNum + "', '" + curRead1 + "', '" + consumed + "', '" + netAmount + "');");
//							
							c.commit();
					} catch (SQLException e) {
						e.printStackTrace();
						out.println("There was a problem saving info to db, try again");
					}
							
				} catch (ClassNotFoundException e1) {
					out.println("Driver for db was not found");
					e1.printStackTrace();
				}
				
						
					
			}
		}
		
		
		
	}

		
}
