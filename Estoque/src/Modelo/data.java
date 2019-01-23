package Modelo;

import java.util.Date;

public class data {
	String mes, dia, ano, dia_semana;
	
	public void le_data() {
		
		Date data = new Date();
		mes = ""+data.getMonth();
		dia = ""+data.getDate();
		ano = ""+(1900 + data.getYear());
		dia_semana = "" + data.getDay();
		
	}
	
}
