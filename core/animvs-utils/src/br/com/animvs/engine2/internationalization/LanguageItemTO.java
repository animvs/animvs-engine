package br.com.animvs.engine2.internationalization;

import com.badlogic.gdx.utils.Array;

public final class LanguageItemTO {
	private String name;
	private String value;

	private Array<LanguageItemTO> items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Array<LanguageItemTO> getItems() {
		return items;
	}

	public void setItems(Array<LanguageItemTO> items) {
		this.items = items;
	}
	
	public LanguageItemTO(){
	}
	
	public LanguageItemTO(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public LanguageItemTO(String name, Array<LanguageItemTO> items){
		this.name = name;
		this.items = items;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
