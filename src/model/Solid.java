package model;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {


    final List<Part> Parts = new ArrayList<>();//casti
    final List<Vertex> vertices = new ArrayList<>();//vrcholy
    final List<Integer> indices = new ArrayList<>();//indexy


    Mat4 model = new Mat4Identity();

    Color color;

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }


    public List<Part> getParts() {
        return Parts;
    }
    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
