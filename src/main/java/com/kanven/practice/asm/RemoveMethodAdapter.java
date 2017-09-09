package com.kanven.practice.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RemoveMethodAdapter extends ClassVisitor {

	private String method;

	private String desc;

	public RemoveMethodAdapter(ClassVisitor cv, String method, String desc) {
		super(Opcodes.ASM4, cv);
		this.method = method;
		this.desc = desc;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (method.equals(name) && this.desc.equals(desc)) {
			return null;
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}

	public static void main(String[] args) throws IOException {
		ClassReader reader = new ClassReader("com.kanven.practice.asm.Student");
		ClassWriter writer = new ClassWriter(0);
		ClassVisitor visitor = new RemoveMethodAdapter(writer, "getName", "");
		reader.accept(visitor, 0);
		byte[] bytes = writer.toByteArray();
		File file = new File("StudentWrapper.class");
		file.createNewFile();
		OutputStream output = new FileOutputStream(file);
		output.write(bytes);
		output.flush();
		output.close();
	}

}
