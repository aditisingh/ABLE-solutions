#include <iostream>
#include "opencv/cv.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <math.h>
#include <ctime>
// #include <tesseract/baseapi.h>
// #include <tesseract/strngs.h>
#include <string> 
#include <sstream>

// #define image_height 1836
// #define image_width 3264

 using namespace std;
 using namespace cv;

RNG rng(12345);

double computeProduct(Point p, Point2f a, Point2f b)
{
    double k = (a.y-b.y) / (a.x-b.x);
    double j = a.y - k*a.x;
    return k*p.x - p.y + j;
}

bool isInROI(Point p, Point2f roi[])
{   
    double pro[4];
    for(int i=0; i<4; ++i)
    {
        pro[i] = computeProduct(p, roi[i], roi[(i+1)%4]);
    }
    if(pro[0]*pro[2]<0 && pro[1]*pro[3]<0)
    {
        return false;
    }
    return true;
}

/** function pro = kx-y+j, take two points a and b,
*** compute the line argument k and j, then return the pro value
*** so that can be used to determine whether the point p is on the left or right
*** of the line ab
**/


 // Give a binary image and get back blobs from that
std::vector <std::vector<cv::Point> > blob_detection(cv::Mat binary_image, int min_blob_size) {
  std::vector <std::vector<cv::Point> > blobs;
  blobs.clear();

  // Fill the label_image with the blobs
  // 0  - background
  // 1  - unlabelled foreground
  // 2+ - labelled foreground

  // input is a binary image therefore values are either 0 or 1
  // out objective is to find a set of 1's that are together and assign 2 to it
  // then look for other 1's, and assign 3 to it....so on a soforth

  cv::Mat label_image;
  binary_image.convertTo(label_image, CV_32FC1); // weird it doesn't support CV_32S! Because the CV::SCALAR is a double value in the function floodfill

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
  for(int y=0; y < binary_image.rows; y++) {
      for(int x=0; x < binary_image.cols; x++) {
          float checker = label_image.at<float>(y,x); //need to look for float and not int as the scalar value is of type double
          cv::Rect rect;
          int size = 0;
          // cout <<checker<<endl;
          if(checker == 0) {
              //fill region from a point
              cv::floodFill(label_image, cv::Point(x,y), cv::Scalar(label_count), &rect, cv::Scalar(0), cv::Scalar(0), 4);
              label_count++;
              // cout << label_image << endl <<"by checking: " << label_image.at<float>(y,x) <<endl;
              //cout << label_image;

              //a vector of all points in a blob
              std::vector<cv::Point> blob;

              for(int i=rect.y; i < (rect.y+rect.height); i++) {
                for(int j=rect.x; j < (rect.x+rect.width); j++) {
                    float chk = label_image.at<float>(i,j);
                    // std::cout << chk << std::endl;
                    if(chk == label_count-1) {
                      blob.push_back(cv::Point(j,i));
                      size++;
                    }                       
                }
              }
              if (size > min_blob_size) {
                //place the points of a single blob in a grouping
                //a vector of vector points
                blobs.push_back(blob);
                // circle(binary_image, *blob.begin(), 10, Scalar(255), 5);
            }
          }
      }
  }

  return blobs;
 }

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

// char p[100];

// sprintf(p,"convert %s -units PixelsPerInch -density 300 input.jpg", argv[1]);
// system(p);
src=imread(argv[1]);
// resize(src,src,Size(src.cols/4,src.rows/4));
// std::cout<<" Basic Linear Transforms "<<std::endl;
//  std::cout<<"-------------------------"<<std::endl;
//  std::cout<<"* Enter the alpha value [1.0-3.0]: ";std::cin>>alpha;
//  std::cout<<"* Enter the beta value [0-100]: "; std::cin>>beta;
 namedWindow("input",CV_WINDOW_NORMAL);
imshow("input",src);
Mat org;
src.copyTo(org);
Mat new_image = Mat::zeros( src.size(), src.type() );



