package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.net.MalformedURLException;
import java.sql.Blob;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Uso_BD;
import com.example.demo.Model.Anuncio;
import com.example.demo.Model.Curso;
import com.example.demo.Model.Foros;
import com.example.demo.Model.Material;
import com.example.demo.Model.Mensaje;
import com.example.demo.Model.Usuario;
import com.example.demo.Repository.AnuncioRepository;
import com.example.demo.Repository.CursoRepository;
import com.example.demo.Repository.ForosRepository;
import com.example.demo.Repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;



@Controller
public class IndexController {
	
	private List<Foros> foros = new ArrayList<>();
	private List<Curso> cursos = new ArrayList<Curso>();
	private List<Anuncio> anuncios = new ArrayList<Anuncio>();
	private List<Mensaje> m;
	public static Usuario usuario = new Usuario();
	public static String bphoto;
	public Uso_BD uso_bd = new Uso_BD();
	//public Service service = new Service();


	//public Service service = new Service();
	
	@Autowired 
	private UsuarioRepository userRep;
	
	@Autowired 
	private AnuncioRepository repositorioAnuncio;
	

	@Autowired 
	private ForosRepository repositorioForos;
	
	@Autowired 
	private CursoRepository repositorioCurso;
	
	
	public IndexController () {		
		
		foros.add(new Foros("DUDA", "BLABLABLA", null));	
	}
	
	
	//************ PAGINA INICIAL ************//
	
	@GetMapping("/")
	public String indexMain(Model model) {
		return "PaginaInicio";
	}
	
	
	@GetMapping("/administrador")
	public String administrador(Model model) {
		return "Administrador";
	}
	
	@PostMapping("/profesorAgregado")
	public String profesorAgregado(Model model, @RequestParam String correo, @RequestParam String contraseña) {

		model.addAttribute("correo", correo);
		
		return "ProfesorAgregadoConfirmacion";
	}
	
	


	//************ FOROS ************//
	
	
	@GetMapping("/foros")
	public String MostrarForos (Model model) {
		
		//foros.add(new Foros("PREGUNTA", "BLABLABLA"));		
	
		
		if (repositorioForos.findAll() != null) {
			model.addAttribute("foro",  repositorioForos.findAll());
		}
		
		
		return "Foros";
	}
	
	
	@PostMapping("/foros/nuevoforo/creado")
	public String NuevoForo(Model model, @RequestParam String asunto, @RequestParam String mensaje) {
		
		Foros foroNuevo = new Foros(asunto, mensaje, null);
		
		repositorioForos.save(foroNuevo);
		
		/*
		foros.add(new Foros(asunto, mensaje, null));
		*/
		
		return "forocreado";
	}
	
	@GetMapping("/foros/{IDForo}")
	public String VerForo(Model model, @PathVariable int IDForo) {
		
		
		Optional<Foros> foro = repositorioForos.findById(IDForo);
		 if (foro.isPresent()) {
				model.addAttribute("infoForo",foro.get());
		 } else {
		
		 }
		/*
		 if(foro.get().getMensajes() != null) {
			 
				m = foro.get().getMensajes();
				usuario.setNombre("Soy un usuario");
				m.add(new Mensaje(usuario, "mensaje enviado"));
				model.addAttribute("mensaje", m);
			 
		 }*/
		
	
		//Foros miforo = foros.get(IDForo-1);
		
		//m = miforo.getMensajes();
		
		//model.addAttribute("info", foro);
	
		//model.addAttribute("IDForo", (IDForo));
		
		
		return "Foro";
	}
	
	@PostMapping("/foros/{IDForo}/respuesta")
	public String VerForo (Model model, @PathVariable int IDForo, @RequestParam String respuesta) {
		
		
		
		Foros foro = foros.get(IDForo-1);
		m = foro.getMensajes();
		usuario.setNombre("Soy un usuario");
		m.add(new Mensaje(usuario, respuesta));
		model.addAttribute("info", foro);
		model.addAttribute("mensaje", m);
		model.addAttribute("IDForo", (IDForo));
		
		
		return "Foro";
	}
	
	
	
	@GetMapping("/foros/nuevoforo")
	public String CrearForo (Model model) {
		
		return "CrearForo";
	}
	
	
	//************ LOGIN Y REGISTRO ************//
	
	//Llamada cuando pulsamos el botón de Login, aparecerá el formulario para logearse.
	@GetMapping("/login")
	public String loginMain(Model model) {
		return "Iniciar_Sesion";
	}
	
