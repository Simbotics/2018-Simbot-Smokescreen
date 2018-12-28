package org.simbotics.frc2018.auton.lift;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.subsystems.Lift;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftHoldPosition extends AutonCommand {

	private Lift liftControl;

	private RobotOutput robotOut;

	private ElevatorPosition desiredElevatorPosition;
	private WristPosition desiredWristPosition;
	private boolean waitForWrist;

	public LiftHoldPosition() {
		super(RobotComponent.LIFT);
	}
	
	public LiftHoldPosition(ElevatorPosition elevatorPosition, WristPosition wristPosition) {
		this( elevatorPosition, wristPosition, false);
	}

	public LiftHoldPosition(ElevatorPosition elevatorPosition, WristPosition wristPosition ,boolean waitForWrist) {
		super(RobotComponent.LIFT);

		this.desiredElevatorPosition = elevatorPosition;
		this.desiredWristPosition = wristPosition;
		this.liftControl = Lift.getInstance();
		this.robotOut = RobotOutput.getInstance();

	}

	@Override
	public void firstCycle() {
		this.liftControl.setTarget(this.desiredElevatorPosition, this.desiredWristPosition);
		if(SmartDashboard.getBoolean("USING WRIST", true)) {
			this.liftControl.ignoreWrist(false);
		} else {
			this.liftControl.ignoreWrist(true);
		}
	}

	@Override
	public boolean calculate() {
		this.liftControl.calculate(!this.waitForWrist);
		return false;
	}

	@Override
	public void override() {

		this.robotOut.setElevator(0);
		this.robotOut.setIntakeWrist(0);

	}

}
