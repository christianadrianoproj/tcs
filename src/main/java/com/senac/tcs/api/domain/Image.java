package com.senac.tcs.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
*
* @author Christian
*/

@Entity
@Table(name = "image")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Integer idImage;
	
	@NotNull
    @Column(length = 100)
    private String nome;
	
	@NotNull
    @Column(length = 5)
    private String extensao;
	
	@NotNull
    private Integer contornos;
	
	@NotNull
	private Integer manchas;
	
	private byte[] image;
	
	@Column(name="image_proc")
	private byte[] imageProc;

	public Integer getIdImage() {
		return idImage;
	}

	public void setIdImage(Integer idImage) {
		this.idImage = idImage;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public Integer getContornos() {
		return contornos;
	}

	public void setContornos(Integer contornos) {
		this.contornos = contornos;
	}

	public Integer getManchas() {
		return manchas;
	}

	public void setManchas(Integer manchas) {
		this.manchas = manchas;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImageProc() {
		return imageProc;
	}

	public void setImageProc(byte[] imageProc) {
		this.imageProc = imageProc;
	}
		
}
