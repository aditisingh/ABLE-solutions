package org.opencv.samples.imagemanipulations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.*;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Rect;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.calib3d.Calib3d;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.googlecode.tesseract.android.TessBaseAPI;
import java.util.Locale;

public class ImageManipulationsActivity extends Activity implements CvCameraViewListener2, TextToSpeech.OnInitListener {
    private static final String  TAG                 = "OCVSample::Activity";

    public static final int      VIEW_MODE_RGBA      = 0;
    public static final int      VIEW_MODE_HIST      = 1;
    public static final int      VIEW_MODE_CANNY     = 2;
    public static final int      VIEW_MODE_SEPIA     = 3;
    public static final int      VIEW_MODE_SOBEL     = 4;
    public static final int      VIEW_MODE_ZOOM      = 5;
    public static final int      VIEW_MODE_PIXELIZE  = 6;
    public static final int      VIEW_MODE_POSTERIZE = 7;

    private MenuItem             mItemPreviewRGBA;
    private MenuItem             mItemPreviewHist;
    private MenuItem             mItemPreviewCanny;
    private MenuItem             mItemPreviewSepia;
    private MenuItem             mItemPreviewSobel;
    private MenuItem             mItemPreviewZoom;
    private MenuItem             mItemPreviewPixelize;
    private MenuItem             mItemPreviewPosterize;
    private CameraBridgeViewBase mOpenCvCameraView;

    private Size                 mSize0;

    private Mat                  mIntermediateMat;
    private Mat                  mMat0;
    private MatOfInt             mChannels[];
    private MatOfInt             mHistSize;
    private int                  mHistSizeNum = 25;
    private MatOfFloat           mRanges;
    private Scalar               mColorsRGB[];
    private Scalar               mColorsHue[];
    private Scalar               mWhilte;
    private Point                mP1;
    private Point                mP2;
    private float                mBuff[];
    private Mat                  mSepiaKernel;

    public static int           viewMode = VIEW_MODE_RGBA;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";

    // You should have the trained data file in assets folder
    // You can get them at:
    // http://code.google.com/p/tesseract-ocr/downloads/list
    public static final String lang = "eng";

    protected Button _button;
    // protected ImageView _image;
    protected EditText _field;
    protected String _path;
    protected boolean _taken;

    protected static final String PHOTO_TAKEN = "photo_taken";


    private double dist(double x1, double x2, double y1, double y2) {
        return Math.sqrt((double)((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
    }


    private TextToSpeech tts;
    private boolean startTTS = false;
    public void onInit(int status) {
        Log.v(TAG, "Language is not supported");
        //TTS is successfully initialized
        if (status == TextToSpeech.SUCCESS) {
            //Setting speech language
            int result = tts.setLanguage(Locale.US);

            //If your device doesn't support language you set above
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //Cook simple toast message with message
                Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Language is not supported");
            } else {
                startTTS = true;
            }
            //TTS is not initialized properly
        } else {
            Toast.makeText(this, "TTS Initilization Failed", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Initilization Failed");
        }
    }

    private void speakOut(String text) {
        if (text.length() == 0) {
            tts.speak("You haven't typed text", TextToSpeech.QUEUE_FLUSH, null);
            }
        else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ImageManipulationsActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.image_manipulations_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.image_manipulations_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

        File directory = new File(DATA_PATH);
        directory.mkdirs();

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }


        // lang.traineddata file with the app (in assets folder)
        // You can get them at:
        // http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {
                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, RESULT_OK);
        tts = new TextToSpeech(this, this);
        tts.speak("Welcome", TextToSpeech.QUEUE_ADD, null);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemPreviewRGBA  = menu.add("Preview RGBA");
        mItemPreviewHist  = menu.add("Histograms");
        mItemPreviewCanny = menu.add("Canny");
        mItemPreviewSepia = menu.add("Sepia");
        mItemPreviewSobel = menu.add("Sobel");
        mItemPreviewZoom  = menu.add("Zoom");
        mItemPreviewPixelize  = menu.add("Pixelize");
        mItemPreviewPosterize = menu.add("Posterize");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemPreviewRGBA)
            viewMode = VIEW_MODE_RGBA;
        if (item == mItemPreviewHist)
            viewMode = VIEW_MODE_HIST;
        else if (item == mItemPreviewCanny)
            viewMode = VIEW_MODE_CANNY;
        else if (item == mItemPreviewSepia)
            viewMode = VIEW_MODE_SEPIA;
        else if (item == mItemPreviewSobel)
            viewMode = VIEW_MODE_SOBEL;
        else if (item == mItemPreviewZoom)
            viewMode = VIEW_MODE_ZOOM;
        else if (item == mItemPreviewPixelize)
            viewMode = VIEW_MODE_PIXELIZE;
        else if (item == mItemPreviewPosterize)
            viewMode = VIEW_MODE_POSTERIZE;
        return true;
    }

