package com.senac.tcs.api.domain;

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
@Table(name="tb_variavel")
public class Variavel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_variavel")
    private Integer idVariavel;
    
	@NotNull
    @Column(length = 200)
    private String nome;
    
	@NotNull    
    private Integer tipoVariavel;
	
    @OneToMany(orphanRemoval = true, mappedBy = "variavel")
    private List<VariavelValor> valores;

	public Integer getIdVariavel() {
		return idVariavel;
	}

	public void setIdVariavel(Integer idVariavel) {
		this.idVariavel = idVariavel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTipoVariavel() {
		return tipoVariavel;
	}

	public void setTipoVariavel(Integer tipoVariavel) {
		this.tipoVariavel = tipoVariavel;
	}

	public List<VariavelValor> getValores() {
		return valores;
	}

	public void setValores(List<VariavelValor> valores) {
		this.valores = valores;
	}
    
}
