package com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.mapping;

import com.redislabs.cytoscape.redisgraph.internal.graph.GraphNode;
import com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.mapping.values.ValueExpression;

/**
 * This class implements a mapping from a node to Cytoscape.
 *
 * @param <T>
 */
public class NodeColumnMapping<T> {
    private final String columnName;
    private final Class<T> columnType;
    private final ValueExpression<GraphNode, T> valueExpression;

    public NodeColumnMapping(String columnName, Class<T> columnType, ValueExpression<GraphNode, T> valueExpression) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.valueExpression = valueExpression;
    }

    public String getColumnName() {
        return columnName;
    }

    public Class<T> getColumnType() {
        return columnType;
    }

    public ValueExpression<GraphNode, T> getValueExpression() {
        return valueExpression;
    }
}
