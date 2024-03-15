package app;

import static spark.Spark.*;
import service.ProdutoService;
import spark.Spark;

public class Aplicacao {
	
	private static ProdutoService produtoService = new ProdutoService();
	public static void main(String[] arg) {
		Spark.port(8080);
		
		post("/produto", (request, response) -> produtoService.insert(request, response));
		
		get("/produto/:id", (request, response) -> produtoService.get(request, response));
		
		get("/produto/update/:id", (request, response) -> produtoService.update(request, response));
		
		get("/produto/delete/:id", (request, response) -> produtoService.delete(request, response));
		
		get("/produto", (request, response) -> produtoService.getAll(request, response));
		
		get("/produto/update/:id", (request, response) -> produtoService.getToUpdate(request, response));
		
	}
}