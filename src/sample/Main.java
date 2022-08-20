package sample;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    ImageInfo imageInfo = new ImageInfo();
    int Width=750,Height=750;
    List<Point2D> point2DS = null;
    List<BufferedImage> storeWithbefore = new ArrayList<>();
    List<BufferedImage> storeWithafter = new ArrayList<>();
    BufferedImage ggg;
    int size = 0;
    public BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public BufferedImage Resize(BufferedImage bi ,int h,int w)
    {
        java.awt.Image im = bi.getScaledInstance(w,h, java.awt.Image.SCALE_SMOOTH);
        BufferedImage tmp = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = tmp.createGraphics();
        gg.drawImage(im,0,0,null);
        gg.dispose();
        return tmp;
    }
    public BufferedImage ToJpg(BufferedImage image)
    {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, java.awt.Color.WHITE, null);
        return result;
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        /* ----------btns---------- */
        point2DS = new ArrayList<>();
        ToggleButton drowbtn = new ToggleButton("Draw");
        drowbtn.setMinWidth(90);
        drowbtn.setCursor(Cursor.HAND);

        Label lslider = new Label("sensitivity:");
        Label l = new Label();
        l.setText("value: " + "125000");
        imageInfo.S = 125000;
        l.setTextFill(Color.BLACK);
        lslider.setStyle("-fx-font-size: 17; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #666;");
        Slider slider = new Slider();
        slider.setMin(125000);
        slider.setMax(500000);
        slider.setValue(150000);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(2000);
        slider.valueProperty().addListener(e->{
            l.setText("value: " + slider.getValue());
            imageInfo.S = (int) slider.getValue();
        });


        ColorPicker cpLine = new ColorPicker(Color.RED);
        Label line_color = new Label("Line Color");
        line_color.setStyle("-fx-font-size: 15; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #666;");
        Button before = new Button("Before");
        Button after = new Button("After");
        Button real_image = new Button("Real Image");
        Button save = new Button("Save");
        Button open = new Button("Open");
        Button[] basicArr = {save, open,before,after,real_image};
        for(Button btn : basicArr) {
            btn.setMinWidth(90);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #666;");
        }
        save.setStyle("-fx-background-color: #80334d;");
        open.setStyle("-fx-background-color: #80334d;");
        before.setStyle("-fx-background-color: #80334d;");
        after.setStyle("-fx-background-color: #80334d;");
        real_image.setStyle("-fx-background-color: #80334d;");

        VBox btns = new VBox(10);
        btns.getChildren().addAll( line_color, cpLine,drowbtn,lslider,slider,l,before ,after , real_image, open, save);
        btns.setPadding(new Insets(15));
        btns.setStyle("-fx-background-color: #999");
        btns.setPrefWidth(125);

        Canvas canvas = new Canvas(Width, Height);
        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(5);

        //image process object
        imageInfo.R = (int)(cpLine.getValue().getRed()*255);
        imageInfo.G = (int)(cpLine.getValue().getGreen()*255);
        imageInfo.B = (int)(cpLine.getValue().getBlue()*255);
        //
        canvas.setOnMousePressed(e->{
            if(drowbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
                point2DS.add(new Point2D(e.getX(),e.getY()));
//                imageInfo.Xpos = (int)e.getX();
//                imageInfo.Ypos = (int)e.getY();
//                imageInfo.S = (int) slider.getValue();
//                if(imageInfo.img!=null) {
//                    imageInfo.ImageProcessing();
//                    gc.drawImage(SwingFXUtils.toFXImage(imageInfo.img,null), 0,0,1000,700);
//                }
            }
        });
        //
        canvas.setOnMouseDragged(e->{
            if(drowbtn.isSelected()) {
                point2DS.add(new Point2D(e.getX(),e.getY()));
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });
        canvas.setOnMouseReleased(e->{
            if(drowbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
                point2DS.add(new Point2D(e.getX(),e.getY()));
                if(imageInfo.img!=null){
                for (Point2D item:point2DS) {
                    imageInfo.Xpos = (int) item.getX();
                    imageInfo.Ypos = (int) item.getY();
                    imageInfo.ImageProcessing();
                }
                point2DS.clear();
                gc.drawImage(SwingFXUtils.toFXImage(imageInfo.img,null), 0,0,Width,Height);
                storeWithbefore.add(copyImage(imageInfo.img));
                }
            }
        });
        // color picker
        cpLine.setOnAction(e->{
            gc.setStroke(cpLine.getValue());
            //
            imageInfo.R = (int)(cpLine.getValue().getRed()*255);
            imageInfo.G = (int)(cpLine.getValue().getGreen()*255);
            imageInfo.B = (int)(cpLine.getValue().getBlue()*255);
            //System.out.println(imageInfo.R+","+imageInfo.G+","+imageInfo.B);
        });


        before.setOnAction((e)->{
            if(storeWithbefore.size()>1){
                storeWithafter.add( copyImage(storeWithbefore.remove(storeWithbefore.size()-1)) );
                imageInfo.img = copyImage(storeWithbefore.get(storeWithbefore.size()-1));
                gc.drawImage(SwingFXUtils.toFXImage(imageInfo.img,null), 0,0,Width,Height);
            }
            else if (storeWithbefore.size()==1) {
                imageInfo.img = copyImage(storeWithbefore.get(0));
                gc.drawImage(SwingFXUtils.toFXImage(imageInfo.img,null), 0,0,Width,Height);
            }
            else {
                System.out.println("You Cant before");
            }
        });
        after.setOnAction((e)->{
            if(!storeWithafter.isEmpty()){
                imageInfo.img = copyImage(storeWithafter.get(storeWithafter.size()-1));
                storeWithbefore.add(copyImage(imageInfo.img));
                storeWithafter.remove(storeWithafter.size()-1);
                gc.drawImage(SwingFXUtils.toFXImage(imageInfo.img,null), 0,0,Width,Height);
            }
        });

        real_image.setOnAction((e)->{
            if(!storeWithbefore.isEmpty()){
                System.out.println("size:"+storeWithbefore.size());
                imageInfo.img = copyImage(storeWithbefore.get(0));
                gc.drawImage(SwingFXUtils.toFXImage(imageInfo.img,null), 0,0,Width,Height);
                storeWithbefore.clear();
                storeWithafter.clear();
                storeWithbefore.add(copyImage(imageInfo.img));
            }
        });

        open.setOnAction((e)->{
            storeWithbefore.clear();
            storeWithafter.clear();
            FileChooser openFile = new FileChooser();
            openFile.setTitle("Open File");
            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    gc.drawImage(img, 0,0,Width,Height);
                    BufferedImage image = Resize(SwingFXUtils.fromFXImage(img,null),Height,Width);
                    imageInfo.img = ToJpg(image);
                    storeWithbefore.add(copyImage(imageInfo.img));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        save.setOnAction((e)->{
            FileChooser savefile = new FileChooser();
            savefile.getExtensionFilters().add(new FileChooser.ExtensionFilter("image jpg" ,"*.jpg"));
            savefile.setTitle("Save File");
            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                imageInfo.ImageProcessing();
                imageInfo.StoreImage(file);
            }
        });
        BorderPane pane = new BorderPane();
        pane.setLeft(btns);
        pane.setCenter(canvas);

        Scene scene = new Scene(pane, 1550,800 );

        primaryStage.setTitle("Paint");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}