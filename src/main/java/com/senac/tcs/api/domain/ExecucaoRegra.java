package com.senac.tcs.api.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Christian
 */

@Entity
@Table(name = "tb_execucao_regra")
public class ExecucaoRegra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_execucao_regra")
	private Integer idExecucaoRegra;

	@ManyToOne
	@JoinColumn(name = "id_execucao")
	@NotNull
	@JsonIgnore
	private Execucao execucao;

	@ManyToOne
	@JoinColumn(name = "id_regra")
	@NotNull
	@JsonIgnore
	private Regra regra;

	@OneToMany(mappedBy = "execucaoRegra")
	private List<ExecucaoRegraResposta> respostas;

	private Boolean validou;

	public Integer getIdExecucaoRegra() {
		return idExecucaoRegra;
	}

	public void setIdExecucaoRegra(Integer idExecucaoRegra) {
		this.idExecucaoRegra = idExecucaoRegra;
	}

	public Execucao getExecucao() {
		return execucao;
	}

	public void setExecucao(Execucao execucao) {
		this.execucao = execucao;
	}

	public Regra getRegra() {
		return regra;
	}

	public void setRegra(Regra regra) {
		this.regra = regra;
	}

	public List<ExecucaoRegraResposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<ExecucaoRegraResposta> respostas) {
		this.respostas = respostas;
	}

	public Boolean getValidou() {
		if (validou != null) {
			return validou;
		} else {
			return false;
		}
	}

	public void setValidou(Boolean validou) {
		this.validou = validou;
	}

}
