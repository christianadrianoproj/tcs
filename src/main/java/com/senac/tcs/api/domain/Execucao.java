package com.senac.tcs.api.domain;

import java.time.LocalDateTime;
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

/**
 *
 * @author Christian
 */

@Entity
@Table(name = "tb_execucao")
public class Execucao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_execucao")
	private Integer idExecucao;

	@NotNull
	private LocalDateTime datahora;

	@OneToMany(mappedBy = "execucao")
	private List<ExecucaoRegra> regras;

	private LocalDateTime concluido;

	@ManyToOne
	@JoinColumn(name = "id_image")
	//@JsonIgnore
	private Image image;

	public Integer getIdExecucao() {
		return idExecucao;
	}

	public void setIdExecucao(Integer idExecucao) {
		this.idExecucao = idExecucao;
	}

	public LocalDateTime getDatahora() {
		return datahora;
	}

	public void setDatahora(LocalDateTime datahora) {
		this.datahora = datahora;
	}

	public LocalDateTime getConcluido() {
		return concluido;
	}

	public void setConcluido(LocalDateTime concluido) {
		this.concluido = concluido;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<ExecucaoRegra> getRegras() {
		return regras;
	}

	public void setRegras(List<ExecucaoRegra> regras) {
		this.regras = regras;
	}

	public Double getPercentualAcerto() {
		Double perc = 0.0d;
		int quantAcertos = 0;
		if (regras.size() > 0) {
			for (ExecucaoRegra execRegra : regras) {
				if (execRegra.getValidou()) {
					quantAcertos++;
				}
			}
			perc = (double) ((quantAcertos * 100) / regras.size());
		}
		return perc;
	}
}
