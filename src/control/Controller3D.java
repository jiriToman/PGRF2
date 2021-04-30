package control;

import model.*;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final GPURenderer renderer;
    private final Raster<Integer> imageBuffer;
    private final Panel panel;

    private final List<Solid> solidBuffer;
    private final List<Part> partBuffer;
    private final List<Integer> indexBuffer;
    private final List<Vertex> vertexBuffer;
    private final List<Solid> axisBuffer;
    private Triangle triangle;
    private Pyramid pyramid;
    private Cube cube;
    private Cuboid cuboid;
    private Line line;
    private Axis axis;
    private SolidBicubic bicubic;
    private int solidID =0;

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
        solidBuffer = new ArrayList<>();


        initMatrices();
        initListeners(panel);

        //tady listenery
//        pohled transforms camera

        initBuffers();

        display();
       }

    private void display() {
        panel.clear();
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        for (int i=0;i< solidBuffer.toArray().length; i++){
        renderer.setModel(solidBuffer.get(i).getModel());
        renderer.draw(solidBuffer.get(i).getParts(), solidBuffer.get(i).getIndices(),solidBuffer.get(i).getVertices());}

        renderer.setModel(axisBuffer.get(0).getModel());
        renderer.draw(axisBuffer.get(0).getParts(), axisBuffer.get(0).getIndices(),axisBuffer.get(0).getVertices());
        renderer.setModel(new Mat4Identity());


        // necessary to manually request update of the UI

        panel.repaint();
    }



    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(5, -10, 5);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(100))
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
                        double daz = (e.getX() - x) / 300.0;
                        double dze = (e.getY() - y) / 300.0;
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
                final double speed = 0.06;

                if (key == KeyEvent.VK_P) {
                    setProjekce();
                }
                if (key == KeyEvent.VK_X) {
                    setRotationModel(0.785398, 0, 0);
                }
                if (key == KeyEvent.VK_Y) {
                    setRotationModel(0, 0.785398, 0);
                }
                if (key == KeyEvent.VK_Z) {
                    setRotationModel(0, 0, 0.785398);
                }
                if (key == KeyEvent.VK_B) {
                    setTranslationModel(1, 0, 0);
                }
                if (key == KeyEvent.VK_N) {
                    setTranslationModel(0, 1, 0);
                }
                if (key == KeyEvent.VK_M) {
                    setTranslationModel(0, 0, 1);
                }
                if (key == KeyEvent.VK_J) {
                    setScaleModel(0.5, 0.5, 0.5);
                }
                if (key == KeyEvent.VK_K) {
                    setScaleModel(1.5, 1.5, 1.5);
                }
                if (key == KeyEvent.VK_L) {
                    setComposeModel();
                }
                if (key == KeyEvent.VK_F) {
                    if (filled) {
                        switchModelType(false);
                        filled = false;
                    } else {
                        switchModelType(true);
                        filled = true;
                    }
                }
                if (key == KeyEvent.VK_C) {
                    if (solidID == solidBuffer.toArray().length) {
                        solidID=0;

                    } else {
                       solidID++;
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

//        solidBuffer.add(new Triangle());

        solidBuffer.add(new Pyramid());
        solidBuffer.add(new Cube());
        solidBuffer.add(new Cuboid());
        solidBuffer.add(new SolidBicubic(Cubic.BEZIER));
//        solidBuffer.add(new Line());
        axisBuffer.add(new Axis());

    }


    private void setRotationModel(double alpha, double beta, double gamma) {
        Mat4RotXYZ rotation = new Mat4RotXYZ(alpha, beta, gamma);
        solidBuffer.get(solidID).setModel(solidBuffer.get(solidID).getModel().mul(rotation));//misto nuly var telesa

        display();
    }

    private void setScaleModel(double x, double y, double z) {

        Mat4Scale scale = new Mat4Scale(x, y, z);

        solidBuffer.get(solidID).setModel(solidBuffer.get(solidID).getModel().mul(scale));//misto nuly var telesa

        display();
    }

    private void setTranslationModel(double x, double y, double z) {

        Mat4Transl translation = new Mat4Transl(x, y, z);
        solidBuffer.get(solidID).setModel(solidBuffer.get(solidID).getModel().mul(translation));
        display();
    }

    private void setComposeModel() {
        Mat4Transl translation = new Mat4Transl(-2, -2, -2);
        Mat4Scale scale = new Mat4Scale(0.5, 0.5, 0.5);
        Mat4RotXYZ rotation = new Mat4RotXYZ(0.785398, 0.785398, 0.785398);
        solidBuffer.get(solidID).setModel(solidBuffer.get(solidID).getModel().mul(translation).mul(rotation).mul(scale));
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

    private void projectionSwitch() {
        if (projection instanceof Mat4PerspRH) {
            projection = new Mat4OrthoRH(5, 5, 0.5, 30);
        } else {
            projection = new Mat4PerspRH(Math.PI / 3, imageBuffer.getHeight() / (float) imageBuffer.getWidth(), 0.5, 30);
        }
    }

    private void switchModelType(boolean filled) {

        renderer.clear();
        renderer.setView(camera.getViewMatrix());
        renderer.setModelType(filled);
        renderer.setModel(model);

        display();
    }

}
