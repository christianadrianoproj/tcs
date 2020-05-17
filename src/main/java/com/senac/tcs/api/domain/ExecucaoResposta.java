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
@Table(name = "tb_execucao_resposta")
public class ExecucaoResposta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_execucao_resposta")
	private Integer idExecucaoResposta;

	@ManyToOne
	@JoinColumn(name = "id_regra_item")
	@NotNull
	private RegraItem regraItem;

	@ManyToOne
	@JoinColumn(name = "id_execucao")
	@NotNull
	@JsonIgnore
	private Execucao execucao;
	
	@ManyToOne
	@JoinColumn(name="id_variavel_valor")
	@NotNull    
	private VariavelValor resposta;

	public Integer getIdExecucaoResposta() {
		return idExecucaoResposta;
	}

	public void setIdExecucaoResposta(Integer idExecucaoResposta) {
		this.idExecucaoResposta = idExecucaoResposta;
	}

	public RegraItem getRegraItem() {
		return regraItem;
	}

	public void setRegraItem(RegraItem regraItem) {
		this.regraItem = regraItem;
	}

	public Execucao getExecucao() {
		return execucao;
	}

	public void setExecucao(Execucao execucao) {
		this.execucao = execucao;
	}

	public VariavelValor getResposta() {
		return resposta;
	}

	public void setResposta(VariavelValor resposta) {
		this.resposta = resposta;
	}	

}
