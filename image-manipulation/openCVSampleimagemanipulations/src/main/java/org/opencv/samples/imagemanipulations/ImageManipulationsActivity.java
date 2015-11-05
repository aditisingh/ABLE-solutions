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
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Rect;
import org.opencv.core.Core;
import org.opencv.core.Core.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfKeyPoint;
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
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
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
import java.io.OutputStreamWriter;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.Locale;

public class ImageManipulationsActivity extends Activity implements CvCameraViewListener2, TextToSpeech.OnInitListener {
    private static final String TAG = "OCVSample::Activity";

    public static final int VIEW_MODE_RGBA = 0;
    public static final int VIEW_MODE_HIST = 1;
    public static final int VIEW_MODE_CANNY = 2;
    public static final int VIEW_MODE_SEPIA = 3;
    public static final int VIEW_MODE_SOBEL = 4;
    public static final int VIEW_MODE_ZOOM = 5;
    public static final int VIEW_MODE_PIXELIZE = 6;
    public static final int VIEW_MODE_POSTERIZE = 7;

    private MenuItem mItemPreviewRGBA;
    private MenuItem mItemPreviewHist;
    private MenuItem mItemPreviewCanny;
    private MenuItem mItemPreviewSepia;
    private MenuItem mItemPreviewSobel;
    private MenuItem mItemPreviewZoom;
    private MenuItem mItemPreviewPixelize;
    private MenuItem mItemPreviewPosterize;
    private JavaCamResView mOpenCvCameraView;

    private Size mSize0;

    private Mat mIntermediateMat;
    private Mat mMat0;
    private MatOfInt mChannels[];
    private MatOfInt mHistSize;
    private int mHistSizeNum = 25;
    private MatOfFloat mRanges;
    private Scalar mColorsRGB[];
    private Scalar mColorsHue[];
    private Scalar mWhilte;
    private Point mP1;
    private Point mP2;
    private float mBuff[];
    private Mat mSepiaKernel;

    public static int viewMode = VIEW_MODE_RGBA;
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
        return Math.sqrt((double) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }

    double computeProduct(Point p, Point a, Point b) {
        double k = (a.y - b.y) / (a.x - b.x);
        double j = a.y - k * a.x;
        return k * p.x - p.y + j;
    }

    private boolean isInROI(Point p, MatOfPoint2f roi) {
        double[] pro = new double[4];
        Point[] points = roi.toArray();
        for (int i = 0; i < 4; ++i) {
            pro[i] = computeProduct(p, points[i], points[(i + 1) % 4]);
        }

        if (pro[0] * pro[2] < 0 && pro[1] * pro[3] < 0) {
            return false;
        }
        return true;
    }

