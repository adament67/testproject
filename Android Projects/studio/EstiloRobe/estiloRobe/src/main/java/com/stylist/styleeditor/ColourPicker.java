package com.stylist.styleeditor;

public class ColourPicker {

		private int red;
		private int green;
		private int blue;
		private int alpha;
		public ColourPicker(int red, int green, int blue, int alpha)
		{
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = 255;
		}
		public int getRed() {
		return red;
		}
		public void setRed(int red) {
		this.red = red;
		}
		public int getGreen() {
		return green;
		}
		public void setGreen(int green) {
		this.green = green;
		}
		public int getBlue() {
		return blue;
		}
		public void setBlue(int blue) {
		this.blue = blue;
		}
		public int getAlpha()
		{
		return alpha;
		}
		public void setAlpha(int alpha)
		{
		this.alpha = alpha;
		}
		@Override
		public String toString()
		{
		return "ColorPicker [red=" + red + ", green=" + green + ", blue=" + blue + ",alpha=" + alpha + "]";
		}
		}

