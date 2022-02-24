// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

public class UpdateInstructionCacheNode
{
    public long time;
    public UpdateInstruction updateInstruction;
    public UpdateInstructionParams updateInstructionParams;
    
    public UpdateInstructionCacheNode(final UpdateInstructionParams updateInstructionParams, final UpdateInstruction updateInstruction, final long time) {
        this.updateInstructionParams = updateInstructionParams;
        this.updateInstruction = updateInstruction;
        this.time = time;
    }
}
