package model;

import transforms.Col;
import transforms.Point3D;

public class Axis extends Solid{

    public Axis() {
        vertices.add(new Vertex(new Point3D(), new Col(255, 255, 255)));
        vertices.add(new Vertex(new Point3D(10, 0, 0), new Col(255, 0, 0)));
        vertices.add(new Vertex(new Point3D(9.9, 0.1, 0), new Col(255, 0, 0)));
        vertices.add(new Vertex(new Point3D(9.9, 0, 0.1), new Col(255, 0, 0)));
        vertices.add(new Vertex(new Point3D(0, 10, 0), new Col(0, 255, 0)));
        vertices.add(new Vertex(new Point3D(0.1, 9.9, 0), new Col(0, 255, 0)));
        vertices.add(new Vertex(new Point3D(0, 9.9, 0.1), new Col(0, 255, 0)));
        vertices.add(new Vertex(new Point3D(0, 0, 10), new Col(0, 0, 255)));
        vertices.add(new Vertex(new Point3D(0.1, 0, 9.9), new Col(0, 0, 255)));
        vertices.add(new Vertex(new Point3D(0, 0.1, 9.9), new Col(0, 0, 255)));
// bez model transformace

        // 9 os
        indices.add(0);
        indices.add(1);

        indices.add(1);
        indices.add(2);

        indices.add(1);
        indices.add(3);

        indices.add(0);
        indices.add(4);

        indices.add(4);
        indices.add(5);

        indices.add(4);
        indices.add(6);

        indices.add(0);
        indices.add(7);

        indices.add(7);
        indices.add(8);

        indices.add(7);
        indices.add(9);
        Parts.add(new Part(TopologyType.AXIS, 0, 9));
    }
}
