package com.redislabs.cytoscape.redisgraph.internal.graph.commands;

import com.redislabs.cytoscape.redisgraph.internal.graph.implementation.GraphImplementation;

/**
 * A command with a graph implementation, used for all commands that modify a graph implementation.
 */
public abstract class GraphCommand extends Command {
    protected GraphImplementation graphImplementation;

    public GraphImplementation getGraphImplementation() {
        return graphImplementation;
    }

    public void setGraphImplementation(GraphImplementation graphImplementation) {
        this.graphImplementation = graphImplementation;
    }
}
