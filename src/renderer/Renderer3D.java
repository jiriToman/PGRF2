package renderer;

import model.Part;
import model.Solid;
import model.TopologyType;
import model.Vertex;
import rasterize.DepthBuffer;
import rasterize.Raster;
import transforms.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Renderer3D implements GPURenderer {

    private final Raster<Integer> raster;
    private final DepthBuffer depthBuffer;

    private Mat4 model, view, projection;
    public boolean filled = true;


    public Renderer3D(Raster<Integer> raster) {
        this.raster = raster;
        depthBuffer = new DepthBuffer(raster.getWidth(), raster.getHeight());

        model = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
    }

    @Override
    public void draw(List<Part> parts, List<Integer> ib, List<Vertex> vb) {
        for (Part part : parts) {
            final TopologyType topologyType = part.getTopologyType();
            final int index = part.getIndex(); // offset? start?
            final int count = part.getCount();

            if (topologyType == TopologyType.TRIANGLE) {
                for (int i = index; i < index + count * 3; i += 3) {
                    Integer i1 = ib.get(i);
                    Integer i2 = ib.get(i + 1);
                    Integer i3 = ib.get(i + 2);

                    Vertex v1 = vb.get(i1);
                    Vertex v2 = vb.get(i2);
                    Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3);
                }


            } else if (topologyType == TopologyType.LINE) {
                for (int i = index; i < index + count * 2; i += 2) {
                    Integer i1 = ib.get(i);
                    Integer i2 = ib.get(i + 1);


                    Vertex v1 = vb.get(i1);
                    Vertex v2 = vb.get(i2);
                    prepareline(v1, v2, null);
                }

                                }else if (topologyType == TopologyType.AXIS) {
                    for (int i = index; i < index + count * 2; i += 2) {
                        Integer i1 = ib.get(i);
                        Integer i2 = ib.get(i + 1);


                        Vertex v1 = vb.get(i1);
                        Vertex v2 = vb.get(i2);
                        Col axis = v2.getColor();
                        prepareline(v1, v2,axis);
                    }

                } // pokud je obj drat model line , plocha triangle
        }
    }


    private void prepareline(Vertex v1, Vertex v2,Col axis) {
        // 1. transformace vrcholů
        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());

        // 2. ořezání
        if (a.getX() > a.getW() && b.getX() > b.getW()) return; // line je moc vpravo
        if (a.getX() < -a.getW() && b.getX() < -b.getW()) return; // moc vlevo
        if (a.getY() > a.getW() && b.getY() > b.getY()) return; // je moc nahore
        if (a.getY() < -a.getW() && b.getY() < -b.getY()) return; // moc dole
        if (a.getZ() > a.getW() && b.getZ() > b.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0) return;

        // 3. seřazení podle Z
        // slide 97
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        // teď máme seřazeno - Z od největšího po nejmenší: A, B

        // 4. ořezání podle hrany Z
        // slide 97
        if (a.getZ() < 0) {
            // A.Z je menší než nula => všechny Z jsou menší než nula => není co zobrazit
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrchol B neni
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože ten nový vrchol (ab), který má vzniknout, bude mít souřadnici Z nula
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));


            drawLine(a, ab,axis);

        } else {

            drawLine(a, b,axis);
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3) {
        // 1. transformace vrcholů
        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());
        Vertex c = new Vertex(v3.getPoint().mul(model).mul(view).mul(projection), v3.getColor());

        // 2. ořezání
        // ořezat trojúhelníky, které jsou CELÉ mimo zobrazovací objem
        // slide 93
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return; // trojúhelník je moc vpravo
        if (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) return; // moc vlevo

        if (a.getY() > a.getW() && b.getY() > b.getY() && c.getY() > c.getY()) return; // trojúhelník je moc nahore
        if (a.getY() < -a.getW() && b.getY() < -b.getY() && c.getW() < -c.getY()) return; // moc dole

        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return;

        // 3. seřazení podle Z
        // slide 97
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
//            var temp = b;
            Vertex temp = b;
            b = c;
            c = temp;
        }
        // teď je v C vrchol, jehož Z je k nám nejblíže
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        // teď máme seřazeno - Z od největšího po nejmenší: A, B, C

        // 4. ořezání podle hrany Z
        // slide 97
        if (a.getZ() < 0) {
            // A.Z je menší než nula => všechny Z jsou menší než nula => není co zobrazit
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrcholy B,C nejsou
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože ten nový vrchol (ab), který má vzniknout, bude mít souřadnici Z nula
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));

            double t2 = -a.getZ() / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            drawTriangle(a, ab, ac);

        } else if (c.getZ() < 0) {
            double t1 = -a.getZ() / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t1).add(c.mul(t1));

            double t2 = -b.getZ() / (c.getZ() - b.getZ());
            Vertex bc = b.mul(1 - t2).add(c.mul(t2));

            drawTriangle(a, b, bc);
            drawTriangle(a, ac, bc);

        } else {
            // vidím celý trojúhelník
            drawTriangle(a, b, c);
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        // 1. dehomogenizace
        Optional<Vertex> dA = a.dehomog();
        Optional<Vertex> dB = b.dehomog();
        Optional<Vertex> dC = c.dehomog();

        // zahodit trojúhelník, pokud některý vrchol má w==0 (nelze provést dehomogenizaci)
        if (dA.isEmpty() || dB.isEmpty() || dC.isEmpty()) return;

        Vertex v1 = dA.get();
        Vertex v2 = dB.get();
        Vertex v3 = dC.get();

        // 2. transformace do okna
        Vec3D vec3D1 = transformToWindow(v1.getPoint());
        Vertex aa = new Vertex(new Point3D(vec3D1), v1.getColor());

        Vec3D vec3D2 = transformToWindow(v2.getPoint());
        Vertex bb = new Vertex(new Point3D(vec3D2), v2.getColor());

        Vec3D vec3D3 = transformToWindow(v3.getPoint());
        Vertex cc = new Vertex(new Point3D(vec3D3), v3.getColor());

        // podminka pro dratovy

        if (filled) {
            // 3. seřazení podle Y
            if (aa.getY() > bb.getY()) {
                Vertex temp = aa;
                aa = bb;
                bb = temp;
            }
            if (bb.getY() > cc.getY()) {
                Vertex temp = bb;
                bb = cc;
                cc = temp;
            }
            if (aa.getY() > bb.getY()) {
                Vertex temp = aa;
                aa = bb;
                bb = temp;
            }

            // 4. interpolace podle Y
            // slide 125
            // 1. for cyklus A->B
            int yStart = Math.max(0, (int) aa.getY() + 1);
            double yEnd = Math.min(raster.getHeight() - 1, bb.getY());
            for (int y = yStart; y <= yEnd; y++) {
                double t1 = (y - aa.getY()) / (bb.getY() - aa.getY());
                Vertex d = aa.mul(1 - t1).add(bb.mul(t1));

                double t2 = (y - aa.getY()) / (cc.getY() - aa.getY());
                Vertex e = aa.mul(1 - t2).add(cc.mul(t2));
                fillLine(d, e);
            }

            // 2. for cyklus B->C
//        spodni cast vykresleni plochy trojuhelniku

            int xStart = Math.max(0, (int) bb.getY() + 1);
            double xEnd = Math.min(raster.getWidth() - 1, cc.getY());
            for (int y = xStart; y <= xEnd; y++) {
                double t3 = (y - bb.getY()) / (cc.getY() - bb.getY());
                Vertex f = bb.mul(1 - t3).add(cc.mul(t3));

                double t4 = (y - aa.getY()) / (cc.getY() - aa.getY());
                Vertex g = aa.mul(1 - t4).add(cc.mul(t4));
                fillLine(f, g);
            }

        } else {
            drawLine(a, b, null);
            drawLine(a, c,null);
            drawLine(b, c,null);

        }

    }

    private void drawLine(Vertex a, Vertex b,Col axis) {
        // 1. dehomogenizace
        Optional<Vertex> dA = a.dehomog();
        Optional<Vertex> dB = b.dehomog();

        // zahodit line, pokud některý vrchol má w==0 (nelze provést dehomogenizaci)
        if (dA.isEmpty() || dB.isEmpty()) return;

        Vertex v1 = dA.get();
        Vertex v2 = dB.get();


        // 2. transformace do okna
        Vec3D vec3D1 = transformToWindow(v1.getPoint());
        Vertex aa = new Vertex(new Point3D(vec3D1), v1.getColor());

        Vec3D vec3D2 = transformToWindow(v2.getPoint());
        Vertex bb = new Vertex(new Point3D(vec3D2), v2.getColor());
// zjistit podle x nebo y dx dy abs x2-x1 abs y2-y1 vyresi cerchovani

        var x1 = aa.getX();
        var x2 = bb.getX();
        var y1 = aa.getY();
        var y2 = bb.getY();
        var dy = y2 - y1;
        var dx = x2 - x1;


        if (Math.abs(dy) < Math.abs(dx)) {
            if (x2 < x1) {
                Vertex temp = aa;
                aa = bb;
                bb = temp;
            }
//            switchPoints(aa,bb);
            // 4. interpolace podle X
            int xStart = Math.max(0, (int) aa.getX() + 1);
            double xEnd = Math.min(raster.getWidth() - 1, bb.getX());
            for (int x = xStart; x <= xEnd; x++) {
                double t1 = (x - aa.getX()) / (bb.getX() - aa.getX());
                Vertex d = aa.mul(1 - t1).add(bb.mul(t1));

                if(axis== null){
                    drawPixel((int) Math.round(d.getX()), (int) Math.round(d.getY()), d.getZ(), d.getColor());

                }
                else{
                    drawPixel((int) Math.round(d.getX()), (int) Math.round(d.getY()), d.getZ(), axis);
                }
            }
        } else {
            if (y2 < y1) {
                Vertex temp = aa;
                aa = bb;
                bb = temp;
            }
//                4. interpolace podle Y
            int yStart = Math.max(0, (int) aa.getY() + 1);
            double yEnd = Math.min(raster.getHeight() - 1, bb.getY());
            for (int y = yStart; y <= yEnd; y++) {
                double t1 = (y - aa.getY()) / (bb.getY() - aa.getY());
                Vertex d = aa.mul(1 - t1).add(bb.mul(t1));
                if(axis== null){
                    drawPixel((int) Math.round(d.getX()), (int) Math.round(d.getY()), d.getZ(), d.getColor());

                }
                else{
                    drawPixel((int) Math.round(d.getX()), (int) Math.round(d.getY()), d.getZ(), axis);
                }
                }
            }


        }

