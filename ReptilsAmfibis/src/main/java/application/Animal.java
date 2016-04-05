package application;

public class Animal {
	
	private int codi;
	private int ordre;
	private String nom;
	private String especie;
	private String estat;
	private String descripcio;
	private String imatge;
	
	public Animal(int codi, int ordre, String nom, String especie, String estat, String descripcio, String imatge){
		this.codi = codi;
		this.ordre = ordre;
		this.nom = nom;
		this.especie = especie;
		this.estat = estat;
		this.descripcio = descripcio;
		this.imatge = imatge;
	}
	
	public Animal(){
		
	}

	public int getCodi() {
		return codi;
	}

	public int getOrdre() {
		return ordre;
	}

	public String getNom() {
		return nom;
	}

	public String getEspecie() {
		return especie;
	}

	public String getEstat() {
		return estat;
	}

	public String getDescripcio() {
		return descripcio;
	}

	public String getImatge() {
		return imatge;
	}

	
}
