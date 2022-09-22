package org.firstinspires.ftc.teamcode.easyopencv;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

//class Yellow_Detection extends OpenCvPipeline {
//    Mat imgHSV = new Mat();
//    Mat thresholdMat0 = new Mat();
//    Mat thresholdMat1 = new Mat();
//    Mat thresholdMat = new Mat();
//    Mat HSLchan0Mat = new Mat();
//    Mat HSLchan1Mat = new Mat();
//    Mat thresholdMatS = new Mat();
//    Mat thresholdMatH = new Mat();
//    Mat contoursOnFrameMat = new Mat();
//    List<MatOfPoint> contoursList = new ArrayList<>();
//    int numContoursFound;
//
//    double hHSVAverage = 0;
//    double sHSVAverage = 0;
//    double vHSVAverage = 0;
//
//    enum Stage {
//        THRESHOLD,
//        RAW_IMAGE,
//    }
//
//    private Stage stageToRenderToViewport = Stage.THRESHOLD;
//    private Stage[] stages = Stage.values();
//
//    private Telemetry telemetry;
//
//    public Yellow_Detection(Telemetry telemetry) {
//        this.telemetry = telemetry;
//    }
//
//    @Override
//    public void onViewportTapped() {
//        /*
//         * Note that this method is invoked from the UI thread
//         * so whatever we do here, we must do quickly.
//         */
//
//        int currentStageNum = stageToRenderToViewport.ordinal();
//
//        int nextStageNum = currentStageNum + 1;
//
//        if (nextStageNum >= stages.length) {
//            nextStageNum = 0;
//        }
//
//        stageToRenderToViewport = stages[nextStageNum];
//    }
//
//    @Override
//    public Mat processFrame(Mat input) {
//        Imgproc.GaussianBlur(input, input, new Size(5, 5), 20);
//        Imgproc.cvtColor(input, imgHSV, Imgproc.COLOR_RGB2HSV);
//        Size size = input.size();
//        double[] pixel1 = imgHSV.get((int) size.height / 2, (int) size.width / 2);
//        double[] pixel2 = imgHSV.get((int) size.height / 2 + 1, (int) size.width / 2);
//        double[] pixel3 = imgHSV.get((int) size.height / 2 + 1, (int) size.width / 2 + 1);
//        double[] pixel4 = imgHSV.get((int) size.height / 2, (int) size.width / 2 + 1);
//
//
//        if (stageToRenderToViewport == Stage.THRESHOLD) {
//            hHSVAverage = (pixel1[0] + pixel2[0] + pixel3[0] + pixel4[0]) / 4;
//            sHSVAverage = (pixel1[1] + pixel2[1] + pixel3[1] + pixel4[1]) / 4;
//            vHSVAverage = (pixel1[2] + pixel2[2] + pixel3[2] + pixel4[2]) / 4;
//        }
//        telemetry.addData("average", "[%.2f, %.2f, %.2f]", hHSVAverage, sHSVAverage, vHSVAverage);
//        contoursList.clear();
//
//        Imgproc.cvtColor(input, imgHSV, Imgproc.COLOR_RGB2HSV);
//        Core.extractChannel(imgHSV, HSLchan0Mat, 0);
//        Core.extractChannel(imgHSV, HSLchan1Mat, 1);
//        Imgproc.threshold(HSLchan0Mat, thresholdMat0, hHSVAverage - 30, 255, Imgproc.THRESH_BINARY);
//        Imgproc.threshold(HSLchan0Mat, thresholdMat1, hHSVAverage + 30, 255, Imgproc.THRESH_BINARY_INV);
//        Core.bitwise_and(thresholdMat0, thresholdMat1, thresholdMatH);
//        Imgproc.threshold(HSLchan1Mat, thresholdMat0, sHSVAverage - 30, 255, Imgproc.THRESH_BINARY);
//        Imgproc.threshold(HSLchan1Mat, thresholdMat1, sHSVAverage + 30, 255, Imgproc.THRESH_BINARY_INV);
//        Core.bitwise_and(thresholdMat0, thresholdMat1, thresholdMatS);
//
//        Core.bitwise_and(thresholdMatH, thresholdMatS, thresholdMat);
//        Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//        numContoursFound = contoursList.size();
//        input.copyTo(contoursOnFrameMat);
//        Imgproc.drawContours(contoursOnFrameMat, contoursList, -1, new Scalar(0, 0, 255), 3, 8);
//
//        telemetry.addData("[Stage]", stageToRenderToViewport);
//        telemetry.addData("[Found Contours]", "%d", numContoursFound);
//        // telemetry.addData("]");
//        telemetry.update();
//
//        return thresholdMat;
//
//    }
//
//    public int getNumContoursFound() {
//        return numContoursFound;
//    }
//}

