package com.kanven.practice.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ClazzWriter {

	public static void main(String[] args) throws IOException {
		ClassWriter cw = new ClassWriter(0);
		cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
				"com/kanven/mqtt/Comparable", null, "java/lang/Object", null);
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I", null, new Integer(-1))
				.visitEnd();
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I", null, new Integer(0))
				.visitEnd();
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I", null, new Integer(1))
				.visitEnd();
		cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null)
				.visitEnd();
		cw.visitEnd();
		byte[] b = cw.toByteArray();
		save(b);
	}

	private static void save(byte[] b) throws IOException {
		File file = new File("Comparable.class");
		file.createNewFile();
		OutputStream os = new FileOutputStream(file);
		os.write(b);
		os.flush();
		os.close();
	}

}
