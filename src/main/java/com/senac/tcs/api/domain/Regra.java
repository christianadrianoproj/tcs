package com.senac.tcs.api.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
*
* @author Christian
*/

@Entity
@Table(name="tb_regra")
public class Regra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_regra")
    private Integer idRegra;
	
	@NotNull
    private Integer ordem;
	
	@NotNull
    @Column(length = 200)
    private String nome;
    
	@NotNull
    private LocalDate dataRegra;

    @OneToMany(mappedBy = "regra")
    private List<RegraItem> itens;
    
    @OneToMany(mappedBy = "regra")
    private List<RegraItemResultado> resultados;

	public Integer getIdRegra() {
		return idRegra;
	}

	public void setIdRegra(Integer idRegra) {
		this.idRegra = idRegra;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataRegra() {
		return dataRegra;
	}

	public void setDataRegra(LocalDate dataRegra) {
		this.dataRegra = dataRegra;
	}

	public List<RegraItem> getItens() {
		return itens;
	}

	public void setItens(List<RegraItem> itens) {
		this.itens = itens;
	}

	public List<RegraItemResultado> getResultados() {
		return resultados;
	}

	public void setResultados(List<RegraItemResultado> resultados) {
		this.resultados = resultados;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
    
}
