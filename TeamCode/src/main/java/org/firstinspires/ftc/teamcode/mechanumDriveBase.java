package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Concept: MechanumBaseDrive", group = "Concept")

public class mechanumDriveBase extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearRight;
    private DcMotor rearLeft;
    private DcMotor intake;
    private DcMotor carousel;
    BNO055IMU imu;

    private boolean gyroFollowEnabled = false;

    private double target = 0;
    private double kp = 0.1;

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
            target = Math.max(target-90, -90);
        }
    }

    void checkModeBtnPressed(){
        if(gamepad1.right_stick_button) {
            modeBtn_pressed = true;
        }
        if(modeBtn_pressed && !gamepad1.right_stick_button) {
            modeBtn_pressed = false;
            if(gyroFollowEnabled) {
                gyroFollowEnabled = false;
            } else {
                gyroFollowEnabled = true;
            }
        }
    }

    void assignIntakePower(boolean right_bumper) {
        if(right_bumper) {
            rBumperPress = true;
        }

        if(rBumperPress && !right_bumper) {
            if(intakeInDuty == "running") {
                intakeInDuty = "reverse";
            } else {
                intakeInDuty = "running";
            }
        }
        if (intakeInDuty == "running") {
            intake.setPower(1);
        } else if(intakeInDuty == "reverse") {
            intake.setPower(-1);
            sleep(500);
            intake.setPower(0);
            intakeInDuty = "stop";
        }
    }


    void assignDrivetrainPower(double xG, double yG, double rxG){
        double rx = (-rxG / 2) * (gamepad1.left_trigger + 1);
        double trig = gamepad1.right_trigger;
        double x = -(((xG * 1.1) / 2) * (1 + trig));
        double y = (yG / 2) * (1 + trig);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        rearRight.setPower((y + x - rx) / denominator);
        rearLeft.setPower((y - x + rx) / denominator);
        frontRight.setPower((y - x - rx) / denominator);
        frontLeft.setPower((y + x + rx) / denominator);
    }

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        intake = hardwareMap.get(DcMotor.class, "intake");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        double gyroValue, rot, tempMin;
        int tempIndex, i;
        double[] allPower, distanceFromDirections = {0, 0, 0, 0};

        while(opModeIsActive()) {
            gyroValue = - imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

            checkDpadPressed();
            checkModeBtnPressed();

//            if (!gyroFollowEnabled) {
//                rot = gamepad1.right_stick_x;
//            } else {
//                //Define the closest degrees position
//                distanceFromDirections[0] = Math.abs(180 - gyroValue);
//                distanceFromDirections[1] = Math.abs(90 - gyroValue);
//                distanceFromDirections[2] = Math.abs(- 90 - gyroValue);
//                distanceFromDirections[3] = Math.abs(- gyroValue);
//
//                tempMin = distanceFromDirections[0];
//                tempIndex = 0;
//
//                i = 0;
//                for (double dif : distanceFromDirections) {
//                    if (dif < tempMin) {
//                        tempMin = dif;
//                        tempIndex = i;
//                    }
//                    i++;
//                }
//
//                target = distanceFromDirections[tempIndex];
//                rot = (gyroValue - target) * kp;
//            }

            assignDrivetrainPower(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            assignIntakePower(gamepad1.right_bumper);



            telemetry.addData(">", "Intake: " + intakeInDuty + "");
            telemetry.update();
        }
    }
}