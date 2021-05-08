package com.icaroerasmo.data;

public class TuplaBuilder {
	
	private int index = 0;
	private Tupla tupla;
	
	public TuplaBuilder(){
		tupla = new Tupla(index++);
	}
	
	public TuplaBuilder add(String chave, Double valor) {
		tupla.put(chave, valor);
		return this;
	}
	
	public TuplaBuilder add(String chave, String valor) {
		tupla.put(chave, valor);
		return this;
	}
	
	public Tupla build() {
		Tupla t = tupla;
		this.tupla = new Tupla(index++);
		return t;
	}
}
