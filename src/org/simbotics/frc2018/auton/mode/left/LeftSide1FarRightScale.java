package org.simbotics.frc2018.auton.mode.left;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveToPoint;
import org.simbotics.frc2018.auton.drive.DriveTurnToAngle;
import org.simbotics.frc2018.auton.drive.DriveWait;
import org.simbotics.frc2018.auton.intake.IntakeSetOutput;
import org.simbotics.frc2018.auton.lift.LiftHoldPosition;
import org.simbotics.frc2018.auton.lift.LiftSetPosition;
import org.simbotics.frc2018.auton.mode.AutonBuilder;
import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.auton.util.AutonWait;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

public class LeftSide1FarRightScale implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		// go to score first cube
		ab.addCommand(new DriveSetPosition(0, 0, 90));
		ab.addCommand(new DriveToPoint(0, 1.4, 90, 4, 4, 0.25, 25, true, 5000));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 1.0, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.STANDBY));
		// ab.addCommand(new DriveToPoint(-3.3, 4.2, 145, 7, 8,0.5, 5000));
		ab.addCommand(new DriveToPoint(18.3, 4.2,180 - 180, 8, 12, 0.5, 25, true, 5000));
		ab.addCommand(new DriveToPoint(20.7, 5.89, 180 -128, 6, 10, 0.25, 25, true, 5000));
		ab.addCommand(new DriveToPoint(22.3, 10.22,180 - 90, 6, 10, 0.25, 25, true, 5000));
		ab.addCommand(new DriveToPoint(22.1, 27.0, 180 -90, 8, 12, 0.5, 25, true, 5000));
		ab.addCommand(new DriveTurnToAngle(180-0, 3, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, 1.0, 2500));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL));
		ab.addCommand(new AutonWait(1000));
		ab.addCommand(new DriveToPoint(19.1, 28.3, 180-0, 0, 3, 0.1, 25, true, 2000));
		ab.addCommand(new DriveWait());

		ab.addCommand(new IntakeSetOutput(-0.7));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));

		ab.addCommand(new DriveToPoint(22.3, 28.3, 180-0, 0, 3.5, 0.5, 25, true, 2000));
		ab.addCommand(new DriveTurnToAngle(180-45, 3, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 1.0, 2000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());

	}

}