    // Give a binary image and get back blobs from that
    ArrayList<MatOfPoint2f> blob_detection(Mat binary_image, int min_blob_size) {
        ArrayList<MatOfPoint2f> blobs = new ArrayList<MatOfPoint2f>();

        // Fill the label_image with the blobs
        // 0  - background
        // 1  - unlabelled foreground
        // 2+ - labelled foreground

        // input is a binary image therefore values are either 0 or 1
        // out objective is to find a set of 1's that are together and assign 2 to it
        // then look for other 1's, and assign 3 to it....so on a soforth

        Mat label_image = new Mat();
        binary_image.convertTo(label_image, CvType.CV_32FC1); // weird it doesn't support CV_32S! Because the CV::SCALAR is a double value in the function floodfill

        int label_count = 2; // starts at 2 because 0,1 are used already

        //  // Erosion. Optional
        //  // TODO Add flag to select
        //  Mat element = getStructuringElement( MORPH_RECT,
        //   Size( 2*3 + 1, 2*3+1 ),
        // Point( 0, 0 ) );
        //  // Apply the erosion operation
        //  erode( label_image, label_image, element );

        // just check the Matrix of label_image to make sure we have 0 and 1 only
        // cout << label_image << endl;
        for (int y = 0; y < binary_image.rows(); y++) {
            for (int x = 0; x < binary_image.cols(); x++) {
                double checker = label_image.get(y, x)[0]; //need to look for float and not int as the scalar value is of type double
                Rect rect = new Rect();
                int size = 0;
                // cout <<checker<<endl;
                if (checker == 0) {
                    //fill region from a point
                    Mat mask = new Mat(label_image.rows() + 2, label_image.cols() + 2, CvType.CV_8UC1);
                    Imgproc.floodFill(label_image, mask, new Point(x, y), new Scalar(label_count), rect, new Scalar(0), new Scalar(0), 4);
                    label_count++;
                    // cout << label_image << endl <<"by checking: " << label_image.at<float>(y,x) <<endl;
                    //cout << label_image;

                    //a vector of all points in a blob
                    ArrayList<Point> temp = new ArrayList<Point>();

                    for (int i = rect.y; i < (rect.y + rect.height); i++) {
                        for (int j = rect.x; j < (rect.x + rect.width); j++) {
                            double chk = label_image.get(i, j)[0];
                            // std::cout << chk << std::endl;
                            if (chk == label_count - 1) {
                                temp.add(new Point(j, i));
                                size++;
                            }
                        }
                    }
                    MatOfPoint2f blob = new MatOfPoint2f();
                    blob.fromList(temp);

                    if (size > min_blob_size) {
                        //place the points of a single blob in a grouping
                        //a vector of vector points
                        blobs.add(blob);
                        Log.d(TAG, "Added blob");
                        // circle(binary_image, *blob.begin(), 10, Scalar(255), 5);
                    }
                }
            }
        }

        return blobs;
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
            tts.speak("No text detected.", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public ImageManipulationsActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.image_manipulations_surface_view);

        mOpenCvCameraView = (JavaCamResView) findViewById(R.id.image_manipulations_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

        File directory = new File(DATA_PATH);
        directory.mkdirs();

        String[] paths = new String[]{DATA_PATH, DATA_PATH + "tessdata/"};

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
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
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
        mItemPreviewRGBA = menu.add("Preview RGBA");
        mItemPreviewHist = menu.add("Histograms");
        mItemPreviewCanny = menu.add("Canny");
        mItemPreviewSepia = menu.add("Sepia");
        mItemPreviewSobel = menu.add("Sobel");
        mItemPreviewZoom = menu.add("Zoom");
        mItemPreviewPixelize = menu.add("Pixelize");
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
        mChannels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};
        mBuff = new float[mHistSizeNum];
        mHistSize = new MatOfInt(mHistSizeNum);
        mRanges = new MatOfFloat(0f, 256f);
        mMat0 = new Mat();
        mColorsRGB = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};
        mColorsHue = new Scalar[]{
                new Scalar(255, 0, 0, 255), new Scalar(255, 60, 0, 255), new Scalar(255, 120, 0, 255), new Scalar(255, 180, 0, 255), new Scalar(255, 240, 0, 255),
                new Scalar(215, 213, 0, 255), new Scalar(150, 255, 0, 255), new Scalar(85, 255, 0, 255), new Scalar(20, 255, 0, 255), new Scalar(0, 255, 30, 255),
                new Scalar(0, 255, 85, 255), new Scalar(0, 255, 150, 255), new Scalar(0, 255, 215, 255), new Scalar(0, 234, 255, 255), new Scalar(0, 170, 255, 255),
                new Scalar(0, 120, 255, 255), new Scalar(0, 60, 255, 255), new Scalar(0, 0, 255, 255), new Scalar(64, 0, 255, 255), new Scalar(120, 0, 255, 255),
                new Scalar(180, 0, 255, 255), new Scalar(255, 0, 255, 255), new Scalar(255, 0, 215, 255), new Scalar(255, 0, 85, 255), new Scalar(255, 0, 0, 255)
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

//        mOpenCvCameraView.setFlashMode(this, 4);
        mOpenCvCameraView.setFocusMode(this, 1);
    }

    public void onCameraViewStopped() {
        // Explicitly deallocate Mats
        if (mIntermediateMat != null)
            mIntermediateMat.release();

        mIntermediateMat = null;
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
//        mOpenCvCameraView.setFlashMode(this, 1);

        Size sizeRgba = rgba.size();
        int rows = (int) sizeRgba.height;//=720
        int cols = (int) sizeRgba.width;//=1280

        Mat gray = new Mat();
        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGR2GRAY);

        // Laplacian
        Mat lap_output = new Mat();
        Imgproc.Laplacian(gray, lap_output, CvType.CV_8U);
        MatOfDouble mu = new MatOfDouble();
        MatOfDouble sigma = new MatOfDouble();
        Core.meanStdDev(lap_output, mu, sigma);

        double focusMeasure = sigma.get(0,0)[0];
        Log.d(TAG, "Focus measure"+focusMeasure);

