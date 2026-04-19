package net.lax1dude.eaglercraft.internal;

import net.lax1dude.eaglercraft.opengl.ImageData;

public class FontAtlasData {

	public ImageData image;
	public int lineHeight;
	public int baseline;
	public int[] glyphX;
	public int[] glyphY;
	public int[] glyphW;
	public int[] glyphH;
	public int[] advance;

	public FontAtlasData(ImageData image, int lineHeight, int baseline, int[] glyphX, int[] glyphY, int[] glyphW, int[] glyphH,
						 int[] advance) {
		this.image = image;
		this.lineHeight = lineHeight;
		this.baseline = baseline;
		this.glyphX = glyphX;
		this.glyphY = glyphY;
		this.glyphW = glyphW;
		this.glyphH = glyphH;
		this.advance = advance;
	}
}