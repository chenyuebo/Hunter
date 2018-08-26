package com.lolita.plugin.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.LocalVariablesSorter;

public class TimingMethodAdapter extends LocalVariablesSorter implements Opcodes {

    private int startVarIndex;

    private String methodName;


    public TimingMethodAdapter(String name, int access, String desc, MethodVisitor mv) {
        super(Opcodes.ASM5, access, desc, mv);
        this.methodName = name;
    }


    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        startVarIndex = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(Opcodes.LSTORE, startVarIndex);
    }


    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(Opcodes.LLOAD, startVarIndex);
            mv.visitInsn(Opcodes.LSUB);
            int index = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(Opcodes.LSTORE, index);
//            mv.visitLdcInsn(methodName);
//            mv.visitVarInsn(Opcodes.LLOAD, index);
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/quinn/asm/sample2/handler/LogManager", "log", "(Ljava/lang/String;J)V", false);
        }
        super.visitInsn(opcode);
    }

}
