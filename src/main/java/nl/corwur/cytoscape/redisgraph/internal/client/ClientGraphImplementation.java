package nl.corwur.cytoscape.redisgraph.internal.client;

import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.GraphImplementation;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.GraphImplementationException;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.NodeLabel;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.PropertyKey;

import java.util.List;
import java.util.Map;

public final class ClientGraphImplementation implements GraphImplementation {

    private static final String START_ID = "startId";
    private static final String END_ID = "endId";
    private static final String PROPS = "props";
    private static final String EDGE_ID = "edgeId";
    private static final String NODE_ID = "nodeId";
    //TODO: move shared global statics to a separate class
    private final String cytoscapeNetworkPorpertyName;
    private final Client neo4jClient;
    private final String networkLabel;

    private ClientGraphImplementation(Client neo4jClient, String cytoscapeNetworkPorpertyName, String networkLabel) {
        this.neo4jClient = neo4jClient;
        this.cytoscapeNetworkPorpertyName = cytoscapeNetworkPorpertyName;
        this.networkLabel = networkLabel;
    }

    public static ClientGraphImplementation create(Client neo4jClient, String cytoscapeNetworkPorpertyName, String networkLabel) {
        return new ClientGraphImplementation(neo4jClient, cytoscapeNetworkPorpertyName, networkLabel);
    }

    @Override
    public void addEdge(PropertyKey<Long> start, PropertyKey<Long> end, String relationship, Map<String, Object> properties) throws GraphImplementationException {
        CypherQuery cypherQuery = CypherQuery.builder()
                .query(
                        "MATCH (s {" + matchNetworkProperty() + "}), " +
                                "(e {" + matchNetworkProperty() + "}) " +
                                "WHERE " +
                                where("s", start, START_ID) + " AND " +
                                where("e", end, END_ID) + " " +
                                "CREATE (s) -[:`" + relationship + "` $" + PROPS + "]-> (e)"
                )
                .params(START_ID, start.getValue())
                .params(END_ID, end.getValue())
                .params(PROPS, properties)
                .build();
        executeQuery(cypherQuery);
    }


    @Override
    public void updateDirectedEdge(PropertyKey<Long> startId, PropertyKey<Long> endId, Map<String, Object> properties) throws GraphImplementationException {
        CypherQuery cypherQuery = CypherQuery.builder()
                .query(
                        "MATCH (s {" + matchNetworkProperty() + "}) " +
                                " -[r]-> " +
                                "(e {" + matchNetworkProperty() + "}) " +
                                "WHERE " +
                                where("s", startId, START_ID) + " AND " +
                                where("e", endId, END_ID) + " " +
                                "SET r = $" + PROPS
                )
                .params(START_ID, startId.getValue())
                .params(END_ID, endId.getValue())
                .params(PROPS, properties)
                .build();
        executeQuery(cypherQuery);
    }

    @Override
    public void updateEdgeById(PropertyKey<Long> edgeId, Map<String, Object> properties) throws GraphImplementationException {
        CypherQuery cypherQuery = CypherQuery.builder()
                .query(
                        "MATCH (s {" + matchNetworkProperty() + "}) " +
                                " -[r]-> " +
                                "(e {" + matchNetworkProperty() + "}) " +
                                "WHERE " +
                                where("r", edgeId, EDGE_ID) + " " +
                                "SET r = $" + PROPS
                )
                .params(EDGE_ID, edgeId.getValue())
                .params(PROPS, properties)
                .build();
        executeQuery(cypherQuery);
    }

    private String where(String alias, PropertyKey<Long> nodeId, String param) {
        if ("id".equalsIgnoreCase(nodeId.getName())) {
            return "id(" + alias + ") = $" + param + " ";
        } else {
            return alias + "." + nodeId.getName() + " = $" + param + " ";
        }
    }

    @Override
    public void removeEdge(PropertyKey<Long> edgeId) throws GraphImplementationException {
        CypherQuery cypherQuery = CypherQuery.builder()
                .query(
                        "MATCH (s) - [r] - (e) " +
                                removeEdgeWhere(edgeId) +
                                " AND  s." + cytoscapeNetworkPorpertyName + "='" + networkLabel + "'" +
                                " AND  e." + cytoscapeNetworkPorpertyName + "='" + networkLabel + "'" +
                                "DELETE r"
                )
                .params(EDGE_ID, edgeId.getValue())
                .build();
        executeQuery(cypherQuery);
    }

