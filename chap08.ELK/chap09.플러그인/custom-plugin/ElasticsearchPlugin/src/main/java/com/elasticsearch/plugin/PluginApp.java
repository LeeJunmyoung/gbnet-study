package com.elasticsearch.plugin;

import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;

/**
 * Hello world!
 *
 */
public class PluginApp extends Plugin implements ActionPlugin
{
	public static void main (String args[]) {
		System.out.println("test");
	}
}