	/*	@PostMapping("/checkLogin")
	public String comprobarLogin(Model model, @RequestParam String correo, @RequestParam String contrasena) {		
	
		String url = "Error_Login";
		
		Optional<Usuario> usuario = repositorio.findByNombre(nombre);
		
		if(usuario.isPresent()) {
			
			if (contrasena == usuario.getContraseña()) {
				url = "Sesion_Iniciada_Template";
			} else {
				String error = "Contraseña errónea";
				model.addAttribute("mensajeError", error);
			}
			
		} else {
			String error = "Nombre de usuario erróneo";
			model.addAttribute("mensajeError", error);
		}
		
		
		return url;
	}*/
	
	/*
	@GetMapping("/descargarMaterial")
	public ResponseEntity<Object> descargarMaterial(Model model) throws MalformedURLException {
		
		Path pathMaterial = MATERIAL_FOLDER.resolve("material.pdf");
		Resource material = new UrlResource(pathMaterial.toUri());
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "file/pdf").body(material);

	}
	*/
	
	//Llamada cuando pulsemos el botón de Registrarse, aparecerá el formulario de New User
	@GetMapping("/newuser")
	public String newuserMain(Model model) {

		return "Registro_NuevoUsuario";
	}
	
	
	//************ BIENVENIDA Y PAGINA PRINCIPAL ************//
	
	//Se llama al método cuando se pulsa el botón "Iniciar Sesión" o "Registrarse", y se muestra la plantilla de bienvenido.
	@PostMapping("/bienvenido")
	public String bienvenido(Model model, @RequestParam String correo, @RequestParam String contraseña_1, @RequestParam MultipartFile image,
			@RequestParam String nombreUsuario, @RequestParam String primerApellido, @RequestParam String apellido2,
			@RequestParam("tipoUsuario") String tipoUsuario, @RequestParam("metodoPago") String metodoPago, HttpSession sesion) {
		
			
		model.addAttribute("correo", correo);
		
		Optional<Usuario> u = userRep.findByCorreo(correo);
		if(u.isPresent()) {
			return "volver_a_registro";
		}else {		
			int tipoU, metodoP;
			
			if(tipoUsuario.equals("Usuario estándar")) tipoU = 0;
			else tipoU = 1;
			
			if(metodoPago.equals("Tarjeta de crédito")) metodoP = 0;
			else metodoP = 1;
			
			Usuario registrado = new Usuario(nombreUsuario, primerApellido, apellido2, 
					contraseña_1, 0, tipoU, correo, metodoP, null);
			
			System.out.println("Metodo Pago "+ metodoPago);
			
			//Comprobar que no haya nadie en la base de datos con ese correo
			//*****************
					
			byte[] bytes;
			
			if (image != null) {
				try {
					// Por si se quiere guardar tambien el nombre y el tamaño de la imagen
					String nombreFoto = image.getOriginalFilename();
					long tamañoFoto = image.getSize();
					
					bytes = image.getBytes();
					Blob imagen = new javax.sql.rowset.serial.SerialBlob(bytes);
					usuario.setFotoPerfil(imagen);
					
					bphoto = java.util.Base64.getEncoder().encodeToString(bytes);
					
					model.addAttribute("fotoperfil", bphoto);
					
					registrado.setFotoPerfil(imagen);
				}
				catch (Exception exc){
					return "Fallo al establecer la imagen de perfil";
				}
			}
			
			userRep.save(registrado);
				
			sesion.setAttribute("user", registrado);
			
			return "bienvenido";
		}
	}
	
	@PostMapping("/bienvenidoI")
	public String bienvenidoInicio(Model model, @RequestParam String correo, @RequestParam String contrasena, HttpSession sesion) {
		
		model.addAttribute("correo", correo);
		
		Optional<Usuario> u = userRep.findByCorreo(correo);
		if(u.isPresent()) {
			if(contrasena.equals(u.get().getContraseña())) {
				sesion.setAttribute("user", u.get());
				return "bienvenidoI";
			}else {
				return "volver_a_login";
			}
		}else {
			return "no_registrado";
		}
	}
	
	/*@GetMapping("/iniciada")
	public String sesionIniciadaMain(Model model, @RequestParam String nombreUsuario) {		
		
		model.addAttribute("name", nombreUsuario);
		
		return "iniciada";
	}*/
	
	
	//************ ANUNCIOS ************//
	
	@GetMapping("/anuncios")
	public String anuncios(Model model) {
		
	
		if (repositorioAnuncio.findAll() != null) {
			model.addAttribute("anuncios",  repositorioAnuncio.findAll());
		}

		return "Anuncios";
	}	
	
	@GetMapping("/crearAnuncio")
	public String crearAnuncio(Model model) {
		
		return "formulario_crear_anuncio";
		
	}
	
	
	@PostMapping("/anuncioCreado")
	public String anuncioCreado(Model model, @RequestParam String materia, @RequestParam String curso, 
			@RequestParam String horario, @RequestParam String precio, @RequestParam String contenido) {
		
		usuario.setNombre("Alvaro");
		//anuncios.add(new Anuncio (usuario, materia, contenido, horario, precio, curso) ); //Falta pasar como primer parámetro el profesor que crea el anuncio
		Anuncio anuncio = new Anuncio(materia, contenido, horario, precio, curso);
		repositorioAnuncio.save(anuncio);
		
		//service.CrearAnuncio();
		
		return "anuncio_creado";
	}
	
	
	