class Yellow_Detection extends OpenCvPipeline {
    Mat imgHSV = new Mat();
    Mat thresholdMat0 = new Mat();
    Mat thresholdMat1 = new Mat();
    Mat thresholdMat = new Mat();
    Mat HSLchan0Mat = new Mat();
    Mat HSLchan1Mat = new Mat();
    Mat thresholdMatS = new Mat();
    Mat thresholdMatH = new Mat();
    Mat contoursOnFrameMat = new Mat();
    List<MatOfPoint> contoursList = new ArrayList<>();
    int numContoursFound;

    double hHSVAverage = 0;
    double sHSVAverage = 0;
    double vHSVAverage = 0;

    enum Stage {
        THRESHOLD,
        RAW_IMAGE,
    }

    private Stage stageToRenderToViewport = Stage.THRESHOLD;
    private Stage[] stages = Stage.values();

    private Telemetry telemetry;

    public Yellow_Detection(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void onViewportTapped() {
        /*
         * Note that this method is invoked from the UI thread
         * so whatever we do here, we must do quickly.
         */

        int currentStageNum = stageToRenderToViewport.ordinal();

        int nextStageNum = currentStageNum + 1;

        if (nextStageNum >= stages.length) {
            nextStageNum = 0;
        }

        stageToRenderToViewport = stages[nextStageNum];
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.GaussianBlur(input, input, new Size(5, 5), 20);
        Imgproc.cvtColor(input, imgHSV, Imgproc.COLOR_RGB2HSV);
        Size size = input.size();
        double[] pixel1 = imgHSV.get((int) size.height / 2, (int) size.width / 2);
        double[] pixel2 = imgHSV.get((int) size.height / 2 + 1, (int) size.width / 2);
        double[] pixel3 = imgHSV.get((int) size.height / 2 + 1, (int) size.width / 2 + 1);
        double[] pixel4 = imgHSV.get((int) size.height / 2, (int) size.width / 2 + 1);


        if (stageToRenderToViewport == Stage.THRESHOLD) {
            hHSVAverage = (pixel1[0] + pixel2[0] + pixel3[0] + pixel4[0]) / 4;
            sHSVAverage = (pixel1[1] + pixel2[1] + pixel3[1] + pixel4[1]) / 4;
            vHSVAverage = (pixel1[2] + pixel2[2] + pixel3[2] + pixel4[2]) / 4;
        }
        telemetry.addData("average", "[%.2f, %.2f, %.2f]", hHSVAverage, sHSVAverage, vHSVAverage);
        contoursList.clear();

        Imgproc.cvtColor(input, imgHSV, Imgproc.COLOR_RGB2HSV);
        Core.extractChannel(imgHSV, HSLchan0Mat, 0);
        Core.extractChannel(imgHSV, HSLchan1Mat, 1);
        Imgproc.threshold(HSLchan0Mat, thresholdMat0, hHSVAverage - 30, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(HSLchan0Mat, thresholdMat1, hHSVAverage + 30, 255, Imgproc.THRESH_BINARY_INV);
        Core.bitwise_and(thresholdMat0, thresholdMat1, thresholdMatH);
        Imgproc.threshold(HSLchan1Mat, thresholdMat0, sHSVAverage - 30, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(HSLchan1Mat, thresholdMat1, sHSVAverage + 30, 255, Imgproc.THRESH_BINARY_INV);
        Core.bitwise_and(thresholdMat0, thresholdMat1, thresholdMatS);

        Core.bitwise_and(thresholdMatH, thresholdMatS, thresholdMat);
        Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        numContoursFound = contoursList.size();
        input.copyTo(contoursOnFrameMat);
        Imgproc.drawContours(contoursOnFrameMat, contoursList, -1, new Scalar(0, 0, 255), 3, 8);

        telemetry.addData("[Stage]", stageToRenderToViewport);
        telemetry.addData("[Found Contours]", "%d", numContoursFound);
        // telemetry.addData("]");
        telemetry.update();

        return thresholdMat;

    }

    public int getNumContoursFound() {
        return numContoursFound;
    }
}
