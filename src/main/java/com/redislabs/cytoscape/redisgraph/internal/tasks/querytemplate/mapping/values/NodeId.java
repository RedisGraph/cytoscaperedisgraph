package com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.mapping.values;

import com.redislabs.cytoscape.redisgraph.internal.graph.GraphNode;

/**
 * This class implements the value expression for a node id.
 */
public class NodeId implements ValueExpression<GraphNode, Long> {
    @Override
    public Long eval(GraphNode val) {
        return val.getId();
    }

    @Override
    public void accept(ValueExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
