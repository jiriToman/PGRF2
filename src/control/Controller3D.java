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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D  {

    private final GPURenderer renderer;
    private final Raster<Integer> imageBuffer;
    private final Panel panel;

    private final List<Part> partBuffer;
    private final List<Integer> indexBuffer;
    private final List<Vertex> vertexBuffer;
    private final List<Part> axisBuffer;

    private Mat4 model, projection;
    private Camera camera;
    private boolean filled = true;

    public Controller3D(Panel panel) {
        this.panel = panel;
        //
        this.imageBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        partBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        vertexBuffer = new ArrayList<>();
        axisBuffer = new ArrayList<>();




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
      renderer.setModel(new Mat4Identity());
    renderer.draw(axisBuffer, indexBuffer, vertexBuffer);

        // necessary to manually request update of the UI

        panel.repaint();
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(5, -10, 1);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-15));
        //
//      camera = camera.addAzimuth(Math.toRadians(180); to same pro zenith rozum hodn pro mys
//       camera.forward(0.1)

        projection = new Mat4PerspRH(
                Math.PI / 3,
                imageBuffer.getHeight() / (float) imageBuffer.getWidth(),
                0.5,
                50
        );
    }

    private void initListeners(Panel panel) {
        MouseAdapter cameraListener = new MouseAdapter() {
            int x = -1, y = -1;
            boolean move = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    move = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                x = y = -1;
                move = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (move) {
                    if (x != -1 && y != -1) {
                        double daz = (e.getX()-x)/300.0;
                        double dze = (e.getY()-y)/300.0;
                        camera = camera.addAzimuth(daz);
                        camera = camera.addZenith(dze);
                        display();
                    }
                    x = e.getX();
                    y = e.getY();
                }
            }
        };
        panel.addMouseListener(cameraListener);
        panel.addMouseMotionListener(cameraListener);

        panel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                final double speed = 0.03;

                if (key == KeyEvent.VK_P) {
                    setProjekce();
                }
                if (key == KeyEvent.VK_X) {
                    setRotationModel(0.785398,0,0);
                }
                if (key == KeyEvent.VK_Y) {
                    setRotationModel(0,0.785398,0);
                }
                if (key == KeyEvent.VK_Z) {
                    setRotationModel(0,0,0.785398);
                }
                if (key == KeyEvent.VK_B) {
                    setTranslationModel(2,0,0);
                }
                if (key == KeyEvent.VK_N) {
                    setTranslationModel(0,2,0);
                }
                if (key == KeyEvent.VK_M) {
                    setTranslationModel(0,0,2);
                }
                if (key == KeyEvent.VK_J) {
                    setScaleModel(0.5,0.5,0.5);
                }
                if (key == KeyEvent.VK_K) {
                    setScaleModel(1.5,1.5,1.5);
                }
                if (key == KeyEvent.VK_L) {
                    setComposeModel();
                }
                if (key == KeyEvent.VK_F) {
                    if (filled) {
                        switchModelType(false);
                        filled=false;}
                    else {
                        switchModelType(true);
                        filled=true;
                    }
                }

                if (key == KeyEvent.VK_UP) {
                    camera = camera.forward(speed);
                }
                if (key == KeyEvent.VK_DOWN) {
                    camera = camera.backward(speed);
                }
                if (key == KeyEvent.VK_LEFT) {
                    camera = camera.left(speed);
                }
                if (key == KeyEvent.VK_RIGHT) {
                    camera = camera.right(speed);
                }

                display();
            }
        });
//
    }

    private void initBuffers() {
        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 255, 255)));
        vertexBuffer.add(new Vertex(new Point3D(10, 10, 6), new Col(0, 125, 0)));
        vertexBuffer.add(new Vertex(new Point3D(-2, 6, -4), new Col(255, 125, 200)));
        vertexBuffer.add(new Vertex(new Point3D(5, 7, -2), new Col(0, 125, 200)));
        vertexBuffer.add(new Vertex(new Point3D(-5, 7, 2), new Col(0, 125, 200)));
        vertexBuffer.add(new Vertex(new Point3D(-7, 5, 3), new Col(0, 125, 200)));

        // 1 trojúhelník
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);

        // 2 úsečky
        indexBuffer.add(4);
        indexBuffer.add(3);
        indexBuffer.add(1);
        indexBuffer.add(5);

        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 1));
        partBuffer.add(new Part(TopologyType.LINE, 3, 2));

    }
    private void initAxis() {
        vertexBuffer.add(new Vertex(new Point3D(10, 0, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 10, 0), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 10), new Col(0, 0, 255)));
// bez model transformace

        // 3 úsečky
        indexBuffer.add(0);
        indexBuffer.add(6);
        indexBuffer.add(0);
        indexBuffer.add(7);
        indexBuffer.add(0);
        indexBuffer.add(8);

        axisBuffer.add(new Part(TopologyType.LINE, 7, 3));

    }
    private void setRotationModel(double alpha, double beta, double gamma) {
        renderer.clear();
        Mat4RotXYZ rotation = new Mat4RotXYZ(alpha, beta, gamma);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        model = model.mul(rotation);
        renderer.setModel(model);
        display();
    }
    private void setScaleModel(double x, double y, double z) {
        renderer.clear();
        Mat4Scale scale = new Mat4Scale(x,y,z);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        model = model.mul(scale);
        renderer.setModel(model);
        display();
    }
    private void setTranslationModel(double x, double y, double z) {
        renderer.clear();
        Mat4Transl translation = new Mat4Transl(x, y, z);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        model = model.mul(translation);
        renderer.setModel(model);
        display();
    }
private void setComposeModel() {
    renderer.clear();
    Mat4Transl translation = new Mat4Transl(-2, -2,-2);
    Mat4Scale scale = new Mat4Scale(0.5,0.5,0.5);
    Mat4RotXYZ rotation = new Mat4RotXYZ(0.785398, 0.785398,0.785398);
    renderer.setView(camera.getViewMatrix());
    renderer.setProjection(projection);
    model = model.mul(scale).mul(rotation).mul(scale);
    renderer.setModel(model);
    display();
}
    private void setProjekce() {

        renderer.clear();
        renderer.setView(camera.getViewMatrix());

        projectionSwitch();

        renderer.setProjection(projection);
        renderer.setModel(model);
        display();
    }
    private void projectionSwitch (){
        if (projection instanceof Mat4PerspRH) {
            projection = new Mat4OrthoRH(5, 5, 0.5, 30);
        } else{ projection = new Mat4PerspRH(Math.PI / 3, imageBuffer.getHeight() / (float) imageBuffer.getWidth(), 0.5, 30);}
    }
    private void switchModelType(boolean filled) {

        renderer.clear();
        renderer.setView(camera.getViewMatrix());
        renderer.setModelType(filled);
        renderer.setModel(model);

        display();
    }

}
