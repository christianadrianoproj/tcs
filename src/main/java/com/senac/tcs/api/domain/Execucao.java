package com.senac.tcs.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(name = "tb_execucao")
public class Execucao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_execucao")
	private Integer idExecucao;

	@NotNull
	private LocalDateTime datahora;

	@OneToMany(mappedBy = "execucao")
	private List<ExecucaoResposta> respostas;

	private LocalDate concluido;

	@Column(name = "id_image")
	private Integer image;

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

	public List<ExecucaoResposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<ExecucaoResposta> respostas) {
		this.respostas = respostas;
	}

	public LocalDate getConcluido() {
		return concluido;
	}

	public void setConcluido(LocalDate concluido) {
		this.concluido = concluido;
	}

	public Integer getImage() {
		return image;
	}

	public void setImage(Integer image) {
		this.image = image;
	}

}
