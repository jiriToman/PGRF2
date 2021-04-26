package model;

import transforms.Point3D;

import java.awt.*;

public class Axis extends Solid {

    public Axis() {
        this(Color.BLUE);
    }

    public Axis(Color color) {
        this.color = color;
        vertices.add(new Point3D(0, 0, 0));
        vertices.add(new Point3D(0, 10, 0));
        vertices.add(new Point3D(10, 0, 0));
        vertices.add(new Point3D(0, 0, 10));



        addIndices(0, 1);
        addIndices(0, 2);
        addIndices(0, 3);

    }
}
