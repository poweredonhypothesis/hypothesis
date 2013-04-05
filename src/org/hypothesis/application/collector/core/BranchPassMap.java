/**
 * 
 */
package org.hypothesis.application.collector.core;

import java.util.HashMap;

import org.hypothesis.entity.Branch;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
@SuppressWarnings("serial")
public class BranchPassMap extends HashMap<Branch, Integer> {

	public Integer get(Branch branch) {
		Integer result = super.get(branch);
		if (result == null) {
			result = new Integer(0);
			put(branch, result);
		}
		return result;
	}

	public void incBranchPass(Branch branch) {
		Integer pass = get(branch);
		pass++;
	}
}
