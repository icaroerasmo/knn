package com.icaroerasmo;

import java.util.List;

import com.icaroerasmo.data.Database;
import com.icaroerasmo.data.Tupla;

public class Main {

	public static void main(String[] args) {
	
		Database db = Database.carregaDatabase("./iris.csv");
		
		Tupla teste =  db.getInstanciaTeste(110);
	
		List<Tupla> tuplas = db.knn(teste, 3);
		
		for(Tupla t : tuplas) {
			System.out.println(t.getIndex()+": "+t.getAsString(db.getColunaRotulo().toLowerCase()));
		}
	}
}
