package com.icaroerasmo.data;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Tupla {
	
	private int index;
	private Set<Par> pares;
	
	public Tupla(int index) {
		this.index = index;
		pares = new HashSet<Tupla.Par>();
	}
	
	public Double getAsDouble(String chave) {
		var valor = get(chave).orElseThrow();
		return (Double) valor;
	}
	
	public String getAsString(String chave) {
		var valor = get(chave).orElseThrow();
		if(valor instanceof Double) {
			return ((Double) valor).toString();
		}
		return (String) valor;
	}
	
	public Optional<Object> get(String chave) {
		return pares.stream().
				filter(p -> p.getChave().equals(chave)).
				map(p -> p.getValor()).findAny();
	}
	
	public List<String> getChaves() {
		return pares.stream().
				map(c -> c.getChave()).
				collect(Collectors.toList());
	}
	
	public List<Object> getValues() {
		return pares.stream().
				map(t -> t.getValor()).
				collect(Collectors.toList());
	}
	
	public void put(String chave, Double valor) {
		pares.add(new Par(chave, valor));
	}
	
	public void put(String chave, String valor) {
		pares.add(new Par(chave, valor.toLowerCase()));
	}
	
	public boolean isEmpty() {
		return pares.isEmpty();
	}
	
	public int getIndex() {
		return index;
	}
	
	private class Par {
		
		private String chave;
		private Object valor;
		
		private Par(String chave, Object valor) {
			this.chave = chave.toLowerCase();
			this.valor = valor;
		}
		
		String getChave() {
			return chave;
		}
		
		Object getValor() {
			return valor;
		}
		
		@Override
		public int hashCode() {
			return 13 + (this.chave != null ? this.chave.hashCode() : 0);
		}
		
		@Override
		public boolean equals(Object obj) {
			var _obj = (Par) obj;
			return this.chave.equals(_obj.getChave());
		}
	}
}
