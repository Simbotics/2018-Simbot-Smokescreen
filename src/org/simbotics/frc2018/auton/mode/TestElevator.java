package org.simbotics.frc2018.auton.mode;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.intake.IntakeAntiJam;
import org.simbotics.frc2018.auton.intake.IntakeSetOutput;
import org.simbotics.frc2018.auton.util.AutonWait;

public class TestElevator implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {

		ab.addCommand(new IntakeAntiJam());
		ab.addCommand(new AutonWait(2000));
		ab.addCommand(new AutonOverride(RobotComponent.INTAKE));
		ab.addCommand(new IntakeSetOutput(0));

		// ab.addCommand(new SetElevatorHeight(5.0, 3000));
		// ab.addCommand(new SetElevatorHeight(3.0, 3000));
		// ab.addCommand(new SetElevatorHeight(6.66, 3000));
		// ab.addCommand(new SetElevatorHeight(0.0, 3000));
		// ab.addCommand(new SetElevatorHeight(4.0, 3000));
		// ab.addCommand(new DriveSetOutput(0.25));
		// ab.addCommand(new AutonWait(1000));
		// ab.addCommand(new DriveSetOutput(0.0));
	}
}
