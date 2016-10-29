package command;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ExtratoDAO;
import dao.SaqueDAO;
import model.Saque;

public class SaqueOutroValor implements Command {

	@Override
	public void executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pValor = request.getParameter("data[valor]");
		String pId = "1"; // request.getParameter("idCliente");
		int id = -1;
		double saldoAtual = -1.00;
		double valorSaque = -1.00;

		try {
			if (pId != null && !pId.equals("")) {
				id = Integer.parseInt(pId);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		try {
			if (pValor != null && !pValor.equals("")) {
				valorSaque = Double.parseDouble(pValor);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		Connection conn = (Connection) request.getAttribute("conexao");
		SaqueDAO saqueDAO = new SaqueDAO(conn);
		
		Saque saque = new Saque();
		saque.carregaUtilmoSaque(id, saqueDAO);
		
		saldoAtual = saque.getSaqueTO().getSaldoAtual();
		
		saque.setSaldoAtual(saldoAtual - valorSaque);
		saque.efetuarSaque(saqueDAO);
		
		ExtratoDAO extratoDAO = new ExtratoDAO(conn);
		saque.salvaMovBanc(extratoDAO);

		HttpSession session = request.getSession();
		session.setAttribute("ultimoSaldo", saque.getSaqueTO());

		RequestDispatcher dispatcher = request.getRequestDispatcher("EfetuarSaque.jsp");
		dispatcher.forward(request, response);
	}

}
