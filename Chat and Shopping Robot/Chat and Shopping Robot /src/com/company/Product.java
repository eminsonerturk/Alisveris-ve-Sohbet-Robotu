package com.company;



// Çoğu ürünün ortak özelliğini bulunduran parent class'tır.
public abstract class Product {
	private int pId, _GarantiSuresi;
	private String pBrand;
	private String pModel;

	public Product() {

	}

	public Product(int pId, int GarantiSuresi, String pBrand, String pModel) {
		super();
		this.pId = pId;
		this.pBrand = pBrand;
		this.pModel = pModel;
		_GarantiSuresi = GarantiSuresi;
	}




	public int get_GarantiSuresi() {
		return _GarantiSuresi;
	}

	public void set_GarantiSuresi(int _GarantiSuresi) {
		this._GarantiSuresi = _GarantiSuresi;
	}


	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getpBrand() {
		return pBrand;
	}

	public void setpBrand(String pBrand) {
		this.pBrand = pBrand;
	}

	public String getpModel() {
		return pModel;
	}

	public void setpModel(String pModel) {
		this.pModel = pModel;
	}

	@Override
	public String toString() {
		return "Product [pId=" + pId + ", pBrand=" + pBrand + ", pModel=" + pModel + "]";
	}

	public String getIdentityDegeri(){
		return pBrand + " " +  pModel;
	}


}