    private String removeEdgeWhere(PropertyKey<Long> edgeId) {
        if ("id".equalsIgnoreCase(edgeId.getName())) {
            return "WHERE id(r) = $" + EDGE_ID + " ";
        } else {
            return "WHERE r." + edgeId.getName() + " = $" + EDGE_ID + " ";
        }
    }

    @Override
    public void removeAllEdgesFromNode(PropertyKey<Long> nodeId) throws GraphImplementationException {
        CypherQuery removerRelationsQuery = CypherQuery.builder().query(
                "MATCH(n {" + nodeId.getName() + ":$" + NODE_ID + ", " + matchNetworkProperty() + "}) - [r] - (e) DELETE r")
                .params(NODE_ID, nodeId.getValue())
                .build();
        executeQuery(removerRelationsQuery);
    }

    @Override
    public void removeNode(PropertyKey<Long> nodeId) throws GraphImplementationException {
        if ("id".equalsIgnoreCase(nodeId.getName())) {
            CypherQuery removerQuery = CypherQuery.builder().query(
                    "MATCH(n) WHERE id(n) = $" + NODE_ID +
                            " AND  n." + cytoscapeNetworkPorpertyName + "='" + networkLabel + "'" +
                            "  DELETE n")
                    .params(NODE_ID, nodeId.getValue())
                    .build();
            executeQuery(removerQuery);
        } else {
            CypherQuery removerQuery = CypherQuery.builder().query(
                    "MATCH(n {" + nodeId.getName() + ":$" + NODE_ID + ", " + matchNetworkProperty() + "}) DELETE n")
                    .params(NODE_ID, nodeId.getValue())
                    .build();
            executeQuery(removerQuery);
        }
    }

    @Override
    public void addNode(List<NodeLabel> labels, Map<String, Object> properties) throws GraphImplementationException {
        String nodeLabelClause = getNodeLabelsClause(labels);
        CypherQuery insertQuery = CypherQuery.builder().query(
                "CREATE(n $" + PROPS + ") SET n." + cytoscapeNetworkPorpertyName + "='" + networkLabel + "' "
                        + (nodeLabelClause.isEmpty() ? "" : ", n" + nodeLabelClause))
                .params(PROPS, properties)
                .build();
        executeQuery(insertQuery);
    }

    @Override
    public void updateNode(PropertyKey<Long> nodeId, List<NodeLabel> labels, Map<String, Object> properties) throws GraphImplementationException {
        String nodeLabelClause = getNodeLabelsClause(labels);
        CypherQuery updateQuery = CypherQuery.builder().query(
                "MATCH(n) " +
                        "WHERE " + nodeIdClause("n", nodeId) + " " +
                        "SET n = $" + PROPS + ", n." + cytoscapeNetworkPorpertyName + " = '" + networkLabel + "' " +
                        (nodeLabelClause.isEmpty() ? "" : ", n" + nodeLabelClause))
                .params(NODE_ID, nodeId.getValue())
                .params(PROPS, properties)
                .build();
        executeQuery(updateQuery);
    }

    private String getNodeLabelsClause(List<NodeLabel> labels) {
        return labels.stream()
                .reduce(
                        "",
                        (str, label) -> str + ":`" + label.getLabel() + "`",
                        (s1, s2) -> s1 + s2
                );
    }

    private String nodeIdClause(String nodeAlias, PropertyKey<Long> nodeId) {
        if("id".equals(nodeId.getName())) {
            return "id(" + nodeAlias + ")=$" + NODE_ID;
        } else {
            return nodeAlias + "." + nodeId.getName() + "=$" + NODE_ID + " AND " + whereNetworkProperty(nodeAlias);
        }
    }

    private void executeQuery(CypherQuery cypherQuery) throws GraphImplementationException {
        try {
            neo4jClient.executeQuery(cypherQuery);
        } catch (ClientException e) {
            throw new GraphImplementationException(e);
        }
    }

    private String matchNetworkProperty() {
        return cytoscapeNetworkPorpertyName + ":'" + networkLabel + "'";
    }

    private String whereNetworkProperty(String nodeALias) {
        return nodeALias + "." + cytoscapeNetworkPorpertyName + " = '" + networkLabel + "'";
    }

}