cvtColor(src,gray,CV_BGR2GRAY);
namedWindow("gray",CV_WINDOW_NORMAL);
imshow("gray",gray);

//blurring
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

 Mat bw1;
detected_edges.copyTo(bw1);
Mat erode_dst;

//     Mat img_threshold,boundRect,element;
   Mat  element = getStructuringElement(cv::MORPH_RECT, cv::Size(50, 5) );

    erode( detected_edges, detected_edges, element );
      dilate(detected_edges,erode_dst,element);
     namedWindow("ting",CV_WINDOW_NORMAL);
 imshow("ting",erode_dst);

Mat eroded=erode_dst;
 // floodFill(erode_dst, cv::Point(0,0), CV_RGB(255,255,255));
// bitwise_not(erode_dst,erode_dst);
Mat bw=(erode_dst | eroded);
 // Mat bw=imfill(erode_dst,'holes');
 namedWindow("ting1",CV_WINDOW_NORMAL);
 imshow("ting1",bw);

vector <vector<Point> >  blobs;
blobs=blob_detection(bw,200);
Mat binary_image=src;
RotatedRect rect_im;

Mat rectangles=src;
// cout<<rectangles.rows<<" "<<rectangles.cols<<endl;
Rect rect_bound;
int num_blob=1;

for(vector<vector<Point> >::iterator it=blobs.begin(); it!=blobs.end();it++)
{
	Point2f rect_points[4];
	rect_im=minAreaRect(*it);
  rect_bound=rect_im.boundingRect();
	rect_im.points(rect_points);
  if(rect_im.size.width>rect_im.size.height)
  {
    num_blob++;
for( int j = 0; j < 4; j++ )
  line( rectangles, rect_points[j], rect_points[(j+1)%4], Scalar(0,0,255), 10, 8 );
  // cout<<rect_im.size.width<<" "<<rect_im.size.height<<endl;
  rectangle(rectangles,rect_bound,Scalar(255,0,0));
  Point center=rect_bound.tl();
  namedWindow("bw1",CV_WINDOW_NORMAL);
  imshow("bw1",bw1);
  cout<<bw1.rows<<" "<<bw1.cols<<endl;

  
    Mat mask = Mat(bw1.size(), CV_8U, Scalar(255));
for(int i=0; i<bw1.rows; ++i)
{
    for(int j=0; j<bw1.cols; ++j)
    {
        Point p = Point(j,i);   
        if(isInROI(p,rect_points))
            mask.at<uchar>(i,j) = 255;
        else
            mask.at<uchar>(i,j)=bw1.at<uchar>(i,j);
    }
}

Rect r  = Rect(center.x-10,center.y,rect_bound.width+30,rect_bound.height);
    Mat roi_ = mask(r);

  namedWindow("roi",CV_WINDOW_NORMAL);
  imshow("roi",roi_);

  namedWindow("mask",CV_WINDOW_NORMAL);
  imshow("mask",mask);

    cv::Mat rot_mat = cv::getRotationMatrix2D(rect_im.center, rect_im.angle, 1);
    cout<<rect_bound.width<<endl;
    cout<<rot_mat<<endl;
    cv::Mat rotated(rect_bound.width,rect_bound.height,CV_8UC1,Scalar(255));
    roi_.copyTo(rotated);
  cv::warpAffine(rotated, rotated, rot_mat, rotated.size(), cv::INTER_CUBIC);
  namedWindow("warped",CV_WINDOW_NORMAL);
imshow("warped",rotated);
ostringstream oss;
oss<<"warped"<<num_blob<<".bmp";
imwrite(oss.str(),rotated);
cvWaitKey();
}
}

// cout<<rectangles.rows<<" "<<rectafngles.cols<<endl;
namedWindow("binary_image",CV_WINDOW_NORMAL);
imshow("binary_image",rectangles);

t2=clock();
cout<<"time"<<((float)(t2-t1))/CLOCKS_PER_SEC;
cvWaitKey();
return 0;


}

    

