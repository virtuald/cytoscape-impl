
package org.cytoscape.internal.select;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.events.AboutToRemoveEdgeViewsEvent;
import org.cytoscape.view.model.events.AboutToRemoveEdgeViewsListener;
import org.cytoscape.view.model.events.AboutToRemoveNodeViewsEvent;
import org.cytoscape.view.model.events.AboutToRemoveNodeViewsListener;
import org.cytoscape.view.model.events.AddedEdgeViewsEvent;
import org.cytoscape.view.model.events.AddedEdgeViewsListener;
import org.cytoscape.view.model.events.AddedNodeViewsEvent;
import org.cytoscape.view.model.events.AddedNodeViewsListener;
import org.cytoscape.view.model.events.NetworkViewAddedEvent;
import org.cytoscape.view.model.events.NetworkViewAddedListener;

public class RowViewTracker implements NetworkViewAddedListener, 
	AddedNodeViewsListener, AddedEdgeViewsListener, 
	AboutToRemoveNodeViewsListener, AboutToRemoveEdgeViewsListener {

	private Map<CyRow,View<?>> rowViewMap;

	public RowViewTracker() {
		rowViewMap = new HashMap<CyRow,View<?>>();
	}

	public void handleEvent(final NetworkViewAddedEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final CyNetworkView view = e.getNetworkView();

				for ( View<CyNode> nv : view.getNodeViews() )
					rowViewMap.put( nv.getModel().getCyRow(), nv);
		
				for ( View<CyEdge> ev : view.getEdgeViews() ) 
					rowViewMap.put( ev.getModel().getCyRow(), ev);
			}
		});
	}
	
	public void handleEvent(final AddedNodeViewsEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for ( View<CyNode> v : e.getNodeViews()) 
					rowViewMap.put( v.getModel().getCyRow(), v );
			}
		});
	}
	
	public void handleEvent(final AddedEdgeViewsEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for ( View<CyEdge> v : e.getEdgeViews()) 
					rowViewMap.put( v.getModel().getCyRow(), v );
			}
		});
	}
	
	public void handleEvent(final AboutToRemoveNodeViewsEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for ( View<CyNode> v : e.getNodeViews()) 
					rowViewMap.remove( v.getModel().getCyRow() );
			}
		});
	}
	
	public void handleEvent(final AboutToRemoveEdgeViewsEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for ( View<CyEdge> v : e.getEdgeViews()) 
					rowViewMap.remove( v.getModel().getCyRow() );
			}
		});
	}
	
	public Map<CyRow,View<?>> getRowViewMap() {
		return Collections.unmodifiableMap(rowViewMap);  
	}
}
