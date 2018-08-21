package nl.corwur.cytoscape.redisgraph.internal.tasks.export;

import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.Label;
import nl.corwur.cytoscape.redisgraph.internal.graph.implementation.NodeLabel;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExportNetworkConfiguration {
    private final Label nodeLabel;
    private final String relationship;
    private final String nodeReferenceIdColumn;
    private final String nodeLabelColumn;
    private final String nodePropertiesColumnName;

    public ExportNetworkConfiguration(Label nodeLabel, String relationship, String nodeReferenceIdColumn, String nodeLabelColumn, String nodePropertiesColumnName) {
        this.nodeLabel = nodeLabel;
        this.relationship = relationship;
        this.nodeReferenceIdColumn = nodeReferenceIdColumn;
        this.nodeLabelColumn = nodeLabelColumn;
        this.nodePropertiesColumnName = nodePropertiesColumnName;
    }

    public Label getNodeLabel() {
        return nodeLabel;
    }

    public String getRelationship(CyNetwork cyNetwork, CyEdge cyEdge) {
        CyRow cyRow = cyNetwork.getRow(cyEdge);
        if (cyRow.isSet(relationship) && cyRow.getRaw(relationship) instanceof String) {
            return escapeNeo4j(cyRow.get("shared name", String.class));
        } else {
            return escapeNeo4j(relationship);
        }
    }

    private String escapeNeo4j(String name) {
        if (name != null && !name.isEmpty()) {
            return name.replaceAll("[^a-zA-Z0-9_]+", "_");
        } else {
            return "empty";
        }
    }

    public String getNodeReferenceIdColumn() {
        return nodeReferenceIdColumn;
    }

    public static ExportNetworkConfiguration create(Label label, String relationship, String nodeReferenceIdColumn, String nodeLabelColumn, String nodePropertiesColumnName) {
        return new ExportNetworkConfiguration(label, relationship, nodeReferenceIdColumn, nodeLabelColumn, nodePropertiesColumnName);
    }

    public long getNodeReferenceId(CyNode cyNode, CyNetwork cyNetwork) {
        CyRow cyRow = cyNetwork.getRow(cyNode);
        if (cyRow.isSet(nodeReferenceIdColumn) && cyRow.getRaw(nodeReferenceIdColumn) instanceof Long) {
            return cyRow.get(nodeReferenceIdColumn, Long.class);
        } else {
            return cyNode.getSUID();
        }
    }

    public List<NodeLabel> getNodeLabels(CyNode cyNode, CyNetwork cyNetwork) {
        CyRow cyRow = cyNetwork.getRow(cyNode);
        if (cyRow.isSet(nodeLabelColumn) && cyRow.getRaw(nodeLabelColumn) instanceof List) {
            @SuppressWarnings("unchecked")
            Stream<NodeLabel> stream = cyRow.get(nodeLabelColumn, List.class).stream().map(obj -> NodeLabel.create(obj.toString()));
            List<NodeLabel> list = stream.collect(Collectors.toList());
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public String getNodePropertiesColumnName() {
        return nodePropertiesColumnName;
    }

    public String getNodeName(CyNetwork cyNetwork, CyNode cyNode) {
        String name = cyNetwork.getRow(cyNode).get("shared name", String.class);
        return name == null ? "" : name;
    }
}
