package com.psp.controller;

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.psp.ftp.ConexionFTP;
import com.psp.modelo.Alumno;

/**
 * Servlet implementation class ServletFTP
 */
@WebServlet("/ServletFTP")
public class ServletFTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFTP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pwriter = response.getWriter();
		HttpSession session=request.getSession();
		System.out.println("Ha entrado en el servletFTP");
		
		ArrayList<Alumno> lista = (ArrayList<Alumno>) session.getAttribute("alum");
		int indice = Integer.parseInt(request.getParameter("id"));
		
		Alumno alum = lista.get(indice);
		
		ConexionFTP ftp = new ConexionFTP();
		System.out.println("Ha creado objeto conexionFTP");
		ftp.setAlumno(alum);
		ftp.createDirectoryUser();
		
		
		
		
		
	}
	
	

}
