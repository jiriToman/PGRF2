package model;

import transforms.Col;
import transforms.Point3D;

public class Line extends Solid{

    public Line() {
        vertices.add(new Vertex(new Point3D(10, 10, 6), new Col(0, 125, 0)));
        vertices.add(new Vertex(new Point3D(5, 7, -2), new Col(0, 125, 200)));
        vertices.add(new Vertex(new Point3D(-5, 7, 2), new Col(0, 125, 200)));
        vertices.add(new Vertex(new Point3D(-7, 5, 3), new Col(0, 125, 200)));

        indices.add(2);
        indices.add(1);
        indices.add(0);
        indices.add(3);
        Parts.add(new Part(TopologyType.LINE, 0, 2));
    }
}
