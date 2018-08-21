# Cytoscape plugin for RedisGraph

RedisGraph Graphs are often too large for Cytoscape: this plugin allows you to write Cypher queries and import the result as a network. 
Queries can be parameterized and stored for reuse.  

<!--
The plugin can be downloaded from the [Cytoscape App Store](http://apps.cytoscape.org/apps/cytoscaperedsigraphplugin)
-->

## Features
Connects to RedisGraph with a username/password using JRedisGraph.

### Importing graphs
There are three main methods of importing a graph:
- Import all nodes and edges from RedisGraph into Cytoscape
- Import a cypher query into Cystoscape
- Import a stored query ([template](doc/template.md)) into Cytoscape

### Exporting networks
The plugin allows you to export any Cytoscape Network to RedisGraph. This can be an updated version of an imported graph or a network from a different source.  
Nodes / relationships removed from a graph in Cytoscape will also result in these elements being removed from the RedisGraph database after the export. 

### Expanding nodes
The plugin allows you to expand a single node, selected nodes or all nodes in the network at once. This way you can browse through your graph.

Main menu:
- Expand all (selected) nodes in the network through all edges (bidirectional)
- Expand all (selected) nodes, incoming edges only
- Expand all (selected) nodes, outgoing edges only

Context menu:
- Expand single node, bidirectional, incoming or outgoing edges
- Expand single node, bidirectional, incoming or outgoing edges, based on the _available edges connected to this node_
- Expand single node, bidirectional, incoming or outgoing edges, based on the _available nodes connected to this node_

### Other features
- Show all edges (relationships) between all nodes in the network or only between selected nodes.
- Get the shortest paths from the database between the selected nodes. When more than two nodes are selected, all combinations will be queried: RedisGraph does not allow shortest path calculations between more than two nodes (a.k.a. 'via').
