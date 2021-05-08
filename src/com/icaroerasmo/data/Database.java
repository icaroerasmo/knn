package com.icaroerasmo.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Database {
	
	private List<Tupla> tuplas;
	
	private String colunaRotulo;
	
	private Database() {}
	
	private Database(String colunaRotulo) {
		this.colunaRotulo = colunaRotulo;
		this.tuplas = new ArrayList<>();
	}

	public List<Tupla> getTuplas() {
		return tuplas;
	}

	private void setTuplas(List<Tupla> tuplas) {
		this.tuplas = tuplas;
	}
	
	public List<Tupla> knn(Tupla teste, Integer k) {
		
		Map<Tupla, Double> distances =  new HashMap<>();
		
		for(Tupla n : tuplas) {
			distances.put(n, calcDistanciaInstancias(n, teste));
		}
		
		distances = distances.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue()).
			       collect(Collectors.toMap(
			    	          Map.Entry::getKey,
			    	          Map.Entry::getValue,
			    	          (e1, e2) -> e1, LinkedHashMap::new));
		
		return distances.entrySet().stream().map(t -> t.getKey()).limit(k).collect(Collectors.toList());
	}
	
	private Double calcDistanciaInstancias(Tupla t1, Tupla t2) {
			
		var diff = 0D;
		
		for(String coluna : t1.getChaves()) {
			
			if(coluna.equalsIgnoreCase(colunaRotulo)) {
				continue;
			}
			
			diff += distanciaAttr(t1.getAsDouble(coluna), t2.getAsDouble(coluna));
		}
		
		return raiz(diff, 2);
	}
	
	private Double distanciaAttr(Double p1, Double p2) {
		return Math.pow(p1 - p2, 2);
	}
	
	public Double raiz(Double sum, Integer index) {
		return Math.pow(sum, ((double)1)/((double)index));
	}
	
	public String getColunaRotulo() {
		return colunaRotulo;
	}
	
	public Tupla getInstanciaTeste(Integer index) {
		var teste = tuplas.get(index);
		tuplas.remove(teste);
		return teste;
	}
	
	public static Database carregaDatabase(String arquivoCSV) {

		List<String> labels = new ArrayList<>();
		List<Tupla> tuplas = new ArrayList<>();
		TuplaBuilder tupla = new TuplaBuilder();

		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		int index = 0;
		try {

			br = new BufferedReader(new FileReader(arquivoCSV));
			while ((linha = br.readLine()) != null) {

				String[] values = linha.split(csvDivisor);

				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					if (index < 1) {
						labels.add(value);
					} else {
						try {
							Double numericValue = Double.parseDouble(value);
							tupla.add(labels.get(i), numericValue);
						} catch(NumberFormatException e) {
							tupla.add(labels.get(i), value);
						}
					}
				}
				
				if(index > 0) {
					tuplas.add(tupla.build());
				}

				index++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Database db = new Database(labels.get(labels.size()-1));
		db.setTuplas(tuplas);
		return db;
	}
}
