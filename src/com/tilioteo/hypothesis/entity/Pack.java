/**
 * 
 */
package com.tilioteo.hypothesis.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author Kamil Morong - Hypothesis
 * 
 *         Database entity for testing pack
 * 
 */
@Entity
@Table(name = "TBL_PACK")
@Access(AccessType.PROPERTY)
public final class Pack extends SerializableIdObject implements HasList<Branch> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3225002856411893041L;

	private String name = "";
	private String description = "";

	/**
	 * pack is published to users
	 */
	private Boolean published = false;
	private String note;

	/**
	 * list of contained branches
	 */
	private List<Branch> branches = new LinkedList<Branch>();

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packGenerator")
	@SequenceGenerator(name = "packGenerator", sequenceName = "hbn_pack_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "ID")
	public final Long getId() {
		return super.getId();
	}

	@Column(name = "NAME", nullable = false, unique = true)
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "PUBLISHED")
	public final Boolean getPublished() {
		return published;
	}

	public final void setPublished(Boolean published) {
		this.published = published;
	}

	@Column(name = "NOTE")
	public final String getNote() {
		return note;
	}

	public final void setNote(String note) {
		this.note = note;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TBL_PACK_BRANCH", joinColumns = @JoinColumn(name = "PACK_ID"), inverseJoinColumns = @JoinColumn(name = "BRANCH_ID"))
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@LazyCollection(LazyCollectionOption.FALSE)
	// @IndexColumn(name="IDX", base = 1)
	public final List<Branch> getBranches() {
		return branches;
	}

	protected void setBranches(List<Branch> list) {
		this.branches = list;
	}

	@Transient
	public final List<Branch> getList() {
		return getBranches();
	}

	public final void addBranch(Branch b) {
		this.branches.add(b);
	}

	public final void removeBranch(Branch b) {
		this.branches.remove(b);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pack))
			return false;
		Pack other = (Pack) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		// TODO remove when Buffered.SourceException occurs
		if (getDescription() == null) {
			if (other.getDescription() != null)
				return false;
		} else if (!getDescription().equals(other.getDescription()))
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (getNote() == null) {
			if (other.getNote() != null)
				return false;
		} else if (!getNote().equals(other.getNote()))
			return false;
		if (getPublished() == null) {
			if (other.getPublished() != null)
				return false;
		} else if (!getPublished().equals(other.getPublished()))
			return false;
		if (getBranches() == null) {
			if (other.getBranches() != null)
				return false;
		} else if (!getBranches().equals(other.getBranches()))
			return false;
		return true;
	}

	@Override
	public final int hashCode() {
		final int prime = 67;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		// TODO remove when Buffered.SourceException occurs
		result = prime
				* result
				+ ((getDescription() == null) ? 0 : getDescription().hashCode());
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result
				+ ((getNote() == null) ? 0 : getNote().hashCode());
		result = prime * result
				+ ((getPublished() == null) ? 0 : getPublished().hashCode());
		result = prime * result
				+ ((getBranches() == null) ? 0 : getBranches().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return getName();
	}

}