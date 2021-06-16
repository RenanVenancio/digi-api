package com.techzone.digi.entity;

import java.io.Serializable;

import com.techzone.digi.enums.UF;

public class Location implements Serializable {

	private static final long serialVersionUID = 1L;
	private String cityIBGECode;
	private String nome;
	private UF uf;

	public Location() {

	}

	public Location(String cityIBGECode, String nome, UF uf) {
		super();
		this.cityIBGECode = cityIBGECode;
		this.nome = nome;
		this.uf = uf;
	}

	public String getCityIBGECode() {
		return cityIBGECode;
	}

	public void setCityIBGECode(String cityIBGECode) {
		this.cityIBGECode = cityIBGECode;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

}
