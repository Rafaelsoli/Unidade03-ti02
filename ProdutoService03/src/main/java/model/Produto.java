package model;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Produto {
	private int id;
	private String Nome;
	private String Marca;
	private float preco;
	private int quantidade;	
	private LocalDateTime dataLancamento;
	
	public Produto() {
	    id = -1;
	    Nome = "";
	    Marca = "";
	    preco = 0.00F;
	    quantidade = 0;
	    dataLancamento = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public Produto(int id, String nome, String marca, float preco, int quantidade, LocalDateTime dataLancamento) {
	    setId(id);
	    setNome(nome);
	    setMarca(marca);
	    setPreco(preco);
	    setQuantidade(quantidade);
	    setDataLancamento(dataLancamento);
	}       

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public String getNome() {
	    return Nome;
	}

	public void setNome(String nome) {
	    this.Nome = nome;
	}

	public String getMarca() {
	    return Marca;
	}

	public void setMarca(String marca) {
	    this.Marca = marca;
	}

	public float getPreco() {
	    return preco;
	}

	public void setPreco(float preco) {
	    this.preco = preco;
	}

	public int getQuantidade() {
	    return quantidade;
	}

	public void setQuantidade(int quantidade) {
	    this.quantidade = quantidade;
	}

	public LocalDateTime getDataLancamento() {
	    return dataLancamento;
	}

	public void setDataLancamento(LocalDateTime dataLancamento) {
	    LocalDateTime agora = LocalDateTime.now();
	    
	    if (agora.compareTo(dataLancamento) >= 0)
	        this.dataLancamento = dataLancamento;
	}
}