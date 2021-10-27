package ast;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;


public class PhotoeditorEvaluator implements PhotoeditorVisitor {
    private Integer posx, posy, iwidth, iheight, rwidth, rheight;
    private int[][] pixels; // store the pixels in the image
    private String fileName;
    private BufferedImage image;      //prior processed
    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private BufferedImage imageRotate;
    private BufferedImage imageMosiac;
    private Map<String, List<Function>> funcList = new HashMap<>();
    private Range range; // store the current range


    public PhotoeditorEvaluator() {
//        try {
//            BufferedImage bi = getMyImage();
//            File outputfile = new File("saved.png");
//            ImageIO.write(bi, "png", outputfile);
//        } catch (IOException e) {
//            // throw new IOException();
//        }

    }


    private int standrad(int i) {
        if (i > 255) {
            return 255;
        } else if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }

    private int setBrightness(int rgb, int i) {
        int R, G, B, r1, g1, b1;
        R = ((rgb >> 16) & 0xff) + i;
        G = ((rgb >> 8) & 0xff) + i;
        B = (rgb & 0xff) + i;      // reference from google
        r1 = (standrad(R) & 0xff) << 16;
        g1 = (standrad(G) & 0xff) << 8;
        b1 = standrad(B) & 0xff;
        return ((0xff) << 24 | r1 | g1 | b1);

    }

    private int setSaturation(int rgb, double i) {
        int R, G, B, r1, g1, b1;
        int R1, G1, B1;
        R = ((rgb >> 16) & 0xff);
        G = ((rgb >> 8) & 0xff);
        B = (rgb & 0xff);

        R1 = (int) (R + (R - (B + G) / 2) * i);
        G1 = (int) (G + (G - (R + B) / 2) * i);
        B1 = (int) (B + (B - (R + G) / 2) * i); // reference from google

        r1 = (standrad(R1) & 0xff) << 16;
        g1 = (standrad(G1) & 0xff) << 8;
        b1 = standrad(B1) & 0xff;
        return ((0xff) << 24 | r1 | g1 | b1);
    }

    @Override
    public Object visit(Program p) throws IOException {
        Create c = p.getCreate();
        Run r = p.getRun();
        c.accept(this);
        r.accept(this);
        return null;
    }