        if (focusMeasure < 8) {
//            return rgba;
        }

        gray.convertTo(gray, CvType.CV_32FC1);
        Mat gray_dst = new Mat();
        Core.pow(gray, 0.7, gray_dst);
        gray_dst.convertTo(gray_dst, CvType.CV_8UC1);
        Mat detected_edges = new Mat();
        Imgproc.adaptiveThreshold(gray_dst, detected_edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 25, 2);

        Mat binary_img = detected_edges.clone();
        Mat binary_img2=binary_img.clone();

        // Smearing implementation
//        int T = 20;
//        int count = 0;
//        int flag = 0;
//        Mat tmpImg = new Mat(binary_img.size(), CvType.CV_8UC1, new Scalar(0, 0, 0));
//        //doing row-wise
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                double[] data_curr = binary_img.get(i, j);
//                if (data_curr[0] == 255) {
//                    flag = 255;
//                    count = count + 1;
//                } else {
//                    if (flag == 255 && count <= T) {
//                        for (int k = 0; k <= count; k++) {
//                            binary_img.put(i - k, j, 0);
//                            binary_img.put(i - k, j + 1, 0);
//                        }
//                    }
//                    flag = 0;
//                    count = 0;
//                }
//            }
//        }
//
//
//        for (int j = 0; j < cols; j++) {
//            for (int i = 0; i < rows; i++) {
//                double[] data_curr = binary_img2.get(i, j);
//                if (data_curr[0] == 255) {
//                    flag = 255;
//                    count = count + 1;
//                } else {
//                    if (flag == 255 && count <= T) {
//                        for (int k = 0; k <= count; k++) {
//                            binary_img2.put(i - k, j, 0);
//                            binary_img2.put(i - k, j + 1, 0);
//                        }
//                    }
//                    flag = 0;
//                    count = 0;
//                }
//            }
//        }
//
//
//        Core.bitwise_or(binary_img,binary_img2,tmpImg);
//        int MAX_KERNEL_LENGTH=7;
//        for(int k=1;k<MAX_KERNEL_LENGTH;k=k+2)
//        {
//            Imgproc.medianBlur(tmpImg,tmpImg,k);
//        }
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//
//        Imgproc.findContours(tmpImg, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//        for(int i=0; i< contours.size();i++) {
//            Rect rect = Imgproc.boundingRect(contours.get(i));
//            RotatedRect rect_r;
//
//            Point[] rect_points = new Point[4];
//            MatOfPoint2f mof = new MatOfPoint2f();
//            MatOfPoint2f mo2f = new MatOfPoint2f(contours.get(i).toArray());
//
//
//            rect_r = Imgproc.minAreaRect(mo2f);
//            rect_r.points(rect_points);
//            mof.fromArray(rect_points);
//            Scalar color = new Scalar(0, 255, 0);
////        for(int p=0;p<4;p++)
////            Imgproc.line(rgba, rect_points[i], rect_points[(i + 1) % 4],color, 4);// Core.LINE_AA);
//        }

