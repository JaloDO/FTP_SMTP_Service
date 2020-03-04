package com.psp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.psp.modelo.Alumno;



public class Handler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<Alumno> alumnos = new ArrayList<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Handler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pwriter = response.getWriter();
		HttpSession session=request.getSession();
		
		
		Alumno al = new Alumno();
		al.setNombre(request.getParameter("nombre"));
		al.setApellido(request.getParameter("apellido"));
		al.setCurso(request.getParameter("curso"));
		al.setEmail(request.getParameter("email"));
		alumnos.add(al);
		
		//añadir alumno a la tabla
		pwriter.println("<table style= cellspacing='1' bgcolor='#0099cc'>");
		pwriter.println("<tr>");
		pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'> NOMBRE </td>");			
		pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'> APELLIDOS </td>");		
		pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'> CURSO </td>");	
		pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'> EMAIL </td>");
		pwriter.println("</tr>");
		for(int i=0; i<alumnos.size(); i++){
			Alumno alu = alumnos.get(i);
			pwriter.println("<tr>");
			pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'>"+alu.getNombre()+"</td>");			
			pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'>"+alu.getApellido()+"</td>");	
			pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'>"+alu.getCurso()+"</td>");
			pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'>"+alu.getEmail()+"</td>");
			pwriter.println("<td style= rowspan='7' align='center' bgcolor='#f8f8f8'><input type='button' value='GENERAR' onclick='crearDirectorioFTP("+i+");'>  </input>  </td>");	
			pwriter.println("</tr>");
		}
		pwriter.println("</table>");
		
		session.setAttribute("alum", alumnos);
		
		
		//	//	//		//CrearDirector
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