    public void onCameraViewStarted(int width, int height) {
        mIntermediateMat = new Mat();
        mSize0 = new Size();
        mChannels = new MatOfInt[] { new MatOfInt(0), new MatOfInt(1), new MatOfInt(2) };
        mBuff = new float[mHistSizeNum];
        mHistSize = new MatOfInt(mHistSizeNum);
        mRanges = new MatOfFloat(0f, 256f);
        mMat0  = new Mat();
        mColorsRGB = new Scalar[] { new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255) };
        mColorsHue = new Scalar[] {
                new Scalar(255, 0, 0, 255),   new Scalar(255, 60, 0, 255),  new Scalar(255, 120, 0, 255), new Scalar(255, 180, 0, 255), new Scalar(255, 240, 0, 255),
                new Scalar(215, 213, 0, 255), new Scalar(150, 255, 0, 255), new Scalar(85, 255, 0, 255),  new Scalar(20, 255, 0, 255),  new Scalar(0, 255, 30, 255),
                new Scalar(0, 255, 85, 255),  new Scalar(0, 255, 150, 255), new Scalar(0, 255, 215, 255), new Scalar(0, 234, 255, 255), new Scalar(0, 170, 255, 255),
                new Scalar(0, 120, 255, 255), new Scalar(0, 60, 255, 255),  new Scalar(0, 0, 255, 255),   new Scalar(64, 0, 255, 255),  new Scalar(120, 0, 255, 255),
                new Scalar(180, 0, 255, 255), new Scalar(255, 0, 255, 255), new Scalar(255, 0, 215, 255), new Scalar(255, 0, 85, 255),  new Scalar(255, 0, 0, 255)
        };
        mWhilte = Scalar.all(255);
        mP1 = new Point();
        mP2 = new Point();

        // Fill sepia kernel
        mSepiaKernel = new Mat(4, 4, CvType.CV_32F);
        mSepiaKernel.put(0, 0, /* R */0.189f, 0.769f, 0.393f, 0f);
        mSepiaKernel.put(1, 0, /* G */0.168f, 0.686f, 0.349f, 0f);
        mSepiaKernel.put(2, 0, /* B */0.131f, 0.534f, 0.272f, 0f);
        mSepiaKernel.put(3, 0, /* A */0.000f, 0.000f, 0.000f, 1f);
    }

    public void onCameraViewStopped() {
        // Explicitly deallocate Mats
        if (mIntermediateMat != null)
            mIntermediateMat.release();

        mIntermediateMat = null;
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Size sizeRgba = rgba.size();
        Mat input = new Mat();
        rgba.copyTo(input);

        int rows = (int) sizeRgba.height;//=720
        int cols = (int) sizeRgba.width;//=1280

        Mat gray = new Mat();
        Size blur_size = new Size(3, 3);

        Mat imageMat = new Mat();
        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, blur_size, 0, 0);
        Imgproc.adaptiveThreshold(gray, imageMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25, 2);

        //use houghlines
