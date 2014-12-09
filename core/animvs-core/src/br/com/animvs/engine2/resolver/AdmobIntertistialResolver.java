package br.com.animvs.engine2.resolver;


public abstract interface AdmobIntertistialResolver {
	public boolean getLodaded();
	public void showInterstitial();
	public void loadInterestial();
	public void update();
	
	public final int NONE = 10000011;
	public final int LOAD_INTERESTIAL = 10000001;
	public final int SHOW_INTERESTIAL = 10000002;
	public final int UPDATE = 10000003;
}
