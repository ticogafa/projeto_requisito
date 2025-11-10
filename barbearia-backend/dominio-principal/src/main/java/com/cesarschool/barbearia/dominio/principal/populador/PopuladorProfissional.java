// package com.cesarschool.barbearia.dominio.principal.populador;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Component;

// import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
// import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
// import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
// import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
// import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;

// @Component
// @Order(1)
// public class PopuladorProfissional implements CommandLineRunner {

//     private final ProfissionalRepositorio profissionalRepositorio;

//     public PopuladorProfissional(ProfissionalRepositorio profissionalRepositorio) {
//         this.profissionalRepositorio = profissionalRepositorio;
//     }

//     @Override
//     public void run(String... args) throws Exception {
//         if (profissionalRepositorio.listarTodos().isEmpty()) {
//             Profissional profissional = new Profissional(
//                 "Barbeiro Padrão",
//                 new Email("barbeiro@barbearia.com"),
//                 new Cpf("12345678901"),
//                 new Telefone("81999999999")
//             );
//             profissionalRepositorio.salvar(profissional);
//             System.out.println(">>> Profissional padrão criado.");
//         }
//     }
// }
