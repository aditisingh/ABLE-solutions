Obj, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: erode(src, dst, kernel, anchor, iterations)
    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations)
    {
        
        erode_1(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations);
        
        return;
    }

    //javadoc: erode(src, dst, kernel)
    public static void erode(Mat src, Mat dst, Mat kernel)
    {
        
        erode_2(src.nativeObj, dst.nativeObj, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void dilate(Mat src, Mat& dst, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    //

    //javadoc: dilate(src, dst, kernel, anchor, iterations, borderType, borderValue)
    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
    {
        
        dilate_0(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: dilate(src, dst, kernel, anchor, iterations)
    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations)
    {
        
        dilate_1(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations);
        
        return;
    }

    //javadoc: dilate(src, dst, kernel)
    public static void dilate(Mat src, Mat dst, Mat kernel)
    {
        
        dilate_2(src.nativeObj, dst.nativeObj, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void morphologyEx(Mat src, Mat& dst, int op, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    //

    //javadoc: morphologyEx(src, dst, op, kernel, anchor, iterations, borderType, borderValue)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
    {
        
        morphologyEx_0(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: morphologyEx(src, dst, op, kernel, anchor, iterations)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations)
    {
        
        morphologyEx_1(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.x, anchor.y, iterations);
        
        return;
    }

    //javadoc: morphologyEx(src, dst, op, kernel)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel)
    {
        
        morphologyEx_2(src.nativeObj, dst.nativeObj, op, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void resize(Mat src, Mat& dst, Size dsize, double fx = 0, double fy = 0, int interpolation = INTER_LINEAR)
    //

    //javadoc: resize(src, dst, dsize, fx, fy, interpolation)
    public static void resize(Mat src, Mat dst, Size dsize, double fx, double fy, int interpolation)
    {
        
        resize_0(src.nativeObj, dst.nativeObj, dsize.width, dsize.height, fx, fy, interpolation);
        
        return;
    }

    //javadoc: resize(src, dst, dsize)
    public static void resize(Mat src, Mat dst, Size dsize)
    {
        
        resize_1(src.nativeObj, dst.nativeObj, dsize.width, dsize.height);
        
        return;
    }


    //
    // C++:  void warpAffine(Mat src, Mat& dst, Mat M, Size dsize, int flags = INTER_LINEAR, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    //

    //javadoc: warpAffine(src, dst, M, dsize, flags, borderMode, borderValue)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode, Scalar borderValue)
    {
        
        warpAffine_0(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: warpAffine(src, dst, M, dsize, flags)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags)
    {
        
        warpAffine_1(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags);
        
        return;
    }

    //javadoc: warpAffine(src, dst, M, dsize)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize)
    {
        
        warpAffine_2(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height);
        
        return;
    }


    //
    // C++:  void warpPerspective(Mat src, Mat& dst, Mat M, Size dsize, int flags = INTER_LINEAR, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    //

    //javadoc: warpPerspective(src, dst, M, dsize, flags, borderMode, borderValue)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode, Scalar borderValue)
    {
        
        warpPerspective_0(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: warpPerspective(src, dst, M, dsize, flags)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags)
    {
        
        warpPerspective_1(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags);
        
        return;
    }

    //javadoc: warpPerspective(src, dst, M, dsize)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize)
    {
        
        warpPerspective_2(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height);
        
        return;
    }


    //
    // C++:  void remap(Mat src, Mat& dst, Mat map1, Mat map2, int interpolation, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    //

    //javadoc: remap(src, dst, map1, map2, interpolation, borderMode, borderValue)
    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation, int borderMode, Scalar borderValue)
    {
        
        remap_0(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: remap(src, dst, map1, map2, interpolation)
    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation)
    {
        
        remap_1(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation);
        
        return;
    }


    //
    // C++:  void convertMaps(Mat map1, Mat map2, Mat& dstmap1, Mat& dstmap2, int dstmap1type, bool nninterpolation = false)
    //

    //javadoc: convertMaps(map1, map2, dstmap1, dstmap2, dstmap1type, nninterpolation)
    public static void convertMaps(Mat map1, Mat map2, Mat dstmap1, Mat dstmap2, int dstmap1type, boolean nninterpolation)
    {
        
        convertMaps_0(map1.nativeObj, map2.nativeObj, dstmap1.nativeObj, dstmap2.nativeObj, dstmap1type, nninterpolation);
        
        return;
    }

    //javadoc: convertMaps(map1, map2, dstmap1, dstmap2, dstmap1type)
    public static void convertMaps(Mat map1, Mat map2, Mat dstmap1, Mat dstmap2, int dstmap1type)
    {
        
        convertMaps_1(map1.nativeObj, map2.nativeObj, dstmap1.nativeObj, dstmap2.nativeObj, dstmap1type);
        
        return;
    }


    //
    // C++:  Mat getRotationMatrix2D(Point2f center, double angle, double scale)
    //

    //javadoc: getRotationMatrix2D(center, angle, scale)
    public static Mat getRotationMatrix2D(Point center, double angle, double scale)
    {
        
        Mat retVal = new Mat(getRotationMatrix2D_0(center.x, center.y, angle, scale));
        
        return retVal;
    }


    //
    // C++:  void invertAffineTransform(Mat M, Mat& iM)
    //

    //javadoc: invertAffineTransform(M, iM)
    public static void invertAffineTransform(Mat M, Mat iM)
    {
        
        invertAffineTransform_0(M.nativeObj, iM.nativeObj);
        
        return;
    }


    //
    // C++:  Mat getPerspectiveTransform(Mat src, Mat dst)
    //

    //javadoc: getPerspectiveTransform(src, dst)
    public static Mat getPerspectiveTransform(Mat src, Mat dst)
    {
        
        Mat retVal = new Mat(getPerspectiveTransform_0(src.nativeObj, dst.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat getAffineTransform(vector_Point2f src, vector_Point2f dst)
    //

    //javadoc: getAffineTransform(src, dst)
    public static Mat getAffineTransform(MatOfPoint2f src, MatOfPoint2f dst)
    {
        Mat src_mat = src;
        Mat dst_mat = dst;
        Mat retVal = new Mat(getAffineTransform_0(src_mat.nativeObj, dst_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  void getRectSubPix(Mat image, Size patchSize, Point2f center, Mat& patch, int patchType = -1)
    //

    //javadoc: getRectSubPix(image, patchSize, center, patch, patchType)
    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch, int patchType)
    {
        
        getRectSubPix_0(image.nativeObj, patchSize.width, patchSize.height, center.x, center.y, patch.nativeObj, patchType);
        
        return;
    }

    //javadoc: getRectSubPix(image, patchSize, center, patch)
    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch)
    {
        
        getRectSubPix_1(image.nativeObj, patchSize.width, patchSize.height, center.x, center.y, patch.nativeObj);
        
        return;
    }


    //
    // C++:  void logPolar(Mat src, Mat& dst, Point2f center, double M, int flags)
    //

    //javadoc: logPolar(src, dst, center, M, flags)
    public static void logPolar(Mat src, Mat dst, Point center, double M, int flags)
    {
        
        logPolar_0(src.nativeObj, dst.nativeObj, center.x, center.y, M, flags);
        
        return;
    }


    //
    // C++:  void linearPolar(Mat src, Mat& dst, Point2f center, double maxRadius, int flags)
    //

    //javadoc: linearPolar(src, dst, center, maxRadius, flags)
    public static void linearPolar(Mat src, Mat dst, Point center, double maxRadius, int flags)
    {
        
        linearPolar_0(src.nativeObj, dst.nativeObj, center.x, center.y, maxRadius, flags);
        
        return;
    }


    //
    // C++:  void adaptiveThreshold(Mat src, Mat& dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C)
    //

    //javadoc: adaptiveThreshold(src, dst, maxValue, adaptiveMethod, thresholdType, blockSize, C)
    public static void adaptiveThreshold(Mat src, Mat dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C)
    {
        
        adaptiveThreshold_0(src.nativeObj, dst.nativeObj, maxValue, adaptiveMethod, thresholdType, blockSize, C);
        
        return;
    }


    //
    // C++:  void pyrDown(Mat src, Mat& dst, Size dstsize = Size(), int borderType = BORDER_DEFAULT)
    //

    //javadoc: pyrDown(src, dst, dstsize, borderType)
    public static void pyrDown(Mat src, Mat dst, Size dstsize, int borderType)
    {
        
        pyrDown_0(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height, borderType);
        
        return;
    }

    //javadoc: pyrDown(src, dst, dstsize)
    public static void pyrDown(Mat src, Mat dst, Size dstsize)
    {
        
        pyrDown_1(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height);
        
        return;
    }

    //javadoc: pyrDown(src, dst)
    public static void pyrDown(Mat src, Mat dst)
    {
        
        pyrDown_2(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void pyrUp(Mat src, Mat& dst, Size dstsize = Size(), int borderType = BORDER_DEFAULT)
    //

    //javadoc: pyrUp(src, dst, dstsize, borderType)
    public static void pyrUp(Mat src, Mat dst, Size dstsize, int borderType)
    {
        
        pyrUp_0(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height, borderType);
        
        return;
    }

    //javadoc: pyrUp(src, dst, dstsize)
    public static void pyrUp(Mat src, Mat dst, Size dstsize)
    {
        
        pyrUp_1(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height);
        
        return;
    }

    //javadoc: pyrUp(src, dst)
    public static void pyrUp(Mat src, Mat dst)
    {
        
        pyrUp_2(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void undistort(Mat src, Mat& dst, Mat cameraMatrix, Mat distCoeffs, Mat newCameraMatrix = Mat())
    //

    //javadoc: undistort(src, dst, cameraMatrix, distCoeffs, newCameraMatrix)
    public static void undistort(Mat src, Mat dst, Mat cameraMatrix, Mat distCoeffs, Mat newCameraMatrix)
    {
        
        undistort_0(src.nativeObj, dst.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, newCameraMatrix.nativeObj);
        
        return;
    }

    //javadoc: undistort(src, dst, cameraMatrix, distCoeffs)
    public static void undistort(Mat src, Mat dst, Mat cameraMatrix, Mat distCoeffs)
    {
        
        undistort_1(src.nativeObj, dst.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj);
        
        return;
    }


    //
    // C++:  void initUndistortRectifyMap(Mat cameraMatrix, Mat distCoeffs, Mat R, Mat newCameraMatrix, Size size, int m1type, Mat& map1, Mat& map2)
    //

    //javadoc: initUndistortRectifyMap(cameraMatrix, distCoeffs, R, newCameraMatrix, size, m1type, map1, map2)
    public static void initUndistortRectifyMap(Mat cameraMatrix, Mat distCoeffs, Mat R, Mat newCameraMatrix, Size size, int m1type, Mat map1, Mat map2)
    {
        
        initUndistortRectifyMap_0(cameraMatrix.nativeObj, distCoeffs.nativeObj, R.nativeObj, newCameraMatrix.nativeObj, size.width, size.height, m1type, map1.nativeObj, map2.nativeObj);
        
        return;
    }


    //
    // C++:  float initWideAngleProjMap(Mat cameraMatrix, Mat distCoeffs, Size imageSize, int destImageWidth, int m1type, Mat& map1, Mat& map2, int projType = PROJ_SPHERICAL_EQRECT, double alpha = 0)
    //

    //javadoc: initWideAngleProjMap(cameraMatrix, distCoeffs, imageSize, destImageWidth, m1type, map1, map2, projType, alpha)
    public static float initWideAngleProjMap(Mat cameraMatrix, Mat distCoeffs, Size imageSize, int destImageWidth, int m1type, Mat map1, Mat map2, int projType, double alpha)
    {
        
        float retVal = initWideAngleProjMap_0(cameraMatrix.nativeObj, distCoeffs.nativeObj, imageSize.width, imageSize.height, destImageWidth, m1type, map1.nativeObj, map2.nativeObj, projType, alpha);
        
        return retVal;
    }

    //javadoc: initWideAngleProjMap(cameraMatrix, distCoeffs, imageSize, destImageWidth, m1type, map1, map2)
    public static float initWideAngleProjMap(Mat cameraMatrix, Mat distCoeffs, Size imageSize, int destImageWidth, int m1type, Mat map1, Mat map2)
    {
        
        float retVal = initWideAngleProjMap_1(cameraMatrix.nativeObj, distCoeffs.nativeObj, imageSize.width, imageSize.height, destImageWidth, m1type, map1.nativeObj, map2.nativeObj);
        
        return retVal;
    }


    //
    // C++:  Mat getDefaultNewCameraMatrix(Mat cameraMatrix, Size imgsize = Size(), bool centerPrincipalPoint = false)
    //

    //javadoc: getDefaultNewCameraMatrix(cameraMatrix, imgsize, centerPrincipalPoint)
    public static Mat getDefaultNewCameraMatrix(Mat cameraMatrix, Size imgsize, boolean centerPrincipalPoint)
    {
        
        Mat retVal = new Mat(getDefaultNewCameraMatrix_0(cameraMatrix.nativeObj, imgsize.width, imgsize.height, centerPrincipalPoint));
        
        return retVal;
    }

    //javadoc: getDefaultNewCameraMatrix(cameraMatrix)
    public static Mat getDefaultNewCameraMatrix(Mat cameraMatrix)
    {
        
        Mat retVal = new Mat(getDefaultNewCameraMatrix_1(cameraMatrix.nativeObj));
        
        return retVal;
    }


    //
    // C++:  void integral(Mat src, Mat& sum, int sdepth = -1)
    //

    //javadoc: integral(src, sum, sdepth)
    public static void integral(Mat src, Mat sum, int sdepth)
    {
        
        integral_0(src.nativeObj, sum.nativeObj, sdepth);
        
        return;
    }

    //javadoc: integral(src, sum)
    public static void integral(Mat src, Mat sum)
    {
        
        integral_1(src.nativeObj, sum.nativeObj);
        
        return;
    }


    //
    // C++:  void integral(Mat src, Mat& sum, Mat& sqsum, int sdepth = -1, int sqdepth = -1)
    //

    //javadoc: integral(src, sum, sqsum, sdepth, sqdepth)
    public static void integral2(Mat src, Mat sum, Mat sqsum, int sdepth, int sqdepth)
    {
        
        integral2_0(src.nativeObj, sum.nativeObj, sqsum.nativeObj, sdepth, sqdepth);
        
        return;
    }

    //javadoc: integral(src, sum, sqsum)
    public static void integral2(Mat src, Mat sum, Mat sqsum)
    {
        
        integral2_1(src.nativeObj, sum.nativeObj, sqsum.nativeObj);
        
        return;
    }


    //
    // C++:  void integral(Mat src, Mat& sum, Mat& sqsum, Mat& tilted, int sdepth = -1, int sqdepth = -1)
    //

    //javadoc: integral(src, sum, sqsum, tilted, sdepth, sqdepth)
    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted, int sdepth, int sqdepth)
    {
        
        integral3_0(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj, sdepth, sqdepth);
        
        return;
    }

    //javadoc: integral(src, sum, sqsum, tilted)
    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted)
    {
        
        integral3_1(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj);
        
        return;
    }


    //
    // C++:  void accumulate(Mat src, Mat& dst, Mat mask = Mat())
    //

    //javadoc: accumulate(src, dst, mask)
    public static void accumulate(Mat src, Mat dst, Mat mask)
    {
        
        accumulate_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulate(src, dst)
    public static void accumulate(Mat src, Mat dst)
    {
        
        accumulate_1(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void accumulateSquare(Mat src, Mat& dst, Mat mask = Mat())
    //

    //javadoc: accumulateSquare(src, dst, mask)
    public static void accumulateSquare(Mat src, Mat dst, Mat mask)
    {
        
        accumulateSquare_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulateSquare(src, dst)
    public static void accumulateSquare(Mat src, Mat dst)
    {
        
        accumulateSquare_1(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void accumulateProduct(Mat src1, Mat src2, Mat& dst, Mat mask = Mat())
    //

    //javadoc: accumulateProduct(src1, src2, dst, mask)
    public static void accumulateProduct(Mat src1, Mat src2, Mat dst, Mat mask)
    {
        
        accumulateProduct_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulateProduct(src1, src2, dst)
    public static void accumulateProduct(Mat src1, Mat src2, Mat dst)
    {
        
        accumulateProduct_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void accumulateWeighted(Mat src, Mat& dst, double alpha, Mat mask = Mat())
    //

    //javadoc: accumulateWeighted(src, dst, alpha, mask)
    public static void accumulateWeighted(Mat src, Mat dst, double alpha, Mat mask)
    {
        
        accumulateWeighted_0(src.nativeObj, dst.nativeObj, alpha, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulateWeighted(src, dst, alpha)
    public static void accumulateWeighted(Mat src, Mat dst, double alpha)
    {
        
        accumulateWeighted_1(src.nativeObj, dst.nativeObj, alpha);
        
        return;
    }


    //
    // C++:  Point2d phaseCorrelate(Mat src1, Mat src2, Mat window = Mat(), double* response = 0)
    //

    //javadoc: phaseCorrelate(src1, src2, window, response)
    public static Point phaseCorrelate(Mat src1, Mat src2, Mat window, double[] response)
    {
        double[] response_out = new double[1];
        Point retVal = new Point(phaseCorrelate_0(src1.nativeObj, src2.nativeObj, window.nativeObj, response_out));
        if(response!=null) response[0] = (double)response_out[0];
        return retVal;
    }

    //javadoc: phaseCorrelate(src1, src2)
    public static Point phaseCorrelate(Mat src1, Mat src2)
    {
        
        Point retVal = new Point(phaseCorrelate_1(src1.nativeObj, src2.nativeObj));
        
        return retVal;
    }


    //
    // C++:  void createHanningWindow(Mat& dst, Size winSize, int type)
    //

    //javadoc: createHanningWindow(dst, winSize, type)
    public static void createHanningWindow(Mat dst, Size winSize, int type)
    {
        
        createHanningWindow_0(dst.nativeObj, winSize.width, winSize.height, type);
        
        return;
    }


    //
    // C++:  double threshold(Mat src, Mat& dst, double thresh, double maxval, int type)
    //

    //javadoc: threshold(src, dst, thresh, maxval, type)
    public static double threshold(Mat src, Mat dst, double thresh, double maxval, int type)
    {
        
        double retVal = threshold_0(src.nativeObj, dst.nativeObj, thresh, maxval, type);
        
        return retVal;
    }


    //
    // C++:  void undistortPoints(vector_Point2f src, vector_Point2f& dst, Mat cameraMatrix, Mat distCoeffs, Mat R = Mat(), Mat P = Mat())
    //

    //javadoc: undistortPoints(src, dst, cameraMatrix, distCoeffs, R, P)
    public static void undistortPoints(MatOfPoint2f src, MatOfPoint2f dst, Mat cameraMatrix, Mat distCoeffs, Mat R, Mat P)
    {
        Mat src_mat = src;
        Mat dst_mat = dst;
        undistortPoints_0(src_mat.nativeObj, dst_mat.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, R.nativeObj, P.nativeObj);
        
        return;
    }

    //javadoc: undistortPoints(src, dst, cameraMatrix, distCoeffs)
    public static void undistortPoints(MatOfPoint2f src, MatOfPoint2f dst, Mat cameraMatrix, Mat distCoeffs)
    {
        Mat src_mat = src;
        Mat dst_mat = dst;
        undistortPoints_1(src_mat.nativeObj, dst_mat.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj);
        
        return;
    }


    //
    // C++:  void calcHist(vector_Mat images, vector_int channels, Mat mask, Mat& hist, vector_int histSize, vector_float ranges, bool accumulate = false)
    //

    //javadoc: calcHist(images, channels, mask, hist, histSize, ranges, accumulate)
    public static void calcHist(List<Mat> images, MatOfInt channels, Mat mask, Mat hist, MatOfInt histSize, MatOfFloat ranges, boolean accumulate)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat channels_mat = channels;
        Mat histSize_mat = histSize;
        Mat ranges_mat = ranges;
        calcHist_0(images_mat.nativeObj, channels_mat.nativeObj, mask.nativeObj, hist.nativeObj, histSize_mat.nativeObj, ranges_mat.nativeObj, accumulate);
        
        return;
    }

    //javadoc: calcHist(images, channels, mask, hist, histSize, ranges)
    public static void calcHist(List<Mat> images, MatOfInt channels, Mat mask, Mat hist, MatOfInt histSize, MatOfFloat ranges)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat channels_mat = channels;
        Mat histSize_mat = histSize;
        Mat ranges_mat = ranges;
        calcHist_1(images_mat.nativeObj, channels_mat.nativeObj, mask.nativeObj, hist.nativeObj, histSize_mat.nativeObj, ranges_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void calcBackProject(vector_Mat images, vector_int channels, Mat hist, Mat& dst, vector_float ranges, double scale)
    //

    //javadoc: calcBackProject(images, channels, hist, dst, ranges, scale)
    public static void calcBackProject(List<Mat> images, MatOfInt channels, Mat hist, Mat dst, MatOfFloat ranges, double scale)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat channels_mat = channels;
        Mat ranges_mat = ranges;
        calcBackProject_0(images_mat.nativeObj, channels_mat.nativeObj, hist.nativeObj, dst.nativeObj, ranges_mat.nativeObj, scale);
        
        return;
    }


    //
    // C++:  double compareHist(Mat H1, Mat H2, int method)
    //

    //javadoc: compareHist(H1, H2, method)
    public static double compareHist(Mat H1, Mat H2, int method)
    {
        
        double retVal = compareHist_0(H1.nativeObj, H2.nativeObj, method);
        
        return retVal;
    }


    //
    // C++:  void equalizeHist(Mat src, Mat& dst)
    //

    //javadoc: equalizeHist(src, dst)
    public static void equalizeHist(Mat src, Mat dst)
    {
        
        equalizeHist_0(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void watershed(Mat image, Mat& markers)
    //

    //javadoc: watershed(image, markers)
    public static void watershed(Mat image, Mat markers)
    {
        
        watershed_0(image.nativeObj, markers.nativeObj);
        
        return;
    }


    //
    // C++:  void pyrMeanShiftFiltering(Mat src, Mat& dst, double sp, double sr, int maxLevel = 1, TermCriteria termcrit = TermCriteria(TermCriteria::MAX_ITER+TermCriteria::EPS,5,1))
    //

    //javadoc: pyrMeanShiftFiltering(src, dst, sp, sr, maxLevel, termcrit)
    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr, int maxLevel, TermCriteria termcrit)
    {
        
        pyrMeanShiftFiltering_0(src.nativeObj, dst.nativeObj, sp, sr, maxLevel, termcrit.type, termcrit.maxCount, termcrit.epsilon);
        
        return;
    }

    //javadoc: pyrMeanShiftFiltering(src, dst, sp, sr)
    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr)
    {
        
        pyrMeanShiftFiltering_1(src.nativeObj, dst.nativeObj, sp, sr);
        
        return;
    }


    //
    // C++:  void grabCut(Mat img, Mat& mask, Rect rect, Mat& bgdModel, Mat& fgdModel, int iterCount, int mode = GC_EVAL)
    //

    //javadoc: grabCut(img, mask, rect, bgdModel, fgdModel, iterCount, mode)
    public static void grabCut(Mat img, Mat mask, Rect rect, Mat bgdModel, Mat fgdModel, int iterCount, int mode)
    {
        
        grabCut_0(img.nativeObj, mask.nativeObj, rect.x, rect.y, rect.width, rect.height, bgdModel.nativeObj, fgdModel.nativeObj, iterCount, mode);
        
        return;
    }

    //javadoc: grabCut(img, mask, rect, bgdModel, fgdModel, iterCount)
    public static void grabCut(Mat img, Mat mask, Rect rect, Mat bgdModel, Mat fgdModel, int iterCount)
    {
        
        grabCut_1(img.nativeObj, mask.nativeObj, rect.x, rect.y, rect.width, rect.height, bgdModel.nativeObj, fgdModel.nativeObj, iterCount);
        
        return;
    }


    //
    // C++:  void distanceTransform(Mat src, Mat& dst, Mat& labels, int distanceType, int maskSize, int labelType = DIST_LABEL_CCOMP)
    //

    //javadoc: distanceTransform(src, dst, labels, distanceType, maskSize, labelType)
    public static void distanceTransformWithLabels(Mat src, Mat dst, Mat labels, int distanceType, int maskSize, int labelType)
    {
        
        distanceTransformWithLabels_0(src.nativeObj, dst.nativeObj, labels.nativeObj, distanceType, maskSize, labelType);
        
        return;
    }

    //javadoc: distanceTransform(src, dst, labels, distanceType, maskSize)
    public static void distanceTransformWithLabels(Mat src, Mat dst, Mat labels, int distanceType, int maskSize)
    {
        
        distanceTransformWithLabels_1(src.nativeObj, dst.nativeObj, labels.nativeObj, distanceType, maskSize);
        
        return;
    }


    //
    // C++:  void distanceTransform(Mat src, Mat& dst, int distanceType, int maskSize, int dstType = CV_32F)
    //

    //javadoc: distanceTransform(src, dst, distanceType, maskSize, dstType)
    public static void distanceTransform(Mat src, Mat dst, int distanceType, int maskSize, int dstType)
    {
        
        distanceTransform_0(src.nativeObj, dst.nativeObj, distanceType, maskSize, dstType);
        
        return;
    }

    //javadoc: distanceTransform(src, dst, distanceType, maskSize)
    public static void distanceTransform(Mat src, Mat dst, int distanceType, int maskSize)
    {
        
        distanceTransform_1(src.nativeObj, dst.nativeObj, distanceType, maskSize);
        
        return;
    }


    //
    // C++:  int floodFill(Mat& image, Mat& mask, Point seedPoint, Scalar newVal, Rect* rect = 0, Scalar loDiff = Scalar(), Scalar upDiff = Scalar(), int flags = 4)
    //

    //javadoc: floodFill(image, mask, seedPoint, newVal, rect, loDiff, upDiff, flags)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect, Scalar loDiff, Scalar upDiff, int flags)
    {
        double[] rect_out = new double[4];
        int retVal = floodFill_0(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3], rect_out, loDiff.val[0], loDiff.val[1], loDiff.val[2], loDiff.val[3], upDiff.val[0], upDiff.val[1], upDiff.val[2], upDiff.val[3], flags);
        if(rect!=null){ rect.x = (int)rect_out[0]; rect.y = (int)rect_out[1]; rect.width = (int)rect_out[2]; rect.height = (int)rect_out[3]; } 
        return retVal;
    }

    //javadoc: floodFill(image, mask, seedPoint, newVal)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal)
    {
        
        int retVal = floodFill_1(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3]);
        
        return retVal;
    }


    //
    // C++:  void cvtColor(Mat src, Mat& dst, int code, int dstCn = 0)
    //

    //javadoc: cvtColor(src, dst, code, dstCn)
    public static void cvtColor(Mat src, Mat dst, int code, int dstCn)
    {
        
        cvtColor_0(src.nativeObj, dst.nativeObj, code, dstCn);
        
        return;
    }

    //javadoc: cvtColor(src, dst, code)
    public static void cvtColor(Mat src, Mat dst, int code)
    {
        
        cvtColor_1(src.nativeObj, dst.nativeObj, code);
        
        return;
    }


    //
    // C++:  void demosaicing(Mat _src, Mat& _dst, int code, int dcn = 0)
    //

    //javadoc: demosaicing(_src, _dst, code, dcn)
    public static void demosaicing(Mat _src, Mat _dst, int code, int dcn)
    {
        
        demosaicing_0(_src.nativeObj, _dst.nativeObj, code, dcn);
        
        return;
    }

    //javadoc: demosaicing(_src, _dst, code)
    public static void demosaicing(Mat _src, Mat _dst, int code)
    {
        
        demosaicing_1(_src.nativeObj, _dst.nativeObj, code);
        
        return;
    }


    //
    // C++:  Moments moments(Mat array, bool binaryImage = false)
    //

    // Return type 'Moments' is not supported, skipping the function


    //
    // C++:  void HuMoments(Moments m, Mat& h