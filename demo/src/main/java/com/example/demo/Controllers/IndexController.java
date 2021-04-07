package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;
import javax.swing.ImageIcon;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Model.Anuncio;
import com.example.demo.Model.Curso;
import com.example.demo.Model.Foros;
import com.example.demo.Model.Mensaje;
import com.example.demo.Model.Usuario;
import com.example.demo.Repository.AnuncioRepository;
import com.example.demo.Repository.CursoRepository;
import com.example.demo.Repository.ForosRepository;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.services.FilterService;
import com.example.demo.services.ImageService;

import java.util.ArrayList;
import java.util.List;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



@Controller
public class IndexController {
	
	private List<Foros> foros = new ArrayList<>();
	public static Usuario usuario = new Usuario();
	public static String bphoto;
	
	@Autowired 
	private UsuarioRepository userRep;
	
	@Autowired
	private ImageService imageServ;
	
	
	public IndexController () {		
		
		foros.add(new Foros("DUDA", "BLABLABLA", null));	
	}
	
	
	@GetMapping("/")
	public String indexMain(Model model/*, HttpServletRequest request*/) {
		
		//model.addAttribute("admin", request.isUserInRole("administrador"));
		
		return "PaginaDeInicio/PaginaInicio";
	}
	
	
	@GetMapping("/administrador")
	public String administrador(Model model) {
		return "Administrador/Administrador";
	}
	
	@PostMapping("/profesorAgregado")
	public String profesorAgregado(Model model, @RequestParam String correo, @RequestParam String contraseña_1, @RequestParam String apellido1,
			@RequestParam String apellido2, @RequestParam String nombreUsuario, HttpSession sesion, @RequestParam MultipartFile image) throws IOException, SerialException, SQLException {
			
		model.addAttribute("correo", correo);
		
		Optional<Usuario> u = userRep.findByCorreo(correo);
		if(u.isPresent()) {
			return "PaginaDeInicio/volver_a_registro";
		}else {					
			//List<String> roles = new ArrayList<String>();
			//roles.add("profesor");
			
			Usuario profesor = new Usuario(nombreUsuario, apellido1, apellido2, contraseña_1, 0, 1, correo, 0, null,
					"USER", "PROFESOR");
				
			byte[] bytes;
			
			if (image != null) {
				try {
					
					String nombreFoto = image.getOriginalFilename();
					long tamañoFoto = image.getSize();
					
					bytes = image.getBytes();
					
					String formatName = nombreFoto.substring(nombreFoto.lastIndexOf(".") + 1);	
					bytes = imageServ.resize(bytes, 200, 200, formatName);
					
					Blob imagen = new javax.sql.rowset.serial.SerialBlob(bytes);
					usuario.setFotoPerfil(imagen);
					
					bphoto = java.util.Base64.getEncoder().encodeToString(bytes);
					
					model.addAttribute("fotoperfil", bphoto);
					
					profesor.setFotoPerfil(imagen);
				}
				catch (Exception exc){
					return "Fallo al establecer la imagen de perfil";
				}
			}
			
			userRep.save(profesor);
				
			sesion.setAttribute("user", profesor);
		}
		
		return "Administrador/ProfesorAgregadoConfirmacion";
	}

	
	
	//Llamada cuando pulsamos el botón de Login, aparecerá el formulario para logearse.
	@GetMapping("/login")
	public String loginMain(Model model, HttpServletRequest request) {
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
		model.addAttribute("token", token.getToken());
		model.addAttribute("atributo", true);
		return "PaginaDeInicio/Iniciar_Sesion";
	}
	
	//Llamada cuando pulsemos el botón de Registrarse, aparecerá el formulario de New User
	@GetMapping("/newuser")
	public String newuserMain(Model model, HttpServletRequest request) {
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
		model.addAttribute("token", token.getToken());
		model.addAttribute("atributo", true);
		return "PaginaDeInicio/Registro_NuevoUsuario";
	}
	
	
	// Se llama al método cuando se pulsa el botón "Iniciar Sesión" o "Registrarse",
	// y se muestra la plantilla de bienvenido.
	@PostMapping("/bienvenido")
	public String bienvenido(Model model, @RequestParam String correo, @RequestParam String contraseña_1,
			@RequestParam MultipartFile image, @RequestParam String nombreUsuario, @RequestParam String primerApellido,
			@RequestParam String apellido2, @RequestParam("tipoUsuario") String tipoUsuario,
			@RequestParam("metodoPago") String metodoPago, HttpSession sesion) {

		model.addAttribute("correo", correo);

		Optional<Usuario> u = userRep.findByCorreo(correo);
		if (u.isPresent()) {
			return "volver_a_registro";
		} else {
			int tipoU, metodoP;

			if (tipoUsuario.equals("Usuario estándar"))
				tipoU = 0;
			else
				tipoU = 1;

			if (metodoPago.equals("Tarjeta de crédito"))
				metodoP = 0;
			else
				metodoP = 1;
			
			//List<String> roles = new ArrayList<String>();
			//roles.add("user");
			
			Usuario registrado = new Usuario(nombreUsuario, primerApellido, apellido2, contraseña_1, 0, tipoU, correo,
					metodoP, null, "ADMIN");

			byte[] bytes;

			if (!image.isEmpty()) {
				try {
					// Por si se quiere guardar tambien el nombre y el tamaño de la imagen
					String nombreFoto = image.getOriginalFilename();
					long tamañoFoto = image.getSize();

					bytes = image.getBytes();

					String formatName = nombreFoto.substring(nombreFoto.lastIndexOf(".") + 1);
					bytes = imageServ.resize(bytes, 200, 200, formatName);

					Blob imagen = new javax.sql.rowset.serial.SerialBlob(bytes);
					usuario.setFotoPerfil(imagen);

					bphoto = java.util.Base64.getEncoder().encodeToString(bytes);

					model.addAttribute("fotoperfil", bphoto);

					registrado.setFotoPerfil(imagen);
				} catch (Exception exc) {
					return "Fallo al establecer la imagen de perfil";
				}
			}

			userRep.save(registrado);

			sesion.setAttribute("user", registrado);

			return "PaginaDeInicio/bienvenido";
		}
	}
	
	
	@PostMapping("/bienvenidoI")
	public String bienvenidoInicio(Model model, @RequestParam String correo, @RequestParam String contrasena, HttpSession sesion) {
		
		System.out.println("Hola, has iniciado sesion");

		
		model.addAttribute("correo", correo);
		
		Optional<Usuario> u = userRep.findByCorreo(correo);
		if(u.isPresent()) {
			if(contrasena.equals(u.get().getContraseña())) {
				sesion.setAttribute("user", u.get());
				return "PaginaDeInicio/bienvenidoI";
			}else {
				return "PaginaDeInicio/volver_a_login";
			}
		}else {
			return "PaginaDeInicio/no_registrado";
		}
	}	
		
	
	
	@GetMapping("/paginaprincipal")
	public String paginaPrincipal(Model model, HttpSession sesion, HttpServletRequest request) {	
		
		model.addAttribute("user", request.isUserInRole("USER"));
		
		/*
		Usuario u = (Usuario) sesion.getAttribute("user");
		if(u == null) {
			Optional<Usuario> user = userRep.findByCorreo("------------");
			if(user.isPresent()){
			  sesion.setAttribute("user", user.get());
			}
			else{
			  u = new Usuario("------------", "------------", "------------", "------------", 0, 0, "------------", 0, null, null);
			  sesion.setAttribute("user", u);
			  userRep.save(u);
			}
		} 
		*/

		return "PaginaPrincipal/paginaprincipal";
	}
	

}
