package org.aksw.rdf_dataset_catalog.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;


@Entity
public class Dataset
//	extends ResourceBase
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	// TODO: How can we reference dataset records in other services?????
	// We need to be able to say: This dataset record originates from some other service - so we need a provenance record

	//private Group group;
	
	// The combination of namespace, name version must be unique (i.e. each dataset record resides in a user space)
	@Column(nullable=false)
	private String name;

	// Version should follow the maven format
	@Column(nullable=false)
	private String version;
	
	
	
	// The basic comment associated with the dataset
	@Column(nullable=false)
	private String comment;
	
	private String primaryIri; // Leave empty for non authorative datasets
	// TODO What if someone hijacks the an iri such as http://dbpedia.org/????

	
	// TODO Ensure that persisting a dataset object cannot create a new user.
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	private UserInfo owner;


	
    //@ElementCollection
    //@OrderColumn(name="sequence_id")
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dataset")
	@OrderBy
	@OrderColumn(name="sequence_id")
	private List<SparqlLocation> sparqlEndpoints;

	@PrePersist
	public void updateChildren() {
	    for(SparqlLocation item : sparqlEndpoints) {
	        item.setDataset(this);
	    }
	}
	
    //@ElementCollection
    //@OneToMany(cascade=CascadeType.ALL)
	//private List<DownloadLocation> downloadLocations;
	
	@ElementCollection
	@OrderColumn(name="sequence_id")
	@Column(name="url")
	private List<String> downloadLocations;
    
    //@OneToMany(cascade=CascadeType.ALL)
	@ElementCollection
	@OrderColumn
    private List<DatasetRelation> relations;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setname(String name) {
        this.name = name;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getPrimaryIri() {
        return primaryIri;
    }


    public void setPrimaryIri(String primaryIri) {
        this.primaryIri = primaryIri;
    }


    public UserInfo getOwner() {
        return owner;
    }


    public void setOwner(UserInfo owner) {
        this.owner = owner;
    }


    public List<SparqlLocation> getSparqlEndpoints() {
        return sparqlEndpoints;
    }


    public void setSparqlEndpoints(List<SparqlLocation> sparqlEndpoints) {
        this.sparqlEndpoints = sparqlEndpoints;
    }


    public List<String> getDownloadLocations() {
        return downloadLocations;
    }


    public void setDownloadLocations(List<String> downloadLocations) {
        this.downloadLocations = downloadLocations;
    }


    public List<DatasetRelation> getRelations() {
        return relations;
    }


    public void setRelations(List<DatasetRelation> relations) {
        this.relations = relations;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime
                * result
                + ((downloadLocations == null) ? 0 : downloadLocations
                        .hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result
                + ((primaryIri == null) ? 0 : primaryIri.hashCode());
        result = prime * result
                + ((relations == null) ? 0 : relations.hashCode());
        result = prime * result
                + ((sparqlEndpoints == null) ? 0 : sparqlEndpoints.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dataset other = (Dataset) obj;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (downloadLocations == null) {
            if (other.downloadLocations != null)
                return false;
        } else if (!downloadLocations.equals(other.downloadLocations))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (primaryIri == null) {
            if (other.primaryIri != null)
                return false;
        } else if (!primaryIri.equals(other.primaryIri))
            return false;
        if (relations == null) {
            if (other.relations != null)
                return false;
        } else if (!relations.equals(other.relations))
            return false;
        if (sparqlEndpoints == null) {
            if (other.sparqlEndpoints != null)
                return false;
        } else if (!sparqlEndpoints.equals(other.sparqlEndpoints))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Dataset [id=" + id + ", name=" + name + ", comment="
                + comment + ", primaryIri=" + primaryIri + ", owner=" + owner
                + ", sparqlEndpoints=" + sparqlEndpoints
                + ", downloadLocations=" + downloadLocations + ", relations="
                + relations + "]";
    }
    
	//private ContainmentList containmentList;
    
    
}
