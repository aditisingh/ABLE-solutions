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

//  std::vector<cv::Rect> detectLetters(cv::Mat img)
// {
//     std::vector<cv::Rect> boundRect;
//     cv::Mat img_gray, img_sobel, img_threshold, element;
//     cvtColor(img, img_gray, CV_BGR2GRAY);
//     imshow("img_gray",img_gray);

//     cv::Sobel(img_gray, img_sobel, CV_8U, 1, 0, 3, 1, 0, cv::BORDER_DEFAULT);
//     imshow("img_sobel",img_sobel);

//     cv::threshold(img_sobel, img_threshold, 0, 255, CV_THRESH_OTSU+CV_THRESH_BINARY);
//     imshow("img_threshold",img_threshold);
    
//     element = getStructuringElement(cv::MORPH_RECT, cv::Size(17, 3) );
//     cv::morphologyEx(img_threshold, img_threshold, CV_MOP_CLOSE, element); //Does the trick
//     std::vector< std::vector< cv::Point> > contours;
//     cv::findContours(img_threshold, contours, 0, 1); 
//     std::vector<std::vector<cv::Point> > contours_poly( contours.size() );
//     for( int i = 0; i < contours.size(); i++ )
//         if (contours[i].size()>100)
//         { 
//             cv::approxPolyDP( cv::Mat(contours[i]), contours_poly[i], 3, true );
//             cv::Rect appRect( boundingRect( cv::Mat(contours_poly[i]) ));
//             if (appRect.width>appRect.height) 
//                 boundRect.push_back(appRect);
//         }
//     return boundRect;
// }

// Mat correctGamma( Mat& img, double gamma ) {
//  double inverse_gamma = 1.0 / gamma;

//  Mat lut_matrix(1, 256, CV_8UC1 );
//  uchar * ptr = lut_matrix.ptr();
//  for( int i = 0; i < 256; i++ )
//    ptr[i] = (int)( pow( (double) i / 255.0, inverse_gamma ) * 255.0 );

//  Mat result;
//  LUT( img, lut_matrix, result );

//  return result;
// }

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


// for( int y = 0; y < src.rows; y++ )
//    { for( int x = 0; x < src.cols; x++ )
//         { for( int c = 0; c < 3; c++ )
//              { new_image.at<Vec3b>(y,x)[c] =
//                          saturate_cast<uchar>( alpha*( src.at<Vec3b>(y,x)[c] ) + beta ); }
//    }
//    }

// char* cnt=argv[2];
// imshow("enhanced",new_image);
// std::vector<cv::Rect> letterBBoxes=detectLetters(src);
// for(int i=0; i< letterBBoxes.size(); i++)
//         cv::rectangle(src,letterBBoxes[i],cv::Scalar(0,255,0),3,8,0);
// imshow("boxes",src);  
// char name[25]="Output/";
// strcat(name,argv[2]);
// imwrite(name,src);

// Mat hsv;
//imshow("input",org);

//improving contrast 
//not very great improvements, but noise also added
/*
for(int x_coor=0;x_coor<src.cols;x_coor++) {
     for(int y_coor=0;y_coor<src.rows;y_coor++) {
         if(org.at<Vec3b>(y_coor,x_coor)[0]>180 && org.at<Vec3b>(y_coor,x_coor)[1]>180 && org.at<Vec3b>(y_coor,x_coor)[2]>180)
         	              org.at<Vec3b>(y_coor,x_coor)[1]=255,org.at<Vec3b>(y_coor,x_coor)[2]=255, org.at<Vec3b>(y_coor,x_coor)[0]=255;

         if(org.at<Vec3b>(y_coor,x_coor)[0]<50 && org.at<Vec3b>(y_coor,x_coor)[1] <50 && org.at<Vec3b>(y_coor,x_coor)[2] <50)
              org.at<Vec3b>(y_coor,x_coor)[1]=0,org.at<Vec3b>(y_coor,x_coor)[2]=0, org.at<Vec3b>(y_coor,x_coor)[0]=0;
     }
 }
 imshow("improved_contrast",org);
 std::vector<cv::Rect> letterBBoxes_1=detectLetters(org);
for(int i=0; i< letterBBoxes_1.size(); i++)
        cv::rectangle(org,letterBBoxes_1[i],cv::Scalar(255,0,0),3,8,0);
 imshow("boxes_new",org);
*/


// Mat kernel = Mat::ones(Size(2,2), CV_8U);
// Mat kernel_b = Mat::ones(Size(3,3), CV_8U);
// Mat kernel_s = Mat::ones(Size(1,1), CV_8U);
// Mat hist_gray, new_gray;
// // cvtColor(hsv,conv,CV_HSV2BGR);
// //char s[100];
cvtColor(src,gray,CV_BGR2GRAY);
namedWindow("gray",CV_WINDOW_NORMAL);
imshow("gray",gray);

//blurring
GaussianBlur(gray,gray,Size(3,3),0,0);
namedWindow("blurred",CV_WINDOW_NORMAL);
imshow("blurred",gray);
//detecting edges
int ratio=3, kernel_size=3;
int lowThreshold=20;

int scale = 1;
int delta = 0;
int ddepth = CV_16S;
// Mat detected_edges;
// cv::threshold(gray, detected_edges, 0, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
Mat grad_x, grad_y;
Mat abs_grad_x, abs_grad_y,grad;

/// Gradient X
Sobel( gray, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT );
/// Gradient Y
Sobel( gray, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT );

convertScaleAbs( grad_x, abs_grad_x );
convertScaleAbs( grad_y, abs_grad_y );
addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );

