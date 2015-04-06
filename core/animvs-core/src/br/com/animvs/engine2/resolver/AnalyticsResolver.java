package br.com.animvs.engine2.resolver;

public interface AnalyticsResolver {
	void dispatch();
	
	void analyticsTela(String tela);
	void analyticsEvento(String tela, String category, String action, String label, Long value);
	
	void analyticsTiming(String categoria, Long intervaloMS, String nome, String label);
	
	void analyticsException(Thread thread, Throwable e);
	
	void analyticsDimension(int index, String dimension);
}
