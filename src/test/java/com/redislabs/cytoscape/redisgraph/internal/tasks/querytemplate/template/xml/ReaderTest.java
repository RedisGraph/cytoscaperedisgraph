package com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.template.xml;

import com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.CypherQueryTemplate;
import com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.mapping.CopyAllMappingStrategy;
import com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.mapping.GraphMapping;
import com.redislabs.cytoscape.redisgraph.internal.tasks.querytemplate.template.Reader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReaderTest {

    @Test
    public void readCopyAllStrategy() throws Exception {
        Reader reader = new Reader();
        CypherQueryTemplate template = reader.read(getClass().getResourceAsStream("/gene-detail-copyall.xml"));
        assertNotNull(template);
        assertTrue(template.getMapping() instanceof CopyAllMappingStrategy);
        assertEquals("Copy All Network", ((CopyAllMappingStrategy) template.getMapping()).getNetworkName());
        assertEquals("referenceId", ((CopyAllMappingStrategy) template.getMapping()).getReferenceColumn());
    }

    @Test
    public void readMappingStrategy() throws Exception {
        Reader reader = new Reader();
        CypherQueryTemplate template = reader.read(getClass().getResourceAsStream("/gene-detail.xml"));
        assertNotNull(template);
        assertTrue(template.getMapping() instanceof GraphMapping);
        assertTrue(((GraphMapping) template.getMapping()).getNodeColumnMapping().size() > 0);
        assertTrue(((GraphMapping) template.getMapping()).getEdgeColumnMapping().size() > 0);
        assertNotNull(((GraphMapping) template.getMapping()).getNodeReferenceIdColumn());
        assertNotNull(((GraphMapping) template.getMapping()).getEdgeReferenceIdColumn());
    }

}