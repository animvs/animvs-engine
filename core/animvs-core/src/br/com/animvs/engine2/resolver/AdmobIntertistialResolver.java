package br.com.animvs.engine2.resolver;


public interface AdmobIntertistialResolver {
	boolean getLodaded();
	void showInterstitial();
	void loadInterestial();
	void update();
	
	int NONE = 10000011;
	int LOAD_INTERESTIAL = 10000001;
	int SHOW_INTERESTIAL = 10000002;
	int UPDATE = 10000003;
}
