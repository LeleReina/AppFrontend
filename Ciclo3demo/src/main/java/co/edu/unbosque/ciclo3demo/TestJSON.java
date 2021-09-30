package co.edu.unbosque.ciclo3demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

//import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestJSON {
	private static URL url;
	private static String sitio = "http://localhost:5000/";
	

// metodo que procesa el mensaje json recibido por getJSON desde la API de listar usuarios y devuelve un arraylist tipo usuarios con los usuarios
	
	public static ArrayList<Usuarios> parsingUsuarios(String json) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
		JSONArray usuarios = null;
		try {
			usuarios = (JSONArray) jsonParser.parse(json);
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator i = usuarios.iterator();
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			Usuarios usuario = new Usuarios();
			usuario.setCedula_usuario(innerObj.get("cedula_usuario").toString());
			usuario.setEmail_usuario(innerObj.get("email_usuario").toString());
			usuario.setNombre_usuario(innerObj.get("nombre_usuario").toString());
			usuario.setPassword(innerObj.get("password").toString());
			usuario.setUsuario(innerObj.get("usuario").toString());
			lista.add(usuario);
		}
		return lista;
	}
	
// metodos que permite recuperar los usuarios. realiza operacion GET desde la url de la API para listar todos los
// usuarios. el mensaje json que obtiene es procesada por el metodo parsingusuarios. devuelve arraylist tipo usuarios
	
	public static ArrayList<Usuarios> getJSON() throws IOException, ParseException {
		url = new URL(sitio + "usuarios/listar");
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");
		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json= "";
		for (int i= 0; i<inp.length; i++) {
			json += (char)inp[i];	
		}
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
		lista = parsingUsuarios(json);
		http.disconnect();
		return lista;
	}
	
	
// metodo que permite insertar un nuevo usuario realizando la operacion POST hacia la url de la API, retorna respuesta 
//http de la url, 200 es exitosa. recibe como parametro objeto de tipo usuario con usuario que se quiere agregar
	
	public static int postJSON(Usuarios usuario) throws IOException {
		url = new URL(sitio + "usuarios/guardar");
		
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();
		try {
			http.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		http.setDoOutput(true);
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("Content-Type", "application/json");
		String data = "{"
				+ "\"cedula_usuario\":\""+ usuario.getCedula_usuario()
				+ "\", \"email_usuario\": \""+ usuario.getEmail_usuario()
				+ "\", \"nombre_usuario\": \""+ usuario.getNombre_usuario()
				+ "\", \"password\": \""+ usuario.getPassword()
				+ "\", \"usuario\": \""+ usuario.getUsuario()
				+ "\"}";
		byte[] out = data.getBytes(StandardCharsets.UTF_8);
		OutputStream stream = http.getOutputStream();
		stream.write(out);
		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;		
	}
}
