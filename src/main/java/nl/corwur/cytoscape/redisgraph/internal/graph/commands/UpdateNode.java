package nl.corwur.cytoscape.redisgraph.internal.graph.commands;

import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.GraphImplementationException;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.NodeLabel;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.PropertyKey;

import java.util.List;
import java.util.Map;

/**
 * Update a node
 */
public class UpdateNode extends GraphCommand {

    private PropertyKey<Long> nodeId;
    private List<NodeLabel> labels;
    private Map<String, Object> properties;

    public static UpdateNode create(PropertyKey<Long> nodeId, List<NodeLabel> labels, Map<String, Object> properties) {
        return new UpdateNode(nodeId, labels, properties);
    }

    public UpdateNode(PropertyKey<Long> nodeId, List<NodeLabel> labels, Map<String, Object> properties) {
        this.nodeId = nodeId;
        this.labels = labels;
        this.properties = properties;
    }

    @Override
    public void execute() throws CommandException {
        try {
            graphImplementation.updateNode(nodeId, labels, properties);
        } catch (GraphImplementationException e) {
            throw new CommandException(e);
        }
    }
}
