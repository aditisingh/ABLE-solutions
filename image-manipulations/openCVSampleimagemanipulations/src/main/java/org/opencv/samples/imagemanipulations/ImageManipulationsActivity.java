package org.opencv.samples.imagemanipulations;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;

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
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;
import org.opencv.calib3d.Calib3d;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class ImageManipulationsActivity extends Activity implements CvCameraViewListener2 {
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

    private double dist(double x1, double x2, double y1, double y2) {
        return Math.sqrt((double)((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
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
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.image_manipulations_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.image_manipulations_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
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

        int rows = (int) sizeRgba.height;//=720
        int cols = (int) sizeRgba.width;//=1280

        Mat gray = new Mat();
        Size blur_size= new Size(3, 3);

        Mat imageMat=new Mat();
        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, blur_size, 0, 0);
        Imgproc.adaptiveThreshold(gray, imageMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);

        int ratio=3, kernel_size=3;
        int lowThreshold=20;
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;

        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();
        Mat grad = new Mat();

        /// Gradient X
        Imgproc.Sobel(gray, grad_x, ddepth, 1, 0, 3, scale, delta, Core.BORDER_DEFAULT);

        /// Gradient Y
        Imgproc.Sobel(gray, grad_y, ddepth, 0, 1, 3, scale, delta, Core.BORDER_DEFAULT);

        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

        double largest_area=0;
        int largest_contour_index=0;

        Mat hierarchy = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Point p = new Point(0, 0);
        Imgproc.findContours(imageMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, p );

        Mat drawing = Mat.zeros( imageMat.size(), CvType.CV_8UC3 );
        Point p_r, p_l, p_t, p_b;
        p_r = new Point(0, 0);
        p_l = new Point(0, 0);
        p_b = new Point(0, 0);
        p_t = new Point(0, 0);

        for( int i = 0; i< contours.size(); i++ )
        {
            double a=Imgproc.contourArea( contours.get(i), false );  //  Find the area of contour
            if(a>largest_area){
                largest_area=a;
                largest_contour_index=i;                //Store the index of largest contour
                // bounding_rect=boundingRect(contours[i]); // Find the bounding rectangle for biggest contour
            }
        }

        p_r.x=p_l.x=p_t.x=p_b.x = contours.get(largest_contour_index).get(1, 0)[0];
        p_r.y=p_l.y=p_t.y=p_b.y = contours.get(largest_contour_index).get(1, 0)[1];


        MatOfPoint2f contours_poly = new MatOfPoint2f();
        MatOfPoint2f MOP2M2f = new MatOfPoint2f();
        //Convert contours(i) from MatOfPoint to MatOfPoint2f
        contours.get(largest_contour_index).convertTo(MOP2M2f, CvType.CV_32FC2);
        //Processing on MOP2M2f which is in type MatOfPoint2f
        Imgproc.approxPolyDP(MOP2M2f, contours_poly, 3, true);

        MatOfPoint MOP2f2M = new MatOfPoint();
        contours_poly.convertTo(MOP2f2M, CvType.CV_32S);
        Rect boundRect = Imgproc.boundingRect( MOP2f2M );

        Scalar color = new Scalar(0, 0, 255);
        Scalar color1 = new Scalar(255, 0, 0);
        Log.d("left","X"+p_l.x+"Y"+p_l.y);
        Log.d("right","X"+p_r.x+"Y"+p_r.y);
        Log.d("top","X"+p_t.x+"Y"+p_t.y);
        Log.d("bottom","X"+p_b.x+"Y"+p_b.y);

       // Imgproc.drawContours(rgba, contours, largest_contour_index, color, 1, 8, hierarchy, 0, new Point(0, 0) );
        Imgproc.rectangle(rgba, boundRect.tl(), boundRect.br(), color1, 1, 8, 0);
        int row_c=contours.get(largest_contour_index).rows();
        int col_c=contours.get(largest_contour_index).cols();
        Log.d("contour row","row"+row_c);
        Log.d("contour col","col"+col_c);
        for( int i=0; i<contours.get(largest_contour_index).rows() ; i++) {
            double y = contours.get(largest_contour_index).get(i,0)[1];
            double x = contours.get(largest_contour_index).get(i,0)[0];
            //Log.d("Row no", "Row "+(i));
            Log.d("XY", "X "+x+" Y "+y);
            Log.d("left","X"+p_l.x+"Y"+p_l.y);
            Log.d("right","X"+p_r.x+"Y"+p_r.y);
            Log.d("top","X"+p_t.x+"Y"+p_t.y);
            Log.d("bottom","X"+p_b.x+"Y"+p_b.y);
            if(x<=p_l.x) {
                p_l.x=x;
                p_l.y=y;
            } else if(x>=p_r.x) {
                p_r.x=x;
                p_r.y=y;
            } else if(y>=p_b.y) {
                p_b.x = x;
                p_b.y = y;
            } else if(y<=p_t.y) {
                p_t.x=x;
                p_t.y=y;
            }
        }

        Scalar color2 = new Scalar(0, 255, 255);
        Scalar color_r = new Scalar(255,0,0);
        Scalar color_g= new Scalar(0,255,0);
        Scalar color_w=new Scalar(255,255,255);
        Scalar color_b=new Scalar(0,0,255);
        Imgproc.circle(rgba, p_b, 10, color_w, 1, 8, 0);
        Imgproc.circle(rgba, p_l, 10, color_r, 1, 8, 0);
        Imgproc.circle(rgba, p_r, 10, color_g, 1, 8, 0);
        Imgproc.circle(rgba, p_t, 10, color_b , 1, 8, 0);
//+++++++++++
        Point r_b,r_t,r_r,r_l;
        r_b = new Point();
        r_t = new Point();
        r_r = new Point();
        r_l = new Point();

        r_t.x = boundRect.br().x;
        r_t.y=boundRect.tl().y;

        r_r.x=boundRect.br().x;
        r_r.y=boundRect.br().y;

        r_b.x=boundRect.tl().x;
        r_b.y=boundRect.br().y;

        r_l.x=boundRect.tl().x;
        r_l.y=boundRect.tl().y;

        MatOfPoint2f initial_pts = new MatOfPoint2f(p_t, p_l, p_b, p_r);
        Imgproc.circle(drawing, r_t, 2, color2, 1, 8, 0);

        double d1 = dist(p_t.x,p_r.x,p_t.y,p_r.y);
        double d2 = dist(p_r.x,p_b.x,p_r.y,p_b.y);
        double d3 = dist(r_t.x,r_r.x,r_t.y,r_r.y);
        double d4 = dist(r_r.x,r_b.x,r_r.y,r_b.y);

        int ratio1 = (int)(d3/d4);
        int ratio2 = (int)(d1/d2);

        Log.d("ADebugTag", "Value: " + Double.toString(ratio1));
//      TODO Change this to an approximate ratio, int is an approximation, but still more approximate like
//      abs(ratio2-ratio1)<=2 or something
        java.util.ArrayList<Point> lp = new java.util.ArrayList<Point>(4);
        if(ratio2==ratio1) {
            lp.add(0, r_t);
            lp.add(1, r_l);
            lp.add(2, r_b);
            lp.add(3, r_r);
        } else {
            lp.add(0, r_t);
            lp.add(1, r_r);
            lp.add(2, r_b);
            lp.add(3, r_l);
        }

        Imgproc.circle(rgba, r_b, 10, color, 1, 8, 0);
        Imgproc.circle(rgba, r_l, 10, color, 1, 8, 0);
        Imgproc.circle(rgba, r_r, 10, color, 1, 8, 0);
        Imgproc.circle(rgba, r_t, 10, color, 1, 8, 0);

        MatOfPoint2f final_pts = new MatOfPoint2f();
        final_pts.fromList(lp);

//        Mat cropped = new Mat(rgba, boundRect);
        int top = (int)boundRect.tl().y;
        int h = (int)boundRect.height;
        int left = (int)boundRect.tl().x;
        int w = (int)boundRect.width;

        Mat im_transformed=rgba.submat(top, top+h, left, left+w);
        Mat image =  new Mat(im_transformed.rows(), im_transformed.cols(), im_transformed.type(), new Scalar(0,0,100));
        image.copyTo(im_transformed);

        Mat H = new Mat();
        H=Calib3d.findHomography(initial_pts, final_pts);
        //TODO doesn't work
        //Mat perspectiveTransform=new Mat();
//        perspectiveTransform=Imgproc.getPerspectiveTransform(initial_pts,final_pts);
        Mat src_mat=new Mat(4,1,CvType.CV_32FC2);
        Mat dst_mat=new Mat(4,1,CvType.CV_32FC2);


        src_mat.put(0,0,p_t.x,p_t.y,p_r.x,p_r.y,p_b.x,p_b.y,p_l.x,p_l.y);
        //0,0,407.0,74.0, 1606.0, 74.0, 420.0, 2589.0, 1698.0,2589.0);
        dst_mat.put(0,0,r_t.x,r_t.y,r_r.x,r_r.y,r_b.x,r_b.y,r_l.x,r_l.y);
        //0.0,0.0,1600.0,0.0, 0.0,2500.0,1600.0,2500.0);
        Mat perspectiveTransform=new Mat();
        perspectiveTransform=Imgproc.getPerspectiveTransform(src_mat, dst_mat);

        Mat dst=rgba.clone();

        Imgproc.warpPerspective(rgba, dst, perspectiveTransform,rgba.size());

        //  Imgproc.rectangle(rgba, r_t, r_b,color, 2);
        //Mat new_img= new Mat();
       //Imgproc.warpPerspective(rgba, new_img, H, rgba.size());

        return dst;//new_img;

    }
}