namedWindow("sobel",CV_WINDOW_NORMAL);
imshow("sobel",grad);

cv::threshold(grad, grad, 0, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
namedWindow("grad binary",CV_WINDOW_NORMAL);
imshow("grad binary",grad);
// 
double largest_area=0;
int largest_contour_index=0;
//now detect contours
vector<vector<Point> > contours;
vector<Vec4i> hierarchy;
findContours( grad, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );

//   /// Draw contours
  Mat drawing = Mat::zeros( grad.size(), CV_8UC3 );
  Point p_r,p_l,p_t,p_b;
  for( int i = 0; i< contours.size(); i++ )
     {
       double a=contourArea( contours[i],false);  //  Find the area of contour
       if(a>largest_area){
       largest_area=a;
       largest_contour_index=i;                //Store the index of largest contour
       // bounding_rect=boundingRect(contours[i]); // Find the bounding rectangle for biggest contour
       }
     }
// 
  p_r.x=p_l.x=p_t.x=p_b.x=contours[largest_contour_index][1].x;
  p_r.y=p_l.y=p_t.y=p_b.y=contours[largest_contour_index][1].y;

  vector<Point> contours_poly;
  Rect boundRect;
  /// Show in a window
  // Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );
  approxPolyDP(contours[largest_contour_index], contours_poly, 3, true);
  boundRect = boundingRect( contours_poly );
  drawContours( drawing, contours, largest_contour_index,Scalar(0,0,255), 1, 8, vector<Vec4i>(), 0, Point() );

  rectangle(drawing,boundRect.tl(),boundRect.br(),Scalar(255,0,0),1,8,0);
  


//find the points to be transformed
for(vector<Point>::iterator it=contours[largest_contour_index].begin();it!=contours[largest_contour_index].end();++it)
{
  if((*it).x>=p_l.x)
    {
      p_l.x=(*it).x;
      p_l.y=(*it).y;
    }
    if((*it).x<=p_r.x)
    {
      p_r.x=(*it).x;
      p_r.y=(*it).y;
    }
    if((*it).y>=p_b.y)
    {
      p_b.x=(*it).x;
      p_b.y=(*it).y;
    }
    if((*it).y<=p_t.y)
    {
      p_t.x=(*it).x;
      p_t.y=(*it).y;
    }
}

// cout<<p_b<<endl<<p_t<<endl<<p_r<<endl<<p_l<<endl; //these can be improved by more constraints, but shud be??
// circle(drawing,p_b,2,Scalar(0,255,255),1,8,0);
// circle(drawing,p_l,2,Scalar(0,255,255),1,8,0);
// circle(drawing,p_r,2,Scalar(0,255,255),1,8,0);
// circle(drawing,p_t,2,Scalar(0,255,255),1,8,0);


cout<<boundRect.tl()<<endl;
cout<<boundRect.br()<<endl;

Point r_b,r_t,r_r,r_l;

r_t.x=boundRect.br().x,r_t.y=boundRect.tl().y;
r_r.x=boundRect.br().x,r_r.y=boundRect.br().y;
r_b.x=boundRect.tl().x,r_b.y=boundRect.br().y;
r_l.x=boundRect.tl().x,r_l.y=boundRect.tl().y;

vector<Point2f> initial;
vector<Point2f> final;

circle(drawing,r_t,2,Scalar(0,255,255),1,8,0);

namedWindow( "Contours", CV_WINDOW_NORMAL);
imshow( "Contours", drawing );

initial.push_back(p_t);
initial.push_back(p_l);
initial.push_back(p_b);
initial.push_back(p_r);

Rect ROI(boundRect.tl().x,boundRect.tl().y,abs(boundRect.br().x-boundRect.tl().x),abs(boundRect.br().y-boundRect.tl().y));
float d1=dist(p_t.x,p_r.x,p_t.y,p_r.y);
float d2=dist(p_r.x,p_b.x,p_r.y,p_b.y);

float d3=dist(r_t.x,r_r.x,r_t.y,r_r.y);
float d4=dist(r_r.x,r_b.x,r_r.y,r_b.y);

cout<<float(d3/d4)<<endl<<float(d1/d2)<<endl;

int ratio1=d3/d4;
int ratio2=d1/d2;


if(ratio2==ratio1)
{
final.push_back(r_t);
final.push_back(r_l);
final.push_back(r_b);
final.push_back(r_r);
}
else
{
final.push_back(r_t);
final.push_back(r_r);
final.push_back(r_b);
final.push_back(r_l);
}

Mat im_transformed;
im_transformed=src;

Mat H=findHomography(initial,final);
warpPerspective(src,im_transformed,H,im_transformed.size());

namedWindow( "transformed", CV_WINDOW_NORMAL);
imshow( "transformed", im_transformed );

// cout<<endl<<boundRect.br().x-boundRect.tl().x;
Mat cropped;
im_transformed(ROI).copyTo(cropped);
// cout<<ROI.size()<<endl;
// Mat cropped=im_transformed(Rect(boundRect.tl().x,boundRect.tl().y,abs(boundRect.br().x-boundRect.tl().x),abs(boundRect.br().y-boundRect.br().y)));
// Mat cropped;
// cout<<endl<<boundRect.br().x-boundRect.tl().x;
// croppedref.copyTo(cropped);
// cout<<cropped;
namedWindow("cropped",CV_WINDOW_NORMAL);
imshow("cropped",cropped);

cout<<"here"<<endl;
//
t2=clock();
cout<<"time"<<((float)(t2-t1))/CLOCKS_PER_SEC;
cvWaitKey();
return 0;


}

    

