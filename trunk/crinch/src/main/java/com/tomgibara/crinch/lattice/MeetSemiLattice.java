package com.tomgibara.crinch.lattice;

public interface MeetSemiLattice<E> extends SemiLattice<E> {

	E meet(E a, E b);

	MeetSemiLattice<E> boundedMeetSemiLattice(E bottom);

	E getBottom();
	
	boolean isBoundedBelow();
	
}
