package org.simbotics.frc2018.auton.mode.right;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.drive.DriveSetOutput;
import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveToPoint;
import org.simbotics.frc2018.auton.drive.DriveTurnToAngle;
import org.simbotics.frc2018.auton.drive.DriveWait;
import org.simbotics.frc2018.auton.intake.IntakeAntiJamNoBump;
import org.simbotics.frc2018.auton.intake.IntakeSetOutput;
import org.simbotics.frc2018.auton.intake.IntakeWait;
import org.simbotics.frc2018.auton.lift.LiftHoldPosition;
import org.simbotics.frc2018.auton.lift.LiftSetPosition;
import org.simbotics.frc2018.auton.mode.AutonBuilder;
import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.auton.util.AutonWait;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

public class RightSide2RightSwitch implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		// go to score first cube
		ab.addCommand(new DriveSetPosition(0, 0, 90));
		ab.addCommand(new DriveToPoint(-1.8, 9.2, 130, 0, 10, 0.5,25,40,true, 5000));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());
		// Score first cube
		ab.addCommand(new IntakeSetOutput(-1.0));
		ab.addCommand(new DriveSetOutput(0.1));
		ab.addCommand(new AutonWait(350));
		ab.addCommand(new IntakeSetOutput(0));
		// go to second cube
		ab.addCommand(new DriveToPoint(0.0, 21.2, 270, 0, 9, 0.5,25,true, 5000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveTurnToAngle(217, 5, 750));
		ab.addCommand(new IntakeAntiJamNoBump(1.0,60));
		ab.addCommand(new DriveToPoint(-2.5, 19.5, 217, 0, 6, 0.5,25,true, 5000));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new DriveSetOutput(-0.1));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 1250));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));
		
		ab.addCommand(new IntakeSetOutput(-1.0));
		ab.addCommand(new DriveSetOutput(0.4));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(0.0));
		
		
		ab.addCommand(new DriveToPoint(-2.3, 20.2, 230, 0, 6, 0.5,25,true, 5000));
		ab.addCommand(new AutonWait(250));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveToPoint(-5.88, 18.5, 190, 0, 4, 0.5,25,true, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0,120));
		ab.addCommand(new IntakeWait());
		
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 1250));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));
		ab.addCommand(new DriveTurnToAngle(270, 5, 1000));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new IntakeSetOutput(-1.0));
		ab.addCommand(new DriveSetOutput(0.1));
		ab.addCommand(new AutonWait(300));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(0.0));
		
		ab.addCommand(new DriveTurnToAngle(180, 5, 1000));
		ab.addCommand(new DriveWait());
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		
		
	}

}
