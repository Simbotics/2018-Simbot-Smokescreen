package org.simbotics.frc2018.auton.lift;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.subsystems.Lift;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftSetPosition extends AutonCommand {

	private Lift liftControl;

	private RobotOutput robotOut;

	private ElevatorPosition desiredElevatorPosition;
	private WristPosition desiredWristPosition;
	private double maxOutput;
	private boolean waitForWrist;

	public LiftSetPosition(ElevatorPosition elevatorPosition, WristPosition wristPosition, long timeout) {
		this(elevatorPosition, wristPosition, 1.0,false, timeout);
	}
	
	public LiftSetPosition(ElevatorPosition elevatorPosition, WristPosition wristPosition,double maxOutput,long timeout) {
		this(elevatorPosition, wristPosition, maxOutput,false, timeout);
	}
	

	public LiftSetPosition(ElevatorPosition elevatorPosition, WristPosition wristPosition, double maxOutput, boolean waitForWrist,
			long timeout) {
		super(RobotComponent.LIFT, timeout);

		this.desiredElevatorPosition = elevatorPosition;
		this.desiredWristPosition = wristPosition;
		this.maxOutput = maxOutput;
		this.waitForWrist = waitForWrist;
		this.liftControl = Lift.getInstance();
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {
		this.robotOut.setElevatorMaxOutput(this.maxOutput);
		this.liftControl.firstCycle();
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
		
		return this.liftControl.isDone();
	}

	@Override
	public void override() {
		this.robotOut.setElevator(0);
		this.robotOut.setIntakeWrist(0);

	}

}
