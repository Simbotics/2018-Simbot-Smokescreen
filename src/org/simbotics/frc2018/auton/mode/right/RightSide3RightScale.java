package org.simbotics.frc2018.auton.mode.right;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.drive.DriveSetOutput;
import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveToPoint;
import org.simbotics.frc2018.auton.drive.DriveTurnToAngle;
import org.simbotics.frc2018.auton.drive.DriveWait;
import org.simbotics.frc2018.auton.intake.IntakeAntiJam;
import org.simbotics.frc2018.auton.intake.IntakeAntiJamClosed;
import org.simbotics.frc2018.auton.intake.IntakeAntiJamNoBump;
import org.simbotics.frc2018.auton.intake.IntakeSetOutput;
import org.simbotics.frc2018.auton.intake.IntakeWait;
import org.simbotics.frc2018.auton.lift.LiftWait;
import org.simbotics.frc2018.auton.lift.LiftHoldPosition;
import org.simbotics.frc2018.auton.lift.LiftSetPosition;
import org.simbotics.frc2018.auton.mode.AutonBuilder;
import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.auton.util.AutonWait;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;
import org.simbotics.frc2018.util.FieldConstants;

public class RightSide3RightScale implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetPosition(0, 0, 90));
		ab.addCommand(new DriveToPoint(0, 19.5, 90, 6, 11, 0.25, 25, false, 5000));

		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 1.0, false, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, false));

		ab.addCommand(new AutonWait(1000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_BETWEEN, WristPosition.LEVEL, 0.6, true, 3500));
		//ab.addCommand(new DriveToPoint(0, 19.5, 90, 5, 5, 0.25, 25, true, 5000));

		ab.addCommand(new DriveToPoint(-0.8, 22.85, 110, 0, 6, 0.25, 25,20, true, 5000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_BETWEEN, WristPosition.LEVEL));
		// ab.addCommand(new DriveToPoint(-1.5, 22, 270, 0,8, 0.25, 5000))
		ab.addCommand(new DriveWait());

		// Score first cube
		
		ab.addCommand(new IntakeSetOutput(-0.9));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));
		
		// Start driving to the 2nd cube
		ab.addCommand(new DriveToPoint(0.34, 20.44, 120, 0, 5, 0.5, 25, true, 4500));
		ab.addCommand(new AutonWait(100));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 1.0, false, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 105));
		ab.addCommand(new DriveToPoint(-1.4, 19.3, 220, 4, 8.5, 0.5, 25, true, 2000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, false));
		ab.addCommand(new DriveToPoint(-2.97, 18.25, 235, 0, 4, 0.1, 25, true, 3000));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));

		// go to scale with 2nd cube
		ab.addCommand(new DriveToPoint(2.0, 22.5, 220, 3.5, 9, 0.5, 25, true, 3000));
		ab.addCommand(new IntakeAntiJamClosed());
		ab.addCommand(new AutonWait(750));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, 1.0, false, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.INTAKE));
		ab.addCommand(new DriveToPoint(-0.49, 24.5, 116, 0, 4.0, 0.5, 25, true, 3000));
		ab.addCommand(new DriveWait());
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, false));

		// score 2nd cube
		ab.addCommand(new IntakeSetOutput(-1.0));
		ab.addCommand(new DriveTurnToAngle(126, 1.0, 300));
		ab.addCommand(new DriveToPoint(1.59, 23.5, 126, 0.5, 4.0, 0.5, 25, true, 3000));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));

		// go to 3rd cube
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 1.0, false, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 85));
		// ab.addCommand(new DriveToPoint(-1.0, 20.3, 217, 4, 8, 0.5, 3000));
		ab.addCommand(new DriveToPoint(-4.1, 18.3, 225, 3, 12.0, 0.5, 15, true, 3000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, false));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));

		// go to score 3rd cube
		ab.addCommand(new IntakeAntiJamClosed());
		ab.addCommand(new DriveToPoint(1.06, 22.2, 225, 3, 12, 0.5, 25, true, 3000));
		ab.addCommand(new AutonWait(750));
		ab.addCommand(new AutonOverride(RobotComponent.INTAKE));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, 0.8, false, 5000));
		ab.addCommand(new DriveToPoint(-0.5, 23.6, 123, 3, 6, 0.5, 25, true, 5000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, false));
		ab.addCommand(new DriveWait());

		ab.addCommand(new IntakeSetOutput(-1.0));
		ab.addCommand(new DriveTurnToAngle(123, 1.0, 500));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(-0.2));
		
	
	}
}
