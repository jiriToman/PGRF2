package model;

import transforms.Col;
import transforms.Point3D;

public class Cube extends Solid{

    public Cube() {
        vertices.add(new Vertex(new Point3D(2, 1, 0), new Col(255, 125, 0)));
        vertices.add(new Vertex(new Point3D(1, 2, 0), new Col(255, 125, 255)));
        vertices.add(new Vertex(new Point3D(2, 2, 0), new Col(50, 125, 255)));
        vertices.add(new Vertex(new Point3D(1, 1, 0), new Col(255, 125, 90)));
        vertices.add(new Vertex(new Point3D(2, 1, 1), new Col(198, 0, 125)));
        vertices.add(new Vertex(new Point3D(2, 2, 1), new Col(255, 0, 0)));
        vertices.add(new Vertex(new Point3D(1, 2, 1), new Col(255, 255, 0)));
        vertices.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 255)));

        indices.add(0);
        indices.add(1);
        indices.add(2);

        indices.add(0);
        indices.add(1);
        indices.add(3);

        indices.add(0);
        indices.add(2);
        indices.add(4);

        indices.add(2);
        indices.add(4);
        indices.add(5);

        indices.add(1);
        indices.add(2);
        indices.add(5);

        indices.add(1);
        indices.add(6);
        indices.add(5);

        indices.add(1);
        indices.add(6);
        indices.add(3);

        indices.add(7);
        indices.add(6);
        indices.add(3);

        indices.add(7);
        indices.add(0);
        indices.add(3);

        indices.add(7);
        indices.add(0);
        indices.add(4);

        indices.add(7);
        indices.add(6);
        indices.add(4);

        indices.add(5);
        indices.add(6);
        indices.add(4);

        Parts.add(new Part(TopologyType.TRIANGLE, 0, 12));
    }
}
