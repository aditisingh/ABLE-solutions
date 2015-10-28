#include <iostream>
#include "opencv/cv.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <math.h>
#include <ctime>
// #include <tesseract/baseapi.h>
// #include <tesseract/strngs.h>
#include <string> 

// #define image_height 1836
// #define image_width 3264

 using namespace std;
 using namespace cv;

RNG rng(12345);

float dist(int x1, int x2, int y1, int y2)
{
  return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
}

int main( int argc, char** argv )
{
clock_t t1,t2;
t1=clock();

// cvUseOptimized(true);

double alpha;
int beta;

Mat src; 
Mat gray;
Mat binary, label_image;

src=imread(argv[1]);
 namedWindow("input",CV_WINDOW_NORMAL);
imshow("input",src);
Mat org;
src.copyTo(org);
Mat new_image = Mat::zeros( src.size(), src.type() );



cvtColor(src,gray,CV_BGR2GRAY);
namedWindow("gray",CV_WINDOW_NORMAL);
imshow("gray",gray);

Mat gray_dst;
// cout<<gray.depth()<<endl;
gray.convertTo(gray,CV_32FC1);
pow(gray,0.7,gray_dst);
gray_dst.convertTo(gray_dst,CV_8UC1);
// cout<<gray_dst.rows<<" "<<gray_dst.cols<<endl;
Mat output;

Mat detected_edges;
// convertScaleAbs(gray_dst,output);
namedWindow("gamma1",CV_WINDOW_NORMAL);
imshow("gamma1",gray_dst);//output);
// cv::threshold(gray_dst, detected_edges, 0, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
 adaptiveThreshold(gray_dst, detected_edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 25, 2);


 namedWindow("threshold1",CV_WINDOW_NORMAL);
 imshow("threshold1",detected_edges);

t2=clock();
cout<<"time"<<((float)(t2-t1))/CLOCKS_PER_SEC;
cvWaitKey();
return 0;


}

    

