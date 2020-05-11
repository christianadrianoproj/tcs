package com.senac.tcs.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
*
* @author Christian
*/
@Entity
@Table(name="tb_regra_item_resultado")
public class RegraItemResultado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_regra_item_resultado")
    private Integer idRegraItemResultado;
	
	@ManyToOne
	@JoinColumn(name="id_variavel")
	@NotNull    
    private Variavel variavel;
	
	@ManyToOne
	@JoinColumn(name="id_variavel_valor")
	@NotNull    
	private VariavelValor variavelValor;
	
	@NotNull    
	private Double fatorConfianca;
	
	@ManyToOne
	@JoinColumn(name="id_regra")
	@NotNull
	@JsonIgnore
    private Regra regra;

	public Integer getIdRegraItemResultado() {
		return idRegraItemResultado;
	}

	public void setIdRegraItemResultado(Integer idRegraItemResultado) {
		this.idRegraItemResultado = idRegraItemResultado;
	}

	public Variavel getVariavel() {
		return variavel;
	}

	public void setVariavel(Variavel variavel) {
		this.variavel = variavel;
	}

	public VariavelValor getVariavelValor() {
		return variavelValor;
	}

	public void setVariavelValor(VariavelValor variavelValor) {
		this.variavelValor = variavelValor;
	}

	public Double getFatorConfianca() {
		return fatorConfianca;
	}

	public void setFatorConfianca(Double fatorConfianca) {
		this.fatorConfianca = fatorConfianca;
	}

	public Regra getRegra() {
		return regra;
	}

	public void setRegra(Regra regra) {
		this.regra = regra;
	}
}
