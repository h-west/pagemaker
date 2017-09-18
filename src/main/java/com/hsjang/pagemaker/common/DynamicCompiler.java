package com.hsjang.pagemaker.common;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.janino.SimpleCompiler;
import org.codehaus.janino.util.ClassFile;

public class DynamicCompiler extends SimpleCompiler{
	
	Map<String, byte[]> bytecodes = new HashMap<String, byte[]>();
	
	@Override
	public void cook(ClassFile[] classFiles) {

        final Map<String , byte[]> classes = new HashMap<String, byte[]>();
        for (ClassFile cf : classFiles) {
            classes.put(cf.getThisClassName(), cf.toByteArray());
            bytecodes.put(cf.getThisClassName(), cf.toByteArray());
        }
        this.cook(classes);
    }
	
	public byte[] getBytecode(String name) {
		return bytecodes.get(name);
	}
}