//       lina se krespi pixel po pixelu



    private void fillLine(Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        int xStart = Math.max(0, (int) a.getX() + 1);
        double xEnd = Math.min(raster.getWidth() - 1, b.getX());

        for (int x = xStart; x <= xEnd; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vertex ab = a.mul(1 - t).add(b.mul(t));

            drawPixel(x, (int) Math.round(ab.getY()), ab.getZ(), ab.getColor());
        }
    }

    private void drawPixel(int x, int y, double z, Col color) {
        Optional<Double> zOptional = depthBuffer.getElement(x, y);
        if (zOptional.isPresent() && zOptional.get() > z) {
            depthBuffer.setElement(x, y, z);
            raster.setElement(x, y, color.getRGB());
        }
    }

    private Vec3D transformToWindow(Point3D vec) {
        return new Vec3D(vec)
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed a my chceme, aby bylo vlevo nahoře
                .mul(new Vec3D(raster.getWidth() / 2f, raster.getHeight() / 2f, 1));
        // máme <0;2> -> vynásobíme polovinou velikosti plátna
    }

    @Override
    public void clear() {
        depthBuffer.clear();
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    public void setModelType(boolean filled) {
        this.filled = filled;
    }

    public void linkPoints(Vertex prvni, Vertex druhy) {
        Vertex temp = prvni;
        prvni = druhy;
        druhy = temp;
    }

}
