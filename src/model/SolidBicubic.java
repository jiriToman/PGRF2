package model;

import transforms.*;

import java.util.ArrayList;
import java.util.List;

public class SolidBicubic extends Solid{
    public List<Point3D> points;

    public SolidBicubic(final Mat4 baseMat) {
        points = new ArrayList<>();

        points.add(new Point3D(-1, 1, -3.5));
        points.add(new Point3D(2, 1.5, -6));
        points.add(new Point3D(7, 5, -6.5));
        points.add(new Point3D(1, 2, -5));

        points.add(new Point3D(3, 2, -3.5));
        points.add(new Point3D(4, 6, -4.5));
        points.add(new Point3D(8, 6, -4));
        points.add(new Point3D(4, 5, -4.4));

        points.add(new Point3D(-6, 4, -3));
        points.add(new Point3D(5, 7, -6.4));
        points.add(new Point3D(10, 7, -4));
        points.add(new Point3D(7, 8, -5));

        points.add(new Point3D(-7, 8, -5));
        points.add(new Point3D(6, 8, -6.5));
        points.add(new Point3D(12, 8, -7.5));
        points.add(new Point3D(16, 16, -4));




        Bicubic bicubic = new Bicubic(baseMat, points.toArray(new Point3D[0]), 0);

        for (double j = 0; j <= 1; j+=0.1) {


          for (double i = 0; i <= 1; i+=0.1) {
            vertices.add(new Vertex(bicubic.compute(i,j), new Col(60*(i+1), 0, 60*(j+1))));
        }}
        for (int i = 1; i < vertices.size(); i++) {
            indices.add(i-1);
            indices.add(i);
        }

        Parts.add(new Part(TopologyType.LINE, 0, indices.size()/2));
    }


}
