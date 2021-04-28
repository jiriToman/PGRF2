package model;

import transforms.Col;
import transforms.Point3D;

public class Pyramid extends Solid{
    public Pyramid() {
        vertices.add(new Vertex(new Point3D(5, 5, 5), new Col(0, 125, 0)));
        vertices.add(new Vertex(new Point3D(0, 5, 0), new Col(255, 125, 200)));
        vertices.add(new Vertex(new Point3D(5, -5, 0), new Col(0, 125, 200)));
        vertices.add(new Vertex(new Point3D(0, 5, -5), new Col(0, 125, 0)));

        indices.add(0);
        indices.add(1);
        indices.add(2);

        indices.add(3);
        indices.add(1);
        indices.add(2);

        indices.add(0);
        indices.add(1);
        indices.add(3);

        indices.add(0);
        indices.add(3);
        indices.add(2);
        Parts.add(new Part(TopologyType.TRIANGLE, 0, 4));
    }
}

