package co.edu.unbosque.ciclo3demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DemoServlet
 */
// anotacion, patron de comportamiento definido, dice que esta clase sera un servlet para las peticiones que se hagan
// desde cualquier pagina web o jsp
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DemoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    // metodos que tienen como proposito el procesamiento de la accion que reciba del JSP, segun el method que se utilice
    // escribimos 2 instrucciones q permiten obtener los parametros de nombre y cedula a traves de request.getParameter
    // luego se procesan con un msje de vuelta a traves de PrintWriter
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		/*String nombre = request.getParameter("nombre");
		String cedula = request.getParameter("cedula");
		PrintWriter writer = response.getWriter();
		if (nombre != null && cedula != null) {
			writer.println("Bienvenido "+nombre+" a mi JSP");
		}
		else
			writer.println("Error: Nombre o Cedula faltante!");
		writer.close();*/
		
		String listar = request.getParameter("Listar");
		String agregar = request.getParameter("Agregar");
		
		// se evalua si la accion que se recibe de iniciojsp es listar o agregar
		if (agregar != null) {
			//instrucciones para agregar un usuario
			agregarUsuario(request, response);
			
		}
		
		if (listar != null) {
			//instrucciones para listar los usuarios
			listarUsuarios(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void agregarUsuario(HttpServletRequest request, HttpServletResponse response) {
		// inicializa objeto de tipo usuarios con los request.getParameter de todos los atributos
		Usuarios usuario = new Usuarios();
		usuario.setNombre_usuario(request.getParameter("nombre"));
		usuario.setCedula_usuario(request.getParameter("cedula"));
		usuario.setEmail_usuario(request.getParameter("email"));
		usuario.setUsuario(request.getParameter("usuario"));
		usuario.setPassword(request.getParameter("password"));
		int respuesta = 0;
		try { // invoca el metodo postJSON con dicho objeto
			respuesta = TestJSON.postJSON(usuario);
			PrintWriter writer = response.getWriter();
			// se obtiene una respuesta http desde la API usuarios/guardar de la backend. si es 200 http ok.
			if (respuesta == 200) { 
				writer.println("Registro agregado!");
			} else {
				writer.println("Error: " + respuesta);		
			}
			writer.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listarUsuarios(HttpServletRequest request, HttpServletResponse response) {
		try { 
			// invocamos el metodo getJSON y se guarda la lista de usuarios en la variable lista de tipo ArrayList
			ArrayList<Usuarios> lista = TestJSON.getJSON();
//nueva pagina jsp que crearemos, a esta nueva pagina enviaremos los resultados de la variable lista, a traves de 
//request.setAtribute la cual asigna un atributo para ser enviado a la pag jsp, por medio de un objeto de la clase requestDispatcher	
			String pagina = "/resultado.jsp";
			request.setAttribute("lista", lista);
	// 	
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagina);
			dispatcher.forward(request, response); // aqui es donde es enviado el atributo request.setAtribute
		} catch (Exception e) {
			e.printStackTrace();
		} // luego podra mostrarse en formato html por medio de scriplets
	}
}

	


