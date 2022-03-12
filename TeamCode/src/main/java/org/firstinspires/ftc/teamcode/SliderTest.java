package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.


@TeleOp(name = "Concept: SliderTest", group = "Concept")

public class SliderTest extends LinearOpMode {
    private DcMotor slider;

    @Override
    public void runOpMode() {
        // Insert whatever initialization your own code does
        slider = hardwareMap.get(DcMotor.class, "slider");

        waitForStart();

        while(opModeIsActive()) {


            slider.setPower(-(gamepad1.right_stick_y / 2));
        }
    }
}