    @Override
    public Object visit(Create c) throws IOException {
//        private String name;
//        private Path path;
//        private List<Func> funcs;
//        private List<Var> vars;
        fileName = c.getName();
        c.getPath().accept(this);
        List<Func> funcs = c.getFuncs();
        List<Var> vars = c.getVars();

        for (Func f : funcs) {
            f.accept(this);
        }

        for (Var v : vars) {
            v.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(Run r) throws IOException {
        //refreshImage();
        File output = new File(fileName + "_output.jpg");
        ImageIO.write(image, "jpg", output);

        // add GUI
        addGUI();
        return null;
    }

    private void addGUI() {
        JFrame jf = new JFrame("Output the new image!");
        jf.setBounds(0, 0, 3 * image.getWidth(), (int) (1.5 * image.getHeight()));
        JPanel jp = new JPanel();
        jp.setBackground(Color.white);
        JLabel oldI;
        JLabel newI;
        // resize the height and width if image is too large
        if (image.getHeight() > 800 || image.getWidth() > 800) {
            ImageIcon oldImage = new ImageIcon(originalImage.getScaledInstance((int) (image.getWidth() / 2), (int) (image.getHeight() / 2), java.awt.Image.SCALE_SMOOTH));
            oldI = new JLabel(oldImage);
            ImageIcon newImage = new ImageIcon(image.getScaledInstance((int) (image.getWidth() / 2), (int) (image.getHeight() / 2), java.awt.Image.SCALE_SMOOTH));
            newI = new JLabel(newImage);
        } else {
            ImageIcon oldImage = new ImageIcon(originalImage);
            oldI = new JLabel(oldImage);
            ImageIcon newImage = new ImageIcon(image);
            newI = new JLabel(newImage);
        }
        jp.add(oldI);
        jp.add(newI);
        jf.add(jp);
        jf.setVisible(true);
    }

    @Override
    public Object visit(Effect e) throws IOException {
        List<String> funcNameList = e.getFuncNameList();
        List<Function> functions;
        for (String funcName : funcNameList) {
            if (funcList.containsKey(funcName)) {
                functions = funcList.get(funcName);
                for (Function f : functions) {
                    f.accept(this);
                }
            } else {
                throw new IllegalArgumentException("ERROR: " + "Didn't find func " + funcName);
            }
        }

        return null;
    }

    @Override
    public Object visit(Flip f) {
        int lb, rb, ub, db;
        BufferedImage bufferedImage = null;
        if (range.getName().equals("full")) {
            int Iwidth = image.getWidth();
            int Iheight = image.getHeight();
            bufferedImage = new BufferedImage(Iwidth, Iheight, image.getType());
            //  cite from google website
            for (int i = 0; i < Iwidth; i++) {
                for (int j = 0; j < Iheight; j++) {
                    bufferedImage.setRGB(i, j, image.getRGB(i, Iheight - 1 - j));

                }
            }
            image = bufferedImage;
        } else {
            lb = posx - rwidth / 2;
            rb = posx + rwidth / 2;
            db = posy - rheight / 2;
            ub = posy + rheight / 2;
            int widthX = rb - lb;
            int widthY = ub - db;
            bufferedImage = image;
            for (int i = 0; i < widthX; i++) {
                for (int j = 0; j < widthY; j++) {
                    bufferedImage.setRGB(lb + i, ub + j, image.getRGB(lb + i, ub + (widthY - 1 - j)));

                }
            }

        }
        return null;
    }


    @Override
    public Object visit(Func f) {
        String funcName = f.getName();
        List<Function> functions = f.getFunctions();
        funcList.put(funcName, functions); // maps funcName to a list of functions

        return null;
    }

    @Override
    public Object visit(Mosaic f) {
        int rgb, lb, rb, db, ub; //left/right bound,down/up bound...
        BufferedImage bufferedImage = null;

        if (range.getName().equals("full")) {
            lb = 0;
            rb = image.getWidth();
            db = 0;
            ub = image.getHeight();
            image = setMosaic(lb, rb, db, ub);
        } else {
            lb = posx - rwidth / 2;
            rb = posx + rwidth / 2;
            db = posy - rheight / 2;
            ub = posy + rheight / 2;
            image = setMosaic(lb, rb, db, ub);
        }

        return null;
    }

    private BufferedImage setMosaic(int lb, int rb, int db, int ub) {
        int size = 10;
        int widthMosaic = rb - lb;
        int heightMosaic = ub - db;
        BufferedImage mosiac = null;
        // mosiac = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        mosiac = image;
        //check size
//        if(widthMosaic < size || heightMosaic < size){
//            throw
//        }
        //reference from google
        int MosiacXNum = 0;
        int MosiacYNum = 0;
        if (widthMosaic % size == 0) {
            MosiacXNum = widthMosaic / size;
        } else {
            MosiacXNum = widthMosaic / size + 1;
        }
        if (heightMosaic % size == 0) {
            MosiacYNum = heightMosaic / size;

        } else {
            MosiacYNum = heightMosaic / size + 1;
        }
        int x = 0;
        int y = 0;
        Graphics2D g = mosiac.createGraphics();
        for (int i = 0; i < MosiacXNum; i++) {
            for (int j = 0; j < MosiacYNum; j++) {
                int mosiacWidthLength = size;
                int mosiacHeightLength = size;
                if (i == lb + MosiacXNum - 1) {
                    mosiacWidthLength = widthMosaic - x;
                }
                if (j == db + MosiacYNum - 1) {
                    mosiacHeightLength = heightMosaic - y;
                }
                int centerX = x;
                int centerY = y;
                if (mosiacWidthLength % 2 == 0) {
                    centerX += mosiacWidthLength / 2;
                } else {
                    centerX += (mosiacWidthLength - 1) / 2;
                }
                if (mosiacHeightLength % 2 == 0) {
                    centerY += mosiacHeightLength / 2;
                } else {
                    centerY += (mosiacHeightLength - 1) / 2;
                }

                Color color = new Color(image.getRGB(lb + centerX, db + centerY));
                g.setColor(color);
                g.fillRect(lb + x, db + y, mosiacWidthLength, mosiacHeightLength);
                y = y + size;
            }
            y = 0;
            x = x + size;
        }
        System.out.println("mosiac successfully");
        g.dispose();
        return mosiac;

    }


    @Override
    public Object visit(Path p) throws IOException {
        String path = p.getPath();
        File file = new File(path);
        // BufferedImage tempImage = (BufferedImage) ImageIO.read(file);
        image = (BufferedImage) ImageIO.read(file);
        originalImage = (BufferedImage) ImageIO.read(file); // store the original image and won't be changed
        iwidth = image.getWidth();
        iheight = image.getHeight();

        pixels = new int[iwidth][iheight];

        // store RGB value of each pixel into pixels
        for (int i = 0; i < iwidth; i++)
            for (int j = 0; j < iheight; j++)
                pixels[i][j] = image.getRGB(i, j);
        return null;
    }


    @Override
    public Object visit(Rotate r) throws IOException, IllegalArgumentException {

        BufferedImage bufferedImage = null;

        String str = r.getDirection().toString();
        if (!range.getName().equals("full")) {
            System.out.println("ERROR" + "INVALID RANGE INPUT");
            throw new IllegalArgumentException("ERROR:" + "INVLAID RANGE INPUT FOR FUNCTION ROTATE");
        }
        Integer numberOfRotate = r.getNum();

        while (numberOfRotate != 0) {
            iwidth = image.getWidth();
            System.out.println("WIDTH" + iwidth);
            iheight = image.getHeight();
            System.out.println("HEIGHT" + iheight);
            bufferedImage = new BufferedImage(iheight, iwidth, image.getType());
            if (str.equals("counterclockwise")) {
                for (int i = 0; i < iheight; i++) {
                    for (int j = 0; j < iwidth; j++) {
                        //cite from google
                        bufferedImage.setRGB(i, (iwidth - 1) - j, image.getRGB(j, i));

                    }
                }

            }

            if (str.equals("clockwise")) {
                for (int i = 0; i < iheight; i++) {
                    for (int j = 0; j < iwidth; j++) {
                        // cite from google
                        bufferedImage.setRGB((iheight - 1) - i, j, image.getRGB(j, i));

                    }
                }
            }
            numberOfRotate--;
            image = bufferedImage;

        }

        return null;
    }

    @Override
    public Object visit(Brightness b) {
        int a, rgb, lb, rb, db, ub; //left/right bound,down/up bound...
        if (b.getSign().equals("+")) {
            a = b.getNum();
        } else {
            a = -b.getNum();
        }
        if (range.getName().equals("full")) {
            lb = 0;
            rb = image.getWidth();
            db = 0;
            ub = image.getHeight();
        } else {
            lb = posx - rwidth / 2;
            rb = posx + rwidth / 2;
            db = posy - rheight / 2;
            ub = posy + rheight / 2;
        }
        for (int i = lb; i < rb; i++) {
            for (int j = db; j < ub; j++) {
//                rgb = pixels[i][j];
//                pixels[i][j] = setBrightness(rgb, a);
                rgb = image.getRGB(i, j);
                image.setRGB(i, j, setBrightness(rgb, a));
            }
        }
        return null;
    }

    @Override
    public Object visit(Saturation s) {
        double a;
        int rgb, lb, rb, db, ub; //left/right bound,down/up bound...
        if (s.getSign().equals("+")) {
            a = 1 + 0.01 * s.getNum();
        } else {
            a = 1 - 0.01 * s.getNum();
        }
        if (range.getName().equals("full")) {
            lb = 0;
            rb = image.getWidth();
            db = 0;
            ub = image.getHeight();
        } else {
            lb = posx - rwidth / 2;
            rb = posx + rwidth / 2;
            db = posy - rheight / 2;
            ub = posy + rheight / 2;
        }
        for (int i = lb; i < rb; i++) {
            for (int j = db; j < ub; j++) {
//                rgb = pixels[i][j];
//                pixels[i][j] = setSaturation(rgb, a);
                rgb = image.getRGB(i, j);
                image.setRGB(i, j, setSaturation(rgb, a));
            }
        }
        return null;
    }


    @Override
    public Object visit(Transparency t) {
        int a;
        int lb, rb, db, ub; //left/right bound,down/up bound...
        double percentage = t.getNumber()* 0.01;
        a = (int) (255* percentage);
        if (range.getName().equals("full")) {
            lb = 0;
            rb = image.getWidth();
            db = 0;
            ub = image.getHeight();
        } else {
            lb = posx - rwidth / 2;
            rb = posx + rwidth / 2;
            db = posy - rheight / 2;
            ub = posy + rheight / 2;
        }

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        // load all the rgb value of image into bufferedImage
        for (int i=0; i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){
                bufferedImage.setRGB(i,j,image.getRGB(i,j));
            }
        }
        // set the transparency of specific area
        for (int i = lb; i < rb; i++) {
            for (int j = db; j < ub; j++) {
                // idea from Internet
                int rgb = image.getRGB(i, j);
                int r = (0xff & rgb);
                int g = (0xff & (rgb >> 8));
                int b = (0xff & (rgb >> 16));
                rgb = r + (g << 8) + (b << 16) + (a << 24);
                bufferedImage.setRGB(i, j, rgb);
            }
        }
        image = bufferedImage;
        return null;
    }

    @Override
    public Object visit(Position p) {
        this.posx = p.getX();
        this.posy = p.getY();
        return null;
    }

    @Override
    public Object visit(RangeSize s) throws IOException {
        s.getSize().accept(this);
        s.getPosition().accept(this);
        return null;
    }

    @Override
    public Object visit(RangeText t) {
        range = t;
        return null;
    }

    @Override
    public Object visit(Size s) {
        this.rwidth = s.getWidth();
        this.rheight = s.getHeight();
        return null;
    }

    @Override
    public Object visit(Var v) throws IOException {
        Range r = v.getRange();
        r.accept(this);
        this.range = r; // track the current range
        Effect e = v.getFuncName();
        e.accept(this);

        return null;
    }


}

