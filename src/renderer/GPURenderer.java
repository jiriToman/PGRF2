package renderer;

import model.Part;
import model.Vertex;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void draw(List<Part> parts, List<Integer> ib, List<Vertex> vb);
//    void draw(Solid... solids);

    void clear();

    void setModel(Mat4 model);

    void setView(Mat4 view);

    void setProjection(Mat4 projection);

    void setModelType(boolean filled);
}