	@GetMapping("/anuncios/{IDAnuncio}")
	public String anuncio(Model model, @PathVariable int IDAnuncio) {
		
		//model.addAttribute("infoProfesor", idAnuncio);
		//model.addAttribute("nombreProfesor", "Nombre del profesor");

		
		Anuncio anuncio = anuncios.get(IDAnuncio-1);
		
		
		
		model.addAttribute("infoAnuncio", anuncio);
		Usuario profesor = anuncio.getProfesor();
		model.addAttribute("profesor", profesor);
		
		
		
		
		return "Anuncio";
	}
	
	@GetMapping("/eliminarAnuncio/{index}")
	public String eliminarAnuncio(Model model, @PathVariable int index) {
		
		//anuncios.remove(index-1);
		
		repositorioAnuncio.deleteById(index);

		return "anuncio_eliminado";
	}
	
	
	//************ CURSOS ************//
	
	@GetMapping("/cursosDisponibles")
	public String cursosDisponibles(Model model, HttpSession sesion) {
		
		/*
		List <String> imagenes_Cursos = Arrays.asList("https://i0.wp.com/mathsteachercircles.org.au/wp-content/uploads/2020/10/cropped-MTC_Icon_RGB.png?fit=190%2C190&ssl=1","https://pbs.twimg.com/profile_images/1110160859689615361/V8CE--1C.png");
		
		for (int i=0; i<imagenes_Cursos.size();i++) {
			Curso c = new Curso();
			c.setImagen(imagenes_Cursos.get(i));
			cursos.add(c);
		}
		*/
		//imagenes_Cursos.add("https://i0.wp.com/mathsteachercircles.org.au/wp-content/uploads/2020/10/cropped-MTC_Icon_RGB.png?fit=190%2C190&ssl=1");
		//imagenes_Cursos.add("https://pbs.twimg.com/profile_images/1110160859689615361/V8CE--1C.png");
		
		Usuario usuario = (Usuario) sesion.getAttribute("user");
		
		List<Curso> listaCursos = usuario.getCursos();
		
		model.addAttribute("cursos", listaCursos);
		
		return "Cursos";
	}
	
	
	@GetMapping("/eliminarCurso/{index}")
	public String eliminarCurso(Model model, @PathVariable int index, HttpSession sesion) {
		
		
		Usuario usuario = (Usuario) sesion.getAttribute("user");
		usuario.EliminarCurso(index);
		
		
		return "CursoEliminado";
	}
	
	@GetMapping("/curso/{index}")
	public String verCurso(Model model, @PathVariable int index) {	
		
		model.addAttribute("index", index);
		
		return "informacion_curso";
	}
	
	/*
	@GetMapping("/cursosDisponibles/nuevoCurso")
	public String nuevoCurso(Model model) {
				
		//List <String> imagenes_Cursos = Arrays.asList("https://i0.wp.com/mathsteachercircles.org.au/wp-content/uploads/2020/10/cropped-MTC_Icon_RGB.png?fit=190%2C190&ssl=1","https://pbs.twimg.com/profile_images/1110160859689615361/V8CE--1C.png", "https://upload.wikimedia.org/wikipedia/en/c/cd/March_for_Science.png");
		
		model.addAttribute("cursos", imagenes_Cursos);
		
		return "Nuevo_Curso";
	}
	*/
	
	@GetMapping("/crearCurso")
	public String crearCurso(Model model) {
	
		return "Crear_Curso";
	}
	
	@PostMapping("/crearCursoConfirmacion")
	public String crearCursoConfirmacion(Model model, @RequestParam String titulo, @RequestParam String descripcion, HttpSession sesion) {
	
		Usuario usuario = (Usuario) sesion.getAttribute("user");
		usuario.AñadirCurso(titulo, descripcion);
			
		return "Curso_Creado_Confirmacion";
	}
	
	@GetMapping("/cursosDisponibles/Actualizado")
	public String actualizarNuevoCurso(Model model) {	
		
		return "Nuevo_Curso";
	}
	
	@GetMapping("/paginaprincipal")
	public String paginaPrincipal(Model model) {	
		
		Optional<Usuario> u = userRep.findByNombre("alvaro");
		if (u.isPresent()) {
			model.addAttribute("hayUsuario", true);
			model.addAttribute("usuario", u);
		}
		else {
			model.addAttribute("hayUsuario", false);
		}
			
		return "paginaprincipal";
	}
	

}
