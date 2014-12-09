package br.com.animvs.engine2.graphics.font;

public final class AnimvsFontInfo {
	private float fontSize;
	// private BitmapFont bitmapFont;
	private String fontName;
	private int pageSize;
	
	public String getFontName() {
		return fontName;
	}
	
	public float getFontSize() {
		return fontSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public AnimvsFontInfo(String fontName, float fontSize, int pageSize) {
		this.fontSize = fontSize;
		this.fontName = fontName;
		this.pageSize = pageSize;
	}
}