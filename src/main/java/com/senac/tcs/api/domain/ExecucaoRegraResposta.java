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
@Table(name = "tb_execucao_regra_resposta")
public class ExecucaoRegraResposta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_execucao_regra_resposta")
	private Integer idExecucaoRegraResposta;

	@ManyToOne
	@JoinColumn(name = "id_regra_item")
	@NotNull
	private RegraItem regraItem;

	@ManyToOne
	@JoinColumn(name = "id_execucao_regra")
	@NotNull
	@JsonIgnore
	private ExecucaoRegra execucaoRegra;

	@ManyToOne
	@JoinColumn(name = "id_variavel_valor")
	// @NotNull
	private VariavelValor resposta;

	private Boolean acertou;

	public Integer getIdExecucaoRegraResposta() {
		return idExecucaoRegraResposta;
	}

	public void setIdExecucaoRegraResposta(Integer idExecucaoRegraResposta) {
		this.idExecucaoRegraResposta = idExecucaoRegraResposta;
	}

	public RegraItem getRegraItem() {
		return regraItem;
	}

	public void setRegraItem(RegraItem regraItem) {
		this.regraItem = regraItem;
	}

	public ExecucaoRegra getExecucaoRegra() {
		return execucaoRegra;
	}

	public void setExecucaoRegra(ExecucaoRegra execucaoRegra) {
		this.execucaoRegra = execucaoRegra;
	}

	public VariavelValor getResposta() {
		return resposta;
	}

	public void setResposta(VariavelValor resposta) {
		this.resposta = resposta;
	}

	public Boolean getAcertou() {
		if (acertou != null) {
			return acertou;
		} else {
			return false;
		}
	}

	public void setAcertou(Boolean acertou) {
		this.acertou = acertou;
	}

}
