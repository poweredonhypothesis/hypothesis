/**
 * 
 */
package com.tilioteo.hypothesis.evaluation;

import java.io.Serializable;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractBasePath implements Serializable {

	public abstract String getBranchKey();

	public abstract void setBranchKey(String branchKey);

}