//        Mat mLines=new Mat();
//        Imgproc.HoughLines(imageMat, mLines, 1, Math.PI/180, 100);
//
//        int ratio=3, kernel_size=3;
//        int lowThreshold=20;
//        int scale = 1;
//        int delta = 0;
//        int ddepth = CvType.CV_16S;
////
//        Mat grad_x = new Mat();
//        Mat grad_y = new Mat();
//        Mat abs_grad_x = new Mat();
//        Mat abs_grad_y = new Mat();
//        Mat grad = new Mat();
//
//        /// Gradient X
//        Imgproc.Sobel(gray, grad_x, ddepth, 1, 0, 3, scale, delta, Core.BORDER_DEFAULT);
//
//        /// Gradient Y
//        Imgproc.Sobel(gray, grad_y, ddepth, 0, 1, 3, scale, delta, Core.BORDER_DEFAULT);
////
//        Core.convertScaleAbs(grad_x, abs_grad_x);
//        Core.convertScaleAbs(grad_y, abs_grad_y);
//        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);
//
        double largest_area = 0;
        int largest_contour_index = 0;
//
        Mat hierarchy = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Point p = new Point(0, 0);
        Imgproc.findContours(imageMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, p);
//
        Mat drawing = Mat.zeros(imageMat.size(), CvType.CV_8UC3);
        Point p_r, p_l, p_t, p_b;
        p_r = new Point(0, 0);
        p_l = new Point(0, 0);
        p_b = new Point(0, 0);
        p_t = new Point(0, 0);

        for (int i = 0; i < contours.size(); i++) {
            double a = Imgproc.contourArea(contours.get(i), false);  //  Find the area of contour
            if (a > largest_area) {
                largest_area = a;
                largest_contour_index = i;                //Store the index of largest contour
                // bounding_rect=boundingRect(contours[i]); // Find the bounding rectangle for biggest contour
            }
        }

//        p_r.x=p_l.x=p_t.x=p_b.x = contours.get(largest_contour_index).get(1, 0)[0];
//        p_r.y=p_l.y=p_t.y=p_b.y = contours.get(largest_contour_index).get(1, 0)[1];


        MatOfPoint2f contours_poly = new MatOfPoint2f();
        MatOfPoint2f MOP2M2f = new MatOfPoint2f();
        //Convert contours(i) from MatOfPoint to MatOfPoint2f
        contours.get(largest_contour_index).convertTo(MOP2M2f, CvType.CV_32FC2);
        //Processing on MOP2M2f which is in type MatOfPoint2f
        Imgproc.approxPolyDP(MOP2M2f, contours_poly, 3, true);

        MatOfPoint MOP2f2M = new MatOfPoint();
        contours_poly.convertTo(MOP2f2M, CvType.CV_32S);
        Rect boundRect = Imgproc.boundingRect(MOP2f2M);

        Point[] r;//,r2,r3,r4;
        r = new Point[4];
        r[1] = new Point(0, 0);
        r[2] = new Point(0, 0);
        r[3] = new Point(0, 0);
        r[0] = new Point(0, 0);

