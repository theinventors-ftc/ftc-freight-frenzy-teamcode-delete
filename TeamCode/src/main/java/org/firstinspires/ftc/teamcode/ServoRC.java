// package org.firstinspires.ftc.teamcode;

// import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// import com.qualcomm.robotcore.hardware.CRServo;
// import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// import com.qualcomm.robotcore.hardware.Servo;

// @TeleOp(name = "Concept: ServoRC", group = "Concept")

// public class ServoRC extends LinearOpMode {
//     CRServo   servo1;

//     @Override
//     public void runOpMode() {
//         servo1 = hardwareMap.get(CRServo.class, "servo1");
        
//         waitForStart();

//         while(opModeIsActive()){
//             servo1.setPower(gamepad1.x);
//             sleep(20);
//             idle();
//         }
//     }
// }
