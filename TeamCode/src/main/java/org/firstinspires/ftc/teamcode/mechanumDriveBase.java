package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.


import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Concept: MechanumBaseDrive", group = "Concept")

public class mechanumDriveBase extends LinearOpMode {
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx rearRight;
    private DcMotorEx rearLeft;
    private DcMotor intake;
    private DcMotor carousel;
    BNO055IMU imu;

    private boolean gyroFollowEnabled = false;

    private double target = 0;
    private double kp = 0.02;
    boolean do_this_once = true;

    private boolean rBumperPress = false;
    private String dpad_pressed = "";
    private boolean modeBtn_pressed = false;
    private String intakeInDuty = "stop";

    void checkDpadPressed() {
        if(gamepad1.dpad_right) {
            dpad_pressed = "right";
        } else if(gamepad1.dpad_left) {
            dpad_pressed = "left";
        }

        if(dpad_pressed == "right" && !gamepad1.dpad_right) {
            dpad_pressed = "";
            target = Math.min(target+90, 90);
        }

        if(dpad_pressed == "left" && !gamepad1.dpad_left) {
            dpad_pressed = "";
            target = Math.max(target-90, -90);
        }
    }


        public DcMotorEx slider;

        int maxTicksPosition = 2150;
        int[] levelPositions = {5, 30, 533, 1121, 1678};
        int levelSelected = 0;
        String levelBtnPressed = "";

        public void setSliderLevel(int level) {
            slider.setTargetPosition(levelPositions[level + 1]);
            telemetry.addData("Level", level);
            telemetry.update();
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slider.setVelocity(2000);
            if (level == -1) {
                slider.setPower(-0.05);
                sleep(300);
                slider.setPower(0);
                slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        }

        public void leave_element_at_level(Button[] buttons) {
            if (buttons[0].is_bumped()) {
                levelBtnPressed = "a";
                levelSelected = 1;
            } else if (buttons[1].is_bumped()) {
                levelBtnPressed = "x";
                levelSelected = 2;
            } else if (buttons[2].is_bumped()) {
                levelBtnPressed = "y";
                levelSelected = 3;
            }
            setSliderLevel(levelSelected);
            telemetry.addData("Button pressed", levelBtnPressed);
            telemetry.update();
            sleep(2000);// do servo stuff
            setSliderLevel(0);
        }



        void checkModeBtnPressed(){
        if(gamepad1.right_stick_button) {
            modeBtn_pressed = true;
        }
        if(modeBtn_pressed && !gamepad1.right_stick_button) {
            modeBtn_pressed = false;
            if(gyroFollowEnabled) {
                gyroFollowEnabled = false;
                do_this_once = true;
            } else {
                gyroFollowEnabled = true;
            }
        }
    }

    void assignIntakePower() {
        if (gamepad2.right_bumper)
            rBumperPress = true;

        if(rBumperPress && !gamepad2.right_bumper) {
            rBumperPress = false;
            if (intakeInDuty == "running") {
                intakeInDuty = "reverse";
                intake.setPower(-1);
                sleep(500);
                intake.setPower(0);
                intakeInDuty = "stop";
            } else {
                intakeInDuty = "running";
                intake.setPower(1);
            }
        }
    }

    public static final double MAX_RPM = 312;
    public static final double TICKS_PER_REV = 537.7;

    void assignDrivetrainPower(double xG, double yG, double rxG){
        double rx = (-rxG / 2) * (gamepad1.left_trigger + 1);
        double trig = gamepad1.right_trigger;
        double x = -(((xG * 1.1) / 2) * (1 + trig));
        double y = (yG / 2) * (1 + trig);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        telemetry.addData(">", "RR: " + (y + x - rx) / denominator + "");
        telemetry.addData(">", "RL: " + (y - x + rx) / denominator + "");
        telemetry.addData(">", "FR: " + (y - x - rx) / denominator + "");
        telemetry.addData(">", "FL: " + (y + x + rx) / denominator + "");

//        rearRight.setVelocity(MAX_RPM * TICKS_PER_REV * (y + x - rx) / (60 * denominator));
//        rearLeft.setVelocity(MAX_RPM * TICKS_PER_REV * (y - x + rx) / (60 * denominator));
//        frontRight.setVelocity(MAX_RPM * TICKS_PER_REV * (y - x - rx) / (60 * denominator));
//        frontLeft.setVelocity(MAX_RPM * TICKS_PER_REV * (y + x + rx) / (60 * denominator));
        rearRight.setVelocity((y + x - rx) / (60 * denominator));
        rearLeft.setVelocity(MAX_RPM * TICKS_PER_REV * (y - x + rx) / (60 * denominator));
        frontRight.setVelocity(MAX_RPM * TICKS_PER_REV * (y - x - rx) / (60 * denominator));
        frontLeft.setVelocity(MAX_RPM * TICKS_PER_REV * (y + x + rx) / (60 * denominator));
    }

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        rearRight = hardwareMap.get(DcMotorEx.class, "rearRight");
        rearLeft = hardwareMap.get(DcMotorEx.class, "rearLeft");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        slider = hardwareMap.get(DcMotorEx.class, "slider");
        slider.setDirection(DcMotorSimple.Direction.REVERSE);
        slider.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        Button sl_lvl_1 = new Button("slider_level_1");
        Button sl_lvl_2 = new Button("slider_level_2");
        Button sl_lvl_3 = new Button("slider_level_3");
        Button[] slider_buttons = {sl_lvl_1, sl_lvl_2, sl_lvl_3};

        waitForStart();
        double gyroValue, rot, tempMin;
        int tempIndex, i;
        double[] allPower, distanceFromDirections = {0, 0, 0, 0};
        while(opModeIsActive()) {
            gyroValue = - imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

            checkDpadPressed();
            checkModeBtnPressed();

            if (!gyroFollowEnabled) {
                rot = gamepad1.right_stick_x;
            } else {
                if (do_this_once) {
                    //Define the closest degrees position
                    distanceFromDirections[0] = Math.abs(180 - gyroValue);
                    distanceFromDirections[1] = Math.abs(90 - gyroValue);
                    distanceFromDirections[2] = Math.abs(-90 - gyroValue);
                    distanceFromDirections[3] = Math.abs(-gyroValue);

                    tempMin = distanceFromDirections[0];
                    tempIndex = 0;

                    i = 0;
                    for (double dif : distanceFromDirections) {
                        if (dif < tempMin) {
                            tempMin = dif;
                            tempIndex = i;
                        }
                        i++;
                    }
                    double[] orntsnt = {180, 90, -90, 0};
                    target = orntsnt[tempIndex];
                    do_this_once=false;
                }
                rot = (target - gyroValue) * kp;
            }


            sl_lvl_1.update(gamepad2.x);
            sl_lvl_2.update(gamepad2.y);
            sl_lvl_3.update(gamepad2.a);
            leave_element_at_level(slider_buttons);
            telemetry.addData("Encoder value", slider.getCurrentPosition());
            telemetry.update();

            assignDrivetrainPower(gamepad1.left_stick_x, gamepad1.left_stick_y, rot);
            assignIntakePower();

            telemetry.addData(">", "Intake: " + intakeInDuty + "");
            telemetry.addData(">", "Target: " + target + "");
            telemetry.addData(">", "gyroValue: " + gyroValue + "");
            telemetry.update();
        }
    }
}