//        Mat points=new MatOfPoint2f();
//        points=contours.get(largest_contour_index);
        RotatedRect minrect_roi = Imgproc.minAreaRect(MOP2M2f);

        r[0].x = minrect_roi.center.x + (minrect_roi.size.width / 2) * Math.cos(minrect_roi.angle) - (minrect_roi.size.height / 2) * Math.sin(minrect_roi.angle);
        r[1].x = minrect_roi.center.x - (minrect_roi.size.width / 2) * Math.cos(minrect_roi.angle) - (minrect_roi.size.height / 2) * Math.sin(minrect_roi.angle);
        r[3].x = minrect_roi.center.x + (minrect_roi.size.width / 2) * Math.cos(minrect_roi.angle) + (minrect_roi.size.height / 2) * Math.sin(minrect_roi.angle);
        r[2].x = minrect_roi.center.x - (minrect_roi.size.width / 2) * Math.cos(minrect_roi.angle) + (minrect_roi.size.height / 2) * Math.sin(minrect_roi.angle);

        r[0].y = minrect_roi.center.y + (minrect_roi.size.height / 2) * Math.cos(minrect_roi.angle) + (minrect_roi.size.width / 2) * Math.sin(minrect_roi.angle);
        r[1].y = minrect_roi.center.y + (minrect_roi.size.height / 2) * Math.cos(minrect_roi.angle) - (minrect_roi.size.width / 2) * Math.sin(minrect_roi.angle);
        r[3].y = minrect_roi.center.y - (minrect_roi.size.height / 2) * Math.cos(minrect_roi.angle) + (minrect_roi.size.width / 2) * Math.sin(minrect_roi.angle);
        r[2].y = minrect_roi.center.y - (minrect_roi.size.height / 2) * Math.cos(minrect_roi.angle) - (minrect_roi.size.width / 2) * Math.sin(minrect_roi.angle);


        //managing negative values
        for (int i = 0; i < 4; i++) {
            if (r[i].x < 0) {
                for (int j = 0; j < 4; j++)
                    r[i].x = r[i].x + r[j].x;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (r[i].y < 0) {
                for (int j = 0; j < 4; j++)
                    r[i].y = r[i].y + r[j].y;
            }
        }

        p_r = p_l = p_b = p_t = r[0];
        for (int i = 0; i < 4; i++) {
            if (r[i].x <= p_l.x)
                p_l = r[i];
            else if (r[i].x >= p_r.x)
                p_r = r[i];
            if (r[i].y >= p_b.y)
                p_b = r[i];
            else if (r[i].y <= p_t.y)
                p_t = r[i];
        }

        Point r_b, r_a, r_c, r_d;
        r_b = new Point(0, 0);
        r_a = new Point(0, 0);
        r_c = new Point(0, 0);
        r_d = new Point(0, 0);

        r_b.x = boundRect.br().x;
        r_b.y = boundRect.tl().y;
        r_d.x = boundRect.tl().x;
        r_d.y = boundRect.br().y;
        r_c.x = boundRect.br().x;
        r_c.y = boundRect.br().y;
        r_a.x = boundRect.tl().x;
        r_a.y = boundRect.tl().y;
//        MatOfPoint2f rect_points;
//        minrect_roi.points(rect_points);
        // points on minrect_roi

//        Scalar color = new Scalar(0, 0, 255);
//        Scalar color1 = new Scalar(255, 0, 0);
//        Log.d("left","X"+p_l.x+"Y"+p_l.y);
//        Log.d("right","X"+p_r.x+"Y"+p_r.y);
//        Log.d("top","X"+p_t.x+"Y"+p_t.y);
//        Log.d("bottom","X"+p_b.x+"Y"+p_b.y);
////
//        Imgproc.drawContours(rgba, contours, largest_contour_index, color, 1, 8, hierarchy, 0, new Point(0, 0));
//        Imgproc.rectangle(rgba, boundRect.tl(), boundRect.br(), color1);
////        //cropping the boundRect
////        Mat cropped=new Mat(rgba,boundRect);
////        Mat out=cropped.clone();
//////        cropped=
//        int row_c=contours.get(largest_contour_index).rows();
//        int col_c=contours.get(largest_contour_index).cols();
//        Log.d("contour row","row"+row_c);
//        Log.d("contour col","col"+col_c);
//        for( int i=0; i<contours.get(largest_contour_index).rows() ; i++) {
//            double y = contours.get(largest_contour_index).get(i,0)[1];
//            double x = contours.get(largest_contour_index).get(i,0)[0];
//            //Log.d("Row no", "Row "+(i));
//            Log.d("XY", "X "+x+" Y "+y);
//            Log.d("left","X"+p_l.x+"Y"+p_l.y);
//            Log.d("right","X"+p_r.x+"Y"+p_r.y);
//            Log.d("top","X"+p_t.x+"Y"+p_t.y);
//            Log.d("bottom","X"+p_b.x+"Y"+p_b.y);
//            if(x>=p_l.x) {
//                p_l.x=x;
//                p_l.y=y;
//            } else if(x<=p_r.x) {
//                p_r.x=x;
//                p_r.y=y;
//            } else if(y>=p_b.y) {
//                p_b.x = x;
//                p_b.y = y;
//            } else if(y<=p_t.y) {
//                p_t.x=x;
//                p_t.y=y;
//            }
//        }
//
//        //Scalar color2 = new Scalar(0, 255, 255);
//        Scalar color_r = new Scalar(255,0,0);
//        Scalar color_g= new Scalar(0,255,0);
//        Scalar color_w=new Scalar(255,255,255);
//        Scalar color_b=new Scalar(0,0,255);
//        Imgproc.circle(rgba, p_b, 10, color_w, 1, 8, 0);
//        Imgproc.circle(rgba, p_l, 10, color_r, 1, 8, 0);
//        Imgproc.circle(rgba, p_r, 10, color_g, 1, 8, 0);
//        Imgproc.circle(rgba, p_t, 10, color_b , 1, 8, 0);
////+++++++++++
//        Point r_b,r_t,r_r,r_l;
//        r_b = new Point();
//        r_t = new Point();
//        r_r = new Point();
//        r_l = new Point();
//
//        r_t.x = boundRect.br().x;
//        r_t.y=boundRect.tl().y;
//
//        r_r.x=boundRect.br().x;
//        r_r.y=boundRect.br().y;
//
//        r_b.x=boundRect.tl().x;
//        r_b.y=boundRect.br().y;
////
//        r_l.x=boundRect.tl().x;
//        r_l.y=boundRect.tl().y;
//
        MatOfPoint2f initial_pts = new MatOfPoint2f(p_t, p_l, p_b, p_r);
//       // Imgproc.circle(drawing, r_t, 2, color2, 1, 8, 0);
//
//        double d1 = dist(p_t.x,p_r.x,p_t.y,p_r.y);
//        double d2 = dist(p_r.x,p_b.x,p_r.y,p_b.y);
//        double d3 = dist(r_t.x,r_r.x,r_t.y,r_r.y);
//        double d4 = dist(r_r.x,r_b.x,r_r.y,r_b.y);
//
//        int ratio1 = (int)(d3/d4);
//        int ratio2 = (int)(d1/d2);
//
//        Log.d("ADebugTag", "Value: " + Double.toString(ratio1));
////      TODO Change this to an approximate ratio, int is an approximation, but still more approximate like
////      abs(ratio2-ratio1)<=2 or something
//        java.util.ArrayList<Point> lp = new java.util.ArrayList<Point>(4);
//        if(ratio2==ratio1) {
//            lp.add(0, r_t);
//            lp.add(1, r_l);
//            lp.add(2, r_b);
//            lp.add(3, r_r);
//        } else {
//            lp.add(0, r_t);
//            lp.add(1, r_r);
//            lp.add(2, r_b);
//            lp.add(3, r_l);
//        }
//
//
//
//        Imgproc.circle(rgba, r_b, 10, color, 1, 8, 0);
//        Imgproc.circle(rgba, r_l, 10, color, 1, 8, 0);
//        Imgproc.circle(rgba, r_r, 10, color, 1, 8, 0);
//        Imgproc.circle(rgba, r_t, 10, color, 1, 8, 0);
//
        MatOfPoint2f final_pts = new MatOfPoint2f(r_b, r_a, r_d, r_c);
//        final_pts.fromList(lp);
//
////        return rgba;
////        Mat cropped = new Mat(rgba, boundRect);
////        int top = (int)boundRect.tl().y;
////        int h = (int)boundRect.height;
////        int left = (int)boundRect.tl().x;
////        int w = (int)boundRect.width;
////
////        Mat im_transformed=rgba.submat(top, top+h, left, left+w);
////        Mat image =  new Mat(im_transformed.rows(), im_transformed.cols(), im_transformed.type(), new Scalar(0,0,100));
////        image.copyTo(im_transformed);
////
////        Mat H = new Mat();
////        H=Calib3d.findHomography(initial_pts, final_pts);
////        //TODO doesn't work
//        Mat perspectiveTransform=new Mat();
//        perspectiveTransform=Imgproc.getPerspectiveTransform(initial_pts,final_pts);
////        Mat src_mat=new Mat(4,1,CvType.CV_32FC2);
////        Mat dst_mat=new Mat(4,1,CvType.CV_32FC2);
////
////
////        src_mat.put(0,0,p_t.x,p_t.y,0.5*(p_t.x+p_r.x),0.5*(p_t.y+p_r.y),p_r.x,p_r.y,0.5*(p_r.x+p_b.x),0.5*(p_r.y+p_b.y),p_b.x,p_b.y,0.5*(p_b.x+p_l.x),0.5*(p_b.y+p_l.y),p_l.x,p_l.y,0.5*(p_l.x+p_t.x),0.5*(p_l.y+p_t.y));
////        //0,0,407.0,74.0, 1606.0, 74.0, 420.0, 2589.0, 1698.0,2589.0);
////        dst_mat.put(0,0,r_t.x,r_t.y,0.5*(r_t.x+r_r.x),0.5*(r_t.y+r_r.y),r_r.x,r_r.y,0.5*(r_r.x+r_b.x),0.5*(r_r.y+r_b.y),r_b.x,r_b.y,0.5*(r_b.x+r_l.x),0.5*(r_b.y+r_l.y),r_l.x,r_l.y,0.5*(r_l.x+r_t.x),0.5*(r_l.y+r_t.y));
//////        dst_mat.put(0,0,0,0,0.5*w,0,w,0,w,0.5*h,w,h,0.5*w,h,0,h,0,0.5*h);
////
////        //0.0,0.0,1600.0,0.0, 0.0,2500.0,1600.0,2500.0);
        Mat perspectiveTransform = new Mat();
        perspectiveTransform = Imgproc.getPerspectiveTransform(initial_pts, final_pts);
//
////        Mat dst=rgba.clone();
        Mat dst = Mat.zeros(imageMat.size(), CvType.CV_8UC3);
        Mat crop = Mat.zeros(imageMat.size(), CvType.CV_8UC3);

////
        Imgproc.warpPerspective(input, dst, perspectiveTransform, rgba.size());
//        Rect roi=new Rect((int)boundRect.tl().x,(int)boundRect.tl().y,(int)boundRect.br().x-(int)boundRect.tl().x,(int) boundRect.br().y-(int)boundRect.tl().y);
//////
//        dst.submat(roi);
//        Mat image =  new Mat(dst.rows(), dst.cols(), dst.type());
//        image.copyTo(dst);
//

////        //  Imgproc.rectangle(rgba, r_t, r_b,color, 2);
////        //Mat new_img= new Mat();
////        //Imgproc.warpPerspective(rgba, new_img, H, rgba.size());
////
        ///improve contrast
        Mat gray_dst = new Mat();
        Mat res = new Mat();

        Imgproc.cvtColor(dst, gray_dst, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray_dst, res, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        File StorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "OCR");

        if (!StorageDir.exists()) {
            boolean dir_status=StorageDir.mkdirs();
            Log.v(TAG, "Result " + String.valueOf(dir_status));

        }

        String filename = "test.png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
        _path = file.toString();

        boolean out = Imgcodecs.imwrite(_path, res);
        Log.v(TAG, "Path "+_path);
        Log.v(TAG, "Result " + String.valueOf(out));
        Log.v(TAG, "State "+String.valueOf(Environment.getExternalStorageState()));

        onPhotoTaken();

        return res;//image;//dst;//rgba;//dst;//grad;//dst;//new_img;

    }

    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

        try {
            ExifInterface exif = new ExifInterface(_path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            if (rotate != 0) {

                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }

            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        } catch (IOException e) {
            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
        }

        // _image.setImageBitmap( bitmap );

        Log.v(TAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, lang);
        baseApi.setImage(bitmap);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        // You now have the text in recognizedText var, you can do anything with it.
        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
        // so that garbage doesn't make it to the display.

        Log.v(TAG, "OCRED TEXT: " + recognizedText);

        if ( lang.equalsIgnoreCase("eng") ) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }

        Log.d(TAG, "Status of tts"+String.valueOf(startTTS));
        if (startTTS) {
            speakOut(recognizedText);
        }
        recognizedText = recognizedText.trim();

//        if ( recognizedText.length() != 0 ) {
//            _field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
//            _field.setSelection(_field.getText().toString().length());
//        }
    }


}

