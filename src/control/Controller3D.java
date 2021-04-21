package control;

import model.Part;
import model.TopologyType;
import model.Vertex;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D  {

    private final GPURenderer renderer;
    private final Raster<Integer> imageBuffer;
    private final Panel panel;

    private final List<Part> partBuffer;
    private final List<Integer> indexBuffer;
    private final List<Vertex> vertexBuffer;

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        //
        this.imageBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        partBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        vertexBuffer = new ArrayList<>();


        initMatrices();
        initListeners(panel);
        //tady listenery
//        pohled transforms camera

        initBuffers();
        initAxis();
        display();
//        // test draw
//        imageBuffer.setElement(50, 50, Color.YELLOW.getRGB());
//        panel.repaint();
    }

    private void display() {
        panel.clear();
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

//        renderer.draw();
      renderer.draw(partBuffer, indexBuffer, vertexBuffer);

        // necessary to manually request update of the UI

        panel.repaint();
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(1, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-15));
        //
//      camera = camera.addAzimuth(Math.toRadians(180); to same pro zenith royum hodn pro mys
//       camera.forward(0.1)

        projection = new Mat4PerspRH(
                Math.PI / 3,
                imageBuffer.getHeight() / (float) imageBuffer.getWidth(),
                0.5,
                50
        );
    }

    private void initListeners(Panel panel) {
        // TODO
    }

    private void initBuffers() {
        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 255, 255)));
        vertexBuffer.add(new Vertex(new Point3D(10, 10, 6), new Col(0, 125, 0)));
        vertexBuffer.add(new Vertex(new Point3D(-2, 6, -4), new Col(255, 125, 200)));
        vertexBuffer.add(new Vertex(new Point3D(5, 7, -2), new Col(0, 125, 200)));
        vertexBuffer.add(new Vertex(new Point3D(-5, 7, 2), new Col(0, 125, 200)));

        // 1 trojúhelník
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);

        // 2 úsečky
        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(1);

        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 1));
        partBuffer.add(new Part(TopologyType.LINE, 3, 2));

    }
    private void initAxis() {
        vertexBuffer.add(new Vertex(new Point3D(10, 0, 0), new Col(255, 255, 255)));
        vertexBuffer.add(new Vertex(new Point3D(0, 10, 0), new Col(255, 255, 255)));
        vertexBuffer.add(new Vertex(new Point3D(-5, 7, 2), new Col(255, 255, 255)));
// bez model transformace

        // 3 úsečky
        indexBuffer.add(0);
        indexBuffer.add(5);
        indexBuffer.add(0);
        indexBuffer.add(6);
        indexBuffer.add(0);
        indexBuffer.add(7);

        partBuffer.add(new Part(TopologyType.LINE, 7, 3));

    }

}
