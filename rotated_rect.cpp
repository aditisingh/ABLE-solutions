#include "opencv2/opencv.hpp"
#include "opencv2/highgui.hpp"

// std::vector<cv::Rect> detectWords(cv::Mat img)
// {
//     std::vector<cv::Rect> boundRect;
//     cv::Mat img_gray, img_sobel, img_threshold, element;
//     element = getStructuringElement(cv::MORPH_RECT, cv::Size(17, 3) );
//     cv::morphologyEx(img, img_threshold, CV_MOP_CLOSE, element);
//     cv::imshow("TEMP", img_threshold);

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

int main(int argc,char** argv)
{
    //Read
    cv::Mat img1=cv::imread("test_out.bmp");

    //Detect
    std::vector<cv::Rect> boundRect;
    cv::Mat img_gray, img_sobel, img_threshold, element;

    element = getStructuringElement(cv::MORPH_RECT, cv::Size(7, 1) );
    cv::morphologyEx(img1, img_threshold, CV_MOP_CLOSE, element);
    cv::imshow("TEMP", img_threshold);
    cvWaitKey();
    // std::vector< std::vector< cv::Point> > contours;
    // cv::findContours(img_threshold, contours, 0, 1); 
    // std::vector<std::vector<cv::Point> > contours_poly( contours.size() );
    // for( int i = 0; i < contours.size(); i++ )
    //     if (contours[i].size()>100)
    //     { 
    //         cv::approxPolyDP( cv::Mat(contours[i]), contours_poly[i], 3, true );
    //         cv::Rect appRect( boundingRect( cv::Mat(contours_poly[i]) ));
    //         if (appRect.width>appRect.height) 
    //             boundRect.push_back(appRect);
    //     }

    // //Display
    // for(int i=0; i< boundRect.size(); i++)
    //     cv::rectangle(img1,boundRect[i],cv::Scalar(0,255,0),3,8,0);
    // cv::imwrite( "imgOut1.jpg", img1);  
    return 0;
}
