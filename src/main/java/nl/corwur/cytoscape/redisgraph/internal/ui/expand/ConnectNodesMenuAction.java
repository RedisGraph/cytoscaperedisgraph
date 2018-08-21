/**
 *
 */
package nl.corwur.cytoscape.redisgraph.internal.ui.expand;

import nl.corwur.cytoscape.redisgraph.internal.Services;
import nl.corwur.cytoscape.redisgraph.internal.tasks.ConnectNodesTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.NetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * @author sven
 */
public class ConnectNodesMenuAction implements NetworkTaskFactory {

    private static final long serialVersionUID = 1L;
    private final transient Services services;
    private Boolean onlySelected;

    /**
     *
     */
    public ConnectNodesMenuAction(Services services, Boolean onlySelected) {
        this.services = services;
        this.onlySelected = onlySelected;
    }

    @Override
    public TaskIterator createTaskIterator(CyNetwork network) {
        if (this.isReady(network)) {
            return new TaskIterator(new ConnectNodesTask(services, network, this.onlySelected));
        } else {
            return null;
        }
    }

    @Override
    public boolean isReady(CyNetwork arg0) {
        return arg0 != null && arg0.getNodeCount() > 0;
    }

    public static ConnectNodesMenuAction create(Services services, Boolean onlySelected) {
        return new ConnectNodesMenuAction(services, onlySelected);
    }


}
