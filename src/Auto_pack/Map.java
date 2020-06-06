package Auto_pack;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/*
 * Game area
 */

public class Map {
	
	// Move some untouched variables to other class 
	private boolean[][] map;

	public Map(String path) {
		try {
			BufferedImage img_map = ImageIO.read(new File(path));
			this.map = render_map_from_image_to_boolean(img_map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean[][] get(){
		return this.map;
	}

	private boolean[][] render_map_from_image_to_boolean(BufferedImage map_img) {
		int w = map_img.getWidth();
		int h = map_img.getHeight();
		boolean[][] map = new boolean[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int clr = map_img.getRGB(x, y);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				if (red != 0 && green != 0 && blue != 0) { // think black
					map[x][y] = true;
				}
			}
		}
		return map;
	}

	boolean isCollide(int x, int y) {
		return !map[x][y];
	}

}