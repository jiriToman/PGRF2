package fill;

import model.Point;
import rasterize.Raster;

public class SeedFill implements Filler {

    private final Raster raster;

    private int backgroundColor;
    private int fillColor;
    private int seedX, seedY;

    public SeedFill(Raster raster) {
        this.raster = raster;
    }

    public void setSeed(Point seed) {
        seedX = seed.x;
        seedY = seed.y;
        backgroundColor = raster.getPixel(seedX, seedY);
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void fill() {
        seedFill(seedX, seedY);
    }

    private void seedFill(int x, int y) {
        // VM options -Xss40m
        if (backgroundColor == raster.getPixel(x, y)) {
            raster.setPixel(x, y, fillColor);
            seedFill(x + 1, y); // doprava
            seedFill(x - 1, y); // doleva
            seedFill(x, y + 1); // dolů
            seedFill(x, y - 1); // nahoru
        }
    }

}
