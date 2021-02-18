package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String indexMain(Model model) {
		return "index.html";
	}
	
	@GetMapping("/cursos")
	public String cursosMain(Model model) {
		return "cursos_main";
	}
	
	@GetMapping("/profesores")
	public String profesoresMain(Model model) {
		return "profesores_main";
	}
	
}
