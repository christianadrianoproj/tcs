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
@Table(name="tb_variavel_valor")
public class VariavelValor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_variavel_valor")
    private Integer idVariavelValor;

	@Column(length = 500)
    private String valor;
	
	@ManyToOne
	@JoinColumn(name="id_variavel")
	@NotNull
	@JsonIgnore
    private Variavel variavel;

	public Integer getIdVariavelValor() {
		return idVariavelValor;
	}

	public void setIdVariavelValor(Integer idVariavelValor) {
		this.idVariavelValor = idVariavelValor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Variavel getVariavel() {
		return variavel;
	}

	public void setVariavel(Variavel variavel) {
		this.variavel = variavel;
	}
}
