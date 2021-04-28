package model;

import transforms.Col;
import transforms.Point3D;

public class Cuboid extends Solid {
    public Cuboid() {


        vertices.add(new Vertex(new Point3D(3, 2, 1), new Col(255, 0, 0)));
        vertices.add(new Vertex(new Point3D(2, 3, 1), new Col(0, 0, 255)));
        vertices.add(new Vertex(new Point3D(3, 3, 1), new Col(0, 255, 0)));
        vertices.add(new Vertex(new Point3D(2, 2, 1), new Col(0, 0, 0)));
        vertices.add(new Vertex(new Point3D(3, 2, 2), new Col(255, 255, 255)));
        vertices.add(new Vertex(new Point3D(3, 3, 2), new Col(255, 255, 0)));
        vertices.add(new Vertex(new Point3D(2, 3, 2), new Col(255, 0, 255)));
        vertices.add(new Vertex(new Point3D(2, 2, 2), new Col(0, 255, 255)));
        vertices.add(new Vertex(new Point3D(3, 2, 3), new Col(255, 255, 255)));
        vertices.add(new Vertex(new Point3D(2, 2, 3), new Col(0, 255, 255)));
        vertices.add(new Vertex(new Point3D(3, 3, 3), new Col(255, 255, 0)));
        vertices.add(new Vertex(new Point3D(2, 3, 3), new Col(255, 0, 255)));


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

        indices.add(4);
        indices.add(7);
        indices.add(8);

        indices.add(5);
        indices.add(8);
        indices.add(4);

        indices.add(9);
        indices.add(7);
        indices.add(8);

        indices.add(10);
        indices.add(8);
        indices.add(5);

        indices.add(10);
        indices.add(6);
        indices.add(5);

        indices.add(9);
        indices.add(7);
        indices.add(6);

        indices.add(9);
        indices.add(11);
        indices.add(6);

        indices.add(6);
        indices.add(11);
        indices.add(10);

        indices.add(9);
        indices.add(11);
        indices.add(10);

        indices.add(9);
        indices.add(10);
        indices.add(8);

        Parts.add(new Part(TopologyType.TRIANGLE, 0, 20));

    }
}
