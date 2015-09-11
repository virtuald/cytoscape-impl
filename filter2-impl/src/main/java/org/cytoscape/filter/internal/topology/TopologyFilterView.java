package org.cytoscape.filter.internal.topology;

import javax.swing.JFormattedTextField;

import org.cytoscape.filter.internal.view.BooleanComboBox;

public interface TopologyFilterView {

	JFormattedTextField getThresholdField();

	JFormattedTextField getDistanceField();

	BooleanComboBox getAtLeastComboBox();
}
