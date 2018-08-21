package nl.corwur.cytoscape.redisgraph.internal.tasks.querytemplate.mapping.values;

import nl.corwur.cytoscape.redisgraph.internal.graph.GraphNode;

/**
 * This class implements the value expression for evaluated javascriptcode.
 *
 * @param <T>
 */
public class NodeScriptExpression<T> extends ValueScriptExpression<GraphNode, T> {

    public NodeScriptExpression(String script, Class<T> type) {
        super(script, "node", type);
    }

    @Override
    public void accept(ValueExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
