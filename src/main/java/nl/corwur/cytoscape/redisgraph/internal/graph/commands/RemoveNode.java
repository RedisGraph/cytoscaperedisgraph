package nl.corwur.cytoscape.redisgraph.internal.graph.commands;

import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.GraphImplementationException;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.PropertyKey;

/**
 * Remove a node from a graph.
 */
public class RemoveNode extends GraphCommand {

    private final PropertyKey<Long> nodeId;

    private RemoveNode(PropertyKey<Long> nodeId) {
        this.nodeId = nodeId;
    }

    public static RemoveNode create(PropertyKey<Long> nodeId) {
        return new RemoveNode(nodeId);
    }

    @Override
    public void execute() throws CommandException {
        try {
            graphImplementation.removeNode(nodeId);
        } catch (GraphImplementationException e) {
            throw new CommandException(e);
        }
    }
}
