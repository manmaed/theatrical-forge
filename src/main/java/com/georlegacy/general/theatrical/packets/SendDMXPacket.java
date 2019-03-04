/*
 * Copyright 2018 Theatrical Team (James Conway (615283) & Stuart (Rushmead)) and it's contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.georlegacy.general.theatrical.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SendDMXPacket implements IMessage {

    public SendDMXPacket() {
    }


    public SendDMXPacket(BlockPos blockPos, int[] data) {
        this.blockPos = blockPos;
        this.data =data;
    }

    private BlockPos blockPos;
    private int[] data;

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int[] getData() {
        return data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        blockPos = new BlockPos(x, y, z);
        int length = buf.readInt();
        int[] data = new int[length];
        for(int i = 0; i < length; i++){
            data[i] = buf.readInt();
        }
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
        buf.writeInt(data.length);
        for(int i = 0; i < data.length; i++){
            buf.writeInt(data[i]);
        }
    }

}