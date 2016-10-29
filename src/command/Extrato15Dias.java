package command;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ExtratoDAO;
import model.Extrato;
import to.ExtratoTO;

public class Extrato15Dias implements Command {
	
	@Override
	public void executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pId = "1"; // request.getParameter("idUser");
		Extrato extrato = new Extrato();
		ArrayList<ExtratoTO> listaExtrato = null;
		HttpSession session = request.getSession();
		int id = 1;

		try {
			id = Integer.parseInt(pId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		Connection conn = (Connection) request.getAttribute("conexao");
		ExtratoDAO extratoDAO = new ExtratoDAO(conn);

		extrato.extratoDias(15);
		listaExtrato = extrato.consultaExtrato(id, extratoDAO);

		session.setAttribute("listaExtrato", listaExtrato);
		RequestDispatcher dispatcher = request.getRequestDispatcher("EfetuarExtrato.jsp");
		dispatcher.forward(request, response);

	}

}