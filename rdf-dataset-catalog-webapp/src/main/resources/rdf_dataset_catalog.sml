
Prefix o: <http://dcat.cc/ontology/>
Prefix r: <http://dcat.cc/resource/>


Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
Prefix owl: <http://www.w3.org/2002/07/owl#>

Prefix dcat: <http://www.w3.org/ns/dcat#>

Create View dataset As
  Construct {
    ?s
      a dcat:Dataset ;
      rdfs:label ?l ;      
      .
  }
  With
    ?s = uri(r:, 'dataset', ?id)
    ?l = plainLiteral(?name)
  From
    dataset

    

Create View dataset_download_seq As
  Construct {
    ?s
      a rdf:Seq ;
      .
    
    ?d
      o:hasDownloads ?s ;
      .
  }
  With
    ?s = uri(r:, 'dataset-downloads', ?dataset_id)
    ?d = uri(r:, 'dataset', ?dataset_id)
  From
    [[SELECT DISTINCT "dataset_id" FROM "dataset_downloadlocations"]]



Create View dataset_downloads As
  Construct {
    ?s ?p ?o
  }
  With
    ?s = uri(r:, 'dataset-downloads', ?dataset_id)
    ?p = uri(rdfs:, '_', ?sid)
    ?o = uri(?url)
  From
    [[SELECT "dataset_id", "sequence_id" + 1 AS "sid", "url" FROM "dataset_downloadlocations"]]

    

Create View dataset_endpoint_seq As
  Construct {
    ?s a rdf:Seq .    
    ?d o:hasEndpoints ?s
  }
  With
    ?s = uri(r:, 'dataset-endpoints', ?dataset_id)
    ?d = uri(r:, 'dataset', ?dataset_id)
  From
    [[SELECT DISTINCT "dataset_id" FROM "sparqllocation"]]


// TODO SML parser needs fixes for missing comma and missing question mark
Create View dataset_endpoints As
  Construct {
    ?s ?p ?o .
    ?o o:url ?u 
  }
  With
    ?s = uri(r:, 'dataset-endpoints', ?dataset_id)
    ?p = uri(rdfs:, '_', ?sid)
    ?o = uri(r:, 'dataset-endpoint', ?dataset_id, '-', ?sid)
    ?u = uri(?url)
  From
    [[SELECT "dataset_id", "sequence_id" + 1 AS "sid", "url" FROM "sparqllocation"]]


Create View endpoint_graph_seq As
  Construct {
    ?s o:hasDefaultGraphs ?g .
    ?g a rdf:Seq .
  }
  With
    ?s = uri(r:, 'dataset-endpoint', ?dataset_id, '-', ?sid)
    ?g = uri(r:, 'dataset-endpoint-graphs', ?dataset_id, '-', ?sid)
  From
      [[SELECT "a"."dataset_id", "a"."sequence_id" + 1 AS "sid" FROM "sparqllocation" "a" JOIN "sparqllocation_defaultgraphs" "b" ON("b"."sparqllocation_id" = "a"."id")]]
      
      
Create View endpoint_graphs As
  Construct {
    ?s ?p ?o
  }
  With
    ?s = uri(r:, 'dataset-endpoint-graphs', ?dataset_id, '-', ?sid)
    ?p = uri(rdf:, '_', ?sid)
    ?o = uri(?uri)
  From
      [[SELECT "a"."dataset_id", "a"."sequence_id" + 1 AS "sid", "uri" FROM "sparqllocation" "a" JOIN "sparqllocation_defaultgraphs" "b" ON("b"."sparqllocation_id" = "a"."id")]]
  