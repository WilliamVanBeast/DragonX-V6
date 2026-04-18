package net.lax1dude.eaglercraft.internal;

import net.lax1dude.eaglercraft.opengl.ImageData;

public class FontAtlasData {

	public final ImageData image;
	public final int lineHeight;
	public final int baseline;
	public final int[] glyphX;
	public final int[] glyphY;
	public final int[] glyphW;
	public final int[] glyphH;
	public final int[] advance;

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