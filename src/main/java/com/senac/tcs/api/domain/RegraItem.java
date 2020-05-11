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
@Table(name="tb_regra_item")
public class RegraItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_regra_item")
    private Integer idRegraItem;
	
	@NotNull
    private TipoConectivo conectivo; /* SE/E/OU */
	
	@ManyToOne
	@JoinColumn(name="id_variavel")
	@NotNull    
    private Variavel variavel;	
	
	@NotNull
    private String condicional; /* =; <>; >; >=; <; <= */
	
	@ManyToOne
	@JoinColumn(name="id_variavel_valor")
	@NotNull    
	private VariavelValor variavelValor;
	
	@ManyToOne
	@JoinColumn(name="id_regra")
	@NotNull
	@JsonIgnore
    private Regra regra;

	public Integer getIdRegraItem() {
		return idRegraItem;
	}

	public void setIdRegraItem(Integer idRegraItem) {
		this.idRegraItem = idRegraItem;
	}

	public TipoConectivo getConectivo() {
		return conectivo;
	}

	public void setConectivo(TipoConectivo conectivo) {
		this.conectivo = conectivo;
	}

	public Variavel getVariavel() {
		return variavel;
	}

	public void setVariavel(Variavel variavel) {
		this.variavel = variavel;
	}

	public String getCondicional() {
		return condicional;
	}

	public void setCondicional(String condicional) {
		this.condicional = condicional;
	}

	public VariavelValor getVariavelValor() {
		return variavelValor;
	}

	public void setVariavelValor(VariavelValor variavelValor) {
		this.variavelValor = variavelValor;
	}

	public Regra getRegra() {
		return regra;
	}

	public void setRegra(Regra regra) {
		this.regra = regra;
	}	
}
