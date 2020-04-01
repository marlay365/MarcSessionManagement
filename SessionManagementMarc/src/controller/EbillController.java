package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ebill")
public class EbillController extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		HttpSession session = req.getSession();
		session.setAttribute("username", username);
		session.setAttribute("password", password);
		if(username.equals("admin") && password.equals("password")) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head><title>Calculate Bill</title></head>");
			out.println("<body>");
			out.println("<form action='calcbill' method='post'>");
			out.println("Consumer Number <input type='number' name='comNum' placeholder='numbers only' required> <br>");
			out.println("Last month meter reading <input type='number' name='lmeterread' step='.01' placeholder='numbers only' required > <br>");
			out.println("Current month meter reading <input type='number' name='cmeterread' step='.01' placeholder='numbers only' required ><br>");
			out.println("<input type='submit' value='Calculate bill' >");
			
		}
		else {
			out.println("Invalid username and/or password");
		}
	}

		
}
