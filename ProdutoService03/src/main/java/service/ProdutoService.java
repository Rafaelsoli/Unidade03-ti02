package service;

import java.util.Scanner;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;


public class ProdutoService {

    private static final int FORM_ORDERBY_NOME = 0;
	private ProdutoDAO produtoDAO = new ProdutoDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_DESCRICAO = 2;
    private final int FORM_ORDERBY_PRECO = 3;
    private final int FORM_ORDERBY_MARCA = 4;
    
    public ProdutoService() {
        makeForm();
    }

    
    public void makeForm() {
        makeForm(FORM_INSERT, new Produto(), FORM_ORDERBY_DESCRICAO);
    }

    
    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Produto(), orderBy);
    }

    
    public void makeForm(int tipo, Produto produto, int orderBy) {
        String nomeArquivo = "form.html";
        form = "";
        try{
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while(entrada.hasNext()){
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        }  catch (Exception e) { System.out.println(e.getMessage()); }
        
        String umProduto = "";
        if(tipo != FORM_INSERT) {
            umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/produto/list/1\">Novo Produto</a></b></font></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t</table>";
            umProduto += "\t<br>";            
        }
        
        if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/produto/";
            String name, descricao, marca, buttonLabel;
            if (tipo == FORM_INSERT){
                action += "insert";
                name = "Inserir Produto";
                descricao = "leite, pão, ...";
                marca = "Marca...";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + produto.getId();
                name = "Atualizar Produto (ID " + produto.getId() + ")";
                descricao = produto.getNome();
                marca = produto.getMarca();
                buttonLabel = "Atualizar";
            }
            umProduto += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td colspan=\"4\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td colspan=\"4\" align=\"left\">&nbsp;</td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\""+ descricao +"\"></td>";
            umProduto += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ produto.getPreco() +"\"></td>";
            umProduto += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ produto.getQuantidade() +"\"></td>";
            umProduto += "\t\t\t<td>Marca: <input class=\"input--register\" type=\"text\" name=\"marca\" value=\""+ marca +"\"></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ produto.getDataLancamento().toString() + "\"></td>";
            umProduto += "\t\t\t<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t</table>";
            umProduto += "\t</form>";       
        } else if (tipo == FORM_DETAIL){
            umProduto += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Produto (ID " + produto.getId() + ")</b></font></td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td colspan=\"6\" align=\"left\">&nbsp;</td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td>&nbsp;Nome: "+ produto.getNome() +"</td>";
            umProduto += "\t\t\t<td>Preco: "+ produto.getPreco() +"</td>";
            umProduto += "\t\t\t<td>Quantidade: "+ produto.getQuantidade() +"</td>";
            umProduto += "\t\t\t<td>Marca: "+ produto.getMarca() +"</td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t\t<tr>";
            umProduto += "\t\t\t<td>&nbsp;Data de fabricação: "+ produto.getDataLancamento().toString() + "</td>";
            umProduto += "\t\t\t<td>&nbsp;</td>";
            umProduto += "\t\t</tr>";
            umProduto += "\t</table>";       
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-PRODUTO>", umProduto);
        
        String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
        list += "\n<tr><td colspan=\"7\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Produtos</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"7\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" + 
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Nome</b></a></td>\n" +
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_MARCA + "\"><b>Marca</b></a></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
                "</tr>\n";
        
        List<Produto> produtos;
        if (orderBy == FORM_ORDERBY_ID) {                  produtos = produtoDAO.getOrderByID();
        } else if (orderBy == FORM_ORDERBY_NOME) {    produtos = produtoDAO.getOrderByNome();
        } else if (orderBy == FORM_ORDERBY_PRECO) {        produtos = produtoDAO.getOrderByPreco();
        } else if (orderBy == FORM_ORDERBY_MARCA) {        produtos = produtoDAO.getOrderByMarca();
        } else {                                            produtos = produtoDAO.get();
        }

        int i = 0;
        String bgcolor = "";
        for (Produto p : produtos) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
                      "\t<td>" + p.getId() + "</td>\n" +
                      "\t<td>" + p.getNome() + "</td>\n" +
                      "\t<td>" + p.getPreco() + "</td>\n" +
                      "\t<td>" + p.getMarca() + "</td>\n" +
                      "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/" + p.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                      "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/update/" + p.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                      "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteProduto('" + p.getId() + "', '" + p.getNome() + "', '" + p.getPreco() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                      "</tr>\n";
        }
        list += "</table>";     
        form = form.replaceFirst("<LISTAR-PRODUTO>", list);               
    }
    
    
    public Object insert(Request request, Response response) {
        String nome = request.queryParams("nome");
        float preco = Float.parseFloat(request.queryParams("preco"));
        int quantidade = Integer.parseInt(request.queryParams("quantidade"));
        String marca = request.queryParams("marca");
        LocalDateTime dataLancamento = LocalDateTime.parse(request.queryParams("dataFabricacao"));
        
        String resp = "";
        
        Produto produto = new Produto(-1, nome, marca, preco, quantidade, dataLancamento);
        
        if(produtoDAO.insert(produto) == true) {
            resp = "Produto (" + nome + ") inserido!";
            response.status(201); // 201 Created
        } else {
            resp = "Produto (" + nome + ") não inserido!";
            response.status(404); // 404 Not found
        }
            
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    
    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));       
        Produto produto = (Produto) produtoDAO.get(id);
        
        if (produto != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, produto, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

        return form;
    }

    
    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));       
        Produto produto = (Produto) produtoDAO.get(id);
        
        if (produto != null) {
            response.status(200); // success
            makeForm(FORM_UPDATE, produto, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

        return form;
    }
    
    
    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }           
    
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);
        String resp = "";       

        if (produto != null) {
            produto.setNome(request.queryParams("nome"));
            produto.setPreco(Float.parseFloat(request.queryParams("preco")));
            produto.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
            produto.setMarca(request.queryParams("marca"));
            produto.setDataLancamento(LocalDateTime.parse(request.queryParams("dataLancamento")));
            produtoDAO.update(produto);
            response.status(200); // success
            resp = "Produto (ID " + produto.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (ID \" + produto.getId() + \") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    
    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);
        String resp = "";       

        if (produto != null) {
            produtoDAO.delete(id);
            response.status(200); // success
            resp = "Produto (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (" + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }
}