        //////////////////////////////////////////////////////////////////////////////////////////////
        ////////// Method to rotate based on Hough transform
        //////////////////////////////////////////////////////////////////////////////////////////////

//        Mat bw_ = new Mat();
//        Core.bitwise_not(detected_edges, bw_);
//
//        RotatedRect rect_im;
//        Mat points=Mat.zeros(bw_.size(),bw_.type());
//        Core.findNonZero(bw_,points);
//
//        MatOfPoint mpoints = new MatOfPoint(points);
//        MatOfPoint2f points2f = new MatOfPoint2f(mpoints.toArray());
//        Point[] rect_points = new Point[4];
//
//        rect_im = Imgproc.minAreaRect(points2f);
//        rect_im.points(rect_points);
//
////        Scalar color = new Scalar( 255, 0, 0 );
////        for(int i=0;i<4;i++)
////            Imgproc.line(rgba, rect_points[i], rect_points[(i + 1) % 4],color, 4);// Core.LINE_AA);
//
//        Mat lines = new Mat();
//        Mat bw_1=new Mat();
//        int erosion_size = 3;
//
//        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size-1));
//        Imgproc.dilate(bw_,bw_1, element1);
//
//        Imgproc.HoughLinesP(bw_1, lines, 1, Math.PI/180, 100, cols / 5, cols / 10);
//        Mat disp_lines = new Mat(bw_1.size(), CvType.CV_8UC1, new Scalar(0, 0, 0));
//        double angle = 0.;
//        double nb_lines = lines.size().height;
//
//        double angle1=Math.toDegrees(angle);
//
//////    TODO Rotated doc -> Extract doc -> Save it
//
//        Mat rot_matrix = Imgproc.getRotationMatrix2D(rect_im.center, angle1, 1);
//        Mat rotated = new Mat(bw_.cols(), bw_.rows(), CvType.CV_8UC3, new Scalar(255));
//
////        Draws the bounding box
////        Rect r = rect_im.boundingRect();
////        Imgproc.rectangle(rgba, r.tl(), r.br(), new Scalar(255, 255, 0));
//
//        Imgproc.warpAffine(rgba, rgba, rot_matrix, rgba.size(), Imgproc.INTER_CUBIC);
//        Imgproc.warpAffine(gray_dst, gray_dst, rot_matrix, rgba.size(), Imgproc.INTER_CUBIC);
//
//        for (double i = 0; i < nb_lines; ++i)
//        {
//            double x1 = lines.get((int)i, 0)[0],
//                    y1 = lines.get((int)i, 0)[1],
//                    x2 = lines.get((int)i, 0)[2],
//                    y2 = lines.get((int)i, 0)[3];
//            Point start = new Point(x1, y1);
//            Point end = new Point(x2, y2);
//
////            Imgproc.line(rgba, start, end, new Scalar(0,255, 0), 3);
//            angle += Math.atan2(y2-y1,x2-x1);
//        }
//        angle /= nb_lines; // mean angle, in radians.
//        Log.d(TAG, "Angle" + angle);//correct
//        Log.d(TAG, "Area" + bw_.size().area());

        // Do a MSER here
        MatOfKeyPoint mokp = new MatOfKeyPoint();
        FeatureDetector fd = FeatureDetector.create(FeatureDetector.MSER);

//        //// Vary properties of MSER here
        File outputDir = getCacheDir();
        try {
            File outputFile = File.createTempFile("MSERDetectorParams", ".YAML", outputDir);
            writeToFile(outputFile, "%YAML:1.0\ndelta: 1000\nminArea: 1\nmaxArea: 921600\nmaxVariation: 0.25\nminDiversity: 0.5\n");
            fd.read(outputFile.getPath());
        }
        catch(IOException e) {
            Log.d(TAG, "No params file");
        }

//        Imgproc.Canny(gray_dst, gray_dst, 400, 450);
        fd.detect(gray_dst, mokp);

        Log.i(TAG, "Mat of key points = " + mokp.rows() + "x" + mokp.cols());

        Mat OutImage = new Mat();
        Imgproc.cvtColor(rgba, OutImage, Imgproc.COLOR_RGBA2RGB);
//
        if (!mokp.empty()) {
            // Draw kewpoints
            Scalar color = new Scalar(0, 0, 255); // BGR
            int flags = Features2d.DRAW_RICH_KEYPOINTS; // For each keypoint, the circle around keypoint with keypoint size and orientation will be drawn.
            Features2d.drawKeypoints(OutImage, mokp, OutImage, color , flags);
            Imgproc.cvtColor(OutImage, rgba, Imgproc.COLOR_RGB2RGBA);
        }

//                Log.d(TAG, "Warping");
//        String filename = "test.bmp";
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
//        _path = file.toString();
////
//        boolean out = Imgcodecs.imwrite(_path,rotated);//rgba);//rgba);
//        Log.v(TAG, "Path " + _path);
//        Log.v(TAG, "Result " + String.valueOf(out));
//        Log.v(TAG, "State "+String.valueOf(Environment.getExternalStorageState()));
//
//        onPhotoTaken();

//        return detected_edges;//rgba;//image;//dst;//rgba;//dst;//grad;//dst;//new_img;
        return  rgba;//tmpImg;//img_smeared;//binary_img;//rotated;//gba;//bw_1;//rgba;//otated;//gba;//otated;//erode_dst;//detected_edges;//bw;//detected_edges;///bw_;//rotated;//gba;//bw_;//rotated;
//        mOpenCvCameraView.setFlashMode(this, 4);
//        return ret;
    }

    private void writeToFile(File file, String data) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            stream.close();
        } catch (IOException e) {
            Log.d(TAG, "Not able to write to file");
        }
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

        if (lang.equalsIgnoreCase("eng")) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }

        Log.d(TAG, "Status of tts" + String.valueOf(startTTS));
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

