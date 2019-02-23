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

package com.georlegacy.general.theatrical.handlers;

import com.georlegacy.general.theatrical.blocks.fixtures.base.IFixture;
import com.georlegacy.general.theatrical.entities.core.IHasModel;
import com.georlegacy.general.theatrical.init.TheatricalBlocks;
import com.georlegacy.general.theatrical.init.TheatricalItems;
import com.georlegacy.general.theatrical.init.TheatricalSoundEvents;
import com.georlegacy.general.theatrical.items.attr.fixture.gel.GelType;
import com.georlegacy.general.theatrical.tiles.fixtures.TileEntityFresnel;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Handler for registry of mod entities
 *
 * @author James Conway
 */
@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(TheatricalItems.ITEMS.toArray(new Item[0]));
        TheatricalBlocks.BLOCKS.forEach(block -> event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName())));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(TheatricalBlocks.BLOCKS.toArray(new Block[0]));
        TheatricalBlocks.BLOCKS.forEach(block -> {
            if(block instanceof IFixture){
                IFixture fixture = (IFixture) block;
                GameRegistry.registerTileEntity(fixture.getTileEntity(), block.getRegistryName());
            }
        });
    }

    @SubscribeEvent
    public static void registerColors(ColorHandlerEvent.Block event){
        event.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            if(pos == null || worldIn == null){
                return 0xFFFFFFFF;
            }
            if(tintIndex == 0) {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if(tileEntity instanceof TileEntityFresnel){
                    return 0xFF000000 | ((TileEntityFresnel) tileEntity).getGelType().getHex();
                }
            }
            return 0;
        }, TheatricalBlocks.BLOCK_FRESNEL);
    }

    @SubscribeEvent
    public static void registerColors(ColorHandlerEvent.Item event){
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> 0xFF000000 | GelType.getGelType(stack.getMetadata()).getHex(), TheatricalItems.ITEM_GEL);
    }

    @SubscribeEvent
    public static void onSoundEventRegister(RegistryEvent.Register<SoundEvent> event) {
        TheatricalSoundEvents.SOUNDS.forEach(sound -> {
            event.getRegistry().register(sound);
        });
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : TheatricalItems.ITEMS)
            if (item instanceof IHasModel)
                ((IHasModel) item).registerModels();
        for (Block block : TheatricalBlocks.BLOCKS)
            if(block instanceof IHasModel)
                ((IHasModel) block).registerModels();
    }

}
