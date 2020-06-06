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

/**
*
* @author Christian
*/

@Entity
@Table(name="tb_interface")
public class Interface {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_interface")
    private Integer idInterface;
	
	@ManyToOne
	@JoinColumn(name="id_variavel")
	@NotNull    
    private Variavel variavel;
	
	@NotNull
    @Column(length = 255)
    private String pergunta;

	public Integer getIdInterface() {
		return idInterface;
	}

	public void setIdInterface(Integer idInterface) {
		this.idInterface = idInterface;
	}

	public Variavel getVariavel() {
		return variavel;
	}

	public void setVariavel(Variavel variavel) {
		this.variavel = variavel;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}
}
