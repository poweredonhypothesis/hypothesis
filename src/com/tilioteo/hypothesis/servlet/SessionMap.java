/**
 * 
 */
package com.tilioteo.hypothesis.servlet;

import java.util.HashMap;

import org.hibernate.Session;

/**
 * @author kamil
 *
 */
@SuppressWarnings("serial")
public class SessionMap extends HashMap<Thread, Session> {

}
